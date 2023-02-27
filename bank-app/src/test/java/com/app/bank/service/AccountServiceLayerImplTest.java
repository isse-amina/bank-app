package com.app.bank.service;

import com.app.bank.dto.*;
import com.app.bank.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class AccountServiceLayerImplTest {
    @Autowired
    UserServiceLayer userServiceLayer;

    @Autowired
    AccountServiceLayer accountServiceLayer;

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
        }
        catch (UserException e) {
            fail("UserException should not be thrown.");
        }
        catch (AccountException e) {
            fail("AccountException should not be thrown.");
        }
    }

    @Test
    public void testValidateAccountProperties() {
        Exception exception;

        // test 1: user does not exist
        User user = new User();
        user.setFirstName("Albert");
        user.setLastName("Einstein");
        user.setEmail("ae@gmail.com");
        user.setPassword("temp");
        try {
            user = userServiceLayer.addUser(user);
        }
        catch (UserException e) {
            fail("UserException should not be thrown.");
        }
        Account account = new Account();
        account.setAccountOwnerId(user.getId() + 1);
        exception = assertThrows(AccountException.class, () -> accountServiceLayer.validateAccountProperties(account));
        assertTrue(exception.getMessage().equals("User does not exist: A user must be created before an account can be opened."));

        // test 2: account name is null
        account.setAccountOwnerId(user.getId());
        exception = assertThrows(AccountException.class, () -> accountServiceLayer.validateAccountProperties(account));
        assertTrue(exception.getMessage().equals("Account name cannot be empty."));

        // test 3: account name is blank
        account.setName("");
        exception = assertThrows(AccountException.class, () -> accountServiceLayer.validateAccountProperties(account));
        assertTrue(exception.getMessage().equals("Account name cannot be empty."));

        // test 3: account type is null
        account.setName("Chequing");
        exception = assertThrows(AccountException.class, () -> accountServiceLayer.validateAccountProperties(account));
        assertTrue(exception.getMessage().equals("Account type cannot be empty."));

        // test 4: account type is blank
        account.setType("");
        exception = assertThrows(AccountException.class, () -> accountServiceLayer.validateAccountProperties(account));
        assertTrue(exception.getMessage().equals("Account type cannot be empty."));

        // test 5: account type does not contain "Chequing", "Saving" or "Credit Card"
        account.setType("Testing");
        exception = assertThrows(AccountException.class, () -> accountServiceLayer.validateAccountProperties(account));
        assertTrue(exception.getMessage().equals("Account type must be 'Account' or 'Credit Card'."));

        // test 6: account number is null
        account.setType("Account");
        exception = assertThrows(AccountException.class, () -> accountServiceLayer.validateAccountProperties(account));
        assertTrue(exception.getMessage().equals("Account number cannot be empty."));

        // test 7: account number is blank
        account.setNumber("");
        exception = assertThrows(AccountException.class, () -> accountServiceLayer.validateAccountProperties(account));
        assertTrue(exception.getMessage().equals("Account number cannot be empty."));

        // test 8: account number does not only contain numerical values
        account.setNumber("A234567");
        exception = assertThrows(AccountException.class, () -> accountServiceLayer.validateAccountProperties(account));
        assertTrue(exception.getMessage().equals("Account number must only contain numerical values."));

        // test 9: account number is negative
        account.setNumber("-234567");
        exception = assertThrows(AccountException.class, () -> accountServiceLayer.validateAccountProperties(account));
        assertTrue(exception.getMessage().equals("Account number must only contain numerical values."));

        // test 10: bank account number does not consist of 7 digits
        account.setNumber("123456");
        exception = assertThrows(AccountException.class, () -> accountServiceLayer.validateAccountProperties(account));
        assertTrue(exception.getMessage().equals("Bank account number must consist of 7 digits."));

        // test 11: account balance is null
        account.setNumber("1234567");
        exception = assertThrows(AccountException.class, () -> accountServiceLayer.validateAccountProperties(account));
        assertTrue(exception.getMessage().equals("Account balance cannot be null."));

        // test 12: account properties validated
        account.setBalance(new BigDecimal("100.00"));
        assertDoesNotThrow(() -> accountServiceLayer.validateAccountProperties(account));
        try {
            accountServiceLayer.addAccount(account);
        }
        catch (AccountException e) {
            fail("AccountException should not be thrown.");
        }
    }

    @Test
    public void testValidateAccountExists() {
        Exception exception;
        User user = new User();
        user.setFirstName("Albert");
        user.setLastName("Einstein");
        user.setEmail("ae@gmail.com");
        user.setPassword("temp");
        try {
            user = userServiceLayer.addUser(user);
        }
        catch (UserException e) {
            fail("UserException should not be thrown.");
        }
        Account account = new Account();
        account.setName("Chequing");
        account.setType("Account");
        account.setNumber("1234567");
        account.setBalance(new BigDecimal("100.00"));
        account.setAccountOwnerId(user.getId());
        try {
            account = accountServiceLayer.addAccount(account);
        }
        catch (AccountException e) {
            fail("AccountException should not be thrown.");
        }
        int accountId = account.getId();

        // test 1: account does not exist
        exception = assertThrows(AccountException.class, () -> accountServiceLayer.validateAccountExists(accountId + 1));
        assertTrue(exception.getMessage().equals("Account does not exist."));

        // test 2: account existence validated
        assertDoesNotThrow(() -> accountServiceLayer.validateAccountExists(accountId));
    }
}
