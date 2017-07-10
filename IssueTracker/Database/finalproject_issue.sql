-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: finalproject
-- ------------------------------------------------------
-- Server version	5.7.10-log

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
-- Table structure for table `issue`
--

DROP TABLE IF EXISTS `issue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issue` (
  `issueId` bigint(20) NOT NULL,
  `creationDate` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `severity` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `issueCurrentlyWithId` bigint(20) DEFAULT NULL,
  `issueRaisedBy` bigint(20) DEFAULT NULL,
  `primaryAssigneeId` bigint(20) DEFAULT NULL,
  `teamId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`issueId`),
  KEY `FKun5tgrgk8ohtataewx4vqja3` (`issueCurrentlyWithId`),
  KEY `FKoh1iivpcd6wyqrxtmuv2nmp7r` (`issueRaisedBy`),
  KEY `FKkpervyh4so1x3w1tkeyq5gfcj` (`primaryAssigneeId`),
  KEY `FKfkkquji9fqxdv9eadwmumad80` (`teamId`),
  CONSTRAINT `FKfkkquji9fqxdv9eadwmumad80` FOREIGN KEY (`teamId`) REFERENCES `team` (`teamId`),
  CONSTRAINT `FKkpervyh4so1x3w1tkeyq5gfcj` FOREIGN KEY (`primaryAssigneeId`) REFERENCES `analyst` (`userId`),
  CONSTRAINT `FKoh1iivpcd6wyqrxtmuv2nmp7r` FOREIGN KEY (`issueRaisedBy`) REFERENCES `customer` (`userId`),
  CONSTRAINT `FKun5tgrgk8ohtataewx4vqja3` FOREIGN KEY (`issueCurrentlyWithId`) REFERENCES `usertable` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issue`
--

LOCK TABLES `issue` WRITE;
/*!40000 ALTER TABLE `issue` DISABLE KEYS */;
INSERT INTO `issue` VALUES (22,'2016-04-26 00:05:18','the java code doesnt seem to work',1,'Closed By Requestor','code not working',NULL,8,16,19),(34,'2016-04-26 00:41:33','my cat didnt come back home. please find her',1,'Open With Assignee','santander issue',18,8,18,21),(35,'2016-04-26 00:43:09','please put my dog down he barks a lot',2,'Open With Assignee','my dog barks too much',14,9,14,20),(51,'2016-04-26 03:06:06','our bank has shut down',1,'Open With Assignee','bank down',17,9,17,20);
/*!40000 ALTER TABLE `issue` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-04-26  5:46:12
