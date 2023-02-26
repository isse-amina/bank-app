package com.app.bank.service;

import com.app.bank.dto.User;
import com.app.bank.exceptions.UserException;

import java.util.List;

public interface UserServiceLayer {
    User getUserById(int id) throws UserException;
    List<User> getAllUsers();
    User addUser(User user) throws UserException;
    void updateUser(User user) throws UserException;
    void deleteUserById(int id) throws UserException;
    void validateUserProperties(User user) throws UserException;
    void validateUserExists(int id) throws UserException;
}
