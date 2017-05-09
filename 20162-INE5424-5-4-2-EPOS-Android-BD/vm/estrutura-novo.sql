-- MySQL Script generated by MySQL Workbench
-- Seg 07 Nov 2016 21:43:41 BRST
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema sodb_novo
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema sodb_novo
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sodb_novo` DEFAULT CHARACTER SET latin1 ;
USE `sodb_novo` ;

-- -----------------------------------------------------
-- Table `sodb_novo`.`smartobject`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sodb_novo`.`smartobject` (
  `idsmartobject` INT(11) NOT NULL AUTO_INCREMENT,
  `serverurl` VARCHAR(100) NOT NULL,
  `idsomodbus` VARCHAR(3) NOT NULL,
  `name` VARCHAR(100) NULL,
  PRIMARY KEY (`idsmartobject`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `sodb_novo`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sodb_novo`.`user` (
  `iduser` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`iduser`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `sodb_novo`.`service`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sodb_novo`.`service` (
  `idservice` INT(11) NOT NULL AUTO_INCREMENT,
  `idsmartobject` INT(11) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`idservice`, `idsmartobject`),
  INDEX `fk_service_smartobject1_idx` (`idsmartobject` ASC),
  UNIQUE INDEX `nome_unico` (`name` ASC, `idsmartobject` ASC),
  CONSTRAINT `fk_service_smartobject1`
    FOREIGN KEY (`idsmartobject`)
    REFERENCES `sodb_novo`.`smartobject` (`idsmartobject`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 13
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `sodb_novo`.`souserjoin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sodb_novo`.`souserjoin` (
  `idsmartobject` INT(11) NULL DEFAULT NULL,
  `iduser` INT(11) NULL DEFAULT NULL,
  INDEX `fkidsmartobject` (`idsmartobject` ASC),
  INDEX `fkiduser` (`iduser` ASC),
  CONSTRAINT `fkidsmartobject`
    FOREIGN KEY (`idsmartobject`)
    REFERENCES `sodb_novo`.`smartobject` (`idsmartobject`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fkiduser`
    FOREIGN KEY (`iduser`)
    REFERENCES `sodb_novo`.`user` (`iduser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `sodb_novo`.`parameter`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sodb_novo`.`parameter` (
  `idparameter` INT(11) NOT NULL AUTO_INCREMENT,
  `idservice` INT(11) NOT NULL,
  `idsmartobject` INT(11) NOT NULL,
  `register_modbus` VARCHAR(3) NULL,
  `name` VARCHAR(100) NULL DEFAULT NULL,
  `type` VARCHAR(45) NULL DEFAULT NULL,
  `minvalue` INT(11) NULL DEFAULT NULL,
  `maxvalue` INT(11) NULL DEFAULT NULL,
  `options` VARCHAR(250) NULL DEFAULT NULL,
  PRIMARY KEY (`idparameter`),
  UNIQUE INDEX `reg_unico` (`register_modbus` ASC, `idsmartobject` ASC),
  INDEX `fk_parameter_service1_idx` (`idservice` ASC, `idsmartobject` ASC),
  CONSTRAINT `fk_parameter_service1`
    FOREIGN KEY (`idservice` , `idsmartobject`)
    REFERENCES `sodb_novo`.`service` (`idservice` , `idsmartobject`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 12
DEFAULT CHARACTER SET = latin1;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
