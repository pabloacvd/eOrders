-- phpMyAdmin SQL Dump
-- version 4.7.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Sep 23, 2017 at 12:54 AM
-- Server version: 5.7.19
-- PHP Version: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `eorders`
--
CREATE DATABASE IF NOT EXISTS `eorders` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `eorders`;

-- --------------------------------------------------------

--
-- Table structure for table `accesoriosPorProducto`
--

DROP TABLE IF EXISTS `accesoriosPorProducto`;
CREATE TABLE `accesoriosPorProducto` (
  `prodID` int(11) NOT NULL,
  `accesorio` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `accesoriosPorProducto`
--

INSERT INTO `accesoriosPorProducto` (`prodID`, `accesorio`) VALUES
(4, 1),
(4, 2),
(4, 3);

-- --------------------------------------------------------

--
-- Stand-in structure for view `accesoriosview`
-- (See below for the actual view)
--
DROP VIEW IF EXISTS `accesoriosview`;
CREATE TABLE `accesoriosview` (
);

-- --------------------------------------------------------

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
CREATE TABLE `productos` (
  `prodID` int(11) NOT NULL,
  `nombreProducto` varchar(50) NOT NULL,
  `detallesProducto` text NOT NULL,
  `precioPorTamanio` text COMMENT 'serializado del hashmap',
  `fechaModificacionPrecio` date DEFAULT NULL,
  `soloAccesorio` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'define si se muestra el producto en la lista principal o no'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `productos`
--

INSERT INTO `productos` (`prodID`, `nombreProducto`, `detallesProducto`, `precioPorTamanio`, `fechaModificacionPrecio`, `soloAccesorio`) VALUES
(1, 'Cargador celular', 'Cargador para celular genérico, 220v, 2A.', '{\"Promo\":0.0,\"Chico\":10.0,\"Medio\":22.0,\"Grande\":35.4}', '2017-08-29', 1),
(2, 'Auriculares blancos', 'Auriculares bluetooth estereo.', '{\"Promo\":0.0,\"Chico\":10.0,\"Medio\":22.0,\"Grande\":35.4}', '2017-08-29', 0),
(3, 'Auriculares negros', 'Auriculares bluetooth negros', '{\"Promo\":0.0,\"Chico\":10.0,\"Medio\":22.0,\"Grande\":35.4}', '2017-08-29', 0),
(4, 'Celular', 'Celular con auriculares y cargador.', '{\"Promo\":0.0,\"Chico\":10.0,\"Medio\":22.0,\"Grande\":35.4}', '2017-08-29', 0);

-- --------------------------------------------------------

--
-- Structure for view `accesoriosview`
--
DROP TABLE IF EXISTS `accesoriosview`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `accesoriosview`  AS  select concat('Accesorios para ',`P`.`nombreProducto`,' (prodID=',`P`.`prodID`,')') AS `Producto`,`O`.`prodID` AS `prodID`,`O`.`nombreProducto` AS `nombreProducto`,`O`.`detallesProducto` AS `detallesProducto`,`O`.`precioPorTamanio` AS `precioPorTamanio`,`O`.`fechaModificacionPrecio` AS `fechaModificacionPrecio`,`o`.`accesorios` AS `accesorios` from ((`productos` `P` join `accesoriosporproducto` `A` on((`a`.`prodID` = `p`.`prodID`))) join `productos` `O` on((`a`.`accesorio` = `o`.`prodID`))) where (`p`.`prodID` = 4) ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accesoriosPorProducto`
--
ALTER TABLE `accesoriosPorProducto`
  ADD PRIMARY KEY (`prodID`,`accesorio`),
  ADD UNIQUE KEY `prodID` (`prodID`,`accesorio`);

--
-- Indexes for table `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`prodID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `productos`
--
ALTER TABLE `productos`
  MODIFY `prodID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
