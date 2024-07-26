package com.Rifath.BankingApp.service;

import com.Rifath.BankingApp.dao.RoleRepository;
import com.Rifath.BankingApp.dao.UserRepository;
import com.Rifath.BankingApp.entity.Role;
import com.Rifath.BankingApp.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${upload.path}")
    private String uploadDir;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public void save(User user) {
        userRepository.save(user);
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public User saveUser(User user, List<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName);
            if (role != null) {
                roles.add(role);
            }
        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Ensure password is encoded
        return userRepository.save(user);
    }

    public Role findRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
    public void assignRolesToUser(User user, List<String> roleNames) {
        Set<Role> roles = roleNames.stream()
                .map(this::findRoleByName)
                .collect(Collectors.toSet());
        user.setRoles(roles);
    }

    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


}