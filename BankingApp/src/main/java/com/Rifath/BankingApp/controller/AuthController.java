package com.Rifath.BankingApp.controller;

import com.Rifath.BankingApp.dao.RoleRepository;
import com.Rifath.BankingApp.entity.Role;
import com.Rifath.BankingApp.entity.User;
import com.Rifath.BankingApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${upload.path}")
    private String uploadDir;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, @RequestParam("profilePicture") MultipartFile profilePicture, RedirectAttributes redirectAttributes, Model model) {
        if (userService.isUsernameExists(user.getUsername())) {
            model.addAttribute("error", "Username already exists");
            return "register";
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
                return "register";
            }
        }

        // Assign the "ROLE_USER" role to the new user
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "Registration successful. Wait until ADMIN Allow You.");
        return "redirect:/login";
    }
    @GetMapping("/")
    public String HomeWithoutLogin() {
        return "homeWithoutLogin";
    }
    @GetMapping("/access-denied")
    public String showAccessDenied(Model model) {
        model.addAttribute("user", new User()); // Adding an empty user to the model
        return "access-denied";
    }
}
