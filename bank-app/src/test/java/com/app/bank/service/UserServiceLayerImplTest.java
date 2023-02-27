package com.app.bank.service;

import com.app.bank.dto.*;
import com.app.bank.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceLayerImplTest {
    @Autowired
    UserServiceLayer userServiceLayer;

    @BeforeEach
    public void setUp() {
        try {
            List<User> users = userServiceLayer.getAllUsers();
            for (User user: users) {
                userServiceLayer.deleteUserById(user.getId());
            }
        }
        catch (UserException e) {
            fail("UserException should not be thrown.");
        }
    }

    @Test
    public void testValidateUserProperties() {
        Exception exception;

        // test 1: user first name is null
        User user = new User();
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user));
        assertTrue(exception.getMessage().equals("User's first name cannot be empty."));

        // test 2: user first name is blank
        user.setFirstName("");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user));
        assertTrue(exception.getMessage().equals("User's first name cannot be empty."));

        // test 3: user last name is null
        user.setFirstName("Albert");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user));
        assertTrue(exception.getMessage().equals("User's last name cannot be empty."));

        // test 4: user last name is blank
        user.setLastName("");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user));
        assertTrue(exception.getMessage().equals("User's last name cannot be empty."));

        // test 5: user email is null
        user.setLastName("Einstein");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user));
        assertTrue(exception.getMessage().equals("User's email cannot be empty."));

        // test 6: user email is blank
        user.setEmail("");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user));
        assertTrue(exception.getMessage().equals("User's email cannot be empty."));

        // test 7: user password is null
        user.setEmail("ae@gmail.com");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user));
        assertTrue(exception.getMessage().equals("User's password cannot be empty."));

        // test 8: user password is blank
        user.setPassword("");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user));
        assertTrue(exception.getMessage().equals("User's password cannot be empty."));

        // test 9: user debit card number is null
        user.setPassword("temp");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user));
        assertTrue(exception.getMessage().equals("User's debit card number cannot be empty."));

        // test 10: user debit card number is blank
        user.setDebitCard("");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user));
        assertTrue(exception.getMessage().equals("User's debit card number cannot be empty."));

        // test 11: user debit card number does not only contain numerical values
        user.setDebitCard("A11122223333");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user));
        assertTrue(exception.getMessage().equals("Debit card number must only contain numerical values."));

        // test 12: user debit card number is negative
        user.setDebitCard("-11122223333");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user));
        assertTrue(exception.getMessage().equals("Debit card number must only contain numerical values."));

        // test 13: user debit card number does not consist of 12 digits
        user.setDebitCard("11112222333");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user));
        assertTrue(exception.getMessage().equals("Debit card number must consist of 12 digits."));

        // test 14: user properties validated
        user.setDebitCard("111122223333");
        assertDoesNotThrow(() -> userServiceLayer.validateUserProperties(user));
        try {
            userServiceLayer.addUser(user);
        }
        catch (UserException e) {
            fail("UserException should not be thrown.");
        }
    }

    @Test
    public void testValidateUserExists() {
        Exception exception;
        User user = new User();
        user.setFirstName("Albert");
        user.setLastName("Einstein");
        user.setEmail("ae@gmail.com");
        user.setPassword("temp");
        user.setDebitCard("111122223333");
        try {
            user = userServiceLayer.addUser(user);
        }
        catch (UserException e) {
            fail("UserException should not be thrown.");
        }
        int userId = user.getId();

        // test 1: user does not exist
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserExists(userId + 1));
        assertTrue(exception.getMessage().equals("User does not exist."));

        // test 2: user existence validated
        assertDoesNotThrow(() -> userServiceLayer.validateUserExists(userId));
    }
}
