-- MySQL Script generated by MySQL Workbench
-- Mon Nov 20 14:29:32 2023
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema mercadito
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mercadito
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mercadito` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `mercadito` ;

-- -----------------------------------------------------
-- Table `mercadito`.`personas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`personas` (
  `id_persona` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL DEFAULT NULL,
  `apellido` VARCHAR(45) NULL DEFAULT NULL,
  `provincia` VARCHAR(50) NULL DEFAULT NULL,
  `localidad` VARCHAR(50) NULL DEFAULT NULL,
  `calle` VARCHAR(50) NULL DEFAULT NULL,
  `dni` VARCHAR(8) NULL DEFAULT NULL,
  `cuil` VARCHAR(11) NULL DEFAULT NULL,
  `cuit` VARCHAR(11) NULL DEFAULT NULL,
  `razon_social` VARCHAR(45) NULL DEFAULT NULL,
  `mail` VARCHAR(255) NULL DEFAULT NULL,
  `telefono` VARCHAR(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id_persona`))
ENGINE = InnoDB
AUTO_INCREMENT = 37
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `mercadito`.`clientes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`clientes` (
  `id_cliente` INT NOT NULL AUTO_INCREMENT,
  `idpersona` INT NOT NULL,
  PRIMARY KEY (`id_cliente`),
  INDEX `fk_clientes_personas1_idx` (`idpersona` ASC) VISIBLE,
  CONSTRAINT `fk_clientes_personas1`
    FOREIGN KEY (`idpersona`)
    REFERENCES `mercadito`.`personas` (`id_persona`))
ENGINE = InnoDB
AUTO_INCREMENT = 21
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `mercadito`.`proveedores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`proveedores` (
  `id_proveedor` INT NOT NULL AUTO_INCREMENT,
  `id_persona` INT NOT NULL,
  PRIMARY KEY (`id_proveedor`),
  INDEX `fk_proveedores_personas1_idx` (`id_persona` ASC) VISIBLE,
  CONSTRAINT `fk_proveedores_personas1`
    FOREIGN KEY (`id_persona`)
    REFERENCES `mercadito`.`personas` (`id_persona`))
ENGINE = InnoDB
AUTO_INCREMENT = 16
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `mercadito`.`factura_compras`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`factura_compras` (
  `id_factura_compras` INT NOT NULL AUTO_INCREMENT,
  `fecha` DATE NULL DEFAULT NULL,
  `numero` VARCHAR(12) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL,
  `tipo` CHAR(1) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL,
  `subtotal` DECIMAL(10,2) NULL DEFAULT NULL,
  `totalSinIVA` DECIMAL(10,2) NULL DEFAULT NULL,
  `total` DECIMAL(10,2) NULL DEFAULT NULL,
  `proveedor` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id_factura_compras`),
  INDEX `foranea_proveedor_idx` (`proveedor` ASC) VISIBLE,
  CONSTRAINT `foranea_proveedor`
    FOREIGN KEY (`proveedor`)
    REFERENCES `mercadito`.`proveedores` (`id_proveedor`))
ENGINE = InnoDB
AUTO_INCREMENT = 19
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `mercadito`.`rubros`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`rubros` (
  `id_rubro` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL,
  PRIMARY KEY (`id_rubro`))
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `mercadito`.`productos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`productos` (
  `id_producto` INT NOT NULL AUTO_INCREMENT,
  `codigo_barra` VARCHAR(13) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL,
  `nombre` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL,
  `precio_venta` DECIMAL(10,2) NULL DEFAULT NULL,
  `cantidad_disponible` INT NULL DEFAULT NULL,
  `cantidad_minima` INT NULL DEFAULT NULL,
  `id_rubro` INT NOT NULL,
  `id_proveedor` INT NOT NULL,
  `precio_lista` DECIMAL(10,2) NULL DEFAULT NULL,
  PRIMARY KEY (`id_producto`),
  INDEX `fk_PRODUCTOS_RUBROS1_idx` (`id_rubro` ASC) VISIBLE,
  INDEX `fk_productos_proveedores1_idx` (`id_proveedor` ASC) VISIBLE,
  CONSTRAINT `fk_productos_proveedores1`
    FOREIGN KEY (`id_proveedor`)
    REFERENCES `mercadito`.`proveedores` (`id_proveedor`),
  CONSTRAINT `fk_PRODUCTOS_RUBROS1`
    FOREIGN KEY (`id_rubro`)
    REFERENCES `mercadito`.`rubros` (`id_rubro`)
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 16
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `mercadito`.`detalle_compra`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`detalle_compra` (
  `id_detalle_compra` INT NOT NULL AUTO_INCREMENT,
  `cantidad` INT NULL DEFAULT NULL,
  `precio` DECIMAL(10,2) NULL DEFAULT NULL,
  `id_factura_compras` INT NOT NULL,
  `id_producto` INT NOT NULL,
  PRIMARY KEY (`id_detalle_compra`),
  INDEX `fk_DETALLE_COMPRA_COMPRAS1_idx` (`id_factura_compras` ASC) VISIBLE,
  INDEX `fk_DETALLE_COMPRA_PRODUCTOS1_idx` (`id_producto` ASC) VISIBLE,
  CONSTRAINT `fk_DETALLE_COMPRA_COMPRAS1`
    FOREIGN KEY (`id_factura_compras`)
    REFERENCES `mercadito`.`factura_compras` (`id_factura_compras`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_DETALLE_COMPRA_PRODUCTOS1`
    FOREIGN KEY (`id_producto`)
    REFERENCES `mercadito`.`productos` (`id_producto`))
