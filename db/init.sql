create database if not exists kursach;
use kursach;
GRANT ALL PRIVILEGES ON kursach.* TO 'javafxTest'@'%' IDENTIFIED BY 'changeme';
FLUSH PRIVILEGES;

CREATE TABLE IF NOT EXISTS `kur sach`.`hotel_room` (
  `idhotel_room` INT NOT NULL,
  `num_room` INT NULL DEFAULT NULL,
  `cost_for_day` INT NOT NULL,
  `statys` VARCHAR(200) NULL DEFAULT NULL,
  `floor` INT NULL DEFAULT NULL,
  `photo` VARCHAR(45) NULL DEFAULT 'zz.jpg',
  PRIMARY KEY (`idhotel_room`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `1`.`klient`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kursach`.`klient` (
  `idklient` INT NOT NULL AUTO_INCREMENT,
  `FIO` VARCHAR(45) NULL DEFAULT NULL,
  `Passport` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`idklient`))
ENGINE = InnoDB
AUTO_INCREMENT = 31
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `1`.`bronirovanie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kursach`.`bronirovanie` (
  `hotel_room_idhotel_room` INT NOT NULL,
  `klient_idklient` INT NOT NULL,
  `Date_arrival` DATE NULL DEFAULT NULL,
  `Date_of_departure` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`hotel_room_idhotel_room`, `klient_idklient`),
  INDEX `fk_hotel_room_has_klient_klient1_idx` (`klient_idklient` ASC) VISIBLE,
  INDEX `fk_hotel_room_has_klient_hotel_room1_idx` (`hotel_room_idhotel_room` ASC) VISIBLE,
  CONSTRAINT `fk_hotel_room_has_klient_hotel_room1`
    FOREIGN KEY (`hotel_room_idhotel_room`)
    REFERENCES `kursach`.`hotel_room` (`idhotel_room`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_hotel_room_has_klient_klient1`
    FOREIGN KEY (`klient_idklient`)
    REFERENCES `kursach`.`klient` (`idklient`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `1`.`payment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kursach`.`payment` (
  `hotel_room_idhotel_room` INT NOT NULL,
  `klient_idklient` INT NOT NULL,
  `cost_full` INT NULL DEFAULT NULL,
  `receipt_number` INT NULL DEFAULT NULL,
  PRIMARY KEY (`hotel_room_idhotel_room`, `klient_idklient`),
  INDEX `fk_hotel_room_has_klient_klient2_idx` (`klient_idklient` ASC) VISIBLE,
  INDEX `fk_hotel_room_has_klient_hotel_room2_idx` (`hotel_room_idhotel_room` ASC) VISIBLE,
  CONSTRAINT `fk_hotel_room_has_klient_hotel_room2`
    FOREIGN KEY (`hotel_room_idhotel_room`)
    REFERENCES `kursach`.`hotel_room` (`idhotel_room`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_hotel_room_has_klient_klient2`
    FOREIGN KEY (`klient_idklient`)
    REFERENCES `kursach`.`klient` (`idklient`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `1`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kursach`.`role` (
  `idrole` INT NOT NULL,
  `name_role` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`idrole`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `1`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kursach`.`user` (
  `iduser` INT NOT NULL AUTO_INCREMENT,
  `name_user` VARCHAR(45) NULL DEFAULT NULL,
  `login` VARCHAR(45) NULL DEFAULT NULL,
  `password` VARCHAR(45) NULL DEFAULT NULL,
  `role_idrole` INT NOT NULL DEFAULT '2',
  PRIMARY KEY (`iduser`),
  INDEX `fk_user_role_idx` (`role_idrole` ASC) VISIBLE,
  CONSTRAINT `fk_user_role`
    FOREIGN KEY (`role_idrole`)
    REFERENCES `kursach`.`role` (`idrole`))
ENGINE = InnoDB
AUTO_INCREMENT = 20
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;

DELIMITER $$
USE `kursach`$$
CREATE DEFINER=`javafxTest`@`localhost` PROCEDURE `fullcost`(IN b INT, IN c INT)
BEGIN
declare result int;
    SET result = b * c;
    select result;
END$$

DELIMITER ;
USE `kursach`;

DELIMITER $$
USE `kursach`$$
CREATE
DEFINER=`javafxTest`@`localhost`
TRIGGER `kursach`.`hotel_room_BEFORE_UPDATE`
BEFORE UPDATE ON `kursach`.`hotel_room`
FOR EACH ROW
BEGIN
 IF NEW.statys != 'Свободен' AND NEW.statys != 'Занят' THEN
       SET NEW.statys = OLD.statys;
     END IF;
END$$

USE `kursach`$$
CREATE
DEFINER=`javafxTest`@`localhost`
TRIGGER `kursach`.`bronirovanie_BEFORE_DELETE`
BEFORE DELETE ON `kursach`.`bronirovanie`
FOR EACH ROW
BEGIN
UPDATE hotel_room
    SET statys = 'Свободен'
    WHERE idhotel_room = OLD.hotel_room_idhotel_room;
END$$

USE `kursach`$$
CREATE
DEFINER=`javafxTest`@`localhost`
TRIGGER `kursach`.`bronirovanie_BEFORE_INSERT`
BEFORE INSERT ON `kursach`.`bronirovanie`
FOR EACH ROW
BEGIN
 IF NEW.Date_arrival>new.Date_of_departure  THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Дата приезда не может быть больше даты уезда"';
    END IF;
END$$

USE `kursach`$$

CREATE
DEFINER=`javafxTest`@`localhost`
TRIGGER `kursach`.`payment_AFTER_INSERT`
AFTER INSERT ON `kursach`.`payment`
FOR EACH ROW
BEGIN
UPDATE hotel_room
SET statys = 'Занят'
WHERE idhotel_room = NEW.hotel_room_idhotel_room;
END$$

USE `kursach` ;
INSERT INTO `hotel_room` VALUES (1,132,800,'Занят',1,'rooms/room1.jpg'),(2,133,1000,'Свободен',1,'rooms/room2.jpg'),(3,134,1500,'Свободен',1,'rooms/room3.jpg'),(4,231,900,'Свободен',2,'rooms/room4.jpeg'),(5,234,4000,'Свободен',2,'rooms/room5.jpg'),(6,236,3000,'Свободен',2,'rooms/room6.jpg'),(7,346,1200,'Свободен',3,'rooms/room7.jpeg'),(8,324,700,'Свободен',3,'rooms/room8.jpg'),(9,321,800,'Свободен',3,'rooms/room9.jpg');
INSERT INTO `klient` VALUES (1,'Пряженцов михаил','2019 789534'),(2,'Поляшов Никита Михайлович','2211 089654'),(3,'Круглов Кирилл Александрович','1234 890765'),(7,'Лашков Максим','1234567898765'),(30,'Гутянская Е М','123456543');
INSERT INTO `payment` VALUES (1,3,1600,5);
INSERT INTO `bronirovanie` VALUES (1,3,'2023-11-22','2023-11-24');
INSERT INTO `role` VALUES (1,'Administrator'),(2,'Person');
INSERT INTO `user` VALUES (1,'Орлов Дмитрий Андреевич','adm','123',1),(2,'Сокуров Александ Владимирович','per','12',2),(7,'grew','qwerty','21',2),(8,'Круглов Кирилл Александрович','st1','567',2),(9,'Кургузов','qwa','143',2),(10,'1','2','3',2),(11,'Травкина А. В.','trav','11052006',2),(12,'bhgf','bgvf','g',2),(18,'енг','per1','12',2),(19,'42','per12','tr',2);

-- -----------------------------------------------------
-- procedure fullcost
-- -----------------------------------------------------






DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


