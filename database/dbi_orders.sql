-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: dbi
-- ------------------------------------------------------
-- Server version	8.0.30

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
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `ordno` int NOT NULL,
  `month` char(3) DEFAULT NULL,
  `cid` char(4) DEFAULT NULL,
  `aid` char(3) DEFAULT NULL,
  `pid` char(3) DEFAULT NULL,
  `qty` int DEFAULT NULL,
  `dollars` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`ordno`),
  KEY `cid` (`cid`),
  KEY `aid` (`aid`),
  KEY `pid` (`pid`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `customers` (`cid`),
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`aid`) REFERENCES `agents` (`aid`),
  CONSTRAINT `orders_ibfk_3` FOREIGN KEY (`pid`) REFERENCES `products` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1011,'jan','c001','a01','p01',1000,450.00),(1012,'jan','c001','a01','p01',1000,450.00),(1013,'jan','c002','a03','p03',1000,880.00),(1014,'jan','c003','a03','p05',1200,1104.00),(1015,'jan','c003','a03','p05',1200,1104.00),(1016,'jan','c006','a01','p01',1000,500.00),(1017,'feb','c001','a06','p03',600,540.00),(1018,'feb','c001','a03','p04',600,540.00),(1019,'feb','c001','a02','p02',400,180.00),(1020,'feb','c006','a03','p07',600,600.00),(1021,'feb','c004','a06','p01',1000,460.00),(1022,'mar','c001','a05','p06',400,720.00),(1023,'mar','c001','a04','p05',500,450.00),(1024,'mar','c006','a06','p01',800,400.00),(1025,'apr','c001','a05','p07',800,720.00),(1026,'may','c002','a05','p03',800,704.00);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-10-11 14:17:32
