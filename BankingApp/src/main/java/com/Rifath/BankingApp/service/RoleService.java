package com.Rifath.BankingApp.service;

import com.Rifath.BankingApp.dao.RoleRepository;
import com.Rifath.BankingApp.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findByName(String name) {
        Optional<Role> theRole = Optional.ofNullable((roleRepository.findByName(name)));
        return theRole.orElseThrow(() -> new IllegalArgumentException("Role not found: " + name));
    }
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
    public Role findRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
}
