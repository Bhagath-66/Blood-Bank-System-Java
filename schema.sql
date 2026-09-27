-- =========================================================
-- Blood Bank Management System — Database Schema (MySQL)
-- Run this once with a MySQL client before starting the app:
--   mysql -u root -p < schema.sql
-- =========================================================

CREATE DATABASE IF NOT EXISTS blood_bank_db;
USE blood_bank_db;

-- Login / account table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,   -- stores a SHA-256 hash, not plain text
    mobile VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Donor registrations
CREATE TABLE IF NOT EXISTS donors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NULL,
    name VARCHAR(100) NOT NULL,
    mobile VARCHAR(20) NOT NULL,
    address VARCHAR(255),
    age INT,
    blood_group VARCHAR(5),
    nearest_bank VARCHAR(100),
    weight DOUBLE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Blood requests from receivers
CREATE TABLE IF NOT EXISTS receivers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NULL,
    name VARCHAR(100) NOT NULL,
    mobile VARCHAR(20) NOT NULL,
    age INT,
    hospital VARCHAR(100),
    bystander_no VARCHAR(20),
    blood_group VARCHAR(5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Blood banks / hospitals directory, searchable by district
CREATE TABLE IF NOT EXISTS blood_banks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    district VARCHAR(100) NOT NULL,
    bank_name VARCHAR(150) NOT NULL,
    contact VARCHAR(20)
);

-- Sample seed data so "Hospital Search" returns something out of the box.
-- Replace/extend with your real districts and blood banks.
INSERT INTO blood_banks (district, bank_name, contact) VALUES
('Ernakulam', 'City Blood Bank', '0484-1234567'),
('Ernakulam', 'General Hospital Blood Bank', '0484-7654321'),
('Thrissur', 'Thrissur Central Blood Bank', '0487-1112233'),
('Kottayam', 'Kottayam Medical College Blood Bank', '0481-9988776');
