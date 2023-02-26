package com.app.bank.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private int id;
    private String name;
    private String type;
    private String number;
    private BigDecimal balance;
    private int accountOwnerId;
    private User accountOwner;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getAccountOwnerId() {
        return accountOwnerId;
    }

    public void setAccountOwnerId(int accountOwnerId) {
        this.accountOwnerId = accountOwnerId;
    }

    public User getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(User accountOwner) {
        this.accountOwner = accountOwner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && accountOwnerId == account.accountOwnerId && name.equals(account.name) && type.equals(account.type) && number.equals(account.number) && balance.equals(account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, number, balance, accountOwnerId);
    }
}
