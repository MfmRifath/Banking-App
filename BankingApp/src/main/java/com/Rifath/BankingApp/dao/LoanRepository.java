package com.Rifath.BankingApp.dao;

import com.Rifath.BankingApp.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {


    List<Loan> findByAccountId(Long accountId);

    List<Loan> findLoanHistoryById(Long loanId);

    Optional<Loan> findLoanById(Long loanId);
}
