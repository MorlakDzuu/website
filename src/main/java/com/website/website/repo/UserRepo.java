package com.website.website.repo;

import com.website.website.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, String> {
    User findByName(String name);
    User findByEmail(String email);
}
