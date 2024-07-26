package com.Rifath.BankingApp.dao;

import com.Rifath.BankingApp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByAccountAccountNumber(String accountNumber);

    Transaction save(Transaction transaction);
}
