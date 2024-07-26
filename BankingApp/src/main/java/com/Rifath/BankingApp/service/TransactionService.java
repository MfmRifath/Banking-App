package com.Rifath.BankingApp.service;

import com.Rifath.BankingApp.dao.AccountRepository;
import com.Rifath.BankingApp.dao.TransactionRepository;
import com.Rifath.BankingApp.entity.Account;
import com.Rifath.BankingApp.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Transaction createTransaction(Transaction transaction, String accountNumber) {
        Optional<Account> optionalaccount = accountRepository.findByAccountNumber(accountNumber);
        if (optionalaccount != null) {
            Account account =optionalaccount.get();
            transaction.setAccount(account);
            return transactionRepository.save(transaction);
        }
        throw new RuntimeException("Account not found");
    }

    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        return transactionRepository.findByAccountAccountNumber(accountNumber);
    }

    public String depositMoney(String accountNumber, double amount) {
        Optional<Account> optionalaccount = accountRepository.findByAccountNumber(accountNumber);
        if (optionalaccount != null) {

            Account account = optionalaccount.get();
            account.setBalance(account.getBalance() + amount);
            accountRepository.save(account);

            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setTransactionType("Credit");
            transaction.setTransactionDate(new Date());
            transaction.setAccount(account);
            transactionRepository.save(transaction);

            return "Deposit successful";
        }
        return "Account not found";
    }

    public String withdrawMoney(String accountNumber, double amount) {
        Optional<Account> optionalaccount = accountRepository.findByAccountNumber(accountNumber);
        if (optionalaccount != null) {

            Account account = optionalaccount.get();
            if (account.getBalance() >= amount) {
                account.setBalance(account.getBalance() - amount);
                accountRepository.save(account);

                Transaction transaction = new Transaction();
                transaction.setAmount(amount);
                transaction.setTransactionType("Debit");
                transaction.setTransactionDate(new Date());
                transaction.setAccount(account);
                transactionRepository.save(transaction);

                return "Withdrawal successful";
            } else {
                return "Insufficient balance";
            }
        }
        return "Account not found";
    }

    public List<Transaction> findByAccountNumber(String accountNumber) {
        return transactionRepository.findByAccountAccountNumber(accountNumber);
    }
}
