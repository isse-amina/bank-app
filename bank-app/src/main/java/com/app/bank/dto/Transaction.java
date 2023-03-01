package com.app.bank.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Transaction {
    private int id;
    private LocalDate date;
    private int fromAccountId;
    private int toAccountId;
    private BigDecimal amount;
    private Account fromAccount;
    private Account toAccount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(int fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public int getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(int toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id && fromAccountId == that.fromAccountId && toAccountId == that.toAccountId && date.equals(that.date) && amount.equals(that.amount) && fromAccount.equals(that.fromAccount) && toAccount.equals(that.toAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, fromAccountId, toAccountId, amount, fromAccount, toAccount);
    }
}
