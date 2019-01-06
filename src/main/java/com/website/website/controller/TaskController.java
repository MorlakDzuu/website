package com.website.website.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.website.website.data.TaskListOutput;
import com.website.website.domain.File;
import com.website.website.domain.Tag;
import com.website.website.domain.Task;
import com.website.website.domain.User;
import com.website.website.repo.FilesRepo;
import com.website.website.repo.TagRepo;
import com.website.website.repo.TaskRepo;
import com.website.website.service.storage.FileSystemStorageService;
import com.website.website.service.tags.TagService;
import com.website.website.service.task.TaskService;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class TaskController {

    @Autowired
    private final TaskRepo taskRepo;

    @Autowired
    private final FilesRepo filesRepo;

    @Autowired
    private TagRepo tagRepo;

    private final TagService tagService;

    private final FileSystemStorageService storageService;

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskRepo taskRepo, FilesRepo filesRepo, TagService tagService, FileSystemStorageService storageService, TaskService taskService) {
        this.taskRepo = taskRepo;
        this.filesRepo = filesRepo;
        this.tagService = tagService;
        this.storageService = storageService;
        this.taskService = taskService;
    }

    @GetMapping("/addTask")
    public String returnTaskForm() {
        return "task/addTask";
    }


    @GetMapping("/taskList")
    public String taskList(@AuthenticationPrincipal User user,
                           @RequestParam(required = false) Long pageNumber,
                           Model model) {
        List<Task> tasks = taskRepo.findAllByUserOrderByDate(user);
        int tasksNumber = tasks.size();
        if (pageNumber != null) {
            Pageable page = new PageRequest(pageNumber.intValue(), 10);
            tasks = taskRepo.findAllByUser(user, page);
            model.addAttribute("currentPage", pageNumber + 1);
        } else {
            Pageable page = new PageRequest(0, 10);
            tasks = taskRepo.findAllByUser(user, page);
        }
        List<TaskListOutput> taskListOutputs = new ArrayList<>();
        for (Task task: tasks) {
            TaskListOutput taskListOutput = new TaskListOutput(task, tagService.getTagsByTask(task));
            taskListOutputs.add(taskListOutput);
        }
        model.addAttribute("data", taskListOutputs);
        model.addAttribute("tasksNumber", tasksNumber);
        return "task/taskList";
    }

    @GetMapping("/task/{task}")
    public String showTask(@PathVariable Task task,
                           @RequestParam(required = false) Long currentPage,
                           Model model) throws ParseException {
        String finish_date = task.getFinish_date();
        if (finish_date != null) {
            SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            Date dateNow = new Date();
            int days_left = (Days.daysBetween(new DateTime(dateNow), new DateTime(format.parse(finish_date)))).getDays();
            if (days_left > 0) {
                model.addAttribute("days", "Days left: " + days_left);
            } else {
                model.addAttribute("days", "Time is up");
            }
        }
        List<File> files = storageService.getTasksFiles(task);
        files.toArray();
        if (files.size() > 0) {
            model.addAttribute("files", files);
        }
        List<Tag> tags = tagService.getTagsByTask(task);
        model.addAttribute("tags", tags);
        model.addAttribute("task", task);
        if (currentPage != null) {
            model.addAttribute("currentPage", currentPage);
        }
        return "task/taskDetails";
    }

    @RequestMapping("/getAllTags")
    public @ResponseBody List<String> getAllTags() {
        List<Tag> tags = tagRepo.findAll();
        List<String> allTags = new ArrayList<>();
        for (Tag tag: tags) {
            allTags.add(tag.getTag());
        }
        return allTags;
    }

    @RequestMapping("/getAllNames")
    public @ResponseBody List<String> getAllNames(@AuthenticationPrincipal User user) {
        List<Tag> tags = tagService.getTagsByUser(user);
        List<Task> tasks = taskRepo.findAllByUserOrderByDate(user);
        List<String> allNames = new ArrayList<>();
        for (Tag tag: tags) {
            allNames.add(tag.getTag());
        }
        for (Task task: tasks) {
            allNames.add(task.getName());
        }
        return allNames;
    }

    @GetMapping("/getSearchResults")
    public String getSearchResults(@AuthenticationPrincipal User user,
                                   @RequestParam String searchString,
                                   Model model) {
        List<Task> tasks = taskService.getSearchResults(user, searchString);
        List<TaskListOutput> taskListOutputs = new ArrayList<>();
        for (Task task:tasks) {
            TaskListOutput taskListOutput = new TaskListOutput(task, tagService.getTagsByTask(task));
            taskListOutputs.add(taskListOutput);
        }
        model.addAttribute("data", taskListOutputs);
        return "task/taskList";
    }

    @RequestMapping("/deleteTag")
    public @ResponseBody void deleteTag(
            @RequestParam long taskId,
            @RequestParam long tagId
    ) {
        tagService.deleteConnection(taskId, tagId);
    }

    @GetMapping("/task/{task}/delete")
    public String deleteTask(@PathVariable Task task) {
        taskService.deleteTask(task);
        return "redirect:/taskList";
    }

    @GetMapping("/task/{task}/edit")
    public String editTaskShow(@PathVariable Task task,
                               Model model) {
        model.addAttribute("task", task);
        return "task/addTask";
    }

    @GetMapping("/task/file/{file}/delete")
    public String deleteFile(@PathVariable File file) {
        Task task = file.getTask();
        storageService.delete(file.getFilename());
        filesRepo.delete(file);
        return "redirect:/task/" + task.getId();
    }

    @GetMapping("/task/file/{file}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable File file) {

        file.setFilename(file.getFilename().substring(file.getFilename().lastIndexOf('/') + 1));
        Resource resFile = storageService.loadAsResource(file.getFilename());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + resFile.getFilename() + "\"").body(resFile);
    }

    @PostMapping("/addTask")
    public String addTask(@AuthenticationPrincipal User user,
                          @RequestParam String nameOfTask,
                          @RequestParam String descriptionOfTask,
                          @RequestParam String finishDate,
                          @RequestPart(name = "files", required = false) MultipartFile[] files,
                          @RequestParam(required = false) Long taskId,
                          @RequestParam String[] tags,
                          Model model) {
        Task task = taskService.updateTask(user, nameOfTask, descriptionOfTask, finishDate, files, tags, taskId);
        List<String> notes = taskService.getNotes();
        notes.toArray();
        if (notes.size() > 0) {
            model.addAttribute("notes", notes);
            model.addAttribute("task", task);
            List<File> files1 = storageService.getTasksFiles(task);
            files1.toArray();
            if (files1.size() > 0) {
                model.addAttribute("files", files1);
            }
            return "task/addTask";
        }
        List<Task> tasks = taskRepo.findAllByUserOrderByDate(user);
        int tasksNumber = tasks.size();
        Pageable page = new PageRequest(0, 10);
        tasks = taskRepo.findAllByUser(user, page);
        List<TaskListOutput> taskListOutputs = new ArrayList<>();
        for (Task tas: tasks) {
            TaskListOutput taskListOutput = new TaskListOutput(tas, tagService.getTagsByTask(tas));
            taskListOutputs.add(taskListOutput);
        }
        model.addAttribute("data", taskListOutputs);
        model.addAttribute("tasksNumber", tasksNumber);
        return "task/taskList";
    }
}