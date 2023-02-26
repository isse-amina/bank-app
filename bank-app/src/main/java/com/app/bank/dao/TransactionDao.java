package com.app.bank.dao;

import com.app.bank.dto.Transaction;

import java.util.List;

public interface TransactionDao {
    Transaction getTransactionById(int id);
    List<Transaction> getAllTransactions();
    List<Transaction> getTransactionsByAccountId(int accountId);
    Transaction addTransaction(Transaction transaction);
    void updateTransaction(Transaction transaction);
    void deleteTransactionById(int id);
}
