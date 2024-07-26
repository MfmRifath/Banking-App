package com.Rifath.BankingApp.service.transaction;

import com.Rifath.BankingApp.dao.AccountRepository;
import com.Rifath.BankingApp.dao.BillPaymentRepository;
import com.Rifath.BankingApp.entity.Account;
import com.Rifath.BankingApp.entity.BillPayment;
import com.Rifath.BankingApp.exception.InsufficientBalanceException;
import com.Rifath.BankingApp.exception.InvalidAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillPaymentService {

    @Autowired
    private BillPaymentRepository billPaymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    public void payBill(String accountNumber, String billType, double amount, String description) throws InvalidAccountException, InsufficientBalanceException {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new InvalidAccountException("Invalid account number"));

        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);

        BillPayment billPayment = new BillPayment();
        billPayment.setAccount(account);
        billPayment.setBillType(billType);
        billPayment.setAmount(amount);
        billPayment.setDescription(description);
        billPayment.setDate(LocalDateTime.now());

        billPaymentRepository.save(billPayment);
        accountRepository.save(account);
    }

    public List<BillPayment> findBillPaymentsByUsername(String username) {
        return billPaymentRepository.findByAccount_User_Username(username);
    }

    public List<BillPayment> findAllBillPayments() {
        return billPaymentRepository.findAll();
    }
}
