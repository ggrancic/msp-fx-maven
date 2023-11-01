-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: mercadito
-- ------------------------------------------------------
-- Server version	8.0.34

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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (1,2),(2,6),(6,13),(7,14),(8,15),(9,16),(10,17);
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_compra`
--

LOCK TABLES `detalle_compra` WRITE;
/*!40000 ALTER TABLE `detalle_compra` DISABLE KEYS */;
INSERT INTO `detalle_compra` VALUES (1,2,250.00,1,2),(2,1,250.00,2,2),(3,1,25.00,2,9),(4,1,250.00,4,2),(5,5,250.00,6,2),(6,2,25.00,7,9),(7,5,250.00,8,2),(8,1,250.00,9,2),(9,5,250.00,10,2),(10,1,250.00,11,2),(11,3,600.00,12,10),(12,3,600.00,13,10),(13,1,250.00,14,2),(14,1,250.00,15,2),(15,1,25.00,16,12),(16,1,25.00,17,16),(17,1,250.00,18,2),(18,1,25.00,19,14),(19,3,25.00,20,14),(20,5,25.00,21,11),(21,1,25.00,22,9);
/*!40000 ALTER TABLE `detalle_compra` ENABLE KEYS */;
UNLOCK TABLES;

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
  PRIMARY KEY (`id_venta_producto`),
  KEY `fk_VENTA_PRODUCTO_PRODUCTO1_idx` (`id_producto`),
  KEY `fk_VENTA_PRODUCTO_VENTAS1_idx` (`id_factura_ventas`),
  CONSTRAINT `fk_VENTA_PRODUCTO_PRODUCTO1` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`),
  CONSTRAINT `fk_VENTA_PRODUCTO_VENTAS1` FOREIGN KEY (`id_factura_ventas`) REFERENCES `factura_ventas` (`id_factura_ventas`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_venta`
--

LOCK TABLES `detalle_venta` WRITE;
/*!40000 ALTER TABLE `detalle_venta` DISABLE KEYS */;
INSERT INTO `detalle_venta` VALUES (1,4,2,1),(2,1,2,2),(3,1,10,3),(4,1,11,4),(5,1,2,5),(6,1,2,6),(7,2,9,6);
/*!40000 ALTER TABLE `detalle_venta` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `devoluciones`
--

LOCK TABLES `devoluciones` WRITE;
/*!40000 ALTER TABLE `devoluciones` DISABLE KEYS */;
/*!40000 ALTER TABLE `devoluciones` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empleados`
--

LOCK TABLES `empleados` WRITE;
/*!40000 ALTER TABLE `empleados` DISABLE KEYS */;
INSERT INTO `empleados` VALUES (1,'40737243','123','S',1),(2,'20651368','123','N',5),(3,'39752040','39752040','S',21);
/*!40000 ALTER TABLE `empleados` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `factura_compras`
--

LOCK TABLES `factura_compras` WRITE;
/*!40000 ALTER TABLE `factura_compras` DISABLE KEYS */;
INSERT INTO `factura_compras` VALUES (1,'2023-10-28','345354353553','A',413.22,86.78,500.00,1),(2,'2023-10-28','475765567567','A',227.27,47.73,275.00,1),(4,'2023-10-28','564645645654','A',206.61,43.39,250.00,2),(5,'2023-10-28','645646454456','A',0.00,0.00,0.00,1),(6,'2023-10-28','634565654645','A',1033.06,216.94,1250.00,1),(7,'2023-10-28','656454655645','B',50.00,0.00,50.00,1),(8,'2023-10-28','534553453453','A',1033.06,216.94,1250.00,1),(9,'2023-10-28','345345635465','A',206.61,43.39,250.00,1),(10,'2023-10-28','457457457547','A',1033.06,216.94,1250.00,1),(11,'2023-10-28','664564565465','A',206.61,43.39,250.00,1),(12,'2023-10-28','654577667565','A',1487.60,312.40,1800.00,1),(13,'2023-10-28','645767675756','A',1487.60,312.40,1800.00,2),(14,'2023-10-30','463434665365','A',206.61,43.39,250.00,3),(15,'2023-10-30','456456456459','A',206.61,43.39,250.00,3),(16,'2023-10-30','677565677657','A',20.66,4.34,25.00,3),(17,'2023-10-30','243543345534','B',25.00,0.00,25.00,3),(18,'2023-10-30','634636534565','A',206.61,43.39,250.00,1),(19,'2023-10-30','546756787568','A',20.66,4.34,25.00,3),(20,'2023-10-30','656456464645','A',61.98,13.02,75.00,3),(21,'2023-10-30','76657756756','A',103.31,21.69,125.00,3),(22,'2023-10-31','456545464564','A',20.66,4.34,25.00,3);
/*!40000 ALTER TABLE `factura_compras` ENABLE KEYS */;
UNLOCK TABLES;

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
  `empleado` int DEFAULT NULL,
  PRIMARY KEY (`id_factura_ventas`),
  KEY `fk_factura_ventas_personas1_idx` (`cliente`),
  CONSTRAINT `fk_factura_ventas_personas1` FOREIGN KEY (`cliente`) REFERENCES `personas` (`id_persona`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `factura_ventas`
--

LOCK TABLES `factura_ventas` WRITE;
/*!40000 ALTER TABLE `factura_ventas` DISABLE KEYS */;
INSERT INTO `factura_ventas` VALUES (1,'000000000001','B','2023-10-28',2000.00,0.00,2000.00,1,1),(2,'000000000002','B','2023-10-28',312.50,0.00,312.50,1,1),(3,'000000000003','A','2023-10-28',619.83,0.00,750.00,2,2),(4,'000000000004','B','2023-10-29',25.00,0.00,25.00,1,1),(5,'000000000005','B','2023-10-30',312.50,0.00,312.50,1,1),(6,'000000000006','B','2023-10-31',1985.00,0.00,1985.00,1,1);
/*!40000 ALTER TABLE `factura_ventas` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personas`
--

LOCK TABLES `personas` WRITE;
/*!40000 ALTER TABLE `personas` DISABLE KEYS */;
INSERT INTO `personas` VALUES (1,'Daniel','Urban','Chaco','Resistencia','Calle 7','40737243',NULL,'31244132342','Daniel Urban','dani@gmail.com','3624569428'),(2,'Consumidor','Final','Chaco','Sáenz Peña','0','99999999','99999999999',NULL,'Consumidor Final','consumidorfinal@gmail.com','0000000000'),(3,'Dario','Dragovich','Córdoba','Córdoba','Calle 6','65346646',NULL,'45645645646','Dario Dragovich','dragui@gmail.com','3624551122'),(4,NULL,NULL,'Catamarca','Catamarca','Calle 15',NULL,NULL,'53454353535','Golomax','golo.max@gmail.com','5234435435'),(5,'Jose Ruben','Urban','Chaco','Sáenz Peña','Calle 7','20651368',NULL,NULL,NULL,'jose@gmail.com','5343455354'),(6,NULL,NULL,'Chaco','Resistencia','Ruta 11 Km 45',NULL,'90595954954',NULL,'Easy Cencosud','easy@gmail.com','4654645465'),(12,NULL,NULL,'Neuquén','Neuquén','Calle 1 Entre 5',NULL,NULL,'34534534553','Azza','azza@gmail.com','2345345354'),(13,NULL,NULL,'Chaco','Sáenz Peña','Calle 21',NULL,'66455464654',NULL,'Changomas','changuito@gmail.com','3644411122'),(14,NULL,NULL,'Jujuy','San Salvador De Jujuy','Calle 1',NULL,'56454564546',NULL,'Los Hermanos','hnos_2023@gmail.com','5345344354'),(15,'Sebastian','Flashka','Chaco','Resistencia','Avenida Sabin 1500','40919191','20409191911',NULL,'Sebastian Flashka','seba_flash@gmail.com','5345345543'),(16,'Facundo','Roque','Chaco','Resistencia','San Lorenzo 320','45645456','65456454565',NULL,'Facundo Roque','facu_roque@gmail.com','4254534533'),(17,NULL,NULL,'Chaco','Resistencia','Avenida Alvear',NULL,'64566755677',NULL,'Orion Srl','orion@gmail.com','5435433453'),(19,NULL,NULL,'Buenos Aires','Bs As','Dsasd',NULL,NULL,'98888888888','La Dolce','new@gmail.com','3425534345'),(21,'Gian Luca','Grancic','Chaco','Sáenz Peña','Avellaneda','39752040',NULL,NULL,NULL,'gian@gmail.com','3644457375');
/*!40000 ALTER TABLE `personas` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (2,'1234567891111','Oreo Frutilla',425.00,14,5,1,1,250.00),(9,'5345345345445','Manaos Uva 2lts',780.00,24,5,2,3,25.00),(10,'3466556445654','Manaos Coca 2lts',960.00,55,5,2,2,600.00),(11,'3463546454564','Pepas 9 De Oro 200g',600.00,50,5,1,2,25.00),(12,'4567475656775','Vino Santa Julia 910ml',350.00,55,5,2,1,25.00),(14,'5463645764756','Vino Tinto Malbec 910ml',1200.00,30,5,2,1,25.00),(16,'7556758756675','Papas Lays 150g',1350.00,45,5,4,6,25.00);
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proveedores`
--

LOCK TABLES `proveedores` WRITE;
/*!40000 ALTER TABLE `proveedores` DISABLE KEYS */;
INSERT INTO `proveedores` VALUES (3,1),(1,3),(2,4),(6,12),(7,19);
/*!40000 ALTER TABLE `proveedores` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rubros`
--

LOCK TABLES `rubros` WRITE;
/*!40000 ALTER TABLE `rubros` DISABLE KEYS */;
INSERT INTO `rubros` VALUES (1,'Galletas'),(2,'Bebidas'),(3,'Limpieza'),(4,'Snacks'),(7,'Lacteos');
/*!40000 ALTER TABLE `rubros` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-31 12:10:39
