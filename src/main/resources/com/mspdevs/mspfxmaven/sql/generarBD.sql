-- MySQL dump 10.13  Distrib 8.0.35, for Linux (x86_64)
--
-- Host: localhost    Database: mercadito
-- ------------------------------------------------------
-- Server version	8.0.35-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `id_cliente` int NOT NULL AUTO_INCREMENT,
  `idpersona` int NOT NULL,
  PRIMARY KEY (`id_cliente`),
  KEY `fk_clientes_personas1_idx` (`idpersona`),
  CONSTRAINT `fk_clientes_personas1` FOREIGN KEY (`idpersona`) REFERENCES `personas` (`id_persona`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `detalle_compra`
--

DROP TABLE IF EXISTS `detalle_compra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_compra` (
  `id_detalle_compra` int NOT NULL AUTO_INCREMENT,
  `cantidad` int DEFAULT NULL,
  `precio` decimal(10,2) DEFAULT NULL,
  `id_factura_compras` int NOT NULL,
  `id_producto` int NOT NULL,
  PRIMARY KEY (`id_detalle_compra`),
  KEY `fk_DETALLE_COMPRA_COMPRAS1_idx` (`id_factura_compras`),
  KEY `fk_DETALLE_COMPRA_PRODUCTOS1_idx` (`id_producto`),
  CONSTRAINT `fk_DETALLE_COMPRA_COMPRAS1` FOREIGN KEY (`id_factura_compras`) REFERENCES `factura_compras` (`id_factura_compras`) ON DELETE CASCADE,
  CONSTRAINT `fk_DETALLE_COMPRA_PRODUCTOS1` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `detalle_venta`
--

DROP TABLE IF EXISTS `detalle_venta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_venta` (
  `id_venta_producto` int NOT NULL AUTO_INCREMENT,
  `cantidad` int DEFAULT NULL,
  `id_producto` int NOT NULL,
  `id_factura_ventas` int NOT NULL,
  `monto` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id_venta_producto`),
  KEY `fk_VENTA_PRODUCTO_PRODUCTO1_idx` (`id_producto`),
  KEY `fk_VENTA_PRODUCTO_VENTAS1_idx` (`id_factura_ventas`),
  CONSTRAINT `fk_VENTA_PRODUCTO_PRODUCTO1` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`),
  CONSTRAINT `fk_VENTA_PRODUCTO_VENTAS1` FOREIGN KEY (`id_factura_ventas`) REFERENCES `factura_ventas` (`id_factura_ventas`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `devoluciones`
--

DROP TABLE IF EXISTS `devoluciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `devoluciones` (
  `id_devoluciones` int NOT NULL AUTO_INCREMENT,
  `fecha_hora` datetime DEFAULT NULL,
  `descripcion` text,
  `total` decimal(10,2) DEFAULT NULL,
  `id_producto` int NOT NULL,
  `factura` int DEFAULT NULL,
  PRIMARY KEY (`id_devoluciones`),
  KEY `fk_devoluciones_productos1_idx` (`id_producto`),
  KEY `foranea_facturaventa_idx` (`factura`),
  CONSTRAINT `fk_devoluciones_productos1` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`),
  CONSTRAINT `foranea_facturaventa` FOREIGN KEY (`factura`) REFERENCES `factura_ventas` (`id_factura_ventas`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `empleados`
--

DROP TABLE IF EXISTS `empleados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empleados` (
  `id_empleado` int NOT NULL AUTO_INCREMENT,
  `nombre_usuario` varchar(45) DEFAULT NULL,
  `clave` varchar(45) DEFAULT NULL,
  `esAdmin` char(1) DEFAULT NULL,
  `idpersona` int NOT NULL,
  PRIMARY KEY (`id_empleado`),
  KEY `fk_empleados_personas1_idx` (`idpersona`),
  CONSTRAINT `fk_empleados_personas1` FOREIGN KEY (`idpersona`) REFERENCES `personas` (`id_persona`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `factura_compras`
--

DROP TABLE IF EXISTS `factura_compras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `factura_compras` (
  `id_factura_compras` int NOT NULL AUTO_INCREMENT,
  `fecha` date DEFAULT NULL,
  `numero` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tipo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `subtotal` decimal(10,2) DEFAULT NULL,
  `totalSinIVA` decimal(10,2) DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  `proveedor` int DEFAULT NULL,
  PRIMARY KEY (`id_factura_compras`),
  KEY `foranea_proveedor_idx` (`proveedor`),
  CONSTRAINT `foranea_proveedor` FOREIGN KEY (`proveedor`) REFERENCES `proveedores` (`id_proveedor`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `factura_ventas`
--

DROP TABLE IF EXISTS `factura_ventas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `factura_ventas` (
  `id_factura_ventas` int NOT NULL AUTO_INCREMENT,
  `numero` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fechaDeEmision` date DEFAULT NULL,
  `subtotal` decimal(10,2) DEFAULT NULL,
  `iva` decimal(10,2) DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  `cliente` int NOT NULL,
  `empleado` int NOT NULL,
  PRIMARY KEY (`id_factura_ventas`),
  KEY `fkcliente_idx` (`cliente`),
  KEY `fkempleado_idx` (`empleado`),
  CONSTRAINT `fkcliente` FOREIGN KEY (`cliente`) REFERENCES `clientes` (`id_cliente`),
  CONSTRAINT `fkempleado` FOREIGN KEY (`empleado`) REFERENCES `empleados` (`id_empleado`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `personas`
--

DROP TABLE IF EXISTS `personas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personas` (
  `id_persona` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) DEFAULT NULL,
  `apellido` varchar(45) DEFAULT NULL,
  `provincia` varchar(50) DEFAULT NULL,
  `localidad` varchar(50) DEFAULT NULL,
  `calle` varchar(50) DEFAULT NULL,
  `dni` varchar(8) DEFAULT NULL,
  `cuil` varchar(11) DEFAULT NULL,
  `cuit` varchar(11) DEFAULT NULL,
  `razon_social` varchar(45) DEFAULT NULL,
  `mail` varchar(255) DEFAULT NULL,
  `telefono` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`id_persona`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `id_producto` int NOT NULL AUTO_INCREMENT,
  `codigo_barra` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nombre` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `precio_venta` decimal(10,2) DEFAULT NULL,
  `cantidad_disponible` int DEFAULT NULL,
  `cantidad_minima` int DEFAULT NULL,
  `id_rubro` int NOT NULL,
  `id_proveedor` int NOT NULL,
  `precio_lista` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id_producto`),
  KEY `fk_PRODUCTOS_RUBROS1_idx` (`id_rubro`),
  KEY `fk_productos_proveedores1_idx` (`id_proveedor`),
  CONSTRAINT `fk_productos_proveedores1` FOREIGN KEY (`id_proveedor`) REFERENCES `proveedores` (`id_proveedor`),
  CONSTRAINT `fk_PRODUCTOS_RUBROS1` FOREIGN KEY (`id_rubro`) REFERENCES `rubros` (`id_rubro`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `proveedores`
--

DROP TABLE IF EXISTS `proveedores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proveedores` (
  `id_proveedor` int NOT NULL AUTO_INCREMENT,
  `id_persona` int NOT NULL,
  PRIMARY KEY (`id_proveedor`),
  KEY `fk_proveedores_personas1_idx` (`id_persona`),
  CONSTRAINT `fk_proveedores_personas1` FOREIGN KEY (`id_persona`) REFERENCES `personas` (`id_persona`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rubros`
--

DROP TABLE IF EXISTS `rubros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rubros` (
  `id_rubro` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_rubro`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-18 19:12:27
