package com.website.website.service.user;

import com.website.website.data.UserData;
import com.website.website.domain.Role;
import com.website.website.domain.User;
import com.website.website.repo.UserRepo;
import com.website.website.service.mail.MailSender;
import com.website.website.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private StorageService storageService;

    @Value("${max.filesize}")
    int MAX_FILE_SIZE;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        return userRepo.findByUsername(username);
    }

    public boolean addUser(User user) {

        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        User userFromDB =  userRepo.findAllByUsername(user.getUsername());

        if(userFromDB != null) {
            return false;
        }

        user.setActivationCode(UUID.randomUUID().toString());
        user.setRoles(Collections.singleton(Role.USER));
        user.setActive(true);

        userRepo.save(user);

        if (user.getEmail() != null) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to sweater. Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(), user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }


        return true;
    }

    public List<UserData> getUserData(List<User> users) {
        List<UserData> userDataList = new ArrayList<>();
        for (User user: users) {
            UserData userData = new UserData(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRoles(),
                    user.getPicture()
            );
            userDataList.add(userData);
        }
        return userDataList;
    }

    public List<String> updateUser(User user, String name, String email, MultipartFile file) {
        List<String> notes = new ArrayList<String>();
        if(!name.isEmpty()) {
            if (name.length() > 25) {
                notes.add("Your nickname is too long! (it should consists less then 25 characters)");
            } else if(userRepo.findByUsername(name) == null) {
                user.setUsername(name);
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
                if (file.getSize() <= MAX_FILE_SIZE) {
                    String filename = user.getPicture();
                    user.setPicture(storageService.store(file));
                    storageService.delete(filename);
                } else {
                    notes.add("File " + file.getOriginalFilename() + " is too big");
                }
            }
        }
        userRepo.save(user);
        return notes;
    }
}