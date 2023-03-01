package com.app.bank.dao;

import com.app.bank.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDaoDB implements UserDao {
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public User getUserById(int id) {
        try {
            final String GET_USER_BY_ID = "SELECT * " +
                    "FROM Users " +
                    "WHERE user_id = ?";
            User user = jdbc.queryForObject(GET_USER_BY_ID, new UserMapper(), id);
            return user;
        }
        catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        final String GET_ALL_USERS = "SELECT * " +
                "FROM Users";
        List<User> users = jdbc.query(GET_ALL_USERS, new UserMapper());
        return users;
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            final String GET_USER_BY_EMAIL = "SELECT * " +
                    "FROM Users " +
                    "WHERE user_email = ?";
            User user = jdbc.queryForObject(GET_USER_BY_EMAIL, new UserMapper(), email);
            return user;
        }
        catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public User addUser(User user) {
        final String ADD_USER = "INSERT INTO Users(user_first_name, user_last_name, user_email, user_password, user_role) " +
                "VALUES(?,?,?,?,?)";
        jdbc.update(ADD_USER,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        user.setId(newId);
        return user;
    }

    @Override
    public void updateUser(User user) {
        final String UPDATE_USER = "UPDATE Users " +
                "SET user_first_name = ?, user_last_name = ?, user_email = ?, user_password = ?, user_role = ? " +
                "WHERE user_id = ?";
        jdbc.update(UPDATE_USER,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getId());
    }

    @Override
    @Transactional
    public void deleteUserById(int id) {
        final String DELETE_USER = "DELETE FROM Users " +
                "WHERE user_id = ?";
        jdbc.update(DELETE_USER, id);
    }

    public static final class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int index) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("user_id"));
            user.setFirstName(rs.getString("user_first_name"));
            user.setLastName(rs.getString("user_last_name"));
            user.setEmail(rs.getString("user_email"));
            user.setPassword(rs.getString("user_password"));
            user.setRole(rs.getString("user_role"));

            return user;
        }
    }
}
