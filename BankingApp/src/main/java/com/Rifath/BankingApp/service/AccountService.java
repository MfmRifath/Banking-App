package com.Rifath.BankingApp.service;

import com.Rifath.BankingApp.dao.AccountRepository;
import com.Rifath.BankingApp.dao.UserRepository;
import com.Rifath.BankingApp.entity.Account;
import com.Rifath.BankingApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

     public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountForCurrentUser() {
        // Get the current authenticated user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Find the user by username
        User user = userRepository.findByUsername(username);
        if (user != null) {
            // Find and return the account associated with the user
            Optional<Account> accountOptional = accountRepository.findByUserId(user.getId());
            return accountOptional.orElse(null);
        }
        return null;
    }
    public boolean accountExists(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).isPresent();
    }



    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }
    public List<Account> findAll() {
        return accountRepository.findAll();
    }




    public Optional<Account> findAccountById(Long id) {
        return accountRepository.findById(id);
    }


    public void save(Account account) {
        accountRepository.save(account);
    }

    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    public boolean isAccountNumberExists(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber) != null;
    }


    public boolean isUserHasAccount(Long userId) {
        return !accountRepository.findByUserId(userId).isEmpty();
    }

    public List<Account> findByUser(Optional<User> user) {
         return accountRepository.findByUser(user);
    }
    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }

    public Optional<Account> findById(Long accountId) {
         return accountRepository.findById(accountId);
    }
}
