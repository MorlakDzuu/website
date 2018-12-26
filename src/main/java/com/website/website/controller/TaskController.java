package com.website.website.controller;

import com.website.website.domain.File;
import com.website.website.domain.Task;
import com.website.website.domain.User;
import com.website.website.repo.FilesRepo;
import com.website.website.repo.TaskRepo;
import com.website.website.service.storage.FileSystemStorageService;
import com.website.website.service.task.TaskService;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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

    private final FileSystemStorageService storageService;

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskRepo taskRepo, FilesRepo filesRepo, FileSystemStorageService storageService, TaskService taskService) {
        this.taskRepo = taskRepo;
        this.filesRepo = filesRepo;
        this.storageService = storageService;
        this.taskService = taskService;
    }

    protected List<File> getTasksFiles(Task task) {
        List<File> files = filesRepo.findAllByTask(task);
        for (File file: files) {
            String name = file.getFilename();
            file.setFilename(name.substring(name.indexOf('.') + 1));
        }
        return files;
    }

    @GetMapping("/addTask")
    public String returnTaskForm() {
        return "task/addTask";
    }


    @GetMapping("/taskList")
    public String taskList(@AuthenticationPrincipal User user,
                           Model model) {
        List<Task> tasks = taskRepo.findAllByUserOrderByDate(user);
        model.addAttribute("tasks", tasks);
        return "task/taskList";
    }

    @GetMapping("/task/{task}")
    public String showTask(@PathVariable Task task,
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
        List<File> files = getTasksFiles(task);
        files.toArray();
        if (files.size() > 0) {
            model.addAttribute("files", files);
        }
        model.addAttribute("task", task);
        return "task/taskDetails";
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
                          Model model) {
        Task task = taskService.updateTask(user, nameOfTask, descriptionOfTask, finishDate, files, taskId);
        List<String> notes = taskService.getNotes();
        notes.toArray();
        if (notes.size() > 0) {
            model.addAttribute("notes", notes);
            model.addAttribute("task", task);
            List<File> files1 = getTasksFiles(task);
            files1.toArray();
            if (files1.size() > 0) {
                model.addAttribute("files", files1);
            }
            return "task/addTask";
        }
        List<Task> tasks = taskRepo.findAllByUserOrderByDate(user);
        model.addAttribute("tasks", tasks);
        return "task/taskList";
    }
}