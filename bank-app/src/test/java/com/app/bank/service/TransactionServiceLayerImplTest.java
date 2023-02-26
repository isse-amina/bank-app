package com.app.bank.service;

import com.app.bank.dto.*;
import com.app.bank.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class TransactionServiceLayerImplTest {
    @Autowired
    UserServiceLayer userServiceLayer;

    @Autowired
    AccountServiceLayer accountServiceLayer;

    @Autowired
    TransactionServiceLayer transactionServiceLayer;

    @BeforeEach
    public void setUp() {
        try {
            List<User> users = userServiceLayer.getAllUsers();
            for (User user: users) {
                userServiceLayer.deleteUserById(user.getId());
            }
            List<Account> accounts = accountServiceLayer.getAllAccounts();
            for (Account account: accounts) {
                accountServiceLayer.deleteAccountById(account.getId());
            }
            List<Transaction> transactions = transactionServiceLayer.getAllTransactions();
            for (Transaction transaction: transactions) {
                transactionServiceLayer.deleteTransactionById(transaction.getId());
            }
        }
        catch (UserException e) {
            fail("UserException should not be thrown.");
        }
        catch (AccountException e) {
            fail("AccountException should not be thrown.");
        }
        catch (TransactionException e) {
            fail("TransactionException should not be thrown.");
        }
    }

    @Test
    public void testValidateTransactionProperties() {
        Exception exception;
        User user = new User();
        user.setFirstName("Albert");
        user.setLastName("Einstein");
        try {
            user = userServiceLayer.addUser(user);
        }
        catch (UserException e) {
            fail("UserException should not be thrown.");
        }

        // test 1: from account does not exist
        Account account1 = new Account();
        account1.setName("Chequing");
        account1.setType("Account");
        account1.setNumber("1234567890");
        account1.setBalance(new BigDecimal("100.00"));
        account1.setAccountOwnerId(user.getId());
        try {
            account1 = accountServiceLayer.addAccount(account1);
        }
        catch (AccountException e) {
            fail("AccountException should not be thrown.");
        }
        Transaction transaction = new Transaction();
        transaction.setFromAccountId(account1.getId() + 1);
        exception = assertThrows(TransactionException.class, () -> transactionServiceLayer.validateTransactionProperties(transaction));
        assertTrue(exception.getMessage().equals("Account does not exist: An account must be opened before a transaction can be completed."));

        // test 2: to account does not exist
        transaction.setFromAccountId(account1.getId());
        Account account2 = new Account();
        account2.setName("Saving");
        account2.setType("Account");
        account2.setNumber("2345678901");
        account2.setBalance(new BigDecimal("200.00"));
        account2.setAccountOwnerId(user.getId());
        try {
            account2 = accountServiceLayer.addAccount(account2);
        }
        catch (AccountException e) {
            fail("AccountException should not be thrown.");
        }
        transaction.setToAccountId(account2.getId() + 1);
        exception = assertThrows(TransactionException.class, () -> transactionServiceLayer.validateTransactionProperties(transaction));
        assertTrue(exception.getMessage().equals("Account does not exist: An account must be opened before a transaction can be completed."));

        // test 3: from account id and to account id cannot be equal
        transaction.setToAccountId(account1.getId());
        exception = assertThrows(TransactionException.class, () -> transactionServiceLayer.validateTransactionProperties(transaction));
        assertTrue(exception.getMessage().equals("Funds cannot be transferred to the same account."));

        // test 4: transaction date is null
        transaction.setToAccountId(account2.getId());
        exception = assertThrows(TransactionException.class, () -> transactionServiceLayer.validateTransactionProperties(transaction));
        assertTrue(exception.getMessage().equals("Transaction date cannot be null."));

        // test 5: transaction date is in the future
        transaction.setDate(LocalDate.now().plusDays(1));
        exception = assertThrows(TransactionException.class, () -> transactionServiceLayer.validateTransactionProperties(transaction));
        assertTrue(exception.getMessage().equals("Transaction date cannot be in the future."));

        // test 6: transaction amount is null
        transaction.setDate(LocalDate.now());
        exception = assertThrows(TransactionException.class, () -> transactionServiceLayer.validateTransactionProperties(transaction));
        assertTrue(exception.getMessage().equals("Transaction amount cannot be null."));

        // test 7: transaction amount is negative
        transaction.setAmount(new BigDecimal("-10.00"));
        exception = assertThrows(TransactionException.class, () -> transactionServiceLayer.validateTransactionProperties(transaction));
        assertTrue(exception.getMessage().equals("Transaction amount must be greater than 0."));

        // test 8: transaction amount is equal to zero
        transaction.setAmount(new BigDecimal("0.00"));
        exception = assertThrows(TransactionException.class, () -> transactionServiceLayer.validateTransactionProperties(transaction));
        assertTrue(exception.getMessage().equals("Transaction amount must be greater than 0."));

        // test 9: transaction failed (from account balance < transaction amount)
        transaction.setAmount(new BigDecimal("100.01"));
        exception = assertThrows(TransactionException.class, () -> transactionServiceLayer.validateTransactionProperties(transaction));
        assertTrue(exception.getMessage().equals("Transaction failed: The account does not have enough funds to complete the transaction."));

        // test 10: transaction properties validated
        transaction.setAmount(new BigDecimal("10.00"));
        assertDoesNotThrow(() -> transactionServiceLayer.validateTransactionProperties(transaction));
        try {
            transactionServiceLayer.addTransaction(transaction);
        }
        catch (TransactionException e) {
            fail("TransactionException should not be thrown.");
        }
    }

    @Test
    public void testValidateTransactionExists() {
        Exception exception;
        User user = new User();
        user.setFirstName("Albert");
        user.setLastName("Einstein");
        try {
            user = userServiceLayer.addUser(user);
        }
        catch (UserException e) {
            fail("UserException should not be thrown.");
        }
        Account account1 = new Account();
        account1.setName("Chequing");
        account1.setType("Account");
        account1.setNumber("1234567890");
        account1.setBalance(new BigDecimal("100.00"));
        account1.setAccountOwnerId(user.getId());
        try {
            account1 = accountServiceLayer.addAccount(account1);
        }
        catch (AccountException e) {
            fail("AccountException should not be thrown.");
        }
        Account account2 = new Account();
        account2.setName("Saving");
        account2.setType("Account");
        account2.setNumber("2345678901");
        account2.setBalance(new BigDecimal("200.00"));
        account2.setAccountOwnerId(user.getId());
        try {
            account2 = accountServiceLayer.addAccount(account2);
        }
        catch (AccountException e) {
            fail("AccountException should not be thrown.");
        }
        Transaction transaction = new Transaction();
        transaction.setDate(LocalDate.of(2001, 1, 1));
        transaction.setFromAccountId(account1.getId());
        transaction.setToAccountId(account2.getId());
        transaction.setAmount(new BigDecimal("10.00"));
        try {
            transaction = transactionServiceLayer.addTransaction(transaction);
        }
        catch (TransactionException e) {
            fail("TransactionException should not be thrown.");
        }
        int transactionId = transaction.getId();

        // test 1: transaction does not exist
        exception = assertThrows(TransactionException.class, () -> transactionServiceLayer.validateTransactionExists(transactionId + 1));
        assertTrue(exception.getMessage().equals("Transaction does not exist."));

        // test 2: transaction existence validated
        assertDoesNotThrow(() -> transactionServiceLayer.validateTransactionExists(transactionId));
    }
}
