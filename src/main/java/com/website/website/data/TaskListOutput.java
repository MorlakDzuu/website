package com.website.website.data;

import com.website.website.domain.Tag;
import com.website.website.domain.Task;

import java.util.List;

public class TaskListOutput {
    Task task;
    List<Tag> tag;

    public TaskListOutput(Task task, List<Tag> tag) {
        this.task = task;
        this.tag = tag;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<Tag> getTag() {
        return tag;
    }

    public void setTag(List<Tag> tag) {
        this.tag = tag;
    }
}