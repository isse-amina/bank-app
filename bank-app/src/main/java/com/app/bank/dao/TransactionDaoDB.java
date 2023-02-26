package com.app.bank.dao;

import com.app.bank.dto.Account;
import com.app.bank.dto.Transaction;
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
public class TransactionDaoDB implements TransactionDao {
    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    AccountDao accountDao;

    @Override
    public Transaction getTransactionById(int id) {
        try {
            final String GET_TRANSACTION_BY_ID = "SELECT * " +
                    "FROM Transactions " +
                    "WHERE transaction_id = ?";
            Transaction transaction = jdbc.queryForObject(GET_TRANSACTION_BY_ID, new TransactionMapper(), id);
            associateTransactionToAccount(transaction);
            return transaction;
        }
        catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Transaction> getAllTransactions() {
        final String GET_ALL_TRANSACTIONS = "SELECT * " +
                "FROM Transactions " +
                "ORDER BY transaction_date DESC;";
        List<Transaction> transactions = jdbc.query(GET_ALL_TRANSACTIONS, new TransactionMapper());
        for(Transaction transaction: transactions) {
            associateTransactionToAccount(transaction);
        }
        return transactions;
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(int accountId) {
        final String GET_ALL_TRANSACTIONS_BY_ACCOUNT = "SELECT * " +
                "FROM Transactions " +
                "WHERE from_account = ? " +
                "OR to_account = ? " +
                "ORDER BY transaction_id DESC;";
        List<Transaction> transactions = jdbc.query(GET_ALL_TRANSACTIONS_BY_ACCOUNT, new TransactionMapper(), accountId, accountId);
        for(Transaction transaction: transactions) {
            associateTransactionToAccount(transaction);
        }
        return transactions;
    }

    @Override
    @Transactional
    public Transaction addTransaction(Transaction transaction) {
        final String ADD_TRANSACTION = "INSERT INTO Transactions(transaction_date, from_account, to_account, transaction_amount) " +
                "VALUES(?,?,?,?)";
        jdbc.update(ADD_TRANSACTION,
                transaction.getDate(),
                transaction.getFromAccountId(),
                transaction.getToAccountId(),
                transaction.getAmount());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        transaction.setId(newId);
        associateTransactionToAccount(transaction);
        return transaction;
    }

    public void associateTransactionToAccount(Transaction transaction) {
        Account fromAccount = accountDao.getAccountById(transaction.getFromAccountId());
        transaction.setFromAccount(fromAccount);
        Account toAccount = accountDao.getAccountById(transaction.getToAccountId());
        transaction.setToAccount(toAccount);
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        final String UPDATE_TRANSACTION = "UPDATE Transactions " +
                "SET transaction_date = ?, from_account = ?, to_account = ?, transaction_amount = ? " +
                "WHERE transaction_id = ?";
        jdbc.update(UPDATE_TRANSACTION,
                transaction.getDate(),
                transaction.getFromAccountId(),
                transaction.getToAccountId(),
                transaction.getAmount(),
                transaction.getId());
    }

    @Override
    @Transactional
    public void deleteTransactionById(int id) {
        final String DELETE_TRANSACTION = "DELETE FROM Transactions " +
                "WHERE transaction_id = ?";
        jdbc.update(DELETE_TRANSACTION, id);
    }

    public static final class TransactionMapper implements RowMapper<Transaction> {
        @Override
        public Transaction mapRow(ResultSet rs, int index) throws SQLException {
            Transaction transaction = new Transaction();
            transaction.setId(rs.getInt("transaction_id"));
            transaction.setDate(rs.getDate("transaction_date").toLocalDate());
            transaction.setFromAccountId(rs.getInt("from_account"));
            transaction.setToAccountId(rs.getInt("to_account"));
            transaction.setAmount(rs.getBigDecimal("transaction_amount"));

            return transaction;
        }
    }
}
