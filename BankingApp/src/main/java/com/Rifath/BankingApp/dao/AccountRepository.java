package com.Rifath.BankingApp.dao;

import com.Rifath.BankingApp.entity.Account;
import com.Rifath.BankingApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUser(Optional<User> user);

    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findByUserId(Long userId);


}
