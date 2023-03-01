package com.app.bank.dao;

import com.app.bank.dto.User;

import java.util.List;

public interface UserDao {
    User getUserById(int id);
    List<User> getAllUsers();
    User getUserByEmail(String email);
    User addUser(User user);
    void updateUser(User user);
    void deleteUserById(int id);
}
