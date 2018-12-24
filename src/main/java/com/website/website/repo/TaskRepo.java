package com.website.website.repo;

import com.website.website.domain.Task;
import com.website.website.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long> {
    List<Task> findAllByUserOrderByDate(User user);

    Task findAllById(Long taskId);
}
