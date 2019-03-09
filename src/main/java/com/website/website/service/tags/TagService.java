package com.website.website.service.tags;

import com.website.website.domain.Tag;
import com.website.website.domain.TagToTask;
import com.website.website.domain.Task;
import com.website.website.domain.User;
import com.website.website.repo.TagRepo;
import com.website.website.repo.TagToTaskRepo;
import com.website.website.repo.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepo tagRepo;

    @Autowired
    private TagToTaskRepo tagToTaskRepo;

    @Autowired
    private TaskRepo taskRepo;

    public List<Tag> getTagsByTask(Task task) {
        List<Tag> tags = new ArrayList<>();
        List<TagToTask> tagToTaskList = tagToTaskRepo.findAllByTask(task);
        for (TagToTask tagToTask: tagToTaskList) {
            tags.add(tagRepo.findAllById(tagToTask.getTag().getId()));
        }
        return tags;
    }

    public List<Tag> getTagsByUser(User user) {
        List<Tag> tags = new ArrayList<>();
        List<Task> tasks = taskRepo.findAllByUserOrderByDate(user);
        for (Task task: tasks) {
            List<TagToTask> tagToTaskList = tagToTaskRepo.findAllByTask(task);
            for (TagToTask tagToTask: tagToTaskList) {
                if (!tags.contains(tagRepo.findAllById(tagToTask.getTag().getId()))) {
                    tags.add(tagRepo.findAllById(tagToTask.getTag().getId()));
                }
            }
        }
        return tags;
    }

    public void deleteConnection(long taskId, long tagId) {
        Task task = taskRepo.findAllById(taskId);
        Tag tag = tagRepo.findAllById(tagId);
        TagToTask tagToTask = tagToTaskRepo.findByTaskAndTag(task, tag);
        tagToTaskRepo.delete(tagToTask);
    }
}