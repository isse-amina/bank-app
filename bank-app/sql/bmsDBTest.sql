DROP DATABASE IF EXISTS bmsDBTest;
CREATE DATABASE bmsDBTest;
USE bmsDBTest;

CREATE TABLE Users (
	user_id INT PRIMARY KEY AUTO_INCREMENT,
    user_first_name VARCHAR(50) NOT NULL,
    user_last_name VARCHAR(50) NOT NULL,
    user_email VARCHAR(50) NOT NULL UNIQUE,
    user_password VARCHAR(50) NOT NULL
);

CREATE TABLE Accounts (
	account_id INT PRIMARY KEY AUTO_INCREMENT,
    account_name VARCHAR(50) NOT NULL,
    account_type VARCHAR(50) NOT NULL,
    account_number VARCHAR(50) NOT NULL,
	account_balance DECIMAL(12,2) NOT NULL,
    account_owner INT NOT NULL,

    FOREIGN KEY (account_owner) REFERENCES Users(user_id) ON DELETE CASCADE
);

CREATE TABLE Transactions (
	transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    transaction_date DATE NOT NULL,
    from_account INT NOT NULL,
    to_account INT NOT NULL,
    transaction_amount DECIMAL(12,2)  NOT NULL,

    FOREIGN KEY (from_account) REFERENCES Accounts(account_id) ON DELETE CASCADE,
    FOREIGN KEY (to_account) REFERENCES Accounts(account_id) ON DELETE CASCADE
);