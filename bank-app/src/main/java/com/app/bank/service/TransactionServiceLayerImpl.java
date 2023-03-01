package com.app.bank.service;

import com.app.bank.dao.AccountDao;
import com.app.bank.dao.TransactionDao;
import com.app.bank.dto.Account;
import com.app.bank.dto.Transaction;
import com.app.bank.exceptions.AccountException;
import com.app.bank.exceptions.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionServiceLayerImpl implements TransactionServiceLayer {
    @Autowired
    AccountDao accountDao;
    @Autowired
    TransactionDao transactionDao;

    @Override
    public Transaction getTransactionById(int id) throws TransactionException {
        validateTransactionExists(id);
        return transactionDao.getTransactionById(id);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionDao.getAllTransactions();
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(int accountId) {
        return transactionDao.getTransactionsByAccountId(accountId);
    }

    @Override
    public Transaction addTransaction(Transaction transaction) throws TransactionException, AccountException {
        validateTransactionProperties(transaction);

        BigDecimal transactionAmount = transaction.getAmount();

        int bankAccountId = transaction.getFromAccountId();
        int accountId = transaction.getToAccountId();

        Account bankAccount = accountDao.getAccountById(bankAccountId);
        Account account = accountDao.getAccountById(accountId);

        BigDecimal fromAccountBalance = bankAccount.getBalance();
        BigDecimal toAccountBalance = account.getBalance();

        bankAccount.setBalance(fromAccountBalance.subtract(transactionAmount));
        if (account.getType().equalsIgnoreCase("account")) {
            account.setBalance(toAccountBalance.add(transactionAmount));
        }
        else if (account.getType().equalsIgnoreCase("credit card")) {
            account.setBalance(toAccountBalance.subtract(transactionAmount));
        }
        else {
            throw new AccountException("Account type must be 'Account' or 'Credit Card'.");
        }

        accountDao.updateAccount(bankAccount);
        accountDao.updateAccount(account);

        return transactionDao.addTransaction(transaction);
    }

    @Override
    public void updateTransaction(Transaction transaction) throws TransactionException, AccountException {
        deleteTransactionById(transaction.getId());
        addTransaction(transaction);
    }

    @Override
    public void deleteTransactionById(int id) throws TransactionException, AccountException {
        validateTransactionExists(id);

        Transaction transaction = transactionDao.getTransactionById(id);

        BigDecimal transactionAmount = transaction.getAmount();

        int bankAccountId = transaction.getFromAccountId();
        int accountId = transaction.getToAccountId();

        Account bankAccount = accountDao.getAccountById(bankAccountId);
        Account account = accountDao.getAccountById(accountId);

        BigDecimal fromAccountBalance = bankAccount.getBalance();
        BigDecimal toAccountBalance = account.getBalance();

        bankAccount.setBalance(fromAccountBalance.add(transactionAmount));
        if (account.getType().equalsIgnoreCase("account")) {
            account.setBalance(toAccountBalance.subtract(transactionAmount));
        }
        else if (account.getType().equalsIgnoreCase("credit card")) {
            account.setBalance(toAccountBalance.add(transactionAmount));
        }
        else {
            throw new AccountException("Account type must be 'Account' or 'Credit Card'.");
        }

        accountDao.updateAccount(bankAccount);
        accountDao.updateAccount(account);

        transactionDao.deleteTransactionById(id);
    }

    @Override
    public void validateTransactionProperties(Transaction transaction) throws TransactionException {
        int fromAccountId = transaction.getFromAccountId();
        int toAccountId = transaction.getToAccountId();
        if (accountDao.getAccountById(fromAccountId) == null || accountDao.getAccountById(toAccountId) == null) {
            throw new TransactionException("Account does not exist: An account must be opened before a transaction can be completed.");
        }
        if (fromAccountId == toAccountId) {
            throw new TransactionException("Funds cannot be transferred to the same account.");
        }
        if (accountDao.getAccountById(fromAccountId).getType().equalsIgnoreCase("credit card")) {
            throw new TransactionException("Funds cannot be transferred from a credit card.");
        }
        LocalDate transactionDate = transaction.getDate();
        if (transactionDate == null) {
            throw new TransactionException("Transaction date cannot be null.");
        }
        if (transactionDate.isAfter(LocalDate.now())) {
            throw new TransactionException("Transaction date cannot be in the future.");
        }
        BigDecimal transactionAmount = transaction.getAmount();
        if (transactionAmount == null) {
            throw new TransactionException("Transaction amount cannot be null.");
        }
        if (transactionAmount.compareTo(new BigDecimal("0.00")) != 1) {
            throw new TransactionException("Transaction amount must be greater than 0.");
        }
        BigDecimal fromAccountBalance = accountDao.getAccountById(fromAccountId).getBalance();
        if (fromAccountBalance.compareTo(transactionAmount) < 0) {
            throw new TransactionException("Transaction failed: The account does not have enough funds to complete the transaction.");
        }
    }

    @Override
    public void validateTransactionExists(int id) throws TransactionException {
        if (transactionDao.getTransactionById(id) == null) {
            throw new TransactionException("Transaction does not exist.");
        }
    }
}