ENGINE = InnoDB
AUTO_INCREMENT = 20
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `mercadito`.`empleados`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`empleados` (
  `id_empleado` INT NOT NULL AUTO_INCREMENT,
  `nombre_usuario` VARCHAR(45) NULL DEFAULT NULL,
  `clave` VARCHAR(45) NULL DEFAULT NULL,
  `esAdmin` CHAR(1) NULL DEFAULT NULL,
  `idpersona` INT NOT NULL,
  PRIMARY KEY (`id_empleado`),
  INDEX `fk_empleados_personas1_idx` (`idpersona` ASC) VISIBLE,
  CONSTRAINT `fk_empleados_personas1`
    FOREIGN KEY (`idpersona`)
    REFERENCES `mercadito`.`personas` (`id_persona`))
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `mercadito`.`factura_ventas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`factura_ventas` (
  `id_factura_ventas` INT NOT NULL AUTO_INCREMENT,
  `numero` VARCHAR(12) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `tipo` CHAR(1) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL,
  `fechaDeEmision` DATE NULL DEFAULT NULL,
  `subtotal` DECIMAL(10,2) NULL DEFAULT NULL,
  `iva` DECIMAL(10,2) NULL DEFAULT NULL,
  `total` DECIMAL(10,2) NULL DEFAULT NULL,
  `cliente` INT NOT NULL,
  `empleado` INT NOT NULL,
  PRIMARY KEY (`id_factura_ventas`),
  INDEX `fkcliente_idx` (`cliente` ASC) VISIBLE,
  INDEX `fkempleado_idx` (`empleado` ASC) VISIBLE,
  CONSTRAINT `fkcliente`
    FOREIGN KEY (`cliente`)
    REFERENCES `mercadito`.`clientes` (`id_cliente`),
  CONSTRAINT `fkempleado`
    FOREIGN KEY (`empleado`)
    REFERENCES `mercadito`.`empleados` (`id_empleado`))
ENGINE = InnoDB
AUTO_INCREMENT = 61
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `mercadito`.`detalle_venta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`detalle_venta` (
  `id_venta_producto` INT NOT NULL AUTO_INCREMENT,
  `cantidad` INT NULL DEFAULT NULL,
  `id_producto` INT NOT NULL,
  `id_factura_ventas` INT NOT NULL,
  `monto` DECIMAL(10,2) NULL DEFAULT NULL,
  PRIMARY KEY (`id_venta_producto`),
  INDEX `fk_VENTA_PRODUCTO_PRODUCTO1_idx` (`id_producto` ASC) VISIBLE,
  INDEX `fk_VENTA_PRODUCTO_VENTAS1_idx` (`id_factura_ventas` ASC) VISIBLE,
  CONSTRAINT `fk_VENTA_PRODUCTO_PRODUCTO1`
    FOREIGN KEY (`id_producto`)
    REFERENCES `mercadito`.`productos` (`id_producto`),
  CONSTRAINT `fk_VENTA_PRODUCTO_VENTAS1`
    FOREIGN KEY (`id_factura_ventas`)
    REFERENCES `mercadito`.`factura_ventas` (`id_factura_ventas`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 44
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `mercadito`.`cajas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`cajas` (
  `idCaja` INT NOT NULL AUTO_INCREMENT,
  `fechaHoraApertura` TIMESTAMP NULL,
  `fechaHoraCierre` TIMESTAMP NULL,
  `montoInicial` DECIMAL(10,2) NULL,
  `montoFinal` DECIMAL(10,2) NULL,
  `ingresos` DECIMAL(10,2) NULL,
  `egresos` DECIMAL(10,2) NULL,
  `responsable` INT NOT NULL,
  PRIMARY KEY (`idCaja`),
  INDEX `fk_cajas_empleados1_idx` (`responsable` ASC) VISIBLE,
  CONSTRAINT `fk_cajas_empleados1`
    FOREIGN KEY (`responsable`)
    REFERENCES `mercadito`.`empleados` (`id_empleado`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
