package com.website.website.controller;

import com.website.website.data.UserData;
import com.website.website.domain.User;
import com.website.website.repo.UserRepo;
import com.website.website.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserCheckoutController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    protected String getAllUsres(Model model) {
        List<User> users = userRepo.findAll();
        List<UserData> userDataList = userService.getUserData(users);
        model.addAttribute("users", userDataList);
        return "user/userList";
    }
}
