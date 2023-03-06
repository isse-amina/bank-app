# Banking App

<h2>General Information</h2>
<p>
    The International Bank is a banking app that has various functionalities. If you login as a "user", you'll have access to your accounts and you'll be     able to transfer money from one of your accounts to another. If you login as an "admin", you'll have access to all of the tables in the  
    database, including the users, the accounts and the transactions. Moreover, you will be able to perform CRUD operations on each one of them. 
</p>
<p>
    For the backend, this app uses MySQL for the database, JDBC to allow Java to connect to the database, Java for the backend development, the MVC           design pattern, REST APIs, Spring and Maven. For the frontend, this app uses HTML, CSS and JavaScript.
</p>
<h2>Database</h2>
<img src="https://github.com/isse-amina/bank-app/blob/main/bank-app/sql/model.png">
<h2>Users</h2>
<ul>
    <li>All users must have the following properties: first name, last name, email, password and role.</li>
    <li>Once a user has been added to the database, they will also have an ID associated with them.</li>
    <li>A user can have one of two roles: user or admin.</li>
    <li>A user with type “user” can be created in the register page, but they will not have access to the admin section of the website.</li>
    <li>A user with type “admin” can only be created by another admin, but they will not have access to the user section of the website.</li>
    <li>If a user’s role is changed from “user” to “admin”, then all accounts and transactions associated with said user will be deleted, because admin’s         do not have accounts.</li>
</ul>
<h2>Accounts</h2>
<ul>
    <li>All accounts must have the following properties: name, type, number, balance and owner.</li>
    <li>Once an account has been added to the database, it will also have an ID associated with it.</li>
    <li>User's must contact the bank to open an account.</li>
    <li>An account can be one of two types: account or credit card.</li>
    <li>The number associated with an account of type “account” must consist of 7 digits.</li>
    <li>The number associated with an account of type “credit card” must consist of 12 digits.</li>
    <li>The account owner must be a user with the role “user”.</li>
</ul>
<h2>Transactions</h2>
<ul>
    <li>All transactions must have the following properties: date, from account, to account and amount.</li>
    <li>Once a transaction has been added to the database, it will also have an ID associated with it.</li>
    <li>Currently, transactions refer to transfers between accounts belonging to the same user.</li>
    <li>The date of a transaction cannot be in the future.</li>
    <li>Transferring money to an account of type “credit card” reduces the balance of the account, because the user is paying off a debt.</li>
    <li>The “from account” cannot be of type “credit card”.</li>
    <li>The transaction amount cannot exceed the balance of the “from account”.</li>
</ul>
<br><br>
<p>
    <b>Note:<b> The styling of this app was inspired by the BMO website.
</p>
