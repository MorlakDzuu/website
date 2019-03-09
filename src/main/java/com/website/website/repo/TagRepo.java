package com.website.website.repo;

import com.website.website.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepo extends JpaRepository<Tag, Long> {
    Tag findAllByTag(String tag);
    Tag findAllById(Long id);
}