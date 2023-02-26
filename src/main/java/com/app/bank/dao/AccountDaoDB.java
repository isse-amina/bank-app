package com.app.bank.dao;

import com.app.bank.dto.Account;
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
public class AccountDaoDB implements AccountDao {
    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    UserDao userDao;

    @Override
    public Account getAccountById(int id) {
        try {
            final String GET_ACCOUNT_BY_ID = "SELECT * " +
                    "FROM Accounts " +
                    "WHERE account_id = ?";
            Account account = jdbc.queryForObject(GET_ACCOUNT_BY_ID, new AccountMapper(), id);
            associateAccountToUser(account);
            return account;
        }
        catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        final String GET_ALL_ACCOUNTS = "SELECT * " +
                "FROM Accounts";
        List<Account> accounts = jdbc.query(GET_ALL_ACCOUNTS, new AccountMapper());
        for(Account account: accounts) {
            associateAccountToUser(account);
        }
        return accounts;
    }

    @Override
    public List<Account> getAccountsByUserId(int accountOwnerId) {
        final String GET_ALL_ACCOUNTS_By_USER = "SELECT * " +
                "FROM Accounts " +
                "WHERE account_owner = ?";
        List<Account> accounts = jdbc.query(GET_ALL_ACCOUNTS_By_USER, new AccountMapper(), accountOwnerId);
        for(Account account: accounts) {
            associateAccountToUser(account);
        }
        return accounts;
    }

    @Override
    @Transactional
    public Account addAccount(Account account) {
        final String ADD_ACCOUNT = "INSERT INTO Accounts(account_name, account_type, account_number, account_balance, account_owner) " +
                "VALUES(?,?,?,?,?)";
        jdbc.update(ADD_ACCOUNT,
                account.getName(),
                account.getType(),
                account.getNumber(),
                account.getBalance(),
                account.getAccountOwnerId());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        account.setId(newId);
        associateAccountToUser(account);
        return account;
    }

    public void associateAccountToUser(Account account) {
        User accountOwner = userDao.getUserById(account.getAccountOwnerId());
        account.setAccountOwner(accountOwner);
    }

    @Override
    public void updateAccount(Account account) {
        final String UPDATE_ACCOUNT = "UPDATE Accounts " +
                "SET account_name = ?, account_type = ?, account_number = ?, account_balance = ?, account_owner = ? " +
                "WHERE account_id = ?";
        jdbc.update(UPDATE_ACCOUNT,
                account.getName(),
                account.getType(),
                account.getNumber(),
                account.getBalance(),
                account.getAccountOwnerId(),
                account.getId());
    }

    @Override
    @Transactional
    public void deleteAccountById(int id) {
        final String DELETE_ACCOUNT = "DELETE FROM Accounts " +
                "WHERE account_id = ?";
        jdbc.update(DELETE_ACCOUNT, id);
    }

    public static final class AccountMapper implements RowMapper<Account> {
        @Override
        public Account mapRow(ResultSet rs, int index) throws SQLException {
            Account account = new Account();
            account.setId(rs.getInt("account_id"));
            account.setName(rs.getString("account_name"));
            account.setType(rs.getString("account_type"));
            account.setNumber(rs.getString("account_number"));
            account.setBalance(rs.getBigDecimal("account_balance"));
            account.setAccountOwnerId(rs.getInt("account_owner"));

            return account;
        }
    }
}
