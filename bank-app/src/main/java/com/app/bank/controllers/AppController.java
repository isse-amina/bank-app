package com.app.bank.controllers;

import com.app.bank.dto.Account;
import com.app.bank.dto.Transaction;
import com.app.bank.dto.User;
import com.app.bank.exceptions.AccountException;
import com.app.bank.exceptions.TransactionException;
import com.app.bank.service.AccountServiceLayer;
import com.app.bank.service.TransactionServiceLayer;
import com.app.bank.service.UserServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AppController {
    @Autowired
    UserServiceLayer userServiceLayer;

    @Autowired
    AccountServiceLayer accountServiceLayer;

    @Autowired
    TransactionServiceLayer transactionServiceLayer;

    @GetMapping("/")
    public String getIndex(Model model) throws AccountException {
        // placeholder starts
        List<User> users = userServiceLayer.getAllUsers();
        User user = users.get(0);
        // placeholder ends

        List<Account> accounts = accountServiceLayer.getAccountsByUserId(user.getId());
        List<Account> bankAccounts = new ArrayList<>();
        List<Account> creditCards = new ArrayList<>();
        for (Account account : accounts) {
            String accountType = account.getType();
            String accountNumber = account.getNumber();
            if (accountType.equalsIgnoreCase("account")) {
                accountNumber = new StringBuilder(accountNumber).insert(3, " ").toString();
                bankAccounts.add(account);
            }
            else if (accountType.equalsIgnoreCase("credit card")) {
                accountNumber = new StringBuilder(accountNumber).insert(4, " ").insert(9, " ").toString();
                creditCards.add(account);
            }
            else {
                throw new AccountException("Account type must be 'Account' or 'Credit Card'.");
            }
            account.setNumber(accountNumber);
        }

        model.addAttribute("user", user);
        model.addAttribute("bankAccounts", bankAccounts);
        model.addAttribute("creditCards", creditCards);

        return "index";
    }

    @GetMapping("history")
    public String viewOrganizations(HttpServletRequest request, Model model) throws AccountException {
        int accountId = Integer.parseInt(request.getParameter("id"));
        Account account = accountServiceLayer.getAccountById(accountId);
        String accountType = account.getType();
        String accountNumber = account.getNumber();
        if (accountType.equalsIgnoreCase("account")) {
            accountNumber = new StringBuilder(accountNumber).insert(3, " ").toString();
        }
        else if (accountType.equalsIgnoreCase("credit card")) {
            accountNumber = new StringBuilder(accountNumber).insert(4, " ").insert(9, " ").toString();
        }
        else {
            throw new AccountException("Account type must be 'Account' or 'Credit Card'.");
        }
        account.setNumber(accountNumber);

        List<Transaction> transactions = transactionServiceLayer.getTransactionsByAccountId(accountId);

        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);

        return "history";
    }

    @GetMapping("transfer")
    public String getTransfer(HttpServletRequest request, Model model) {
        // placeholder starts
        List<User> users = userServiceLayer.getAllUsers();
        User user = users.get(0);
        // placeholder ends

        List<Account> accounts = accountServiceLayer.getAccountsByUserId(user.getId());
        List<Account> bankAccounts = new ArrayList<>();
        for (Account account : accounts) {
            String accountType = account.getType().toLowerCase();
            if (accountType.equalsIgnoreCase("account")) {
                bankAccounts.add(account);
            }
        }

        String status = request.getParameter("status");
        if (status == null || !status.equalsIgnoreCase("success")) {
            status = "none";
        }
        String error = request.getParameter("error");
        if (error == null) {
            error = "none";
        }

        model.addAttribute("bankAccounts", bankAccounts);
        model.addAttribute("accounts", accounts);
        model.addAttribute("status", status);
        model.addAttribute("error", error);

        return "transfer";
    }

    @PostMapping("executeTransfer")
    public String executeTransfer(HttpServletRequest request, RedirectAttributes attributes) {
        // placeholder starts
        List<User> users = userServiceLayer.getAllUsers();
        User user = users.get(0);
        // placeholder ends

        try {
            int bankAccountId = Integer.parseInt(request.getParameter("bank-account"));
            int accountId = Integer.parseInt(request.getParameter("account"));
            BigDecimal transactionAmount = new BigDecimal(request.getParameter("transfer-amount"));

            Transaction transaction = new Transaction();
            transaction.setDate(LocalDate.now());
            transaction.setFromAccountId(bankAccountId);
            transaction.setToAccountId(accountId);
            transaction.setAmount(transactionAmount);
            transactionServiceLayer.addTransaction(transaction);

            attributes.addAttribute("status", "success");
        }
        catch (AccountException e) {
            String errorMessage = e.getMessage();
            attributes.addAttribute("error", errorMessage);
            attributes.addAttribute("error", errorMessage);
        }
        catch (TransactionException e) {
            String errorMessage = e.getMessage();
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }
        catch (NumberFormatException e) {
            String errorMessage = "Transaction amount cannot be null.";
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }
        catch (Exception e) {
            String errorMessage = e.getMessage();
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }

        return "redirect:/transfer";
    }

    @GetMapping("contact")
    public String getContact(Model model) {
        return "contact";
    }
}
