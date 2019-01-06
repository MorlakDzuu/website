package com.website.website.repo;

import com.website.website.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findAllByUsername(String username);

    User findAllByActivationCode(String code);
}
