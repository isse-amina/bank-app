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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

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

    @GetMapping("add-user")
    public String getAddUser(HttpServletRequest request, Model model) {
        String status = request.getParameter("status");
        if (status == null || !status.equalsIgnoreCase("success")) {
            status = "none";
        }
        String error = request.getParameter("error");
        if (error == null) {
            error = "none";
        }

        model.addAttribute("status", status);
        model.addAttribute("error", error);

        return "add-user";
    }

    @PostMapping("addUser")
    public String addUser(HttpServletRequest request, RedirectAttributes attributes) {
        try {
            String firstName = request.getParameter("first-name");
            String lastName = request.getParameter("last-name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String role = request.getParameter("role");

            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(role);
            userServiceLayer.addUser(user);

            attributes.addAttribute("status", "success");
        }
        catch (UserException e) {
            String errorMessage = e.getMessage();
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }

        return "redirect:/add-user";
    }

    @GetMapping("add-account")
    public String getAddAccount(HttpServletRequest request, Model model) {
        String status = request.getParameter("status");
        if (status == null || !status.equalsIgnoreCase("success")) {
            status = "none";
        }
        String error = request.getParameter("error");
        if (error == null) {
            error = "none";
        }

        model.addAttribute("status", status);
        model.addAttribute("error", error);

        return "add-account";
    }

    @PostMapping("addAccount")
    public String addAccount(HttpServletRequest request, RedirectAttributes attributes) {
        try {
            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String number = request.getParameter("number");
            BigDecimal balance = new BigDecimal(request.getParameter("balance"));
            String ownerEmail = request.getParameter("owner-email");

            User user = userServiceLayer.getUserByEmail(ownerEmail);

            Account account = new Account();
            account.setName(name);
            account.setType(type);
            account.setNumber(number);
            account.setBalance(balance);
            account.setAccountOwnerId(user.getId());

            accountServiceLayer.addAccount(account);

            attributes.addAttribute("status", "success");
        }
        catch (UserException e) {
            String errorMessage = e.getMessage();
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }
        catch (AccountException e) {
            String errorMessage = e.getMessage();
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }
        catch (NumberFormatException e) {
            String errorMessage = "Account balance must be a decimal number.";
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }

        return "redirect:/add-account";
    }

    @GetMapping("add-transaction")
    public String getAddTransaction(HttpServletRequest request, Model model) {
        String status = request.getParameter("status");
        if (status == null || !status.equalsIgnoreCase("success")) {
            status = "none";
        }
        String error = request.getParameter("error");
        if (error == null) {
            error = "none";
        }
        String userEmail = request.getParameter("user-email");
        User user;
        try {
            user = userServiceLayer.getUserByEmail(userEmail);
            List<Account> accounts = accountServiceLayer.getAccountsByUserId(user.getId());
            model.addAttribute("accounts", accounts);
        }
        catch (UserException e) {
            user = null;
        }

        model.addAttribute("user-email", userEmail);
        model.addAttribute("user", user);
        model.addAttribute("status", status);
        model.addAttribute("error", error);

        return "add-transaction";
    }

    @PostMapping("addUserToTransaction")
    public String addUserToTransaction(HttpServletRequest request, RedirectAttributes attributes) {
        try {
            String userEmail = request.getParameter("user-email");
            User user = userServiceLayer.getUserByEmail(userEmail);
            if (user.getRole().equalsIgnoreCase("admin")) {
                throw new UserException("Transaction cannot be performed by an admin.");
            }

            attributes.addAttribute("user-email", userEmail);
        }
        catch (UserException e) {
            String errorMessage = e.getMessage();
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }

        return "redirect:/add-transaction";
    }

    @PostMapping("addTransaction")
    public String addTransaction(HttpServletRequest request, RedirectAttributes attributes) {
        String userEmail = request.getParameter("user-email");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter = formatter.withLocale(Locale.US);
        try {
            LocalDate date = LocalDate.parse(request.getParameter("date"), formatter);
            int fromAccountId = Integer.parseInt(request.getParameter("from-account-id"));
            int toAccountId = Integer.parseInt(request.getParameter("to-account-id"));
            BigDecimal amount = new BigDecimal(request.getParameter("amount"));

            Transaction transaction = new Transaction();
            transaction.setDate(date);
            transaction.setFromAccountId(fromAccountId);
            transaction.setToAccountId(toAccountId);
            transaction.setAmount(amount);

            transactionServiceLayer.addTransaction(transaction);

            attributes.addAttribute("status", "success");
        }
        catch (AccountException e) {
            attributes.addAttribute("user-email", userEmail);

            String errorMessage = e.getMessage();
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }
        catch (TransactionException e) {
            attributes.addAttribute("user-email", userEmail);

            String errorMessage = e.getMessage();
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }
        catch (DateTimeParseException e) {
            attributes.addAttribute("user-email", userEmail);

            String errorMessage = "Date could not be parsed. Ensure that the date is in the appropriate format.";
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);

            System.out.println(userEmail);
        }
        catch (NumberFormatException e) {
            attributes.addAttribute("user-email", userEmail);

            String errorMessage = "Transaction amount must be a decimal number.";
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }

        return "redirect:/add-transaction";
    }

    @GetMapping("edit-user")
    public String getEditUser(HttpServletRequest request, Model model) throws UserException {
        int id = Integer.parseInt(request.getParameter("id"));
        User user = userServiceLayer.getUserById(id);

        String status = request.getParameter("status");
        if (status == null || !status.equalsIgnoreCase("success")) {
            status = "none";
        }
        String error = request.getParameter("error");
        if (error == null) {
            error = "none";
        }

        model.addAttribute("user", user);
        model.addAttribute("status", status);
        model.addAttribute("error", error);

        return "edit-user";
    }

    @PostMapping("editUser")
    public String editUser(HttpServletRequest request, RedirectAttributes attributes) {
        int id = Integer.parseInt(request.getParameter("id"));
        int newId = -1;
        try {
            String firstName = request.getParameter("first-name");
            String lastName = request.getParameter("last-name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String role = request.getParameter("role");

            User user = new User();
            user.setId(id);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(role);

            String initialRole = userServiceLayer.getUserById(id).getRole();
            String updatedRole = role;

            if (initialRole.equalsIgnoreCase("user") && updatedRole.equalsIgnoreCase("admin")) {
                userServiceLayer.deleteUserById(id);
                newId = userServiceLayer.addUser(user).getId();
            }
            else {
                userServiceLayer.updateUser(user);
            }

            attributes.addAttribute("status", "success");
        }
        catch (UserException e) {
            String errorMessage = e.getMessage();
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }

        if (newId == -1) {
            attributes.addAttribute("id", id);
        }
        else {
            attributes.addAttribute("id", newId);
        }

        return "redirect:/edit-user";
    }

    @GetMapping("edit-account")
    public String getEditAccount(HttpServletRequest request, Model model) throws AccountException {
        int id = Integer.parseInt(request.getParameter("id"));
        Account account = accountServiceLayer.getAccountById(id);

        String status = request.getParameter("status");
        if (status == null || !status.equalsIgnoreCase("success")) {
            status = "none";
        }
        String error = request.getParameter("error");
        if (error == null) {
            error = "none";
        }

        model.addAttribute("account", account);
        model.addAttribute("status", status);
        model.addAttribute("error", error);

        return "edit-account";
    }

    @PostMapping("editAccount")
    public String editAccount(HttpServletRequest request, RedirectAttributes attributes) {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String number = request.getParameter("number");
            BigDecimal balance = new BigDecimal(request.getParameter("balance"));
            String ownerEmail = request.getParameter("owner-email");

            User user = userServiceLayer.getUserByEmail(ownerEmail);

            Account account = new Account();
            account.setId(id);
            account.setName(name);
            account.setType(type);
            account.setNumber(number);
            account.setBalance(balance);
            account.setAccountOwnerId(user.getId());

            accountServiceLayer.updateAccount(account);

            attributes.addAttribute("status", "success");
        }
        catch (UserException e) {
            String errorMessage = e.getMessage();
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }
        catch (AccountException e) {
            String errorMessage = e.getMessage();
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }
        catch (NumberFormatException e) {
            String errorMessage = "Account balance must be a decimal number.";
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }

        attributes.addAttribute("id", id);

        return "redirect:/edit-account";
    }

    @GetMapping("edit-transaction")
    public String getEditTransaction(HttpServletRequest request, Model model) throws TransactionException {
        int id = Integer.parseInt(request.getParameter("id"));
        Transaction transaction = transactionServiceLayer.getTransactionById(id);
        List<Account> accounts = accountServiceLayer.getAccountsByUserId(transaction.getFromAccount().getAccountOwner().getId());

        String status = request.getParameter("status");
        if (status == null || !status.equalsIgnoreCase("success")) {
            status = "none";
        }
        String error = request.getParameter("error");
        if (error == null) {
            error = "none";
        }

        model.addAttribute("accounts", accounts);
        model.addAttribute("transaction", transaction);
        model.addAttribute("status", status);
        model.addAttribute("error", error);

        return "edit-transaction";
    }

    @PostMapping("editTransaction")
    public String editTransaction(HttpServletRequest request, RedirectAttributes attributes) {
        int id = Integer.parseInt(request.getParameter("id"));
        int newId = -1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter = formatter.withLocale(Locale.US);
        try {
            LocalDate date = LocalDate.parse(request.getParameter("date"), formatter);
            int fromAccountId = Integer.parseInt(request.getParameter("from-account-id"));
            int toAccountId = Integer.parseInt(request.getParameter("to-account-id"));
            BigDecimal amount = new BigDecimal(request.getParameter("amount"));

            Transaction transaction = new Transaction();
            transaction.setId(id);
            transaction.setDate(date);
            transaction.setFromAccountId(fromAccountId);
            transaction.setToAccountId(toAccountId);
            transaction.setAmount(amount);

            transaction = transactionServiceLayer.updateTransaction(transaction);
            newId = transaction.getId();

            attributes.addAttribute("status", "success");
        }
        catch (AccountException e) {
            String errorMessage = e.getMessage();
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }
        catch (TransactionException e) {
            String errorMessage = e.getMessage();
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }
        catch (DateTimeParseException e) {
            String errorMessage = "Date could not be parsed. Ensure that the date is in the appropriate format.";
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }
        catch (NumberFormatException e) {
            String errorMessage = "Transaction amount must be a decimal number.";
            attributes.addAttribute("status", "failed");
            attributes.addAttribute("error", errorMessage);
        }

        if (newId != -1) {
            attributes.addAttribute("id", newId);
        }

        return "redirect:/edit-transaction";
    }
}
