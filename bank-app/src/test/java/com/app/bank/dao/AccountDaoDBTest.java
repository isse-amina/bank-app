package com.app.bank.dao;

import com.app.bank.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccountDaoDBTest {
    @Autowired
    UserDao userDao;

    @Autowired
    AccountDao accountDao;

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
    }

    @Test
    public void testAddAndGetAccount() {
        User user = new User();
        user.setFirstName("Albert");
        user.setLastName("Einstein");
        user.setEmail("ae@gmail.com");
        user.setPassword("temp");
        user.setDebitCard("111122223333");
        userDao.addUser(user);

        Account account = new Account();
        account.setName("Chequing");
        account.setType("Account");
        account.setNumber("1234567");
        account.setBalance(new BigDecimal("100.00"));
        account.setAccountOwnerId(user.getId());

        Account fromAdd = accountDao.addAccount(account);
        Account fromGet = accountDao.getAccountById(fromAdd.getId());

        assertEquals(fromAdd, fromGet);
    }

    @Test
    public void testGetAllAccounts() {
        User user1 = new User();
        user1.setFirstName("Albert");
        user1.setLastName("Einstein");
        user1.setEmail("ae@gmail.com");
        user1.setPassword("temp");
        user1.setDebitCard("111122223333");
        userDao.addUser(user1);

        Account account1 = new Account();
        account1.setName("Chequing");
        account1.setType("Account");
        account1.setNumber("1234567");
        account1.setBalance(new BigDecimal("100.00"));
        account1.setAccountOwnerId(user1.getId());
        accountDao.addAccount(account1);

        Account account2 = new Account();
        account2.setName("Saving");
        account2.setType("Account");
        account2.setNumber("2345678");
        account2.setBalance(new BigDecimal("200.00"));
        account2.setAccountOwnerId(user1.getId());
        accountDao.addAccount(account2);

        User user2 = new User();
        user2.setFirstName("Isaac");
        user2.setLastName("Newton");
        user2.setEmail("in@gmail.com");
        user2.setPassword("temp");
        user2.setDebitCard("222233334444");
        userDao.addUser(user2);

        Account account3 = new Account();
        account3.setName("Chequing");
        account3.setType("Account");
        account3.setNumber("3456789");
        account3.setBalance(new BigDecimal("300.00"));
        account3.setAccountOwnerId(user2.getId());
        accountDao.addAccount(account3);

        Account account4 = new Account();
        account4.setName("Saving");
        account4.setType("Account");
        account4.setNumber("4567890");
        account4.setBalance(new BigDecimal("400.00"));
        account4.setAccountOwnerId(user2.getId());
        accountDao.addAccount(account4);

        List<Account> accounts = accountDao.getAllAccounts();
        assertTrue(accounts.size() == 4);
        assertTrue(accounts.contains(account1));
        assertTrue(accounts.contains(account2));
        assertTrue(accounts.contains(account3));
        assertTrue(accounts.contains(account4));

        assertEquals(account1.getAccountOwner(), account2.getAccountOwner());
        assertEquals(account1.getAccountOwner().getLastName(), "Einstein");
        assertEquals(account2.getAccountOwner().getLastName(), "Einstein");
        assertEquals(account3.getAccountOwner(), account4.getAccountOwner());
        assertEquals(account3.getAccountOwner().getLastName(), "Newton");
        assertEquals(account4.getAccountOwner().getLastName(), "Newton");
    }

    @Test
    public void testGetAccountsByUser() {
        User user1 = new User();
        user1.setFirstName("Albert");
        user1.setLastName("Einstein");
        user1.setEmail("ae@gmail.com");
        user1.setPassword("temp");
        user1.setDebitCard("111122223333");
        userDao.addUser(user1);

        Account account1 = new Account();
        account1.setName("Chequing");
        account1.setType("Account");
        account1.setNumber("1234567");
        account1.setBalance(new BigDecimal("100.00"));
        account1.setAccountOwnerId(user1.getId());
        accountDao.addAccount(account1);

        Account account2 = new Account();
        account2.setName("Saving");
        account2.setType("Account");
        account2.setNumber("2345678");
        account2.setBalance(new BigDecimal("200.00"));
        account2.setAccountOwnerId(user1.getId());
        accountDao.addAccount(account2);

        User user2 = new User();
        user2.setFirstName("Isaac");
        user2.setLastName("Newton");
        user2.setEmail("in@gmail.com");
        user2.setPassword("temp");
        user2.setDebitCard("222233334444");
        userDao.addUser(user2);

        Account account3 = new Account();
        account3.setName("Chequing");
        account3.setType("Account");
        account3.setNumber("3456789");
        account3.setBalance(new BigDecimal("300.00"));
        account3.setAccountOwnerId(user2.getId());
        accountDao.addAccount(account3);

        Account account4 = new Account();
        account4.setName("Saving");
        account4.setType("Account");
        account4.setNumber("4567890");
        account4.setBalance(new BigDecimal("400.00"));
        account4.setAccountOwnerId(user2.getId());
        accountDao.addAccount(account4);

        List<Account> accountsUser1 = accountDao.getAccountsByUserId(user1.getId());
        assertTrue(accountsUser1.size() == 2);
        assertTrue(accountsUser1.contains(account1));
        assertTrue(accountsUser1.contains(account2));

        assertEquals(account1.getAccountOwner(), account2.getAccountOwner());
        assertEquals(account1.getAccountOwner().getLastName(), "Einstein");
        assertEquals(account2.getAccountOwner().getLastName(), "Einstein");

        List<Account> accountsUser2 = accountDao.getAccountsByUserId(user2.getId());
        assertTrue(accountsUser2.size() == 2);
        assertTrue(accountsUser2.contains(account3));
        assertTrue(accountsUser2.contains(account4));

        assertEquals(account3.getAccountOwner(), account4.getAccountOwner());
        assertEquals(account3.getAccountOwner().getLastName(), "Newton");
        assertEquals(account4.getAccountOwner().getLastName(), "Newton");
    }

    @Test
    public void testUpdateAccount() {
        User user = new User();
        user.setFirstName("Albert");
        user.setLastName("Einstein");
        user.setEmail("ae@gmail.com");
        user.setPassword("temp");
        user.setDebitCard("111122223333");
        userDao.addUser(user);

        Account account = new Account();
        account.setName("Chequing");
        account.setType("Account");
        account.setNumber("1234567");
        account.setBalance(new BigDecimal("100.00"));
        account.setAccountOwnerId(user.getId());
        accountDao.addAccount(account);

        Account initialAccount = accountDao.getAccountById(account.getId());

        account.setName("Saving");
        account.setType("Account");
        account.setNumber("2345678");
        account.setBalance(new BigDecimal("200.00"));
        account.setAccountOwnerId(user.getId());
        accountDao.updateAccount(account);

        Account updatedAccount = accountDao.getAccountById(account.getId());

        assertNotEquals(initialAccount, updatedAccount);
        assertEquals(initialAccount.getName(), "Chequing");
        assertEquals(initialAccount.getType(), "Account");
        assertEquals(initialAccount.getNumber(), "1234567");
        assertEquals(initialAccount.getBalance(), new BigDecimal("100.00"));
        assertEquals(updatedAccount.getName(), "Saving");
        assertEquals(updatedAccount.getType(), "Account");
        assertEquals(updatedAccount.getNumber(), "2345678");
        assertEquals(updatedAccount.getBalance(), new BigDecimal("200.00"));
    }

    @Test
    public void testDeleteAccount() {
        User user = new User();
        user.setFirstName("Albert");
        user.setLastName("Einstein");
        user.setEmail("ae@gmail.com");
        user.setPassword("temp");
        user.setDebitCard("111122223333");
        userDao.addUser(user);

        Account account = new Account();
        account.setName("Chequing");
        account.setType("Account");
        account.setNumber("1234567");
        account.setBalance(new BigDecimal("100.00"));
        account.setAccountOwnerId(user.getId());
        accountDao.addAccount(account);

        assertTrue(accountDao.getAccountById(account.getId()) != null);

        accountDao.deleteAccountById(account.getId());

        assertTrue(accountDao.getAccountById(account.getId()) == null);
    }
}
