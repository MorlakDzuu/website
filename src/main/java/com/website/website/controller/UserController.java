package com.website.website.controller;

import com.website.website.domain.Task;
import com.website.website.domain.User;
import com.website.website.repo.TaskRepo;
import com.website.website.repo.UserRepo;
import com.website.website.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepo userRepo;

    @Autowired
    TaskRepo taskRepo;

    @Value("${upload.path}")
    private String uploadPath;

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String main(@AuthenticationPrincipal User user,
                       Model model) {
        List<Task> tasks = taskRepo.findAllByUserOrderByDate(user);
        model.addAttribute("tasksNumber", tasks.size());
        return "user/userPersonalArea";
    }

    @PostMapping
    public String updateUser(@AuthenticationPrincipal User user,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) String email,
                             @RequestPart(name = "file", required = false) MultipartFile file) {
        userService.updateUser(user, name, email, file);
        return "redirect:/user";
    }
}
