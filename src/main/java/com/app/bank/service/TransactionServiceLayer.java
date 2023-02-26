package com.app.bank.service;

import com.app.bank.dto.Transaction;
import com.app.bank.exceptions.TransactionException;

import java.util.List;

public interface TransactionServiceLayer {
    Transaction getTransactionById(int id) throws TransactionException;
    List<Transaction> getAllTransactions();
    List<Transaction> getTransactionsByAccountId(int accountId);
    Transaction addTransaction(Transaction transaction) throws TransactionException;
    void updateTransaction(Transaction transaction) throws TransactionException;
    void deleteTransactionById(int id) throws TransactionException;
    void validateTransactionProperties(Transaction transaction) throws TransactionException;
    void validateTransactionExists(int id) throws TransactionException;
}
