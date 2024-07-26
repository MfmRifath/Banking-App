package com.Rifath.BankingApp.controller;

import com.Rifath.BankingApp.entity.Role;
import com.Rifath.BankingApp.entity.User;
import com.Rifath.BankingApp.service.RoleService;
import com.Rifath.BankingApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/users")
public class UserManagementController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;
    @Value("${upload.path}")
    private String uploadDir;

    @GetMapping("/home")
    public String userManagementHome() {
        return "admin/user/home";
    }

    @GetMapping("/list")
    public String listUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/user/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<User> userOptional = userService.findUserById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.findAllRoles());
            return "admin/user/edit";
        } else {
            return "redirect:/admin/users/home";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User user, @RequestParam List<String> roles, Model model, @RequestParam("profilePicture") MultipartFile profilePicture) {
        Optional<User> existingUserOptional = userService.findUserById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            if (userService.isUsernameExists(user.getUsername()) && !existingUser.getUsername().equals(user.getUsername())) {
                model.addAttribute("error", "Username already exists");
                model.addAttribute("roles", roleService.findAllRoles());
                return "admin/user/edit";
            }
            if (!profilePicture.isEmpty()) {
                try {
                    Files.createDirectories(Paths.get(uploadDir));
                    String fileName = profilePicture.getOriginalFilename();
                    String filePath = Paths.get(uploadDir, fileName).toString();

                    // Save the file to the filesystem
                    profilePicture.transferTo(new File(filePath));

                    // Set the profile picture URL in the user entity
                    existingUser.setProfilePictureUrl("/user-photos/" + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    model.addAttribute("error", "Error uploading file: " + e.getMessage());
                    return "admin/user/edit";
                }
            }

            Set<Role> roleSet = roles.stream()
                    .map(roleName -> roleService.findRoleByName(roleName))
                    .collect(Collectors.toSet());

            existingUser.setRoles(roleSet);
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            existingUser.setEnabled(user.isEnabled());

            userService.saveUser(existingUser);
            return "redirect:/admin/users/home";
        } else {
            return "redirect:/admin/users/home";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/users/home";
    }

    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        List<Role> roles = roleService.findAllRoles();
        model.addAttribute("roles", roles);
        return "admin/user/create";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user, @RequestParam List<String> roles, @RequestParam("profilePicture") MultipartFile profilePicture, Model model) {
        if (userService.isUsernameExists(user.getUsername())) {
            model.addAttribute("error", "Username already exists");
            model.addAttribute("roles", roleService.findAllRoles());
            return "admin/user/create";
        }
        if (!profilePicture.isEmpty()) {
            try {
                Files.createDirectories(Paths.get(uploadDir));
                String fileName = profilePicture.getOriginalFilename();
                String filePath = Paths.get(uploadDir, fileName).toString();

                // Save the file to the filesystem
                profilePicture.transferTo(new File(filePath));

                // Set the profile picture URL in the user entity
                user.setProfilePictureUrl("/user-photos/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "Error uploading file: " + e.getMessage());
                return "admin/user/create";
            }
        }

        Set<Role> roleSet = roles.stream()
                .map(roleName -> roleService.findRoleByName(roleName))
                .collect(Collectors.toSet());

        user.setRoles(roleSet);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(user.isEnabled());

        userService.saveUser(user);


        return "redirect:/admin/users/home";
    }

    @PostMapping("/toggleEnabled/{id}")
    public String toggleUserEnabled(@PathVariable("id") Long id) {
        Optional<User> userOptional = userService.findUserById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEnabled(!user.isEnabled());
            userService.saveUser(user);
        }
        return "redirect:/admin/users/list";
    }
}
