package com.Rifath.BankingApp.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "loan")
public class Loan {

    public enum LoanStatus {
        PENDING,
        APPROVED,
        REJECTED,
        PAID
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;
    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "remaining_amount")
    private BigDecimal remainingAmount;
    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,name = "status")
    private LoanStatus status; // e.g., "Pending", "Approved", "Rejected"
    @Column(name = "applied_date")
    private LocalDate appliedDate;
    @Column(name = "approved_date")
    private LocalDate approvedDate;

    @Column(name = "rejected_date")
    private LocalDate rejectedDate;





    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<Payment> payments;



    public Loan(){

    }

    public Loan(BigDecimal amount, BigDecimal interestRate, LoanStatus status, LocalDate appliedDate, LocalDate approvedDate, Account account) {
        this.amount = amount;
        this.interestRate = interestRate;
        this.status = status;
        this.appliedDate = appliedDate;
        this.approvedDate = approvedDate;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public LocalDate getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
    }

    public LocalDate getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(LocalDate approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getAccountNumber() {
        return account.getAccountNumber();
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public LocalDate getRejectedDate() {
        return rejectedDate;
    }

    public void setRejectedDate(LocalDate rejectedDate) {
        this.rejectedDate = rejectedDate;
    }

    public void reduceAmount(BigDecimal paymentAmount) {
        this.remainingAmount = this.remainingAmount.subtract(paymentAmount);
    }
}
