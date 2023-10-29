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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_compra`
--

LOCK TABLES `detalle_compra` WRITE;
/*!40000 ALTER TABLE `detalle_compra` DISABLE KEYS */;
INSERT INTO `detalle_compra` VALUES (1,2,250.00,1,2),(2,1,250.00,2,2),(3,1,25.00,2,9),(4,1,250.00,4,2),(5,5,250.00,6,2),(6,2,25.00,7,9),(7,5,250.00,8,2),(8,1,250.00,9,2),(9,5,250.00,10,2),(10,1,250.00,11,2),(11,3,600.00,12,10),(12,3,600.00,13,10);
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_venta`
--

LOCK TABLES `detalle_venta` WRITE;
/*!40000 ALTER TABLE `detalle_venta` DISABLE KEYS */;
INSERT INTO `detalle_venta` VALUES (1,4,2,1),(2,1,2,2),(3,1,10,3),(4,1,11,4);
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empleados`
--

LOCK TABLES `empleados` WRITE;
/*!40000 ALTER TABLE `empleados` DISABLE KEYS */;
INSERT INTO `empleados` VALUES (1,'40737243','123','S',1),(2,'20651368','123','N',5);
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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `factura_compras`
--

LOCK TABLES `factura_compras` WRITE;
/*!40000 ALTER TABLE `factura_compras` DISABLE KEYS */;
INSERT INTO `factura_compras` VALUES (1,'2023-10-28','345354353553','A',413.22,86.78,500.00,1),(2,'2023-10-28','475765567567','A',227.27,47.73,275.00,1),(4,'2023-10-28','564645645654','A',206.61,43.39,250.00,2),(5,'2023-10-28','645646454456','A',0.00,0.00,0.00,1),(6,'2023-10-28','634565654645','A',1033.06,216.94,1250.00,1),(7,'2023-10-28','656454655645','B',50.00,0.00,50.00,1),(8,'2023-10-28','534553453453','A',1033.06,216.94,1250.00,1),(9,'2023-10-28','345345635465','A',206.61,43.39,250.00,1),(10,'2023-10-28','457457457547','A',1033.06,216.94,1250.00,1),(11,'2023-10-28','664564565465','A',206.61,43.39,250.00,1),(12,'2023-10-28','654577667565','A',1487.60,312.40,1800.00,1),(13,'2023-10-28','645767675756','A',1487.60,312.40,1800.00,2);
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `factura_ventas`
--

LOCK TABLES `factura_ventas` WRITE;
/*!40000 ALTER TABLE `factura_ventas` DISABLE KEYS */;
INSERT INTO `factura_ventas` VALUES (1,'000000000001','B','2023-10-28',2000.00,0.00,2000.00,1,1),(2,'000000000002','B','2023-10-28',312.50,0.00,312.50,1,1),(3,'000000000003','A','2023-10-28',619.83,0.00,750.00,2,2),(4,'000000000004','B','2023-10-29',25.00,0.00,25.00,1,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personas`
--

LOCK TABLES `personas` WRITE;
/*!40000 ALTER TABLE `personas` DISABLE KEYS */;
INSERT INTO `personas` VALUES (1,'Daniel','Urban','Chaco','Sp','Aa','40737243',NULL,'31244132342','Los Perezzz','da@gmail.com','3624569428'),(2,'Consumidor','Final','Chaco','Sp','20','99999999','99999999999',NULL,'Consumidor Final','da@gmail.com','5242353443'),(3,'Arcor','Sa','Chaco','Sp','25253534','65346646',NULL,'45645645646','Arcor Sa','sa@gmail.com','5344354353'),(4,NULL,NULL,'Chaco','Sass','525234',NULL,NULL,'53454353535','Arcoroso','5@gmail.com','5234435435'),(5,'Jose Ruben','Urban','Chaco','Sp','Sp','20651368',NULL,NULL,NULL,'jose@gmail.com','5343455354'),(6,NULL,NULL,'Catamarca','Sa','Calle 28',NULL,'90595954954',NULL,'Confyyy','confy@gmail.com','4654645465'),(12,NULL,NULL,'Neuquén','Saasas','45345',NULL,NULL,'34534534553','Azza','azza@gmail.com','2345345354'),(13,NULL,NULL,'Catamarca','Sp','Za',NULL,'66455464654',NULL,'Nejardo','na@gmail.com','5443553453'),(14,NULL,NULL,'Chaco','Saenz','Sp',NULL,'56454564546',NULL,'Los Hermanos','heramnos@gmail.com','5345344354'),(15,'Dany','Nyda','Chaco','Eeee','Sp','00000000','00000000000',NULL,'Dany Nyda','a@gmail.com','5345345543'),(16,'Ree','Reaa','Chaco','Afgff','Sp','45645456','65446564565',NULL,'Ree Reaa','aa@gmail.com','4254534533'),(17,NULL,NULL,'Catamarca','Aaa','Gg',NULL,'64566755677',NULL,'See','aaa@gmail.com','5435433453'),(19,NULL,NULL,'Chaco','Asda','Dsasd',NULL,NULL,'98888888888','New Proveed','new@gmail.com','3425534345');
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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (2,'1234567891111','Oreo',312.50,66,5,1,1,250.00),(9,'5345345345445','Manaos Uva 2lts',780.00,25,5,2,1,25.00),(10,'3466556445654','Manaos Coca 2lts',960.00,55,5,2,2,600.00),(11,'3463546454564','Pepas',25.00,5,52,1,3,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rubros`
--

LOCK TABLES `rubros` WRITE;
/*!40000 ALTER TABLE `rubros` DISABLE KEYS */;
INSERT INTO `rubros` VALUES (1,'Galletas'),(2,'Bebidas'),(3,'Limpieza'),(4,'Snacks'),(5,'Cigarrillos');
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

-- Dump completed on 2023-10-29 18:32:43
