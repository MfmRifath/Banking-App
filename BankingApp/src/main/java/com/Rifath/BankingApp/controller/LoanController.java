package com.Rifath.BankingApp.controller;

import com.Rifath.BankingApp.entity.Account;
import com.Rifath.BankingApp.entity.Loan;
import com.Rifath.BankingApp.service.AccountService;
import com.Rifath.BankingApp.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;



    @Autowired
    private AccountService accountService;

    @GetMapping("/home")
    public String home() {
        return "loan/home";
    }

    @GetMapping("/applyloan")
    public String applyLoan() {
        return "loan/apply";
    }

    @PostMapping("/apply")
    public String applyLoan(@RequestParam BigDecimal amount, @RequestParam Double interestRate, @RequestParam Long accountId, Model model) {
        Optional<Account> accountOpt = accountService.findById(accountId);
        if (accountOpt.isPresent()) {
            Loan loan = new Loan();
            loan.setAmount(amount);
            loan.setInterestRate(BigDecimal.valueOf(interestRate));
            loan.setAppliedDate(LocalDate.now());
            loan.setStatus(Loan.LoanStatus.PENDING);
            loan.setAccount(accountOpt.get());
            loanService.applyLoan(loan);
            model.addAttribute("message", "Loan application submitted successfully.");
            return "redirect:/loans/applyloan";
        } else {
            model.addAttribute("error", "Account not found");
            return "loan/apply";
        }
    }







    @GetMapping("/showstatus")
    public String loanStatus() {
        return "loan/status";
    }

    @GetMapping("/status")
    public String getLoanStatus(@RequestParam("loanId") Long loanId, Model model) {
        Optional<Loan> loanOpt = loanService.findLoanById(loanId);
        if (loanOpt.isPresent()) {
            model.addAttribute("loan", loanOpt.get());
        } else {
            model.addAttribute("error", "Loan not found");
        }
        return "loan/status";
    }



    @GetMapping("/showhistory")
    public String loanHistory() {
        return "loan/history";
    }

    @GetMapping("/history")
    public String getLoanHistory(@RequestParam("accountId") Long accountId, Model model) {
        List<Loan> loans = loanService.getLoanHistoryById(accountId);
        if (loans != null && !loans.isEmpty()) {
            model.addAttribute("loans", loans);
            model.addAttribute("accountId", accountId);
        } else {
            model.addAttribute("error", "No loan history found for the provided Account ID");
        }
        return "loan/history";
    }

    @GetMapping("/all")
    public String getAllLoans(Model model) {
        List<Loan> loans = loanService.getAllLoans();
        model.addAttribute("loans", loans);
        return "loan/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditLoanForm(@PathVariable Long id, Model model) {
        Optional<Loan> loanOpt = loanService.findLoanById(id);
        if (loanOpt.isPresent()) {
            model.addAttribute("loan", loanOpt.get());
            model.addAttribute("loanStatuses", Loan.LoanStatus.values());
        } else {
            model.addAttribute("error", "Loan not found");
        }
        return "loan/edit";
    }

    @PostMapping("/update")
    public String updateLoan(@ModelAttribute Loan loan, @RequestParam Long accountId) {
        return accountService.findById(accountId).map(account -> {
            loan.setAccount(account);
            loanService.save(loan);
            return "redirect:/loans/all";
        }).orElseGet(() -> {
            // Handle error: account not found
            return "loan/edit"; // redirect to an error page or the edit page with error message
        });
    }

    @GetMapping("/approve")
    public String approveLoan(@RequestParam Long id) {
        Optional<Loan> loanOpt = loanService.findLoanById(id);
        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            loan.setStatus(Loan.LoanStatus.APPROVED);
            loan.setApprovedDate(LocalDate.now());
            loanService.updateLoan(loan);
        }
        return "redirect:/loans/all";
    }

    @GetMapping("/reject")
    public String rejectLoan(@RequestParam Long id) {
        Optional<Loan> loanOpt = loanService.findLoanById(id);
        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            loan.setStatus(Loan.LoanStatus.REJECTED);
            loan.setRejectedDate(LocalDate.now());
            loanService.updateLoan(loan);
        }
        return "redirect:/loans/all";
    }

    @GetMapping("/delete/{loanId}")
    public String deleteLoan(@PathVariable Long loanId) {
        loanService.deleteLoan(loanId);
        return "redirect:/loans/all";
    }
}
