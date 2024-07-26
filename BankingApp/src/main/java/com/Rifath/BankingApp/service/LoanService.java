package com.Rifath.BankingApp.service;

import com.Rifath.BankingApp.dao.LoanRepository;
import com.Rifath.BankingApp.entity.Loan;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    public void applyLoan(Loan loan) {
        loanRepository.save(loan);
    }

    public Loan getLoanById(Long loanId) {
        return loanRepository.findById(loanId).orElse(null);
    }

    public List<Loan> getLoanHistoryByAccountId(Long accountId) {
        return loanRepository.findByAccountId(accountId);
    }




    public List<Loan> getLoanHistoryById(Long loanId) {
        return loanRepository.findLoanHistoryById(loanId);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }

    public void deleteLoan(Long loanId) {
        loanRepository.deleteById(loanId);
    }

    public void updateLoan(Loan loan) {
        loanRepository.save(loan);
    }

    public void save(Loan loan) {
        loanRepository.save(loan);
    }

    public Optional<Loan> findById(Long id) {
        return loanRepository.findById(id);
    }

    @Transactional
    public void makePayment(Long loanId, Double paymentAmount) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus().equals("APPROVED")) {
            BigDecimal newAmount = loan.getAmount().subtract(BigDecimal.valueOf(paymentAmount)) ;
            loan.setAmount(newAmount);

            // Update loan status if fully paid
            if (newAmount == null) {
                loan.setStatus(Loan.LoanStatus.valueOf("PAID"));
            }

            loanRepository.save(loan);
        } else {
            throw new RuntimeException("Cannot make a payment on a loan that is not approved.");
        }
    }

    public Optional<Loan> findLoanById(Long loanId) {
        return loanRepository.findLoanById(loanId);
    }
}
