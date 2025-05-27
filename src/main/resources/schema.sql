CREATE TABLE customer_transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_date DATE,
    transaction_amount DECIMAL(10, 2),
    customer_id VARCHAR(50),
    customer_name VARCHAR(100)
);