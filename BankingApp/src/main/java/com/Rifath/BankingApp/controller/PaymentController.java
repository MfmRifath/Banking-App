package com.Rifath.BankingApp.controller;

import com.Rifath.BankingApp.dao.LoanRepository;
import com.Rifath.BankingApp.dao.PaymentRepository;
import com.Rifath.BankingApp.entity.Loan;
import com.Rifath.BankingApp.entity.Payment;
import com.Rifath.BankingApp.service.LoanService;
import com.Rifath.BankingApp.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/makepayment")
    public String showMakePaymentForm(@RequestParam(value = "loanId", required = false) Long loanId, Model model) {
        if (loanId != null) {
            loanService.findLoanById(loanId).ifPresent(loan -> model.addAttribute("loan", loan));
        }
        return "loan/makepayment";
    }

    @PostMapping("/postmakepayment")
    public String makePayment(@RequestParam(value = "loanId") Long id,BigDecimal amount,@RequestParam("paymentStatus") String paymentStatus,  Model model) {
        Optional<Loan> loanOpt = loanService.findLoanById(id);
        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            Payment payment =new Payment();

            // Additional logic can be added here if needed
            payment.setLoan(loan);
            payment.setAmount(amount);
            payment.setStatus(String.valueOf(Payment.PaymentStatus.valueOf(paymentStatus.toUpperCase())));
            payment.setDate(LocalDate.now());
            // Reduce the loan amount
            loan.setAmount(loan.getAmount().subtract(amount));

            if(loan.getAmount().equals(0)){
                payment.setStatus("COMPLETED"); // Set a default status for the payment
            }
            // Update the loan in the database
            loanRepository.save(loan);
            paymentRepository.save(payment);
            model.addAttribute("payment", payment);
            model.addAttribute("message", "Payment successful.");
            return "loan/paymentsuccess";
        }

        model.addAttribute("error","loan is not found");
        return "redirect:/payments/makepayments";
    }


    @GetMapping("/paymenthistory")
    public String paymentHistory(Model model) {
        List<Payment> payments = paymentService.findAllPayments();
        model.addAttribute("payments", payments);
        return "loan/paymenthistory";
    }

    @PostMapping("/delete/{id}")
    public String deletePayment(@PathVariable("id") Long id, Model model) {
        paymentService.deletePayment(id);
        return "redirect:/payments/paymenthistory";
    }
}
