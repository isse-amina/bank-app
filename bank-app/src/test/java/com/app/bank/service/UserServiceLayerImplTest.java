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
        User user1 = new User();
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user1));
        assertTrue(exception.getMessage().equals("User's first name cannot be empty."));

        // test 2: user first name is blank
        user1.setFirstName("");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user1));
        assertTrue(exception.getMessage().equals("User's first name cannot be empty."));

        // test 3: user last name is null
        user1.setFirstName("Albert");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user1));
        assertTrue(exception.getMessage().equals("User's last name cannot be empty."));

        // test 4: user last name is blank
        user1.setLastName("");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user1));
        assertTrue(exception.getMessage().equals("User's last name cannot be empty."));

        // test 5: user email is null
        user1.setLastName("Einstein");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user1));
        assertTrue(exception.getMessage().equals("User's email cannot be empty."));

        // test 6: user email is blank
        user1.setEmail("");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user1));
        assertTrue(exception.getMessage().equals("User's email cannot be empty."));

        // test 7: user password is null
        user1.setEmail("in@gmail.com");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user1));
        assertTrue(exception.getMessage().equals("User's password cannot be empty."));

        // test 8: user password is blank
        user1.setPassword("");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user1));
        assertTrue(exception.getMessage().equals("User's password cannot be empty."));

        // test 9: user role is null
        user1.setPassword("temp");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user1));
        assertTrue(exception.getMessage().equals("User's role cannot be empty."));

        // test 10: user role is blank
        user1.setRole("");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user1));
        assertTrue(exception.getMessage().equals("User's role cannot be empty."));

        // test 11: user role is neither user nor admin
        user1.setRole("testing");
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user1));
        assertTrue(exception.getMessage().equals("User's role must be 'User' or 'Admin'."));

        // test 12: email is already in use
        user1.setRole("user");
        User user2 = new User();
        user2.setFirstName("Isaac");
        user2.setLastName("Newton");
        user2.setEmail("in@gmail.com");
        user2.setPassword("temp");
        user2.setRole("user");
        try {
            userServiceLayer.addUser(user2);
        }
        catch (UserException e) {
            fail("UserException should not be thrown.");
        }
        exception = assertThrows(UserException.class, () -> userServiceLayer.validateUserProperties(user1));
        assertTrue(exception.getMessage().equals("Email is already in use."));

        // test 12: user properties validated
        user1.setEmail("ae@gmail.com");
        assertDoesNotThrow(() -> userServiceLayer.validateUserProperties(user1));
        try {
            userServiceLayer.addUser(user1);
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
        user.setRole("user");
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
