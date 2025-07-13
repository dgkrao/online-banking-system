SHOW DATABASES;
USE online_banking;
SHOW TABLES;
SELECT DATABASE();
DESCRIBE user;
SELECT * FROM user;
SELECT * FROM transaction;
SELECT SUM(amount) FROM transaction WHERE type = 'Debit';
SELECT name, balance FROM user ORDER BY balance DESC LIMIT 1;
SELECT u.name, COUNT(t.id) AS transaction_count
FROM user u
JOIN transaction t ON u.id = t.sender_id
GROUP BY u.name
ORDER BY transaction_count DESC;
