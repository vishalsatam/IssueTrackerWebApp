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
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messages` (
  `messageId` bigint(20) NOT NULL,
  `actionPerformed` varchar(255) DEFAULT NULL,
  `attachment` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `senderId` bigint(20) DEFAULT NULL,
  `senderName` varchar(255) DEFAULT NULL,
  `sentDate` datetime DEFAULT NULL,
  `issueId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`messageId`),
  KEY `FK69sucw1vq7h7ga6vsdh9mi123` (`issueId`),
  CONSTRAINT `FK69sucw1vq7h7ga6vsdh9mi123` FOREIGN KEY (`issueId`) REFERENCES `issue` (`issueId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (23,'Comment Posted','8-20160426-120937-WaterManagementModel.jpg','please check attachment',8,'Jossh joh','2016-04-26 00:09:37',22),(24,'Sent for Clarification',NULL,'need more data',13,'Vinoda Kambli','2016-04-26 00:14:02',22),(25,'Rejected Clarification Request',NULL,'rejecting be more specific',10,'Chris Gayle','2016-04-26 00:17:53',22),(26,'Closed By Analyst',NULL,'lets close this',13,'Vinoda Kambli','2016-04-26 00:19:30',22),(27,'Approved for Closure',NULL,'ok but open another one please',10,'Chris Gayle','2016-04-26 00:20:14',22),(28,'Re-Opened',NULL,'what',8,'Jossh joh','2016-04-26 00:31:25',22),(29,'Closed By Analyst',NULL,'dude im opening a new one',13,'Vinoda Kambli','2016-04-26 00:32:09',22),(30,'Transferring Issue',NULL,'actually lets give this to harry potter',10,'Chris Gayle','2016-04-26 00:33:13',22),(31,'Closed By Analyst',NULL,'closing',16,'Harry Potter','2016-04-26 00:36:03',22),(32,'Approved for Closure',NULL,'ok',10,'Chris Gayle','2016-04-26 00:36:53',22),(33,'Closed By Requestor',NULL,'confirm',8,'Jossh joh','2016-04-26 00:37:13',22),(36,'Sent for Clarification',NULL,'should we kill him?',14,'Yuvraj Singh','2016-04-26 02:08:10',35),(37,'Rejected Clarification Request',NULL,'thats what hes asking for suggest a solution',10,'Chris Gayle','2016-04-26 02:09:07',35),(38,'Sent for Clarification','14-20160426-020947-zipcar.jpg','How about this?',14,'Yuvraj Singh','2016-04-26 02:09:47',35),(39,'Approved for Clarification',NULL,'ok',10,'Chris Gayle','2016-04-26 02:10:22',35),(40,'Provided Clarification',NULL,'this is good please close this issue',9,'John Jackson','2016-04-26 02:11:13',35),(41,'Transferring Issue',NULL,'transferring',14,'Yuvraj Singh','2016-04-26 02:12:06',35),(42,'Closed By Analyst',NULL,'closing on behlalf of yuv',17,'Binny Stuart','2016-04-26 02:12:35',35),(43,'Approved for Closure',NULL,'approving closure',10,'Chris Gayle','2016-04-26 02:13:11',35),(44,'Closed By Requestor',NULL,'closing',9,'John Jackson','2016-04-26 02:18:46',35),(45,'Re-Opened',NULL,'sorry gotta reopen',9,'John Jackson','2016-04-26 02:19:02',35),(46,'Transferring Issue',NULL,'s',15,'Josh Butler','2016-04-26 02:22:00',34),(47,'Comment Posted',NULL,'hhh',15,'Josh Butler','2016-04-26 02:23:12',35),(48,'Transferring Issue',NULL,'back to yuvi',17,'Binny Stuart','2016-04-26 02:27:50',35),(49,'Sent for Clarification',NULL,'sa',15,'Josh Butler','2016-04-26 02:33:51',34),(50,'Transferring Issue','11-20160426-023802-VishalSatam.pdf','new issue for you ganguly',11,'Sachin Tendulkar','2016-04-26 02:38:02',34);
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-04-26  5:46:09
