DROP DATABASE IF EXISTS bmsDB;
CREATE DATABASE bmsDB;
USE bmsDB;

CREATE TABLE Users (
	user_id INT PRIMARY KEY AUTO_INCREMENT,
    user_first_name VARCHAR(50) NOT NULL,
    user_last_name VARCHAR(50) NOT NULL
);

CREATE TABLE Accounts (
	account_id INT PRIMARY KEY AUTO_INCREMENT,
    account_name VARCHAR(50) NOT NULL,
    account_type VARCHAR(50) NOT NULL,
    account_number VARCHAR(50) NOT NULL UNIQUE,
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

INSERT INTO Users(user_first_name, user_last_name) 
VALUES("Albert", "Einstein");
INSERT INTO Users(user_first_name, user_last_name) 
VALUES("Isaac", "Newton");
INSERT INTO Accounts(account_name, account_type, account_number, account_balance, account_owner) 
VALUES("Chequing", "Account", "1234567890", "1000.00", 1);
INSERT INTO Accounts(account_name, account_type, account_number, account_balance, account_owner) 
VALUES("Saving", "Account", "2345678901", "2000.00", 1);
INSERT INTO Accounts(account_name, account_type, account_number, account_balance, account_owner) 
VALUES("Cash Back", "Credit Card", "3456789012", "300.00", 1);
INSERT INTO Transactions(transaction_date, from_account, to_account, transaction_amount) 
VALUES("2001-01-01", 1, 2, 10.00);
INSERT INTO Transactions(transaction_date, from_account, to_account, transaction_amount) 
VALUES("2002-02-02", 2, 1, 20.00);
INSERT INTO Transactions(transaction_date, from_account, to_account, transaction_amount) 
VALUES("2003-03-03", 1, 3, 30.00);