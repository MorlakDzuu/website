package com.website.website.service.task;

import com.website.website.domain.*;
import com.website.website.repo.FilesRepo;
import com.website.website.repo.TagRepo;
import com.website.website.repo.TagToTaskRepo;
import com.website.website.repo.TaskRepo;
import com.website.website.service.storage.StorageService;
import com.website.website.service.tags.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private FilesRepo filesRepo;

    @Autowired
    private TagRepo tagRepo;

    @Autowired
    private TagToTaskRepo tagToTaskRepo;

    private final TagService tagService;

    List<String> notes = new ArrayList<String>();

    @Value("${max.files.number}")
    int MAX_FILES_NUMBER;

    @Value("${max.filesize}")
    int MAX_FILE_SIZE;

    private final StorageService storageService;

    public TaskService(TagService tagService, StorageService storageService) {
        this.tagService = tagService;
        this.storageService = storageService;
    }

    public void deleteTask(Task task) {
        List<File> files = filesRepo.findAllByTask(task);
        List<Tag> tags = tagService.getTagsByTask(task);
        for (File file: files) {
            storageService.delete(file.getFilename());
            filesRepo.delete(file);
        }
        for (Tag tag: tags) {
            TagToTask tagToTask = tagToTaskRepo.findByTaskAndTag(task, tag);
            tagToTaskRepo.delete(tagToTask);
        }
        taskRepo.delete(task);
    }

    public List<String> getNotes() {
        List<String> tempNotes = new ArrayList<>();
        for (String str: notes) {
            tempNotes.add(str);
        }
        notes.clear();
        return tempNotes;
    }

    public List<Task> getSearchResults(User user, String searchString) {
        searchString = searchString.toLowerCase();
        List<Task> resultTasks = new ArrayList<>();
        List<Task> userTasks = taskRepo.findAllByUserOrderByDate(user);
        List<Tag> tags = tagService.getTagsByUser(user);
        for (Task task: userTasks) {
            if (task.getName().toLowerCase().contains(searchString)) {
                resultTasks.add(task);
            }
        }
        for (Tag tag: tags) {
            if (tag.getTag().toLowerCase().contains(searchString)) {
                List<TagToTask> tagToTaskList = tagToTaskRepo.findAllByTag(tag);
                for (TagToTask tagToTask: tagToTaskList) {
                    Task tempTask = tagToTask.getTask();
                    if (tempTask.getUser().getId().equals(user.getId()) && !resultTasks.contains(tempTask)) {
                        resultTasks.add(tempTask);
                    }
                }
            }
        }
        return resultTasks;
    }

    public Task updateTask(User user,
                           String nameOfTask,
                           String descriptionOfTask,
                           String finishDate,
                           MultipartFile[] files,
                           String[] tags,
                           Long taskId) {
        Task task;
        if (taskId == null) {
            task = new Task();
            task.setUser(user);
            task.setDate(new Date());
        } else {
            task = taskRepo.findAllById(taskId);
        }
        if (task != null) {
            if (!finishDate.isEmpty()) { task.setFinish_date(finishDate); }
            if (descriptionOfTask.length() < 4000) {
                descriptionOfTask = descriptionOfTask.replace("<", "&lt");
                descriptionOfTask = descriptionOfTask.replace(">", "&gt");
                task.setDescription(descriptionOfTask);
            } else {
                notes.add("Description is too big");
            }
            if (nameOfTask.length() < 255) {
                nameOfTask = nameOfTask.replace("<", "&lt");
                nameOfTask = nameOfTask.replace(">", "&gt");
                task.setName(nameOfTask);
            }
            for (MultipartFile file: files) {
                if (!file.isEmpty()) {
                    if (storageService.getFilesNumber(user) < MAX_FILES_NUMBER) {
                        if (file.getSize() <= MAX_FILE_SIZE) {
                            File fileOfTask = new File();
                            fileOfTask.setFilename(storageService.store(file));
                            fileOfTask.setTask(task);
                            filesRepo.save(fileOfTask);
                        } else {
                            notes.add("This file " + file.getOriginalFilename() + " is too big");
                            return task;
                        }
                    } else {
                        notes.add("We can't download this file " + file.getOriginalFilename() + " because you have uploaded too many files");
                        return task;
                    }
                }
            }
            taskRepo.save(task);
            for (String str: tags) {
                if (str.length() > 50) {
                    notes.add("Tag " + str + " is too big");
                }
                Tag tag = tagRepo.findAllByTag(str);
                if (tag == null) {
                    tag = new Tag();
                    tag.setTag(str);
                    tagRepo.save(tag);
                }
                tag = tagRepo.findAllByTag(str);
                TagToTask tagToTask = tagToTaskRepo.findByTaskAndTag(task, tag);
                if (tagToTask == null) {
                    tagToTask = new TagToTask();
                    tagToTask.setTag(tag);
                    tagToTask.setTask(task);
                    tagToTaskRepo.save(tagToTask);
                }
            }
        }
        return task;
    }
}
