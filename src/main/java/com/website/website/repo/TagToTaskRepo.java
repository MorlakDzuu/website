package com.website.website.repo;

import com.website.website.domain.Tag;
import com.website.website.domain.TagToTask;
import com.website.website.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagToTaskRepo extends JpaRepository<TagToTask, Long> {
    TagToTask findByTaskAndTag(Task task, Tag tag);
    List<TagToTask> findAllByTask(Task task);
    List<TagToTask> findAllByTag(Tag tag);
}
