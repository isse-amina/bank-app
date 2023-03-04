package com.app.bank.service;

import com.app.bank.dao.AccountDao;
import com.app.bank.dao.UserDao;
import com.app.bank.dto.Account;
import com.app.bank.exceptions.AccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceLayerImpl implements AccountServiceLayer {
    @Autowired
    UserDao userDao;
    @Autowired
    AccountDao accountDao;

    @Override
    public Account getAccountById(int id) throws AccountException {
        validateAccountExists(id);
        return accountDao.getAccountById(id);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountDao.getAllAccounts();
    }

    @Override
    public List<Account> getAccountsByUserId(int accountOwnerId) {
        return accountDao.getAccountsByUserId(accountOwnerId);
    }

    @Override
    public Account addAccount(Account account) throws AccountException {
        validateAccountProperties(account);
        return accountDao.addAccount(account);
    }

    @Override
    public void updateAccount(Account account) throws AccountException {
        validateAccountProperties(account);
        accountDao.updateAccount(account);
    }

    @Override
    public void deleteAccountById(int id) throws AccountException {
        validateAccountExists(id);
        accountDao.deleteAccountById(id);
    }

    @Override
    public void validateAccountProperties(Account account) throws AccountException {
        int accountOwnerId = account.getAccountOwnerId();
        if (userDao.getUserById(accountOwnerId) == null) {
            throw new AccountException("User does not exist: A user must be created before an account can be opened.");
        }
        if (userDao.getUserById(accountOwnerId).getRole().equalsIgnoreCase("admin")) {
            throw new AccountException("Account owner cannot be an admin.");
        }
        String accountName = account.getName();
        if (accountName == null || accountName.isBlank()) {
            throw new AccountException("Account name cannot be empty.");
        }
        String accountType = account.getType();
        if (accountType == null || accountType.isBlank()) {
            throw new AccountException("Account type cannot be empty.");
        }
        if (!accountType.equalsIgnoreCase("account") && !accountType.equalsIgnoreCase("credit card")) {
            throw  new AccountException("Account type must be 'Account' or 'Credit Card'.");
        }
        String accountNumber = account.getNumber();
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new AccountException("Account number cannot be empty.");
        }
        try {
            Long accountNumberLong = Long.parseLong(account.getNumber());
            if (accountNumberLong != Math.abs(accountNumberLong)) {
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e) {
            throw new AccountException("Account number must only contain numerical values.");
        }
        if (accountType.equalsIgnoreCase("account")) {
            if (accountNumber.length() != 7) {
                throw new AccountException("Bank account number must consist of 7 digits.");
            }
        }
        else if (accountType.equalsIgnoreCase("credit card")) {
            if (accountNumber.length() != 12) {
                throw new AccountException("Credit card number must consist of 12 digits.");
            }
        }
        else {
            throw new AccountException("Account type must be 'Account' or 'Credit Card'.");
        }
        try {
            BigDecimal accountBalance = account.getBalance();
            if (accountBalance == null) {
                throw new AccountException("Account balance cannot be null.");
            }
            if (accountBalance.compareTo(new BigDecimal("0.00")) < 0 && accountType.equalsIgnoreCase("account")) {
                throw new AccountException("Accounts of type 'Account' must have a positive balance.");
            }
        }
        catch (NumberFormatException e) {
            throw new AccountException("Account balance must be a decimal number.");
        }
    }

    @Override
    public void validateAccountExists(int id) throws AccountException {
        if (accountDao.getAccountById(id) == null) {
            throw new AccountException("Account does not exist.");
        }
    }
}
