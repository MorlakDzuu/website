package com.website.website.service.task;

import com.website.website.domain.File;
import com.website.website.domain.Task;
import com.website.website.domain.User;
import com.website.website.repo.FilesRepo;
import com.website.website.repo.TaskRepo;
import com.website.website.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private FilesRepo filesRepo;

    @Value("${max.files.number}")
    int MAX_FILES_NUMBER;

    private final StorageService storageService;

    public TaskService(StorageService storageService) {
        this.storageService = storageService;
    }

    public void deleteTask(Task task) {
        List<File> files = filesRepo.findAllByTask(task);
        for (File file: files) {
            storageService.delete(file.getFilename());
            filesRepo.delete(file);
        }
        taskRepo.delete(task);
    }

    public Task updateTask(User user,
                           String nameOfTask,
                           String descriptionOfTask,
                           String finishDate,
                           MultipartFile[] files,
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
            descriptionOfTask = descriptionOfTask.replace("<", "&lt");
            descriptionOfTask = descriptionOfTask.replace(">", "&gt");
            task.setDescription(descriptionOfTask);
            if (nameOfTask.length() < 255) {
                nameOfTask = nameOfTask.replace("<", "&lt");
                nameOfTask = nameOfTask.replace(">", "&gt");
                task.setName(nameOfTask);
            }
            for (MultipartFile file: files) {
                if (!file.isEmpty()) {
                    if (storageService.getFilesNumber(user) < MAX_FILES_NUMBER) {
                        File fileOfTask = new File();
                        fileOfTask.setFilename(storageService.store(file));
                        fileOfTask.setTask(task);
                        filesRepo.save(fileOfTask);
                    }
                }
            }
            taskRepo.save(task);
        }
        return task;
    }
}
