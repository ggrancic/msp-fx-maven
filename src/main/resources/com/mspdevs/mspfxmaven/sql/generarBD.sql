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
  `cuil` varchar(11) NOT NULL,
  PRIMARY KEY (`id_cliente`),
  KEY `fk_clientes_personas1_idx` (`idpersona`),
  CONSTRAINT `fk_clientes_personas1` FOREIGN KEY (`idpersona`) REFERENCES `personas` (`id_persona`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (1,5,'00000000000'),(2,6,'20407372430'),(3,2,'20407372434'),(4,24,'34345534534'),(5,25,'34554334534'),(6,2,'20407372434');
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
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_compra`
--

LOCK TABLES `detalle_compra` WRITE;
/*!40000 ALTER TABLE `detalle_compra` DISABLE KEYS */;
INSERT INTO `detalle_compra` VALUES (1,15,22500.00,77,6),(2,10,15000.00,78,6),(3,10,2000.00,78,3),(4,8,1200.00,79,9),(5,5,1000.00,81,3),(6,1,200.00,83,3),(7,1,1500.00,83,6),(8,1,200.00,84,3),(9,1,1500.00,84,6),(10,2,3000.00,85,6),(11,2,400.00,86,3),(12,12,2400.00,87,3),(13,2,400.00,88,3),(14,3,600.00,89,3),(15,1,1500.00,90,6),(16,2,400.00,91,3),(17,5,750.00,92,3),(18,5,750.00,93,3),(19,2,300.00,94,3),(20,3,450.00,94,9),(21,5,150.00,95,3),(22,5,25.00,96,15),(23,1,1600.00,99,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_venta`
--

LOCK TABLES `detalle_venta` WRITE;
/*!40000 ALTER TABLE `detalle_venta` DISABLE KEYS */;
INSERT INTO `detalle_venta` VALUES (1,2,1,12),(2,5,1,13),(3,12,1,14);
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
INSERT INTO `empleados` VALUES (1,'39752040','39752040','S',1),(2,'40737243','123','S',2);
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
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `factura_compras`
--

LOCK TABLES `factura_compras` WRITE;
/*!40000 ALTER TABLE `factura_compras` DISABLE KEYS */;
INSERT INTO `factura_compras` VALUES (1,'2023-10-15','432342342423','A',123967.00,26033.00,150000.00,1),(2,'2023-10-15','555555555111','A',1363636.00,286364.00,1650000.00,1),(3,'2023-10-15','000000000000','B',619835.00,130165.00,750000.00,1),(4,'2023-10-15','555554353454','A',1157025.00,242975.00,1400000.00,1),(5,'2023-10-15','144444444444','B',65000.00,0.00,65000.00,1),(6,'2023-10-15','511111111111','A',123967.00,26033.00,150000.00,1),(7,'2023-10-15','010101010101','A',1239.67,260.33,1500.00,1),(8,'2023-10-05','235445353453','A',826.45,173.55,1000.00,1),(9,'2023-10-15','423432424234','A',0.00,0.00,0.00,1),(10,'2023-10-15','545435453453','A',0.00,0.00,0.00,1),(11,'2023-10-15','534533455353','A',1239.67,260.33,1500.00,1),(12,'2023-10-15','076575675765','A',1239.67,260.33,1500.00,1),(13,'2023-10-15','666666666666','A',1239.67,260.33,1500.00,1),(14,'2023-10-15','545354344444','B',15000.00,0.00,15000.00,1),(15,'2023-10-15','645645645664','B',90000.00,0.00,90000.00,2),(16,'2023-10-15','554441112222','A',1239.67,260.33,1500.00,1),(17,'2023-10-16','553245345335','A',1239.67,260.33,1500.00,1),(18,'2023-10-16','343111111111','A',2479.34,520.66,3000.00,1),(19,'2023-10-16','545354353453','A',1239.67,260.33,1500.00,1),(20,'2023-10-16','764646666666','A',2479.34,520.66,3000.00,1),(21,'2023-10-16','534534534534','A',1239.67,260.33,1500.00,1),(22,'2023-10-16','577775555555','A',1239.67,260.33,1500.00,1),(23,'2023-10-16','565444444444','A',1239.67,260.33,1500.00,1),(24,'2023-10-16','566644444444','A',1239.67,260.33,1500.00,1),(25,'2023-10-16','798999999999','A',1239.67,260.33,1500.00,1),(26,'2023-10-16','545354354353','A',1239.67,260.33,1500.00,1),(27,'2023-10-16','009999999999','A',1239.67,260.33,1500.00,1),(28,'2023-10-16','121122456666','A',1239.67,260.33,1500.00,1),(29,'2023-10-16','587866666666','A',1239.67,260.33,1500.00,1),(30,'2023-10-16','008888888888','A',1239.67,260.33,1500.00,1),(31,'2023-10-16','641111111111','A',1239.67,260.33,1500.00,1),(32,'2023-10-16','688889999999','A',1239.67,260.33,1500.00,1),(33,'2023-10-16','688888888888','A',7438.02,1561.98,9000.00,1),(34,'2023-10-16','774444411111','A',1239.67,260.33,1500.00,1),(35,'2023-10-16','098989898989','A',2479.34,520.66,3000.00,1),(36,'2023-10-16','788877788888','A',1239.67,260.33,1500.00,1),(37,'2023-10-16','666787777777','A',1239.67,260.33,1500.00,1),(38,'2023-10-16','000595959595','A',1239.67,260.33,1500.00,1),(39,'2023-10-16','867866678678','A',8677.69,1822.31,10500.00,1),(40,'2023-10-16','555555553333','A',7438.02,1561.98,9000.00,1),(41,'2023-10-16','543534444444','A',1239.67,260.33,1500.00,1),(42,'2023-10-16','534535344444','A',7438.02,1561.98,9000.00,1),(43,'2023-10-16','575565675656','A',1239.67,260.33,1500.00,1),(44,'2023-10-16','099000000000','A',4545.45,954.55,5500.00,1),(45,'2023-10-16','099988899999','A',1239.67,260.33,1500.00,1),(46,'2023-10-16','555444444444','A',1239.67,260.33,1500.00,1),(47,'2023-10-16','544442222222','A',1239.67,260.33,1500.00,1),(48,'2023-10-16','099900000000','A',1239.67,260.33,1500.00,1),(49,'2023-10-16','099999999999','A',1239.67,260.33,1500.00,1),(50,'2023-10-16','646456546456','A',1239.67,260.33,1500.00,1),(51,'2023-10-16','543435345343','A',1239.67,260.33,1500.00,1),(52,'2023-10-16','414234423423','A',1239.67,260.33,1500.00,1),(53,'2023-10-16','234234234232','A',1239.67,260.33,1500.00,1),(54,'2023-10-16','534544442322','A',1239.67,260.33,1500.00,1),(55,'2023-10-16','534534343434','A',1239.67,260.33,1500.00,1),(56,'2023-10-16','665455454444','A',2479.34,520.66,3000.00,1),(57,'2023-10-16','009677777777','A',1239.67,260.33,1500.00,1),(58,'2023-10-16','888777777766','A',1239.67,260.33,1500.00,1),(59,'2023-10-16','111222333333','A',25702.48,5397.52,31100.00,1),(60,'2023-10-16','543535354334','A',3719.01,780.99,4500.00,1),(61,'2023-10-16','545453444444','A',1859.50,390.50,2250.00,1),(62,'2023-10-16','986887686786','A',1325950.41,278449.59,1604400.00,1),(63,'2023-10-16','009888888888','B',17294000.00,0.00,17294000.00,2),(64,'2023-10-16','543534343333','A',3966.94,833.06,4800.00,1),(65,'2023-10-16','675675675675','A',2644.63,555.37,3200.00,2),(66,'2023-10-16','999888999999','A',6611.57,1388.43,8000.00,2),(67,'2023-10-16','000009999999','A',13223.14,2776.86,16000.00,3),(68,'2023-10-16','456455554443','A',4958.68,1041.32,6000.00,1),(69,'2023-10-16','543334453453','A',6611.57,1388.43,8000.00,2),(70,'2023-10-16','098799999999','A',4132.23,867.77,5000.00,2),(71,'2023-10-16','545353543543','A',13636.36,2863.64,16500.00,1),(72,'2023-10-16','999888888888','A',101652.89,21347.11,123000.00,1),(73,'2023-10-16','646456464564','A',4132.23,867.77,5000.00,1),(74,'2023-10-16','523223421343','A',6198.35,1301.65,7500.00,1),(75,'2023-10-16','452342322242','A',414.26,86.99,501.25,1),(76,'2023-10-16','453535345345','A',826.45,173.55,1000.00,2),(77,'2023-10-16','566678866666','A',18595.04,3904.96,22500.00,1),(78,'2023-10-16','123456789011','A',14049.59,2950.41,17000.00,1),(79,'2023-10-16','111333555555','A',991.74,208.26,1200.00,2),(80,'2023-10-16','542354334435','A',826.45,173.55,1000.00,1),(81,'2023-10-16','534534553543','A',826.45,173.55,1000.00,1),(82,'2023-10-16','545353534534','A',0.00,0.00,0.00,1),(83,'2023-10-16','756765756767','A',1404.96,295.04,1700.00,1),(84,'2023-10-16','756765756767','A',1404.96,295.04,1700.00,1),(85,'2023-10-17','345354354353','A',2479.34,520.66,3000.00,1),(86,'2023-10-17','345354354353','A',330.58,69.42,400.00,1),(87,'2023-10-17','466456464664','A',1983.47,416.53,2400.00,1),(88,'2023-10-17','654646465464','A',330.58,69.42,400.00,1),(89,'2023-10-17','464564564564','A',495.87,104.13,600.00,1),(90,'2023-10-17','967966788678','A',1239.67,260.33,1500.00,1),(91,'2023-10-17','786868768686','C',400.00,0.00,400.00,1),(92,'2023-10-17','767856856567','A',619.83,130.17,750.00,1),(93,'2023-10-17','542342234324','A',619.83,130.17,750.00,1),(94,'2023-10-17','000099999999','A',619.83,130.17,750.00,1),(95,'2023-10-17','41234324245','A',619.83,130.17,750.00,1),(96,'2023-10-18','563456456546','A',103.31,21.69,125.00,2),(97,'2023-10-18','535354354353','A',0.00,0.00,0.00,1),(98,'2023-10-18','636436564646','A',0.00,0.00,0.00,1),(99,'2023-10-22','645645645464','A',1322.31,277.69,1600.00,1);
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
  PRIMARY KEY (`id_factura_ventas`),
  KEY `fk_factura_ventas_personas1_idx` (`cliente`),
  CONSTRAINT `fk_factura_ventas_personas1` FOREIGN KEY (`cliente`) REFERENCES `personas` (`id_persona`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `factura_ventas`
--

LOCK TABLES `factura_ventas` WRITE;
/*!40000 ALTER TABLE `factura_ventas` DISABLE KEYS */;
INSERT INTO `factura_ventas` VALUES (1,'345435354353','B','2023-10-22',2000.00,0.00,2000.00,1),(2,'423442423423','A','2023-10-22',3305.79,694.21,4000.00,2),(3,'545435353434','B','2023-10-22',24000.00,0.00,24000.00,1),(4,'545355353455','B','2023-10-22',562.50,0.00,562.50,1),(5,'636345645654','B','2023-10-22',2000.00,0.00,2000.00,1),(6,'543543535345','B','2023-10-22',2080.00,0.00,2080.00,1),(7,'234324424324','B','2023-10-22',2080.00,0.00,2080.00,1),(8,'345345435353','B','2023-10-22',2080.00,0.00,2080.00,1),(9,'234234234242','B','2023-10-22',4160.00,0.00,4160.00,1),(10,'454353543534','B','2023-10-22',4160.00,0.00,4160.00,1),(11,'534354534354','B','2023-10-22',4160.00,0.00,4160.00,1),(12,'435353453453','B','2023-10-22',4160.00,0.00,4160.00,1),(13,'435543345343','B','2023-10-23',10400.00,0.00,10400.00,4),(14,'345345433545','B','2023-10-23',24960.00,0.00,24960.00,1);
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
  `mail` varchar(255) DEFAULT NULL,
  `telefono` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`id_persona`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personas`
--

LOCK TABLES `personas` WRITE;
/*!40000 ALTER TABLE `personas` DISABLE KEYS */;
INSERT INTO `personas` VALUES (1,'Gianluca','Grancic Lapasini','Chaco','Saenz Peña','Avellaneda 933','39752040','grancicg@gmail.com','3644457375'),(2,'Daniel','Urban','Chubut','Saenz Peña','Sarmiento','40737243','daniel@gmail.com','3624569428'),(3,'Arcor','Arcoroso','Chaco','Sp','Sp','4099910','arcormayorista@gmail.com','3644101010'),(4,'Random','Randomiz','Chaco','Sp','Sp','40011345','random@gmail.com','3644001122'),(5,'Consumidor','Final','Chaco','Sp','Sp','00000000','consumidorfinal@gmail.com','3644456204'),(6,'Pedro','Perez','Chaco','Sp','Sp','40737243','pedro@gmail.com','3644456204'),(7,'Sin Nombre','Texto','Chaco','Sp','Sp','56435345','sin@gmail.com','5232523452'),(8,'Yaaki','Yuuki','Corrientes','Corrientezzz','Corrientezzz','53453563',NULL,'4234232342'),(9,'Test','Testtt','Chaco','Sp','Sp','45534535',NULL,'5423535435'),(12,'Juancito','Juancito','Chaco','Sp','Ola','53435353',NULL,'5325435345'),(13,'Juan','Juan','Chaco','Sp','345','53455435',NULL,'3545345335'),(14,'Chaco','Sp','Sp','Sp','Sp','54245354',NULL,'3453453543'),(15,'Yo','Yo','Córdoba','Fdfd','5','53534534','da@gmail.com','5345354334'),(16,'Freddie','Mercury','Chaco','Sp','Sp','54535345',NULL,'5345353535'),(18,'Pap','Apapapa','Chaco','Sp','423423','45435355','','5232345353'),(19,'Pruebas','Pruebas','Chaco','Sp','414324','14234234','55@gail.com','2452352234'),(20,'Testttt','Asd','Cccc','Ccc','53453535345435','54334563','a@gmail.com','3453454353'),(21,'Testttto','Asdasd','Ccc','Gsdsfs','Fdsfd','55443565','d@gmail.com','5245435544'),(22,'Holahola','Gregfdg','Chaco','Sp','423534','53645676','DA@GMAIL.COM','5234543253'),(23,'Olamundoooo','Mundo','Chaco','Sp','2534435','45345435','da@gmail.com','3453453434'),(24,'Pepe','Gonzalez','Chaco','Sp','24534435','53454334','pepe@gmail.com','5345343453'),(25,'Pepito','Gonzo','Chaco','Sp','53454354','55435334','pep@gmail.com','5324255254');
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
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (1,'1231423442342','3lts Manaos',2080.00,961,5,1,1,1600.00),(2,'4235345435354','Manaos 2lts',400.00,40,5,1,2,500.00),(3,'3346345345345','Merengadas',187.50,720,20,3,1,150.00),(5,'5345345434534','Pepsi 1lt',300.00,50,2,1,2,150.00),(6,'3453454354334','Lays 100grs',1875.00,77,5,3,1,1500.00),(7,'5345345435345','Papitas',500.00,59,5,2,1,NULL),(8,'5254354345354','Pepitos',588.00,5,1,1,1,NULL),(9,'5524355345353','Facturas',187.50,31,5,3,1,150.00),(15,'3453535345353','Dulces Grandes',39.25,5,1,1,2,25.00),(16,'5245342534533','Dulces Parte 2g',0.00,0,3,2,2,NULL),(17,'5345334534543','Chaco',0.00,0,1,1,1,NULL),(18,'5435353454543','Chaco 3',0.00,0,5,1,1,NULL),(19,'9879897978797','Chaco 5',0.00,0,1,1,1,NULL),(20,'5345345345535','Sp5',0.00,0,1,1,1,NULL),(21,'6757575757567','Sp90',0.00,0,1,1,1,NULL),(22,'2412122323233','Sp100',0.00,0,6,2,1,NULL),(23,'5435353534545','Merengadas50',0.00,0,1,2,1,NULL),(24,'3454335353455','Chocotorta',0.00,0,10,2,1,NULL),(25,'5453453434534','345345',0.00,0,1,1,1,NULL),(26,'5453534535353','Werwrewr',0.00,0,1,1,1,NULL),(27,'1312444234234','3534543534',0.00,0,5,1,1,NULL),(28,'8575676577657','24a',0.00,0,6,2,1,NULL),(29,'4314123423242','A',0.00,0,1,1,1,NULL),(30,'5352423523543','Pepassss',0.00,0,45,1,1,NULL),(31,'3453453455345','131231',0.00,0,5,1,1,NULL);
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
  `CUIT` varchar(11) DEFAULT NULL,
  `id_persona` int NOT NULL,
  PRIMARY KEY (`id_proveedor`),
  KEY `fk_proveedores_personas1_idx` (`id_persona`),
  CONSTRAINT `fk_proveedores_personas1` FOREIGN KEY (`id_persona`) REFERENCES `personas` (`id_persona`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proveedores`
--

LOCK TABLES `proveedores` WRITE;
/*!40000 ALTER TABLE `proveedores` DISABLE KEYS */;
INSERT INTO `proveedores` VALUES (1,'1040999102',3),(2,'20400113454',4),(3,'20564353454',7),(4,'55353534535',8),(5,'53435334534',9),(8,'54353453453',12),(9,'53543553453',13),(10,'53455345345',14),(11,'34535345345',15),(12,'53453453453',16),(14,'44234242342',3),(15,'20397520402',1),(16,'20397520405',1),(17,'20407372434',2),(18,'34534534535',18),(19,'25334534534',19),(20,'34534554334',20),(21,'24535343455',21),(22,'65465456564',22),(23,'32434223443',23);
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rubros`
--

LOCK TABLES `rubros` WRITE;
/*!40000 ALTER TABLE `rubros` DISABLE KEYS */;
INSERT INTO `rubros` VALUES (1,'Bebidas'),(2,'Fiambres'),(3,'Galletas'),(4,'Bebidas Alcoholicas');
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

-- Dump completed on 2023-10-23 13:11:28
