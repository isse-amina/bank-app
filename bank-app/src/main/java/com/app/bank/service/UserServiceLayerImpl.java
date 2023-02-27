package com.app.bank.service;

import com.app.bank.dao.UserDao;
import com.app.bank.dto.User;
import com.app.bank.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceLayerImpl implements UserServiceLayer {
    @Autowired
    UserDao userDao;

    @Override
    public User getUserById(int id) throws UserException {
        validateUserExists(id);
        return userDao.getUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User addUser(User user) throws UserException {
        validateUserProperties(user);
        return userDao.addUser(user);
    }

    @Override
    public void updateUser(User user) throws UserException {
        validateUserProperties(user);
        userDao.updateUser(user);
    }

    @Override
    public void deleteUserById(int id) throws UserException {
        validateUserExists(id);
        userDao.deleteUserById(id);
    }

    @Override
    public void validateUserProperties(User user) throws UserException {
        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            throw new UserException("User's first name cannot be empty.");
        }
        if (user.getLastName() == null || user.getLastName().isBlank()) {
            throw new UserException("User's last name cannot be empty.");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new UserException("User's email cannot be empty.");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new UserException("User's password cannot be empty.");
        }
        if (user.getDebitCard() == null || user.getDebitCard().isBlank()) {
            throw new UserException("User's debit card number cannot be empty.");
        }
        try {
            Long debitCardNumberLong = Long.parseLong(user.getDebitCard());
            if (debitCardNumberLong != Math.abs(debitCardNumberLong)) {
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e) {
            throw new UserException("Debit card number must only contain numerical values.");
        }
        if (user.getDebitCard().length() != 12) {
            throw new UserException("Debit card number must consist of 12 digits.");
        }
    }

    @Override
    public void validateUserExists(int id) throws UserException {
        if (userDao.getUserById(id) == null) {
            throw new UserException("User does not exist.");
        }
    }
}
