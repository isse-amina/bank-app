package com.app.bank.dao;

import com.app.bank.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionDaoDBTest {
    @Autowired
    UserDao userDao;

    @Autowired
    AccountDao accountDao;

    @Autowired
    TransactionDao transactionDao;

    @BeforeEach
    public void setUp() {
        List<User> users = userDao.getAllUsers();
        for (User user: users) {
            userDao.deleteUserById(user.getId());
        }
        List<Account> accounts = accountDao.getAllAccounts();
        for (Account account: accounts) {
            accountDao.deleteAccountById(account.getId());
        }
        List<Transaction> transactions = transactionDao.getAllTransactions();
        for (Transaction transaction: transactions) {
            transactionDao.deleteTransactionById(transaction.getId());
        }
    }

    @Test
    public void testAddAndGetTransaction() {
        User user = new User();
        user.setFirstName("Albert");
        user.setLastName("Einstein");
        user.setEmail("ae@gmail.com");
        user.setPassword("temp");
        userDao.addUser(user);

        Account account1 = new Account();
        account1.setName("Chequing");
        account1.setType("Account");
        account1.setNumber("1234567");
        account1.setBalance(new BigDecimal("100.00"));
        account1.setAccountOwnerId(user.getId());
        accountDao.addAccount(account1);

        Account account2 = new Account();
        account2.setName("Saving");
        account2.setType("Account");
        account2.setNumber("2345678");
        account2.setBalance(new BigDecimal("200.00"));
        account2.setAccountOwnerId(user.getId());
        accountDao.addAccount(account2);

        Transaction transaction = new Transaction();
        transaction.setDate(LocalDate.of(2001, 1, 1));
        transaction.setFromAccountId(account1.getId());
        transaction.setToAccountId(account2.getId());
        transaction.setAmount(new BigDecimal("10.00"));

        Transaction fromAdd = transactionDao.addTransaction(transaction);
        Transaction fromGet = transactionDao.getTransactionById(fromAdd.getId());

        assertEquals(fromAdd, fromGet);
    }

    @Test
    public void testGetAllTransactions() {
        User user = new User();
        user.setFirstName("Albert");
        user.setLastName("Einstein");
        user.setEmail("ae@gmail.com");
        user.setPassword("temp");
        userDao.addUser(user);

        Account account1 = new Account();
        account1.setName("Chequing");
        account1.setType("Account");
        account1.setNumber("1234567");
        account1.setBalance(new BigDecimal("100.00"));
        account1.setAccountOwnerId(user.getId());
        accountDao.addAccount(account1);

        Account account2 = new Account();
        account2.setName("Saving");
        account2.setType("Account");
        account2.setNumber("2345678");
        account2.setBalance(new BigDecimal("200.00"));
        account2.setAccountOwnerId(user.getId());
        accountDao.addAccount(account2);

        Transaction transaction1 = new Transaction();
        transaction1.setDate(LocalDate.of(2001, 1, 1));
        transaction1.setFromAccountId(account1.getId());
        transaction1.setToAccountId(account2.getId());
        transaction1.setAmount(new BigDecimal("10.00"));
        transactionDao.addTransaction(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setDate(LocalDate.of(2002, 2, 2));
        transaction2.setFromAccountId(account2.getId());
        transaction2.setToAccountId(account1.getId());
        transaction2.setAmount(new BigDecimal("20.00"));
        transactionDao.addTransaction(transaction2);

        List<Transaction> transactions = transactionDao.getAllTransactions();
        assertTrue(transactions.size() == 2);
        assertTrue(transactions.contains(transaction1));
        assertTrue(transactions.contains(transaction2));

        assertEquals(transaction1.getDate(), LocalDate.of(2001, 1, 1));
        assertEquals(transaction2.getDate(), LocalDate.of(2002, 2, 2));
    }

    @Test
    public void testGetTransactionsByAccount() {
        User user = new User();
        user.setFirstName("Albert");
        user.setLastName("Einstein");
        user.setEmail("ae@gmail.com");
        user.setPassword("temp");
        userDao.addUser(user);

        Account account1 = new Account();
        account1.setName("Chequing");
        account1.setType("Account");
        account1.setNumber("1234567");
        account1.setBalance(new BigDecimal("100.00"));
        account1.setAccountOwnerId(user.getId());
        accountDao.addAccount(account1);

        Account account2 = new Account();
        account2.setName("Saving");
        account2.setType("Account");
        account2.setNumber("2345678");
        account2.setBalance(new BigDecimal("200.00"));
        account2.setAccountOwnerId(user.getId());
        accountDao.addAccount(account2);

        Account account3 = new Account();
        account3.setName("Cash Back");
        account3.setType("Credit Card");
        account3.setNumber("345678901234");
        account3.setBalance(new BigDecimal("300.00"));
        account3.setAccountOwnerId(user.getId());
        accountDao.addAccount(account3);

        Transaction transaction1 = new Transaction();
        transaction1.setDate(LocalDate.of(2001, 1, 1));
        transaction1.setFromAccountId(account1.getId());
        transaction1.setToAccountId(account2.getId());
        transaction1.setAmount(new BigDecimal("10.00"));
        transactionDao.addTransaction(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setDate(LocalDate.of(2002, 2, 2));
        transaction2.setFromAccountId(account2.getId());
        transaction2.setToAccountId(account1.getId());
        transaction2.setAmount(new BigDecimal("20.00"));
        transactionDao.addTransaction(transaction2);

        Transaction transaction3 = new Transaction();
        transaction3.setDate(LocalDate.of(2002, 2, 2));
        transaction3.setFromAccountId(account1.getId());
        transaction3.setToAccountId(account3.getId());
        transaction3.setAmount(new BigDecimal("30.00"));
        transactionDao.addTransaction(transaction3);

        List<Transaction> transactionsByAccount1 = transactionDao.getTransactionsByAccountId(account1.getId());
        assertTrue(transactionsByAccount1.size() == 3);
        assertTrue(transactionsByAccount1.contains(transaction1));
        assertTrue(transactionsByAccount1.contains(transaction2));
        assertTrue(transactionsByAccount1.contains(transaction3));

        List<Transaction> transactionsByAccount2 = transactionDao.getTransactionsByAccountId(account2.getId());
        assertTrue(transactionsByAccount2.size() == 2);
        assertTrue(transactionsByAccount2.contains(transaction1));
        assertTrue(transactionsByAccount2.contains(transaction2));

        List<Transaction> transactionsByAccount3 = transactionDao.getTransactionsByAccountId(account3.getId());
        assertTrue(transactionsByAccount3.size() == 1);
        assertTrue(transactionsByAccount3.contains(transaction3));
    }

    @Test
    public void testUpdateTransaction() {
        User user = new User();
        user.setFirstName("Albert");
        user.setLastName("Einstein");
        user.setEmail("ae@gmail.com");
        user.setPassword("temp");
        userDao.addUser(user);

        Account account1 = new Account();
        account1.setName("Chequing");
        account1.setType("Account");
        account1.setNumber("1234567");
        account1.setBalance(new BigDecimal("100.00"));
        account1.setAccountOwnerId(user.getId());
        accountDao.addAccount(account1);

        Account account2 = new Account();
        account2.setName("Saving");
        account2.setType("Account");
        account2.setNumber("2345678");
        account2.setBalance(new BigDecimal("200.00"));
        account2.setAccountOwnerId(user.getId());
        accountDao.addAccount(account2);

        Transaction transaction = new Transaction();
        transaction.setDate(LocalDate.of(2001, 1, 1));
        transaction.setFromAccountId(account1.getId());
        transaction.setToAccountId(account2.getId());
        transaction.setAmount(new BigDecimal("10.00"));
        transactionDao.addTransaction(transaction);

        Transaction initialTransaction = transactionDao.getTransactionById(transaction.getId());

        transaction.setDate(LocalDate.of(2002, 2, 2));
        transaction.setFromAccountId(account2.getId());
        transaction.setToAccountId(account1.getId());
        transaction.setAmount(new BigDecimal("20.00"));
        transactionDao.updateTransaction(transaction);

        Transaction updatedTransaction = transactionDao.getTransactionById(transaction.getId());

        assertNotEquals(initialTransaction, updatedTransaction);
        assertEquals(initialTransaction.getDate(), LocalDate.of(2001, 1, 1));
        assertEquals(initialTransaction.getFromAccountId(), account1.getId());
        assertEquals(initialTransaction.getToAccountId(), account2.getId());
        assertEquals(initialTransaction.getAmount(), new BigDecimal("10.00"));
        assertEquals(updatedTransaction.getDate(), LocalDate.of(2002, 2, 2));
        assertEquals(updatedTransaction.getFromAccountId(), account2.getId());
        assertEquals(updatedTransaction.getToAccountId(), account1.getId());
        assertEquals(updatedTransaction.getAmount(), new BigDecimal("20.00"));
    }

    @Test
    public void testDeleteTransaction() {
        User user = new User();
        user.setFirstName("Albert");
        user.setLastName("Einstein");
        user.setEmail("ae@gmail.com");
        user.setPassword("temp");
        userDao.addUser(user);

        Account account1 = new Account();
        account1.setName("Chequing");
        account1.setType("Account");
        account1.setNumber("1234567");
        account1.setBalance(new BigDecimal("100.00"));
        account1.setAccountOwnerId(user.getId());
        accountDao.addAccount(account1);

        Account account2 = new Account();
        account2.setName("Saving");
        account2.setType("Account");
        account2.setNumber("2345678");
        account2.setBalance(new BigDecimal("200.00"));
        account2.setAccountOwnerId(user.getId());
        accountDao.addAccount(account2);

        Transaction transaction = new Transaction();
        transaction.setDate(LocalDate.of(2001, 1, 1));
        transaction.setFromAccountId(account1.getId());
        transaction.setToAccountId(account2.getId());
        transaction.setAmount(new BigDecimal("10.00"));
        transactionDao.addTransaction(transaction);

        assertTrue(transactionDao.getTransactionById(transaction.getId()) != null);

        transactionDao.deleteTransactionById(transaction.getId());

        assertTrue(transactionDao.getTransactionById(transaction.getId()) == null);
    }
}
