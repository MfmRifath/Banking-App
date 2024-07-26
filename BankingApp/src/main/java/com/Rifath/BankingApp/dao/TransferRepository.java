package com.Rifath.BankingApp.dao;

import com.Rifath.BankingApp.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer,Long> {
    List<Transfer> findByFromAccount_User_Username(String username);
}
