package com.Rifath.BankingApp.service.transaction;

import com.Rifath.BankingApp.dao.AccountRepository;
import com.Rifath.BankingApp.dao.ScheduledPaymentRepository;
import com.Rifath.BankingApp.entity.Account;
import com.Rifath.BankingApp.entity.ScheduledPayment;
import com.Rifath.BankingApp.exception.InvalidAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledPaymentService {

    @Autowired
    private ScheduledPaymentRepository scheduledPaymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    public void schedulePayment(String accountNumber, double amount, LocalDateTime date, String description) throws InvalidAccountException {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new InvalidAccountException("Invalid account number"));

        ScheduledPayment scheduledPayment = new ScheduledPayment();
        scheduledPayment.setAccount(account);
        scheduledPayment.setAmount(amount);
        scheduledPayment.setScheduledDate(date);
        scheduledPayment.setDescription(description);

        scheduledPaymentRepository.save(scheduledPayment);
    }

    public List<ScheduledPayment> findScheduledPaymentsByUsername(String username) {
        return scheduledPaymentRepository.findByAccount_User_Username(username);
    }

    public List<ScheduledPayment> findAllScheduledPayments() {
        return scheduledPaymentRepository.findAll();
    }

}
