package com.website.website.data;

import com.website.website.domain.Role;

import java.util.Set;

public class UserData {

    private Long id;
    private String username;
    private String email;
    private Set<Role> roles;
    private String picture;

    public UserData() {
    }

    public UserData(Long id, String username, String email, Set<Role> roles, String picture) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.picture = picture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
