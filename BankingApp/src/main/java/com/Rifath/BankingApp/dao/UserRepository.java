package com.Rifath.BankingApp.dao;

import com.Rifath.BankingApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
        User findByUsername(String username);
        User findUserById(Long id);


}

