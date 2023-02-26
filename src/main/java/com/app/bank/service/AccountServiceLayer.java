package com.app.bank.service;

import com.app.bank.dto.Account;
import com.app.bank.exceptions.AccountException;

import java.util.List;

public interface AccountServiceLayer {
    Account getAccountById(int id) throws AccountException;
    List<Account> getAllAccounts();
    List<Account> getAccountsByUserId(int accountOwnerId);
    Account addAccount(Account account) throws AccountException;
    void updateAccount(Account account) throws AccountException;
    void deleteAccountById(int id) throws AccountException;
    void validateAccountProperties(Account account) throws AccountException;
    void validateAccountExists(int id) throws AccountException;
}
