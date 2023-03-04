package com.app.bank.service;

import com.app.bank.dto.Transaction;
import com.app.bank.exceptions.AccountException;
import com.app.bank.exceptions.TransactionException;

import java.util.List;

public interface TransactionServiceLayer {
    Transaction getTransactionById(int id) throws TransactionException;
    List<Transaction> getAllTransactions();
    List<Transaction> getTransactionsByAccountId(int accountId);
    Transaction addTransaction(Transaction transaction) throws TransactionException, AccountException;
    Transaction updateTransaction(Transaction transaction) throws TransactionException, AccountException;
    void deleteTransactionById(int id) throws TransactionException, AccountException;
    void validateTransactionProperties(Transaction transaction) throws TransactionException;
    void validateTransactionExists(int id) throws TransactionException;
}
