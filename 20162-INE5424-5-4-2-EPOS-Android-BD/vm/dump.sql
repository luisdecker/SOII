-- MySQL dump 10.13  Distrib 5.7.16, for Linux (x86_64)
--
-- Host: localhost    Database: sodb_novo
-- ------------------------------------------------------
-- Server version	5.7.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `sodb_novo`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `sodb_novo` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `sodb_novo`;

--
-- Table structure for table `parameter`
--

DROP TABLE IF EXISTS `parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parameter` (
  `idparameter` int(11) NOT NULL AUTO_INCREMENT,
  `idservice` int(11) NOT NULL,
  `idsmartobject` int(11) NOT NULL,
  `register_modbus` varchar(3) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  `minvalue` int(11) DEFAULT NULL,
  `maxvalue` int(11) DEFAULT NULL,
  `options` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`idparameter`),
  UNIQUE KEY `reg_unico` (`register_modbus`,`idsmartobject`),
  KEY `fk_parameter_service1_idx` (`idservice`,`idsmartobject`),
  CONSTRAINT `fk_parameter_service1` FOREIGN KEY (`idservice`, `idsmartobject`) REFERENCES `service` (`idservice`, `idsmartobject`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parameter`
--

LOCK TABLES `parameter` WRITE;
/*!40000 ALTER TABLE `parameter` DISABLE KEYS */;
INSERT INTO `parameter` VALUES (12,13,1,'2','Ligado','boolean',NULL,NULL,NULL),(13,14,4,'2','Ligado','boolean',NULL,NULL,NULL),(14,15,3,'1','Aceso','boolean',NULL,NULL,NULL),(15,16,1,'3','Temperatura','double',18,28,NULL),(16,16,1,'6','Humidade','double',0,100,NULL),(17,16,1,'7','Velocidade','combo',NULL,NULL,'Fraco|Médio|Forte'),(18,16,1,'8','Timer','double',0,7,NULL),(19,17,4,'3','Temperatura','double',18,28,NULL),(20,17,4,'6','Humidade','double',0,100,NULL),(21,17,4,'7','Velocidade','combo',NULL,NULL,'Fraco|Médio|Forte'),(22,17,4,'8','Timer','double',0,7,NULL),(23,18,2,'5','Comida','combo',NULL,NULL,'Frango|Arroz|Picanha|Macarrão'),(24,19,1,'9','Função','combo',NULL,NULL,'Circular|Refrigerar|Aquecer'),(25,20,4,'9','Função','combo',NULL,NULL,'Circular|Refrigerar|Aquecer'),(26,21,5,'B','CO2','get',NULL,NULL,NULL),(27,22,1,'A','Temperatura','get',NULL,NULL,NULL),(28,23,4,'A','Temperatura','get',NULL,NULL,NULL);
/*!40000 ALTER TABLE `parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service` (
  `idservice` int(11) NOT NULL AUTO_INCREMENT,
  `idsmartobject` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`idservice`,`idsmartobject`),
  UNIQUE KEY `nome_unico` (`name`,`idsmartobject`),
  KEY `fk_service_smartobject1_idx` (`idsmartobject`),
  CONSTRAINT `fk_service_smartobject1` FOREIGN KEY (`idsmartobject`) REFERENCES `smartobject` (`idsmartobject`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service`
--

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
INSERT INTO `service` VALUES (15,3,'Acender'),(16,1,'Configurar'),(17,4,'Configurar'),(18,2,'Cozinhar'),(13,1,'Ligar'),(14,4,'Ligar'),(22,1,'Pegar Temperatura'),(23,4,'Pegar Temperatura'),(21,5,'Quantidade de CO2'),(19,1,'Tipo de Função'),(20,4,'Tipo de Função');
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smartobject`
--

DROP TABLE IF EXISTS `smartobject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `smartobject` (
  `idsmartobject` int(11) NOT NULL AUTO_INCREMENT,
  `serverurl` varchar(100) NOT NULL,
  `idsomodbus` varchar(3) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idsmartobject`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smartobject`
--

LOCK TABLES `smartobject` WRITE;
/*!40000 ALTER TABLE `smartobject` DISABLE KEYS */;
INSERT INTO `smartobject` VALUES (1,'http://192.168.43.138:8080/SOServer/','A1','Ar Condicionado'),(2,'http://192.168.43.138:8080/SOServer/','0','Forno'),(3,'http://192.168.43.138:8080/SOServer/','A2','Lâmpada'),(4,'http://192.168.43.138:8080/SOServer/','A1','Ar Condicionado'),(5,'http://192.168.43.138:8080/SOServer/','A3','CO2');
/*!40000 ALTER TABLE `smartobject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `souserjoin`
--

DROP TABLE IF EXISTS `souserjoin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `souserjoin` (
  `idsmartobject` int(11) DEFAULT NULL,
  `iduser` int(11) DEFAULT NULL,
  KEY `fkidsmartobject` (`idsmartobject`),
  KEY `fkiduser` (`iduser`),
  CONSTRAINT `fkidsmartobject` FOREIGN KEY (`idsmartobject`) REFERENCES `smartobject` (`idsmartobject`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fkiduser` FOREIGN KEY (`iduser`) REFERENCES `user` (`iduser`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `souserjoin`
--

LOCK TABLES `souserjoin` WRITE;
/*!40000 ALTER TABLE `souserjoin` DISABLE KEYS */;
INSERT INTO `souserjoin` VALUES (1,1),(3,1),(5,1);
/*!40000 ALTER TABLE `souserjoin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `iduser` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  PRIMARY KEY (`iduser`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'ercilio','$2a$12$yWYgChXIgaOR/U31mBHznujAYiYMBM10xxJ/1xUmV2c26kxtQgd82'),(2,'joão','$2a$12$Nx4zc2.nWywL8p.ej/d3huNPtX0GwZe0Uoll2mi.61Wmb.NT4pmpy'),(3,'maria','$2a$12$edvw/zkCfssTBuyUPv.gbuFbqJgB6Wolpj.Kgy0R36vLA7NoxRHyi');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-11-08  2:37:47
