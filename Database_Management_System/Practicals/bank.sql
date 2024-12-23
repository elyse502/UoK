-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 20, 2024 at 02:24 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bank`
--

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

CREATE TABLE `accounts` (
  `acc_ID` int(11) NOT NULL,
  `acc_name` varchar(256) DEFAULT NULL,
  `customer_ID` int(11) DEFAULT NULL,
  `employee_ID` int(11) DEFAULT NULL,
  `trans_ID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `admin_ID` int(11) NOT NULL,
  `fname` varchar(256) DEFAULT NULL,
  `lname` varchar(256) DEFAULT NULL,
  `contact_add` varchar(256) DEFAULT NULL,
  `address` varchar(256) DEFAULT NULL,
  `email` varchar(256) DEFAULT NULL,
  `password` varchar(256) DEFAULT NULL,
  `acc_ID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`admin_ID`, `fname`, `lname`, `contact_add`, `address`, `email`, `password`, `acc_ID`) VALUES
(1, 'Elysee', 'NIYIBIZI', '0784652570', 'KK-806-st', 'elyseniyibizi502@gmail.com', 'UoK@123', NULL),
(2, 'John', 'SMITH', '+19014679', 'UK-10-st', 'john10@gmail.com', 'HellO@123', NULL),
(3, 'Calvin', 'RUMURI', '0781536670', 'KN-201-St', 'rumuricalvin@gmail.com', '12345\r\n', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `customer_ID` int(11) NOT NULL,
  `fname` varchar(256) DEFAULT NULL,
  `lname` varchar(256) DEFAULT NULL,
  `contact_add` varchar(256) DEFAULT NULL,
  `address` varchar(256) DEFAULT NULL,
  `email` varchar(256) DEFAULT NULL,
  `password` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`customer_ID`, `fname`, `lname`, `contact_add`, `address`, `email`, `password`) VALUES
(1, 'Elysee', 'NIYIBIZI', '0784652570', 'KK-806-st', 'elyseniyibizi502@gmail.com', 'UoK@123'),
(2, 'Clement', 'TUYISHIME', '0788462591', 'KK-806-st', 'clement2@gmail.com', 'This@123');

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

CREATE TABLE `employee` (
  `employee_ID` int(11) NOT NULL,
  `fname` varchar(256) DEFAULT NULL,
  `lname` varchar(256) DEFAULT NULL,
  `contact_add` varchar(256) DEFAULT NULL,
  `address` varchar(256) DEFAULT NULL,
  `email` varchar(256) DEFAULT NULL,
  `password` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `logs`
--

CREATE TABLE `logs` (
  `logs_ID` int(11) NOT NULL,
  `trans_ID` int(11) DEFAULT NULL,
  `login_date` date DEFAULT NULL,
  `admin_ID` int(11) DEFAULT NULL,
  `customer_ID` int(11) DEFAULT NULL,
  `report_ID` int(11) DEFAULT NULL,
  `employee_ID` int(11) DEFAULT NULL,
  `login_time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `reports`
--

CREATE TABLE `reports` (
  `report_ID` int(11) NOT NULL,
  `acc_ID` int(11) DEFAULT NULL,
  `logs_ID` int(11) DEFAULT NULL,
  `trans_ID` int(11) DEFAULT NULL,
  `report_name` varchar(256) DEFAULT NULL,
  `report_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `trans_ID` int(11) NOT NULL,
  `employee_ID` int(11) DEFAULT NULL,
  `customer_ID` int(11) DEFAULT NULL,
  `transaction_name` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`acc_ID`),
  ADD KEY `customer_ID` (`customer_ID`),
  ADD KEY `employee0_ID` (`employee_ID`),
  ADD KEY `trans0_ID` (`trans_ID`);

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`admin_ID`),
  ADD KEY `acc0_ID` (`acc_ID`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customer_ID`);

--
-- Indexes for table `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`employee_ID`);

--
-- Indexes for table `logs`
--
ALTER TABLE `logs`
  ADD PRIMARY KEY (`logs_ID`),
  ADD KEY `transa_ID` (`trans_ID`),
  ADD KEY `admin_ID` (`admin_ID`),
  ADD KEY `customer2_ID` (`customer_ID`),
  ADD KEY `report_ID` (`report_ID`),
  ADD KEY `employee2_ID` (`employee_ID`);

--
-- Indexes for table `reports`
--
ALTER TABLE `reports`
  ADD PRIMARY KEY (`report_ID`),
  ADD KEY `acc_ID` (`acc_ID`),
  ADD KEY `logs_ID` (`logs_ID`),
  ADD KEY `trans_ID` (`trans_ID`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`trans_ID`),
  ADD KEY `employee_ID` (`employee_ID`),
  ADD KEY `customer1_ID` (`customer_ID`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `accounts`
--
ALTER TABLE `accounts`
  ADD CONSTRAINT `customer_ID` FOREIGN KEY (`customer_ID`) REFERENCES `customer` (`customer_ID`),
  ADD CONSTRAINT `employee0_ID` FOREIGN KEY (`employee_ID`) REFERENCES `employee` (`employee_ID`),
  ADD CONSTRAINT `trans0_ID` FOREIGN KEY (`trans_ID`) REFERENCES `transactions` (`trans_ID`);

--
-- Constraints for table `admin`
--
ALTER TABLE `admin`
  ADD CONSTRAINT `acc0_ID` FOREIGN KEY (`acc_ID`) REFERENCES `accounts` (`acc_ID`);

--
-- Constraints for table `logs`
--
ALTER TABLE `logs`
  ADD CONSTRAINT `admin_ID` FOREIGN KEY (`admin_ID`) REFERENCES `admin` (`admin_ID`),
  ADD CONSTRAINT `customer2_ID` FOREIGN KEY (`customer_ID`) REFERENCES `customer` (`customer_ID`),
  ADD CONSTRAINT `employee2_ID` FOREIGN KEY (`employee_ID`) REFERENCES `employee` (`employee_ID`),
  ADD CONSTRAINT `report_ID` FOREIGN KEY (`report_ID`) REFERENCES `reports` (`report_ID`),
  ADD CONSTRAINT `transa_ID` FOREIGN KEY (`trans_ID`) REFERENCES `transactions` (`trans_ID`);

--
-- Constraints for table `reports`
--
ALTER TABLE `reports`
  ADD CONSTRAINT `acc_ID` FOREIGN KEY (`acc_ID`) REFERENCES `accounts` (`acc_ID`),
  ADD CONSTRAINT `logs_ID` FOREIGN KEY (`logs_ID`) REFERENCES `logs` (`logs_ID`),
  ADD CONSTRAINT `trans_ID` FOREIGN KEY (`trans_ID`) REFERENCES `transactions` (`trans_ID`);

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `customer1_ID` FOREIGN KEY (`customer_ID`) REFERENCES `customer` (`customer_ID`),
  ADD CONSTRAINT `employee_ID` FOREIGN KEY (`employee_ID`) REFERENCES `employee` (`employee_ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
