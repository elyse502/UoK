-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 20, 2024 at 02:21 PM
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
-- Database: `sales`
--

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `Cust_ID` varchar(256) NOT NULL,
  `CUst_Name` varchar(256) DEFAULT NULL,
  `Shipping_Address` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`Cust_ID`, `CUst_Name`, `Shipping_Address`) VALUES
('am_smith', 'Alan Smith', '47 Campus Rd, Boston'),
('at_smith', 'Alan Smith', '35 Palm St, Miami'),
('roger25', 'Roger Banks', '47 Campus Rd, Boston'),
('wilson44', 'Evan Wilson', '28 Rock Av, Denver');

-- --------------------------------------------------------

--
-- Table structure for table `item`
--

CREATE TABLE `item` (
  `Item` varchar(256) NOT NULL,
  `Supplier` varchar(256) DEFAULT NULL,
  `Price` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `item`
--

INSERT INTO `item` (`Item`, `Supplier`, `Price`) VALUES
('PlayStation 4', NULL, 300),
('PS Vita', NULL, 200),
('Xbox One', NULL, 250);

-- --------------------------------------------------------

--
-- Table structure for table `sales_invoice`
--

CREATE TABLE `sales_invoice` (
  `Cust_ID` varchar(256) DEFAULT NULL,
  `Item` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sales_invoice`
--

INSERT INTO `sales_invoice` (`Cust_ID`, `Item`) VALUES
('at_smith', 'Xbox One'),
('wilson44', 'Xbox One'),
('roger25', 'PlayStation 4'),
('am_smith', 'PlayStation 4'),
('wilson44', 'PS Vita');

-- --------------------------------------------------------

--
-- Table structure for table `subscription`
--

CREATE TABLE `subscription` (
  `Cust_ID` varchar(256) DEFAULT NULL,
  `Newsletter` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `subscription`
--

INSERT INTO `subscription` (`Cust_ID`, `Newsletter`) VALUES
('at_smith', 'Xbox News'),
('roger25', 'PlayStation News'),
('wilson44', 'Xbox News'),
('wilson44', 'PlayStation News'),
('am_smith', 'PlayStation News');

-- --------------------------------------------------------

--
-- Table structure for table `supplier`
--

CREATE TABLE `supplier` (
  `Supplier` varchar(256) NOT NULL,
  `Supplier_Phone` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `supplier`
--

INSERT INTO `supplier` (`Supplier`, `Supplier_Phone`) VALUES
('Microsoft', '(800) BUY-XBOX'),
('Sony', '(800) BUY-SONY');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`Cust_ID`);

--
-- Indexes for table `item`
--
ALTER TABLE `item`
  ADD PRIMARY KEY (`Item`),
  ADD KEY `Supplier` (`Supplier`);

--
-- Indexes for table `sales_invoice`
--
ALTER TABLE `sales_invoice`
  ADD KEY `Cust1_ID` (`Cust_ID`),
  ADD KEY `Item` (`Item`);

--
-- Indexes for table `subscription`
--
ALTER TABLE `subscription`
  ADD KEY `Cust_ID` (`Cust_ID`);

--
-- Indexes for table `supplier`
--
ALTER TABLE `supplier`
  ADD PRIMARY KEY (`Supplier`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `item`
--
ALTER TABLE `item`
  ADD CONSTRAINT `Supplier` FOREIGN KEY (`Supplier`) REFERENCES `supplier` (`Supplier`);

--
-- Constraints for table `sales_invoice`
--
ALTER TABLE `sales_invoice`
  ADD CONSTRAINT `Cust1_ID` FOREIGN KEY (`Cust_ID`) REFERENCES `customer` (`Cust_ID`),
  ADD CONSTRAINT `Item` FOREIGN KEY (`Item`) REFERENCES `item` (`Item`);

--
-- Constraints for table `subscription`
--
ALTER TABLE `subscription`
  ADD CONSTRAINT `Cust_ID` FOREIGN KEY (`Cust_ID`) REFERENCES `customer` (`Cust_ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
