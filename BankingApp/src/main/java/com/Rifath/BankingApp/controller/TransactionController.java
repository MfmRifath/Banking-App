package com.Rifath.BankingApp.controller;

import com.Rifath.BankingApp.entity.BillPayment;
import com.Rifath.BankingApp.entity.ScheduledPayment;
import com.Rifath.BankingApp.entity.Transaction;
import com.Rifath.BankingApp.entity.Transfer;
import com.Rifath.BankingApp.exception.InsufficientBalanceException;
import com.Rifath.BankingApp.exception.InvalidAccountException;
import com.Rifath.BankingApp.service.TransactionService;
import com.Rifath.BankingApp.service.TransferService;
import com.Rifath.BankingApp.service.transaction.BillPaymentService;
import com.Rifath.BankingApp.service.transaction.ScheduledPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private BillPaymentService billPaymentService;

    @Autowired
    private ScheduledPaymentService scheduledPaymentService;



    @GetMapping("/home")
    public String viewAccountManagement(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        List<Transfer> transfers = getTransfersForUser(auth, username);
        List<BillPayment> billPayments = getBillPaymentsForUser(auth, username);
        List<ScheduledPayment> scheduledPayments = getScheduledPaymentsForUser(auth, username);

        model.addAttribute("transfers", transfers);
        model.addAttribute("billPayments", billPayments);
        model.addAttribute("scheduledPayments", scheduledPayments);
        return "transaction/home";
    }

    @GetMapping("/transfer")
    public String showTransferForm(Model model) {
        return "transaction/transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam double amount, Model model) {
        try {
            transferService.transfer(fromAccountId, toAccountId, amount);
            model.addAttribute("successMessage", "Transfer successful");
        } catch (InsufficientBalanceException | InvalidAccountException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "transaction/transfer";
    }

    @GetMapping("/bill")
    public String showBillPaymentForm(Model model) {
        return "transaction/bill_payment";
    }

    @PostMapping("/bill")
    public String payBill(@RequestParam String accountNumber,
                          @RequestParam String billType,
                          @RequestParam Double amount,
                          @RequestParam String description,
                          Model model) {
        try {
            billPaymentService.payBill(accountNumber, billType, amount, description);
            model.addAttribute("message", "Bill payment successful.");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "transaction/bill_payment";
    }

    @GetMapping("/schedule")
    public String showScheduledPaymentForm(Model model) {
        return "transaction/scheduled_payment";
    }

    @PostMapping("/schedule")
    public String schedulePayment(@RequestParam String accountNumber,
                                  @RequestParam Double amount,
                                  @RequestParam LocalDateTime date,
                                  @RequestParam String description,
                                  Model model) {
        try {
            scheduledPaymentService.schedulePayment(accountNumber, amount, date, description);
            model.addAttribute("message", "Scheduled payment successful.");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "transaction/scheduled_payment";
    }

    @GetMapping("/transferList")
    public String showTransferList(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Transfer> transfers = getTransfersForUser(auth, username);
        model.addAttribute("transfers", transfers);
        return "transaction/transfer_list";
    }

    @GetMapping("/billPaymentList")
    public String showBillPaymentList(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<BillPayment> billPayments = getBillPaymentsForUser(auth, username);
        model.addAttribute("billPayments", billPayments);
        return "transaction/bill_list";
    }

    @GetMapping("/scheduledPaymentList")
    public String showScheduledPaymentList(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<ScheduledPayment> scheduledPayments = getScheduledPaymentsForUser(auth, username);
        model.addAttribute("scheduledPayments", scheduledPayments);
        return "transaction/scheduled_payment_list";
    }


    private List<Transfer> getTransfersForUser(Authentication auth, String username) {
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
            return transferService.findAllTransfers();
        } else {
            return transferService.findTransfersByUsername(username);
        }
    }

    private List<BillPayment> getBillPaymentsForUser(Authentication auth, String username) {
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
            return billPaymentService.findAllBillPayments();
        } else {
            return billPaymentService.findBillPaymentsByUsername(username);
        }
    }

    private List<ScheduledPayment> getScheduledPaymentsForUser(Authentication auth, String username) {
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
            return scheduledPaymentService.findAllScheduledPayments();
        } else {
            return scheduledPaymentService.findScheduledPaymentsByUsername(username);
        }
    }

    @Autowired
    private TransactionService transactionService;


    @PostMapping("/deposit")
    public String depositMoney(@RequestParam String accountNumber, @RequestParam double amount, Model model) {
        try {
            String message = transactionService.depositMoney(accountNumber, amount);
            model.addAttribute("successMessage", message);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "transaction/home";
    }

    @PostMapping("/withdraw")
    public String withdrawMoney(@RequestParam String accountNumber, @RequestParam double amount, Model model) {
        try {
            String message = transactionService.withdrawMoney(accountNumber, amount);
            model.addAttribute("successMessage", message);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "transaction/home";
    }
    @GetMapping("/home2")
    public String showTransactions(@RequestParam("accountNumber") String accountNumber, Model model) {
        // Assume you have a service to fetch transactions by account number
        List<Transaction> transactions = transactionService.findByAccountNumber(accountNumber);
        model.addAttribute("transactions", transactions);
        model.addAttribute("accountNumber", accountNumber);
        return "transaction/home";
    }
}
