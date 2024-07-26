package com.Rifath.BankingApp.service;

import com.Rifath.BankingApp.dao.AccountRepository;
import com.Rifath.BankingApp.dao.TransferRepository;
import com.Rifath.BankingApp.entity.Account;
import com.Rifath.BankingApp.entity.Transfer;
import com.Rifath.BankingApp.exception.InsufficientBalanceException;
import com.Rifath.BankingApp.exception.InvalidAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private AccountRepository accountRepository;

    public void transfer(Long fromAccountId, Long toAccountId, double amount) throws InsufficientBalanceException, InvalidAccountException {
        Account fromAccount = accountRepository.findByAccountNumber(String.valueOf(fromAccountId))
                .orElseThrow(() -> new InvalidAccountException("Invalid from account ID"));
        Account toAccount = accountRepository.findByAccountNumber(String.valueOf(toAccountId))
                .orElseThrow(() -> new InvalidAccountException("Invalid to account ID"));

        if (fromAccount.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        Transfer transfer = new Transfer();
        transfer.setFromAccount(fromAccount);
        transfer.setToAccount(toAccount);
        transfer.setAmount(amount);
        transfer.setDate(LocalDateTime.now());

        transferRepository.save(transfer);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    public List<Transfer> findTransfersByUsername(String username) {
        return transferRepository.findByFromAccount_User_Username(username);
    }

    public List<Transfer> findAllTransfers() {
        return transferRepository.findAll();
    }



}
