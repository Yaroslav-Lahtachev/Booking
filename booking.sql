-- MySQL Script generated by MySQL Workbench
-- Mon Jan 21 17:24:53 2019
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema bookingdb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema bookingdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bookingdb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `bookingdb` ;

-- -----------------------------------------------------
-- Table `bookingdb`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookingdb`.`users` (
  `id_user` INT(10) NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `status` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 671
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bookingdb`.`seller`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookingdb`.`seller` (
  `id_user` INT(10) NULL DEFAULT NULL,
  `id_offer` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id_offer`),
  UNIQUE INDEX `id_offer_UNIQUE` (`id_offer` ASC) VISIBLE,
  INDEX `id_user_fk_idx` (`id_user` ASC) VISIBLE,
  CONSTRAINT `id_user_fk`
    FOREIGN KEY (`id_user`)
    REFERENCES `bookingdb`.`users` (`id_user`))
ENGINE = InnoDB
AUTO_INCREMENT = 61
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bookingdb`.`offers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookingdb`.`offers` (
  `id_offer` INT(10) UNSIGNED NOT NULL,
  `city` VARCHAR(45) NULL DEFAULT NULL,
  `address` VARCHAR(45) NULL DEFAULT NULL,
  `max_people_count` INT(11) NULL DEFAULT NULL,
  `price` DOUBLE NULL DEFAULT NULL,
  `availability_parking` TINYINT(4) NULL DEFAULT NULL,
  `availability_wifi` TINYINT(4) NULL DEFAULT NULL,
  `availability_animal` TINYINT(4) NULL DEFAULT NULL,
  `availability_smoking` TINYINT(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id_offer`),
  CONSTRAINT `id_offer_fk`
    FOREIGN KEY (`id_offer`)
    REFERENCES `bookingdb`.`seller` (`id_offer`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bookingdb`.`order_history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookingdb`.`order_history` (
  `id_order` INT(10) NOT NULL AUTO_INCREMENT,
  `id_user` INT(10) NULL DEFAULT NULL,
  `id_offer` INT(10) NOT NULL,
  `date_start` DATE NULL DEFAULT NULL,
  `date_finish` DATE NULL DEFAULT NULL,
  `status` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id_order`),
  UNIQUE INDEX `id_order_UNIQUE` (`id_order` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 80
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
