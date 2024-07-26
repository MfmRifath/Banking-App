package com.Rifath.BankingApp.controller;

import com.Rifath.BankingApp.entity.Account;
import com.Rifath.BankingApp.entity.User;
import com.Rifath.BankingApp.model.BankService;
import com.Rifath.BankingApp.service.AccountService;
import com.Rifath.BankingApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String home(Model model) {
        // Add services to the model
        List<BankService> services = new ArrayList<>();
        services.add(new BankService("Account Management", "Manage your bank accounts with ease."));
        services.add(new BankService("Funds Transfer", "Transfer funds between your accounts or to other banks."));
        services.add(new BankService("loan Services", "Apply for personal, home, or auto loans."));
        services.add(new BankService("Customer Support", "24/7 customer support to assist you with your banking needs."));
        services.add(new BankService("Online Banking", "Access your bank accounts anytime, anywhere."));
        model.addAttribute("services", services);

        // Get the currently logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);


        // Fetch the accounts of the logged-in user
        List<Account> accounts = accountService.findByUser(Optional.ofNullable(user));
        model.addAttribute("accounts", accounts);
        String username1 = authentication.getName();
        User user1 = userService.findUserByUsername(username);
        model.addAttribute("user", user);
        // Get the current authenticated user
        Object principal1 = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Get the current authenticated user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username2;
        if (principal instanceof UserDetails) {
            username2 = ((UserDetails) principal).getUsername();
        } else {
            username2 = principal.toString();
        }

        // Fetch the account details
        Account account = accountService.getAccountForCurrentUser();
        model.addAttribute("account", account);


        return "home";
    }
}
