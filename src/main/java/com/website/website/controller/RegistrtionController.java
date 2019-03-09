package com.website.website.controller;

import com.website.website.domain.User;
import com.website.website.repo.UserRepo;
import com.website.website.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class RegistrtionController {


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public  String registration() {
        return "login/registration";
    }

    @PostMapping("/registration")
    public String addUser(User user) {

        if(!userService.addUser(user)) {
            return "login/registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code,
                           Model model) {
        User user = userRepo.findAllByActivationCode(code);

        if(user != null) {
            user.setActivationCode(null);
            userRepo.save(user);
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }
}
