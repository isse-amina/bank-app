package com.app.bank.dao;

import com.app.bank.dto.Account;

import java.util.List;

public interface AccountDao {
    Account getAccountById(int id);
    List<Account> getAllAccounts();
    List<Account> getAccountsByUserId(int accountOwnerId);
    Account addAccount(Account account);
    void updateAccount(Account account);
    void deleteAccountById(int id);
}
