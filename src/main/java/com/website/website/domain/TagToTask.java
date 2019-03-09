package com.website.website.domain;

import javax.persistence.*;

@Entity
@Table(name = "tag_to_task")
public class TagToTask {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "task_id")
    private Task task;

    public TagToTask() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}