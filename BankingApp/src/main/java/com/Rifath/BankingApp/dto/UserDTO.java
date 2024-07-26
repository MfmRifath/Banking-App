package com.Rifath.BankingApp.dto;

import com.Rifath.BankingApp.entity.Role;

import java.util.Set;

public class UserDTO {
    private Long id;
    private String username;
    private Set<Role> roles;

    // Getters and Setters

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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
