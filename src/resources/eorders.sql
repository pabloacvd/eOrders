-- phpMyAdmin SQL Dump
-- version 4.7.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Sep 30, 2017 at 12:05 AM
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
-- Table structure for table `accesoriosPorLineaDetalle`
--

DROP TABLE IF EXISTS `accesoriosPorLineaDetalle`;
CREATE TABLE `accesoriosPorLineaDetalle` (
  `lineaID` int(11) NOT NULL,
  `accesorioID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `accesoriosPorProducto`
--

DROP TABLE IF EXISTS `accesoriosPorProducto`;
CREATE TABLE `accesoriosPorProducto` (
  `prodID` int(11) NOT NULL,
  `accesorio` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `lineasDetalle`
--

DROP TABLE IF EXISTS `lineasDetalle`;
CREATE TABLE `lineasDetalle` (
  `lineaID` int(11) NOT NULL,
  `prodID` int(11) NOT NULL,
  `tamanioElegido` varchar(50) DEFAULT NULL,
  `precioUnitario` double DEFAULT NULL,
  `cantidad` int(11) NOT NULL,
  `subtotal` double NOT NULL,
  `total` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ordenes`
--

DROP TABLE IF EXISTS `ordenes`;
CREATE TABLE `ordenes` (
  `idOrden` int(11) NOT NULL,
  `nombreContacto` varchar(60) NOT NULL,
  `telefonoContacto` varchar(20) DEFAULT NULL,
  `detallesAdicionales` text,
  `fechaEntrega` date DEFAULT NULL,
  `detallesEntrega` text,
  `lineasDetalle` text,
  `montoAbonado` double DEFAULT NULL,
  `descuento` double DEFAULT NULL,
  `status` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ordenes`
--

INSERT INTO `ordenes` (`idOrden`, `nombreContacto`, `telefonoContacto`, `detallesAdicionales`, `fechaEntrega`, `detallesEntrega`, `lineasDetalle`, `montoAbonado`, `descuento`, `status`) VALUES
(1, 'Pablo Acevedo A', '+541553473440', 'N/A', '2017-09-29', 'Retira por local', NULL, 5, 7, 'Preparado'),
(2, 'Pablo Areco', '234', 'asdfasf', '2017-09-28', 'Retira por local', NULL, 0, 0, 'Entregado'),
(3, 'Pablo Acevedo Areco', '+5491123456789', 'Profe JavaSE8', '2017-10-19', 'Retira por local 13hs', NULL, 10, 5, 'Cancelado'),
(4, 'Richard Stallman', '+3445566', 'GNU dad', '2017-07-31', 'Entregar por DHL a USA', NULL, 2, 3, 'Cancelado'),
(5, 'Linus Torvalds', '+358 91911', 'GIT and Linux dad', '2017-07-30', 'Entregar por DHL a Finlandia', NULL, 1, 0, 'Confirmado');

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
(1, 'Cargador celular', 'Cargador para celular genérico, 220v, 2A.', '{\"Accesorio\":1.0,\"Gratis\":0.0,\"Free\":0.0}', '2017-09-24', 1),
(19, 'Picada especial criolla', 'Jamón cocido\nJamón crudo\nBondiola\nLomito ahumado\nSalame\nLonganiza calabresa\nSalamin \nMortadela\nCantimpalo\nQueso tybo\nQueso pategras \nAceitunas descarozadas\nTomates cherry\nEspaditas de copetin', '{\"Individual\":200.0,\"2 personas\":430.0,\"3 personas \":550.0,\"4 personas \":680.0,\"5 personas \":810.0,\"6 personas \":935.0,\"7 personas \":1060.0,\"8 personas \":1180.0,\"9 personas \":1300.0,\"10 personas \":1420.0,\"11 personas\":1540.0}', '2017-09-26', 0),
(20, 'Picada XEVEN', 'Queso roquefort\nCantimpalo\nQueso tybo\nQueso pategras\nJamón crudo\nLomito ahumado\nSalamin \nLonganiza\nAceitunas descarozadas\nTomates cherry\nEspaditas de copetin ', '{\"individual\":190.0,\"2 personas \":380.0,\"3 personas \":430.0,\"4 personas \":500.0,\"5 personas \":625.0}', '2017-09-26', 0),
(21, 'Picada economica ', 'queso tybo\njamon cocido\nmortadela \nsalame milan \nsalamin \nbondiola \nqueso pategras \naceitunas descarozadas\nespaditas de copetin ', '{\"individual\":165.0,\"2 personas \":330.0,\"3 personas \":380.0,\"4 personas \":480.0,\"5 personas \":580.0}', '2017-09-26', 0),
(22, 'Matambre', 'Cortado en 22 rodajas ', '{\"Unitario\":450.0}', '2017-09-26', 0),
(23, 'Sanwhiches de chips ', 'jamon y queso ', '{\"la docena \":75.0}', '2017-09-26', 0);

-- --------------------------------------------------------

--
-- Indexes for table `accesoriosPorLineaDetalle`
--
ALTER TABLE `accesoriosPorLineaDetalle`
  ADD PRIMARY KEY (`lineaID`,`accesorioID`);

--
-- Indexes for table `accesoriosPorProducto`
--
ALTER TABLE `accesoriosPorProducto`
  ADD PRIMARY KEY (`prodID`,`accesorio`),
  ADD UNIQUE KEY `prodID` (`prodID`,`accesorio`);

--
-- Indexes for table `lineasDetalle`
--
ALTER TABLE `lineasDetalle`
  ADD PRIMARY KEY (`lineaID`);

--
-- Indexes for table `ordenes`
--
ALTER TABLE `ordenes`
  ADD PRIMARY KEY (`idOrden`);

--
-- Indexes for table `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`prodID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `lineasDetalle`
--
ALTER TABLE `lineasDetalle`
  MODIFY `lineaID` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `ordenes`
--
ALTER TABLE `ordenes`
  MODIFY `idOrden` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `productos`
--
ALTER TABLE `productos`
  MODIFY `prodID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
