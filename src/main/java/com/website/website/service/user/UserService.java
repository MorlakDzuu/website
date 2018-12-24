package com.website.website.service.user;

import com.website.website.domain.User;
import com.website.website.repo.UserRepo;
import com.website.website.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {
    private final StorageService storageService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    public UserService(StorageService storageService) {
        this.storageService = storageService;
    }

    public void updateUser(User user, String name, String email, MultipartFile file) {
        if(!name.isEmpty()) {
            if(userRepo.findByName(name) == null) { user.setName(name);}
        }
        if(!email.isEmpty()) {
            if(userRepo.findByEmail(email) == null) { user.setEmail(email);}
        }
        if(!file.isEmpty() && storageService.rightFormat(file.getOriginalFilename())) {
            String filename = user.getPicture();
            user.setPicture(storageService.store(file));
            storageService.delete(filename);
        }
        userRepo.save(user);
    }
}