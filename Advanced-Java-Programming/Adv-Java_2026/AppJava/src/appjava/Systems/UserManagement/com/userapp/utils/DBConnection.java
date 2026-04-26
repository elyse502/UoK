/*

Database Query

-- Database creation
CREATE DATABASE IF NOT EXISTS User_db;
USE User_db;

-- Users table creation with all required fields
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fullname VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    country VARCHAR(50) NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create application user with limited privileges (recommended for security)
CREATE USER IF NOT EXISTS 'app_user'@'localhost' IDENTIFIED BY 'SecurePass123!';
GRANT SELECT, INSERT, UPDATE, DELETE ON java_user_db.* TO 'app_user'@'localhost';
FLUSH PRIVILEGES;

-- Sample test data
INSERT INTO users (fullname, email, phone, country) VALUES
('Josbert', 'Josbert', '1234567890', 'Rwanda');

-- Verification query
SELECT * FROM users;

*/

package com.userapp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/User_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}