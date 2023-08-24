CREATE DATABASE IF NOT EXISTS `mercadito`;
USE `mercadito` ;

-- -----------------------------------------------------
-- Table `mercadito`.`personas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`personas` (
  `id_persona` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL,
  `apellido` VARCHAR(45) NULL,
  `provincia` VARCHAR(50) NULL,
  `localidad` VARCHAR(50) NULL,
  `calle` VARCHAR(50) NULL,
  `dni` VARCHAR(8) NULL,
  `mail` VARCHAR(255) NULL,
  `telefono` VARCHAR(11) NULL,
  PRIMARY KEY (`id_persona`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mercadito`.`empleados`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`empleados` (
  `id_empleado` INT NOT NULL AUTO_INCREMENT,
  `nombre_usuario` VARCHAR(45) NULL,
  `clave` VARCHAR(45) NULL,
  `esAdmin` CHAR(1) NULL,
  `idpersona` INT NOT NULL,
  PRIMARY KEY (`id_empleado`),
  INDEX `fk_empleados_personas1_idx` (`idpersona` ASC),
  CONSTRAINT `fk_empleados_personas1`
    FOREIGN KEY (`idpersona`)
    REFERENCES `mercadito`.`personas` (`id_persona`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mercadito`.`factura_compras`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`factura_compras` (
  `id_factura_compras` INT NOT NULL AUTO_INCREMENT,
  `fecha_hora` DATETIME NULL,
  `numero_de_factura` VARCHAR(8) NULL,
  `CAE` VARCHAR(45) NULL,
  `total` DECIMAL(10,2) NULL,
  `subtotal` DECIMAL(10,2) NULL,
  `id_empleado` INT NOT NULL,
  PRIMARY KEY (`id_factura_compras`),
  INDEX `fk_factura_compras_empleados1_idx` (`id_empleado` ASC),
  CONSTRAINT `fk_factura_compras_empleados1`
    FOREIGN KEY (`id_empleado`)
    REFERENCES `mercadito`.`empleados` (`id_empleado`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `mercadito`.`rubros`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`rubros` (
  `id_rubro` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NULL DEFAULT NULL,
  PRIMARY KEY (`id_rubro`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `mercadito`.`proveedores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`proveedores` (
  `id_proveedor` INT NOT NULL AUTO_INCREMENT,
  `CUIT` VARCHAR(11) NULL,
  `id_persona` INT NOT NULL,
  PRIMARY KEY (`id_proveedor`),
  INDEX `fk_proveedores_personas1_idx` (`id_persona` ASC) ,
  CONSTRAINT `fk_proveedores_personas1`
    FOREIGN KEY (`id_persona`)
    REFERENCES `mercadito`.`personas` (`id_persona`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mercadito`.`productos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`productos` (
  `id_producto` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL,
  `precio_venta` DECIMAL(10,2) NULL,
  `precio_lista` DECIMAL(10,2) NULL,
  `cantidad_disponible` INT NULL,
  `cantidad_minima` INT NULL,
  `id_rubro` INT NOT NULL,
  `id_proveedor` INT NOT NULL,
  PRIMARY KEY (`id_producto`),
  INDEX `fk_PRODUCTOS_RUBROS1_idx` (`id_rubro` ASC) ,
  INDEX `fk_productos_proveedores1_idx` (`id_proveedor` ASC) ,
  CONSTRAINT `fk_PRODUCTOS_RUBROS1`
    FOREIGN KEY (`id_rubro`)
    REFERENCES `mercadito`.`rubros` (`id_rubro`)
    ON UPDATE CASCADE,
  CONSTRAINT `fk_productos_proveedores1`
    FOREIGN KEY (`id_proveedor`)
    REFERENCES `mercadito`.`proveedores` (`id_proveedor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `mercadito`.`detalle_compra`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`detalle_compra` (
  `id_detalle_compra` INT NOT NULL AUTO_INCREMENT,
  `cantidad` INT NULL DEFAULT NULL,
  `descuento` INT NULL,
  `id_factura_compras` INT NOT NULL,
  `id_producto` INT NOT NULL,
  PRIMARY KEY (`id_detalle_compra`),
  INDEX `fk_DETALLE_COMPRA_COMPRAS1_idx` (`id_factura_compras` ASC) ,
  INDEX `fk_DETALLE_COMPRA_PRODUCTOS1_idx` (`id_producto` ASC) ,
  CONSTRAINT `fk_DETALLE_COMPRA_COMPRAS1`
    FOREIGN KEY (`id_factura_compras`)
    REFERENCES `mercadito`.`factura_compras` (`id_factura_compras`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_DETALLE_COMPRA_PRODUCTOS1`
    FOREIGN KEY (`id_producto`)
    REFERENCES `mercadito`.`productos` (`id_producto`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `mercadito`.`factura_ventas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`factura_ventas` (
  `id_factura_ventas` INT NOT NULL AUTO_INCREMENT,
  `numero_de_boleta` VARCHAR(8) NULL,
  `CAE` VARCHAR(45) NULL,
  `fecha_hora` DATETIME NULL,
  `subtotal` DECIMAL(10,2) NULL,
  `total` DECIMAL(10,2) NULL,
  `idpersona` INT NOT NULL,
  PRIMARY KEY (`id_factura_ventas`),
  INDEX `fk_factura_ventas_personas1_idx` (`idpersona` ASC) ,
  CONSTRAINT `fk_factura_ventas_personas1`
    FOREIGN KEY (`idpersona`)
    REFERENCES `mercadito`.`personas` (`id_persona`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `mercadito`.`detalle_venta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`detalle_venta` (
  `id_venta_producto` INT NOT NULL AUTO_INCREMENT,
  `id_producto` INT NOT NULL,
  `id_factura_ventas` INT NOT NULL,
  `cantidad_vendida` DECIMAL(2,2) NULL DEFAULT NULL,
  `descuento` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id_venta_producto`),
  INDEX `fk_VENTA_PRODUCTO_PRODUCTO1_idx` (`id_producto` ASC) ,
  INDEX `fk_VENTA_PRODUCTO_VENTAS1_idx` (`id_factura_ventas` ASC) ,
  CONSTRAINT `fk_VENTA_PRODUCTO_PRODUCTO1`
    FOREIGN KEY (`id_producto`)
    REFERENCES `mercadito`.`productos` (`id_producto`),
  CONSTRAINT `fk_VENTA_PRODUCTO_VENTAS1`
    FOREIGN KEY (`id_factura_ventas`)
    REFERENCES `mercadito`.`factura_ventas` (`id_factura_ventas`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `mercadito`.`clientes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`clientes` (
  `id_cliente` INT NOT NULL AUTO_INCREMENT,
  `idpersona` INT NOT NULL,
  PRIMARY KEY (`id_cliente`),
  INDEX `fk_clientes_personas1_idx` (`idpersona` ASC) ,
  CONSTRAINT `fk_clientes_personas1`
    FOREIGN KEY (`idpersona`)
    REFERENCES `mercadito`.`personas` (`id_persona`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mercadito`.`devoluciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercadito`.`devoluciones` (
  `id_devoluciones` INT NOT NULL AUTO_INCREMENT,
  `fecha_hora` DATETIME NULL,
  `descripcion` TEXT NULL,
  `total` DECIMAL(10,2) NULL,
  `id_producto` INT NOT NULL,
  PRIMARY KEY (`id_devoluciones`),
  INDEX `fk_devoluciones_productos1_idx` (`id_producto` ASC) ,
  CONSTRAINT `fk_devoluciones_productos1`
    FOREIGN KEY (`id_producto`)
    REFERENCES `mercadito`.`productos` (`id_producto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;