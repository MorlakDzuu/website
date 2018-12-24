package com.website.website.repo;

import com.website.website.domain.File;
import com.website.website.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilesRepo extends JpaRepository<File, Long> {
    List<File> findAllByTask(Task task);
}
