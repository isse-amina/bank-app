package com.app.bank.controllers;

import com.app.bank.dto.Account;
import com.app.bank.dto.Transaction;
import com.app.bank.dto.User;
import com.app.bank.exceptions.AccountException;
import com.app.bank.exceptions.TransactionException;
import com.app.bank.exceptions.UserException;
import com.app.bank.service.AccountServiceLayer;
import com.app.bank.service.TransactionServiceLayer;
import com.app.bank.service.UserServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    UserServiceLayer userServiceLayer;

    @Autowired
    AccountServiceLayer accountServiceLayer;

    @Autowired
    TransactionServiceLayer transactionServiceLayer;

    @GetMapping("/admin")
    public String getAdmin(Model model) {
        return "admin";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> users = userServiceLayer.getAllUsers();

        model.addAttribute("users", users);

        return "users";
    }

    @GetMapping("/accounts")
    public String getAccounts(Model model) throws AccountException {
        List<User> users = userServiceLayer.getAllUsers();
        List<Account> accounts = accountServiceLayer.getAllAccounts();

        String accountType;
        String accountNumber;
        for (Account account: accounts) {
            accountType = account.getType();
            accountNumber = account.getNumber();
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
        }

        model.addAttribute("users", users);
        model.addAttribute("accounts", accounts);

        return "accounts";
    }

    @GetMapping("/transactions")
    public String getTransactions(Model model) {
        List<User> users = userServiceLayer.getAllUsers();
        List<Transaction> transactions = transactionServiceLayer.getAllTransactions();

        model.addAttribute("users", users);
        model.addAttribute("transactions", transactions);

        return "transactions";
    }

    @GetMapping("deleteUser")
    public String deleteUser(HttpServletRequest request) throws UserException {
        int id = Integer.parseInt(request.getParameter("id"));
        userServiceLayer.deleteUserById(id);

        return "redirect:/users";
    }

    @GetMapping("deleteAccount")
    public String deleteAccount(HttpServletRequest request) throws AccountException {
        int id = Integer.parseInt(request.getParameter("id"));
        accountServiceLayer.deleteAccountById(id);

        return "redirect:/accounts";
    }

    @GetMapping("deleteTransaction")
    public String deleteTransaction(HttpServletRequest request) throws TransactionException, AccountException {
        int id = Integer.parseInt(request.getParameter("id"));
        transactionServiceLayer.deleteTransactionById(id);

        return "redirect:/transactions";
    }
}
