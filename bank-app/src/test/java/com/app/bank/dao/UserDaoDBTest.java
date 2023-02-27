package com.app.bank.dao;

import com.app.bank.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserDaoDBTest {
    @Autowired
    UserDao userDao;

    @BeforeEach
    public void setUp() {
        List<User> users = userDao.getAllUsers();
        for (User user: users) {
            userDao.deleteUserById(user.getId());
        }
    }

    @Test
    public void testAddAndGetUser() {
        User user = new User();
        user.setFirstName("Albert");
        user.setLastName("Einstein");
        user.setEmail("ae@gmail.com");
        user.setPassword("temp");

        User fromAdd = userDao.addUser(user);
        User fromGet = userDao.getUserById(fromAdd.getId());

        assertEquals(fromAdd, fromGet);
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        user1.setFirstName("Albert");
        user1.setLastName("Einstein");
        user1.setEmail("ae@gmail.com");
        user1.setPassword("temp");
        userDao.addUser(user1);

        User user2 = new User();
        user2.setFirstName("Isaac");
        user2.setLastName("Newton");
        user2.setEmail("in@gmail.com");
        user2.setPassword("temp");
        userDao.addUser(user2);

        List<User> users = userDao.getAllUsers();
        assertTrue(users.size() == 2);
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setFirstName("Albert");
        user.setLastName("Einstein");
        user.setEmail("ae@gmail.com");
        user.setPassword("temp");
        userDao.addUser(user);

        User initialUser = userDao.getUserById(user.getId());

        user.setFirstName("Isaac");
        user.setLastName("Newton");
        user.setEmail("in@gmail.com");
        user.setPassword("temp");
        userDao.updateUser(user);

        User updatedUser = userDao.getUserById(user.getId());

        assertNotEquals(initialUser, updatedUser);
        assertEquals(initialUser.getFirstName(), "Albert");
        assertEquals(initialUser.getLastName(), "Einstein");
        assertEquals(initialUser.getEmail(), "ae@gmail.com");
        assertEquals(initialUser.getPassword(), "temp");
        assertEquals(updatedUser.getFirstName(), "Isaac");
        assertEquals(updatedUser.getLastName(), "Newton");
        assertEquals(updatedUser.getEmail(), "in@gmail.com");
        assertEquals(updatedUser.getPassword(), "temp");
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setFirstName("Albert");
        user.setLastName("Einstein");
        user.setEmail("ae@gmail.com");
        user.setPassword("temp");
        userDao.addUser(user);

        assertTrue(userDao.getUserById(user.getId()) != null);

        userDao.deleteUserById(user.getId());

        assertTrue(userDao.getUserById(user.getId()) == null);
    }
}
