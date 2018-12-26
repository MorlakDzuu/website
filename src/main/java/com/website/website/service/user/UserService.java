package com.website.website.service.user;

import com.website.website.domain.User;
import com.website.website.repo.UserRepo;
import com.website.website.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final StorageService storageService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    public UserService(StorageService storageService) {
        this.storageService = storageService;
    }

    public List<String> updateUser(User user, String name, String email, MultipartFile file) {
        List<String> notes = new ArrayList<String>();
        if(!name.isEmpty()) {
            if (name.length() > 25) {
                notes.add("Your nickname is too long! (it should consists less then 25 characters)");
            } else if(userRepo.findByName(name) == null) {
                user.setName(name);
            } else {
                notes.add("This name has already exists!");
            }
        }
        if(!email.isEmpty()) {
            if(userRepo.findByEmail(email) == null) {
                user.setEmail(email);
            } else {
                notes.add("This email has already used!");
            }
        }
        if(!file.isEmpty()) {
            if (!storageService.rightFormat(file.getOriginalFilename())) {
                notes.add("Your file has wrong format!");
            } else {
                String filename = user.getPicture();
                user.setPicture(storageService.store(file));
                storageService.delete(filename);
            }
        }
        userRepo.save(user);
        return notes;
    }
}