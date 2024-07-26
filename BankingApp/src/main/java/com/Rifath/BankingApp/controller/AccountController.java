package com.Rifath.BankingApp.controller;

import com.Rifath.BankingApp.entity.Account;
import com.Rifath.BankingApp.entity.User;
import com.Rifath.BankingApp.service.AccountService;
import com.Rifath.BankingApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/manager/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    public AccountController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }
    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("accounts", accountService.findAll());
        return "accounts/list";
    }

    @GetMapping("/create")
    public String showCreateAccountForm(Model model) {
        model.addAttribute("account", new Account());
        List<User> allUsers = userService.findAllUsers();
        List<User> usersWithoutAccounts = allUsers.stream()
                .filter(user -> !accountService.isUserHasAccount(user.getId()))
                .collect(Collectors.toList());
        model.addAttribute("users", usersWithoutAccounts);
        return "accounts/create";
    }

    @PostMapping("/create")
    public String createAccount(@ModelAttribute("account") Account account, Model model) {
        if (accountService.isAccountNumberExists(account.getAccountNumber())) {
            model.addAttribute("error", "Account number already exists");
            List<User> allUsers = userService.findAllUsers();
            List<User> usersWithoutAccounts = allUsers.stream()
                    .filter(user -> !accountService.isUserHasAccount(user.getId()))
                    .collect(Collectors.toList());
            model.addAttribute("users", usersWithoutAccounts);
            return "accounts/create";
        }
        accountService.save(account);
        return "redirect:/manager/accounts/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditAccountForm(@PathVariable("id") Long id, Model model) {
        Optional<Account> accountOptional = accountService.findAccountById(id);
        if (accountOptional.isPresent()) {
            model.addAttribute("account", accountOptional.get());
            model.addAttribute("users", userService.findAllUsers());
            return "accounts/edit";
        } else {
            return "redirect:/manager/accounts/list"; // Redirect if account not found
        }
    }

    @PostMapping("/edit/{id}")
    public String updateAccount(@PathVariable("id") Long id, @ModelAttribute("account") Account account, Model model) {
        Optional<Account> existingAccountOptional = accountService.findAccountById(id);
        if (existingAccountOptional.isPresent()) {
            Account existingAccount = existingAccountOptional.get();

            if (accountService.isAccountNumberExists(account.getAccountNumber()) && !existingAccount.getAccountNumber().equals(account.getAccountNumber())) {
                model.addAttribute("error", "Account number already exists");
                model.addAttribute("users", userService.findAllUsers());
                return "accounts/edit";
            }

            existingAccount.setAccountNumber(account.getAccountNumber());
            existingAccount.setBalance(account.getBalance());
            existingAccount.setUser(userService.findUserById(account.getUser().getId()).orElse(null));

            accountService.save(existingAccount);
            return "redirect:/manager/accounts/list";
        } else {
            return "redirect:/manager/accounts/list"; // Redirect if account not found
        }
    }
    @GetMapping("/admin/delete/{id}")
    public String deleteAccount(@PathVariable("id") Long id) {
        accountService.deleteById(id);
        return "redirect:/manager/accounts/list";
    }
    @GetMapping("/management")
    public String viewAccountManagement() {
        return "accounts/account-management";
    }


}

