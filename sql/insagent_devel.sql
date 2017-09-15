-- MySQL dump 10.13  Distrib 5.6.24, for Win32 (x86)
--
-- Host: 5.200.55.79    Database: insagent_devel
-- ------------------------------------------------------
-- Server version	5.1.73

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
-- Table structure for table `b_menu`
--

DROP TABLE IF EXISTS `b_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `b_menu` (
  `id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `parentId` tinyint(3) unsigned DEFAULT NULL,
  `level` tinyint(3) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `action` varchar(255) DEFAULT NULL,
  `order` tinyint(3) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `b_menu_parentId` (`parentId`),
  CONSTRAINT `b_menu_parentId` FOREIGN KEY (`parentId`) REFERENCES `b_menu` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `b_menu`
--

LOCK TABLES `b_menu` WRITE;
/*!40000 ALTER TABLE `b_menu` DISABLE KEYS */;
INSERT INTO `b_menu` VALUES (1,NULL,1,'Журналы',NULL,10),(2,NULL,1,'Отчёты',NULL,20),(3,NULL,1,'Настройка',NULL,30),(4,1,2,'Журнал БСО','bsoJournal.action',12),(5,1,2,'Журнал актов','actJournal.action',14),(6,2,2,'Отчёт по БСО','#',22),(7,2,2,'Отчёт по актам','#',24),(8,3,2,'Города','cityManagement.action',32),(9,3,2,'Подразделения','unitManagement.action',34),(10,3,2,'Пользователи','userManagement.action',36),(11,1,2,'Журнал БСО (архив)','bsoArchivedJournal.action',13);
/*!40000 ALTER TABLE `b_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `b_menu_roles`
--

DROP TABLE IF EXISTS `b_menu_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `b_menu_roles` (
  `menuId` tinyint(3) unsigned NOT NULL,
  `roleId` tinyint(3) unsigned NOT NULL,
  PRIMARY KEY (`menuId`,`roleId`),
  KEY `b_menu_roles_menuId` (`menuId`),
  KEY `b_menu_roles_roleId` (`roleId`),
  CONSTRAINT `b_menu_roles_menuId` FOREIGN KEY (`menuId`) REFERENCES `b_menu` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `b_menu_roles_roleId` FOREIGN KEY (`roleId`) REFERENCES `m_roles` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `b_menu_roles`
--

LOCK TABLES `b_menu_roles` WRITE;
/*!40000 ALTER TABLE `b_menu_roles` DISABLE KEYS */;
INSERT INTO `b_menu_roles` VALUES (1,1),(1,2),(1,3),(1,4),(2,1),(3,1),(4,1),(4,2),(4,3),(4,4),(5,1),(5,2),(5,3),(5,4),(6,1),(7,1),(8,1),(9,1),(10,1),(11,1);
/*!40000 ALTER TABLE `b_menu_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_act_pack_bsos`
--

DROP TABLE IF EXISTS `d_act_pack_bsos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_act_pack_bsos` (
  `packId` int(10) unsigned NOT NULL,
  `bsoId` int(10) unsigned NOT NULL,
  PRIMARY KEY (`packId`,`bsoId`),
  KEY `d_act_pack_bsos_packId` (`packId`),
  KEY `d_act_pack_bsos_bsoId` (`bsoId`),
  CONSTRAINT `d_act_pack_bsos_bsoId` FOREIGN KEY (`bsoId`) REFERENCES `d_bsos` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `d_act_pack_bsos_packId` FOREIGN KEY (`packId`) REFERENCES `d_act_packs` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_act_pack_bsos`
--

LOCK TABLES `d_act_pack_bsos` WRITE;
/*!40000 ALTER TABLE `d_act_pack_bsos` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_act_pack_bsos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_act_packs`
--

DROP TABLE IF EXISTS `d_act_packs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_act_packs` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `actId` int(10) unsigned NOT NULL,
  `series` varchar(8) NOT NULL,
  `numberFrom` bigint(20) unsigned NOT NULL,
  `numberTo` bigint(20) unsigned NOT NULL,
  `amount` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `d_act_packs_actId` (`actId`),
  CONSTRAINT `d_act_packs_actId` FOREIGN KEY (`actId`) REFERENCES `d_acts` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_act_packs`
--

LOCK TABLES `d_act_packs` WRITE;
/*!40000 ALTER TABLE `d_act_packs` DISABLE KEYS */;
INSERT INTO `d_act_packs` VALUES (1,1,'AA',1,1,1),(2,2,'AA',1,1,1),(3,3,'AA',3,4,2),(4,4,'AA',6,8,3),(5,5,'BB',1,2,2),(6,5,'BB',5,8,4),(7,6,'AA',6,8,3),(8,7,'AA',6,8,3),(9,8,'AA',1,1,1),(10,9,'СС',1,10,10),(11,10,'СС',1,8,8),(12,11,'AA',1,1,1),(13,12,'AA',1,1,1),(14,13,'AA',1,1,1),(15,14,'AA',1,1,1),(16,15,'AA',1,1,1),(17,16,'AA',1,1,1),(18,17,'AA',1,1,1),(19,18,'AA',1,1,1),(20,19,'AA',3,4,2),(21,19,'AA',6,6,1),(22,19,'BB',1,2,2),(23,19,'BB',5,5,1),(24,20,'AA',7,7,1),(25,21,'AA',3,3,1),(26,22,'AA',3,3,1),(27,23,'AA',8,8,1),(28,24,'AA',6,7,2),(29,25,'AA',6,8,3),(30,26,'AA',6,8,3),(31,27,'AA',6,7,2),(32,28,'AA',8,8,1),(33,29,'AA',8,8,1),(34,30,'AA',6,8,3),(35,31,'AA',1,1,1),(36,32,'AA',3,3,1),(37,33,'СС',10,10,1),(38,33,'BB',7,7,1),(39,34,'СС',10,10,1),(40,34,'BB',7,7,1),(41,35,'СС',10,10,1),(42,35,'BB',7,7,1),(43,36,'BB',6,6,1),(44,36,'BB',8,8,1),(45,37,'BB',6,6,1),(46,37,'BB',8,8,1),(47,38,'BB',6,6,1),(48,38,'BB',8,8,1),(49,39,'BB',6,6,1),(50,39,'BB',8,8,1),(51,40,'AA',4,4,1),(52,40,'BB',1,2,2),(53,40,'BB',5,5,1),(54,41,'BB',1,2,2),(55,41,'BB',5,5,1),(56,42,'BB',2,2,1),(57,42,'BB',5,5,1),(58,43,'AA',1,1,1),(59,43,'AA',3,3,1),(60,44,'AA',1,1,1),(61,45,'AA',1,1,1),(62,46,'BB',6,6,1),(63,46,'BB',8,8,1),(64,47,'AA',3,3,1),(65,47,'AA',6,8,3),(66,48,'AA',2,2,1),(67,49,'AA',5,5,1),(68,49,'AA',9,10,2),(69,50,'BB',5,5,1),(70,51,'BB',1,2,2),(71,51,'BB',5,5,1),(72,51,'BB',7,7,1),(73,52,'BB',1,2,2),(74,52,'BB',5,5,1),(75,52,'BB',7,7,1),(76,53,'BB',1,2,2),(77,53,'BB',5,5,1),(78,53,'BB',7,7,1);
/*!40000 ALTER TABLE `d_act_packs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_act_types`
--

DROP TABLE IF EXISTS `d_act_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_act_types` (
  `id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `idx` varchar(45) NOT NULL,
  `shortName` varchar(45) NOT NULL,
  `fullName` varchar(255) NOT NULL,
  `order` tinyint(3) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_act_types`
--

LOCK TABLES `d_act_types` WRITE;
/*!40000 ALTER TABLE `d_act_types` DISABLE KEYS */;
INSERT INTO `d_act_types` VALUES (1,'input','Ввод','Акт о приёме на склад',10),(2,'transfer','Передача','Акт о приёме-передачи',20),(3,'output','Вывод','Акт о передаче со склада',30);
/*!40000 ALTER TABLE `d_act_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_acts`
--

DROP TABLE IF EXISTS `d_acts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_acts` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `typeId` tinyint(3) unsigned NOT NULL,
  `created` datetime NOT NULL,
  `nodeFromId` int(10) unsigned NOT NULL,
  `nodeToId` int(10) unsigned NOT NULL,
  `unitFromId` tinyint(3) unsigned DEFAULT NULL,
  `unitToId` tinyint(3) unsigned DEFAULT NULL,
  `amount` int(10) unsigned NOT NULL,
  `comment` varchar(2048) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `d_acts_typeId` (`typeId`),
  KEY `d_acts_nodeFromId` (`nodeFromId`),
  KEY `d_acts_nodeToId` (`nodeToId`),
  KEY `d_acts_unitToId` (`unitToId`),
  KEY `d_acts_unitFromId` (`unitFromId`),
  CONSTRAINT `d_acts_nodeFromId` FOREIGN KEY (`nodeFromId`) REFERENCES `w_nodes` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `d_acts_nodeToId` FOREIGN KEY (`nodeToId`) REFERENCES `w_nodes` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `d_acts_typeId` FOREIGN KEY (`typeId`) REFERENCES `d_act_types` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `d_acts_unitFromId` FOREIGN KEY (`unitFromId`) REFERENCES `m_units` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `d_acts_unitToId` FOREIGN KEY (`unitToId`) REFERENCES `m_units` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_acts`
--

LOCK TABLES `d_acts` WRITE;
/*!40000 ALTER TABLE `d_acts` DISABLE KEYS */;
INSERT INTO `d_acts` VALUES (1,1,'2015-06-27 11:09:13',2,4,NULL,1,1,''),(2,1,'2015-06-27 11:09:55',2,4,NULL,1,1,''),(3,1,'2015-06-27 11:25:00',2,4,NULL,1,2,''),(4,1,'2015-06-27 11:25:24',2,4,NULL,1,3,''),(5,1,'2015-06-27 15:21:43',2,4,NULL,1,6,''),(6,2,'2015-06-27 20:27:07',4,5,1,NULL,3,''),(7,2,'2015-06-27 20:32:04',4,5,1,NULL,3,''),(8,2,'2015-06-27 20:37:46',4,5,1,NULL,1,''),(9,1,'2015-06-27 20:57:18',2,4,NULL,1,10,''),(10,2,'2015-06-27 20:58:57',4,5,1,NULL,8,''),(11,2,'2015-06-27 21:06:15',4,5,1,NULL,1,''),(12,2,'2015-06-27 21:16:31',4,5,1,NULL,1,''),(13,2,'2015-06-27 21:19:08',4,5,1,NULL,1,''),(14,2,'2015-06-27 21:19:23',4,5,1,NULL,1,''),(15,2,'2015-06-27 21:22:19',4,5,1,NULL,1,''),(16,2,'2015-06-27 21:23:48',4,5,1,NULL,1,''),(17,2,'2015-06-27 21:43:27',5,6,NULL,6,1,''),(18,2,'2015-06-27 21:43:59',6,7,6,7,1,''),(19,2,'2015-06-27 21:44:14',4,5,1,NULL,6,''),(20,2,'2015-07-01 08:43:04',4,5,1,NULL,1,''),(21,2,'2015-07-01 08:43:41',5,6,NULL,6,1,''),(22,2,'2015-07-01 08:43:57',6,7,6,8,1,''),(23,2,'2015-07-08 19:13:00',4,6,1,2,1,''),(24,2,'2015-07-08 19:16:16',5,6,NULL,2,2,''),(25,2,'2015-07-08 19:16:31',6,7,2,4,3,''),(26,2,'2015-07-08 19:17:59',7,9,4,2,3,''),(27,2,'2015-07-08 19:19:44',9,11,2,1,2,''),(28,2,'2015-07-08 19:20:00',9,10,2,NULL,1,''),(29,2,'2015-07-08 19:20:17',10,11,NULL,1,1,''),(30,3,'2015-07-08 19:20:41',11,3,1,NULL,3,''),(31,2,'2015-07-09 16:52:12',7,9,7,6,1,''),(32,2,'2015-07-09 16:52:24',7,9,8,6,1,''),(33,2,'2015-07-28 22:24:38',4,6,1,6,2,''),(34,2,'2015-07-28 22:25:00',6,7,6,7,2,''),(35,2,'2015-07-29 19:02:49',7,9,7,6,2,''),(36,2,'2015-07-29 19:04:49',4,6,1,2,2,''),(37,2,'2015-07-29 19:05:06',6,7,2,5,2,''),(38,2,'2015-07-29 19:06:23',7,9,5,2,2,''),(39,2,'2015-07-29 19:08:27',9,11,2,1,2,''),(40,2,'2015-07-30 19:00:33',5,6,NULL,6,4,''),(41,2,'2015-07-30 19:01:08',6,7,6,7,3,''),(42,2,'2015-07-30 19:05:38',7,9,7,6,2,''),(43,2,'2015-07-30 19:13:02',9,11,6,1,2,''),(44,3,'2015-07-30 20:39:38',11,3,1,NULL,1,''),(45,3,'2015-07-30 20:41:23',11,3,1,NULL,1,''),(46,3,'2015-07-30 20:41:53',11,3,1,NULL,2,''),(47,3,'2015-07-30 20:42:47',11,3,1,NULL,4,''),(48,1,'2015-07-31 09:49:20',2,4,NULL,1,1,''),(49,1,'2015-07-31 09:49:46',2,4,NULL,1,3,''),(50,2,'2015-07-31 10:28:13',9,11,6,1,1,''),(51,2,'2015-07-31 10:33:08',7,9,7,6,4,''),(52,2,'2015-07-31 10:33:21',9,11,6,1,4,''),(53,3,'2015-07-31 10:33:57',11,3,1,NULL,4,'');
/*!40000 ALTER TABLE `d_acts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_bsos`
--

DROP TABLE IF EXISTS `d_bsos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_bsos` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nodeId` int(10) unsigned NOT NULL,
  `unitId` tinyint(3) unsigned DEFAULT NULL,
  `created` datetime NOT NULL,
  `series` varchar(8) NOT NULL,
  `number` bigint(20) unsigned NOT NULL,
  `issued` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `issuedDate` datetime DEFAULT NULL,
  `issuedBy` int(10) unsigned DEFAULT NULL,
  `issuedUnitId` tinyint(3) unsigned DEFAULT NULL,
  `corrupted` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `corruptedDate` datetime DEFAULT NULL,
  `corruptedBy` int(10) unsigned DEFAULT NULL,
  `corruptedUnitId` tinyint(3) unsigned DEFAULT NULL,
  `registered` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `registeredDate` datetime DEFAULT NULL,
  `registeredBy` int(10) unsigned DEFAULT NULL,
  `registeredUnitId` tinyint(3) unsigned DEFAULT NULL,
  `insured` varchar(255) DEFAULT NULL,
  `premium` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `d_bsos_series_number` (`series`,`number`),
  KEY `d_bsos_issuedBy` (`issuedBy`),
  KEY `d_bsos_unitId` (`unitId`),
  KEY `d_bsos_nodeId` (`nodeId`),
  KEY `d_bsos_corruptedBy_idx` (`corruptedBy`),
  KEY `d_bsos_registeredBy_idx` (`registeredBy`),
  KEY `d_bsos_corruptedUnitId_idx` (`corruptedUnitId`),
  KEY `d_bsos_registeredUnitId_idx` (`registeredUnitId`),
  KEY `d_bsos_issuedUnitId_idx` (`issuedUnitId`),
  CONSTRAINT `d_bsos_issuedUnitId` FOREIGN KEY (`issuedUnitId`) REFERENCES `m_units` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `d_bsos_corruptedBy` FOREIGN KEY (`corruptedBy`) REFERENCES `m_users` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `d_bsos_corruptedUnitId` FOREIGN KEY (`corruptedUnitId`) REFERENCES `m_units` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `d_bsos_issuedBy` FOREIGN KEY (`issuedBy`) REFERENCES `m_users` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `d_bsos_nodeId` FOREIGN KEY (`nodeId`) REFERENCES `w_nodes` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `d_bsos_registeredBy` FOREIGN KEY (`registeredBy`) REFERENCES `m_users` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `d_bsos_registeredUnitId` FOREIGN KEY (`registeredUnitId`) REFERENCES `m_units` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `d_bsos_unitId` FOREIGN KEY (`unitId`) REFERENCES `m_units` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_bsos`
--

LOCK TABLES `d_bsos` WRITE;
/*!40000 ALTER TABLE `d_bsos` DISABLE KEYS */;
INSERT INTO `d_bsos` VALUES (3,6,6,'2015-06-27 11:25:00','AA',4,0,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL),(13,4,1,'2015-06-27 20:57:18','СС',1,0,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL),(14,4,1,'2015-06-27 20:57:18','СС',2,0,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL),(15,4,1,'2015-06-27 20:57:18','СС',3,0,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL),(16,4,1,'2015-06-27 20:57:18','СС',4,0,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL),(17,4,1,'2015-06-27 20:57:18','СС',5,0,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL),(18,4,1,'2015-06-27 20:57:18','СС',6,0,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL),(19,4,1,'2015-06-27 20:57:18','СС',7,0,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL),(20,4,1,'2015-06-27 20:57:18','СС',8,0,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL),(21,4,1,'2015-06-27 20:57:18','СС',9,0,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL),(22,9,6,'2015-06-27 20:57:18','СС',10,0,'1970-01-01 03:00:00',NULL,NULL,1,'2015-07-29 19:02:38',1,7,0,'1970-01-01 03:00:00',NULL,NULL,NULL,NULL),(74,4,1,'2015-07-31 09:49:20','AA',2,0,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL),(75,4,1,'2015-07-31 09:49:46','AA',5,0,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `d_bsos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_bsos_archived`
--

DROP TABLE IF EXISTS `d_bsos_archived`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_bsos_archived` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nodeId` int(10) unsigned NOT NULL,
  `unitId` tinyint(3) unsigned DEFAULT NULL,
  `created` datetime NOT NULL,
  `series` varchar(8) NOT NULL,
  `number` bigint(20) unsigned NOT NULL,
  `issued` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `issuedDate` datetime DEFAULT NULL,
  `issuedBy` int(10) unsigned DEFAULT NULL,
  `issuedUnitId` tinyint(3) unsigned DEFAULT NULL,
  `corrupted` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `corruptedDate` datetime DEFAULT NULL,
  `corruptedBy` int(10) unsigned DEFAULT NULL,
  `corruptedUnitId` tinyint(3) unsigned DEFAULT NULL,
  `registered` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `registeredDate` datetime DEFAULT NULL,
  `registeredBy` int(10) unsigned DEFAULT NULL,
  `registeredUnitId` tinyint(3) unsigned DEFAULT NULL,
  `insured` varchar(255) DEFAULT NULL,
  `premium` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`,`created`),
  KEY `d_bsos_issuedBy` (`issuedBy`),
  KEY `d_bsos_unitId` (`unitId`),
  KEY `d_bsos_nodeId` (`nodeId`),
  KEY `d_bsos_corruptedBy_idx` (`corruptedBy`),
  KEY `d_bsos_registeredBy_idx` (`registeredBy`),
  KEY `d_bsos_corruptedUnitId_idx` (`corruptedUnitId`),
  KEY `d_bsos_registeredUnitId_idx` (`registeredUnitId`),
  KEY `d_bsos_issuedUnitId_idx` (`issuedUnitId`),
  KEY `d_bsos_series` (`series`),
  KEY `d_bsos_number` (`number`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (YEAR(created))
(PARTITION part2015 VALUES LESS THAN (2016) ENGINE = InnoDB,
 PARTITION part2016 VALUES LESS THAN (2017) ENGINE = InnoDB,
 PARTITION part2017 VALUES LESS THAN (2018) ENGINE = InnoDB,
 PARTITION part2018 VALUES LESS THAN (2019) ENGINE = InnoDB,
 PARTITION part2019 VALUES LESS THAN (2020) ENGINE = InnoDB,
 PARTITION part2020 VALUES LESS THAN (2021) ENGINE = InnoDB,
 PARTITION part2021 VALUES LESS THAN (2022) ENGINE = InnoDB,
 PARTITION part2022 VALUES LESS THAN (2023) ENGINE = InnoDB,
 PARTITION part2023 VALUES LESS THAN (2024) ENGINE = InnoDB,
 PARTITION part2024 VALUES LESS THAN (2025) ENGINE = InnoDB,
 PARTITION part2025 VALUES LESS THAN (2026) ENGINE = InnoDB,
 PARTITION part2026 VALUES LESS THAN (2027) ENGINE = InnoDB,
 PARTITION part2027 VALUES LESS THAN (2028) ENGINE = InnoDB,
 PARTITION part2028 VALUES LESS THAN (2029) ENGINE = InnoDB,
 PARTITION part2029 VALUES LESS THAN (2030) ENGINE = InnoDB,
 PARTITION part2030 VALUES LESS THAN (2031) ENGINE = InnoDB,
 PARTITION part2031 VALUES LESS THAN (2032) ENGINE = InnoDB,
 PARTITION part2032 VALUES LESS THAN (2033) ENGINE = InnoDB,
 PARTITION part2033 VALUES LESS THAN (2034) ENGINE = InnoDB,
 PARTITION part2034 VALUES LESS THAN (2035) ENGINE = InnoDB,
 PARTITION part2035 VALUES LESS THAN (2036) ENGINE = InnoDB,
 PARTITION part2036 VALUES LESS THAN (2037) ENGINE = InnoDB,
 PARTITION part2037 VALUES LESS THAN (2038) ENGINE = InnoDB,
 PARTITION part2038 VALUES LESS THAN (2039) ENGINE = InnoDB,
 PARTITION part2039 VALUES LESS THAN (2040) ENGINE = InnoDB,
 PARTITION part2040 VALUES LESS THAN (2041) ENGINE = InnoDB,
 PARTITION part2041 VALUES LESS THAN (2042) ENGINE = InnoDB,
 PARTITION part2042 VALUES LESS THAN (2043) ENGINE = InnoDB,
 PARTITION part2043 VALUES LESS THAN (2044) ENGINE = InnoDB,
 PARTITION part2044 VALUES LESS THAN (2045) ENGINE = InnoDB,
 PARTITION part2045 VALUES LESS THAN (2046) ENGINE = InnoDB,
 PARTITION part2046 VALUES LESS THAN (2047) ENGINE = InnoDB,
 PARTITION part2047 VALUES LESS THAN (2048) ENGINE = InnoDB,
 PARTITION part2048 VALUES LESS THAN (2049) ENGINE = InnoDB,
 PARTITION part2049 VALUES LESS THAN (2050) ENGINE = InnoDB,
 PARTITION part2050 VALUES LESS THAN (2051) ENGINE = InnoDB,
 PARTITION part2051 VALUES LESS THAN (2052) ENGINE = InnoDB,
 PARTITION part2052 VALUES LESS THAN (2053) ENGINE = InnoDB,
 PARTITION part2053 VALUES LESS THAN (2054) ENGINE = InnoDB,
 PARTITION part2054 VALUES LESS THAN (2055) ENGINE = InnoDB,
 PARTITION part2055 VALUES LESS THAN (2056) ENGINE = InnoDB,
 PARTITION part2056 VALUES LESS THAN (2057) ENGINE = InnoDB,
 PARTITION part2057 VALUES LESS THAN (2058) ENGINE = InnoDB,
 PARTITION part2058 VALUES LESS THAN (2059) ENGINE = InnoDB,
 PARTITION part2059 VALUES LESS THAN (2060) ENGINE = InnoDB,
 PARTITION part2060 VALUES LESS THAN (2061) ENGINE = InnoDB,
 PARTITION part2061 VALUES LESS THAN (2062) ENGINE = InnoDB,
 PARTITION part2062 VALUES LESS THAN (2063) ENGINE = InnoDB,
 PARTITION part2063 VALUES LESS THAN (2064) ENGINE = InnoDB,
 PARTITION part2064 VALUES LESS THAN (2065) ENGINE = InnoDB,
 PARTITION part2065 VALUES LESS THAN (2066) ENGINE = InnoDB,
 PARTITION part2066 VALUES LESS THAN (2067) ENGINE = InnoDB,
 PARTITION part2067 VALUES LESS THAN (2068) ENGINE = InnoDB,
 PARTITION part2068 VALUES LESS THAN (2069) ENGINE = InnoDB,
 PARTITION part2069 VALUES LESS THAN (2070) ENGINE = InnoDB,
 PARTITION part2070 VALUES LESS THAN (2071) ENGINE = InnoDB,
 PARTITION part2071 VALUES LESS THAN (2072) ENGINE = InnoDB,
 PARTITION part2072 VALUES LESS THAN (2073) ENGINE = InnoDB,
 PARTITION part2073 VALUES LESS THAN (2074) ENGINE = InnoDB,
 PARTITION part2074 VALUES LESS THAN (2075) ENGINE = InnoDB,
 PARTITION part2075 VALUES LESS THAN (2076) ENGINE = InnoDB,
 PARTITION part2076 VALUES LESS THAN (2077) ENGINE = InnoDB,
 PARTITION part2077 VALUES LESS THAN (2078) ENGINE = InnoDB,
 PARTITION part2078 VALUES LESS THAN (2079) ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_bsos_archived`
--

LOCK TABLES `d_bsos_archived` WRITE;
/*!40000 ALTER TABLE `d_bsos_archived` DISABLE KEYS */;
INSERT INTO `d_bsos_archived` VALUES (1,3,NULL,'2015-06-27 11:09:55','AA',1,1,'2015-07-01 08:50:37',1,NULL,0,NULL,NULL,NULL,1,'2015-07-09 17:10:45',1,6,'Кулаков Вячеслав Анатольевич',1000.55),(2,3,NULL,'2015-06-27 11:25:00','AA',3,1,'2015-07-01 08:53:58',1,NULL,0,NULL,NULL,NULL,1,'2015-07-30 20:42:33',1,1,'Кулаков Вячеслав Анатольевич',8500.00),(4,3,NULL,'2015-06-27 11:25:24','AA',6,1,'2015-07-08 19:16:56',1,NULL,0,NULL,NULL,NULL,1,'2015-07-30 20:42:55',1,1,'Василий Васечкин',900.00),(5,3,NULL,'2015-06-27 11:25:24','AA',7,1,'2015-07-08 19:17:11',1,NULL,0,NULL,NULL,NULL,1,'2015-07-30 20:42:42',1,1,'Петров Пётр Петрович',4555.00),(6,3,NULL,'2015-06-27 11:25:24','AA',8,1,'2015-07-08 19:17:29',1,NULL,0,NULL,NULL,NULL,1,'2015-07-30 20:42:38',1,1,'Скоробогатько Иван',6200.00),(7,3,NULL,'2015-06-27 15:21:43','BB',1,1,'2015-07-31 10:31:55',1,7,0,NULL,NULL,NULL,1,'2015-07-31 10:33:44',1,1,'Ильюшин Петр Николаевич',6300.00),(8,3,NULL,'2015-06-27 15:21:43','BB',2,1,'2015-07-30 19:02:57',7,7,0,NULL,NULL,NULL,1,'2015-07-31 10:33:49',1,1,'Прея Валентина Викторовна',4300.00),(9,3,NULL,'2015-06-27 15:21:43','BB',5,0,NULL,NULL,NULL,1,'2015-07-30 19:02:30',7,7,1,'2015-07-31 10:28:41',1,1,NULL,NULL),(10,3,NULL,'2015-06-27 15:21:43','BB',6,1,'2015-07-29 19:05:55',1,5,0,NULL,NULL,NULL,1,'2015-07-29 19:09:16',1,1,'Петросян Александр Викторович',1200.00),(11,3,NULL,'2015-06-27 15:21:43','BB',7,0,NULL,NULL,NULL,1,'2015-07-28 22:25:21',1,7,0,NULL,NULL,NULL,NULL,NULL),(12,3,NULL,'2015-06-27 15:21:43','BB',8,1,'2015-07-29 19:06:20',1,5,0,NULL,NULL,NULL,1,'2015-07-29 19:09:21',1,1,'Староста Анатолий Евгеньевич',1400.00);
/*!40000 ALTER TABLE `d_bsos_archived` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_cities`
--

DROP TABLE IF EXISTS `m_cities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_cities` (
  `id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `comment` varchar(2048) NOT NULL DEFAULT '',
  `removed` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='Города';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_cities`
--

LOCK TABLES `m_cities` WRITE;
/*!40000 ALTER TABLE `m_cities` DISABLE KEYS */;
INSERT INTO `m_cities` VALUES (1,'0000-00-00 00:00:00','Воронеж','Этот город самый лучший город на земле...',0),(2,'2015-06-18 20:57:06','Липецк','Долгожданный город.',1),(3,'2015-06-18 21:00:42','Киров','Где же этот город?',0),(4,'2015-06-20 12:13:08','Энгельс','',0),(5,'2015-06-20 13:15:05','Россошь','',0);
/*!40000 ALTER TABLE `m_cities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_roles`
--

DROP TABLE IF EXISTS `m_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_roles` (
  `id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `idx` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `m_roles_idx` (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_roles`
--

LOCK TABLES `m_roles` WRITE;
/*!40000 ALTER TABLE `m_roles` DISABLE KEYS */;
INSERT INTO `m_roles` VALUES (1,'admin','Администратор'),(2,'director','Директор филиала'),(3,'manager','Менеджер сервисных продаж '),(4,'register','Специалист отдела ввода ');
/*!40000 ALTER TABLE `m_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_unit_types`
--

DROP TABLE IF EXISTS `m_unit_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_unit_types` (
  `id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `parentId` tinyint(3) unsigned DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `m_unit_types_parentId` (`parentId`),
  CONSTRAINT `m_unit_types_parentId` FOREIGN KEY (`parentId`) REFERENCES `m_unit_types` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_unit_types`
--

LOCK TABLES `m_unit_types` WRITE;
/*!40000 ALTER TABLE `m_unit_types` DISABLE KEYS */;
INSERT INTO `m_unit_types` VALUES (1,NULL,'Центральный офис'),(2,1,'Центральный офис филиала'),(3,2,'Точка продаж');
/*!40000 ALTER TABLE `m_unit_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_units`
--

DROP TABLE IF EXISTS `m_units`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_units` (
  `id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `typeId` tinyint(3) unsigned NOT NULL,
  `cityId` tinyint(3) unsigned NOT NULL,
  `created` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `comment` varchar(2048) NOT NULL DEFAULT '',
  `removed` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `m_units_typeId` (`typeId`),
  KEY `m_units_cityId` (`cityId`),
  CONSTRAINT `m_units_cityId` FOREIGN KEY (`cityId`) REFERENCES `m_cities` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `m_units_typeId` FOREIGN KEY (`typeId`) REFERENCES `m_unit_types` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='Структурные подразделения';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_units`
--

LOCK TABLES `m_units` WRITE;
/*!40000 ALTER TABLE `m_units` DISABLE KEYS */;
INSERT INTO `m_units` VALUES (1,1,1,'0000-00-00 00:00:00','Центр Воронеж 1','',0),(2,2,1,'2015-06-18 20:02:08','Филиал Воронеж','Обычный комментарий для обычного города.',0),(3,3,1,'2015-06-18 20:08:09','Липецк Транспортная 2','',1),(4,3,1,'2015-06-23 21:02:14','Точка 1 Воронеж','',0),(5,3,1,'2015-06-23 21:02:28','Точка 2 Воронеж','',0),(6,2,3,'2015-06-23 21:02:47','Филиал Киров','',0),(7,3,3,'2015-06-23 21:03:02','Точка 1 Киров','',0),(8,3,3,'2015-06-23 21:03:17','Точка 2 Киров','',0),(9,3,3,'2015-08-06 09:59:33','Тест','',0);
/*!40000 ALTER TABLE `m_units` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_user_roles`
--

DROP TABLE IF EXISTS `m_user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_user_roles` (
  `userId` int(10) unsigned NOT NULL,
  `roleId` tinyint(3) unsigned NOT NULL,
  PRIMARY KEY (`userId`,`roleId`) USING BTREE,
  KEY `m_user_roles_userId` (`userId`),
  KEY `m_user_roles_roleId` (`roleId`),
  CONSTRAINT `m_user_roles_roleId` FOREIGN KEY (`roleId`) REFERENCES `m_roles` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `m_user_roles_userId` FOREIGN KEY (`userId`) REFERENCES `m_users` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_user_roles`
--

LOCK TABLES `m_user_roles` WRITE;
/*!40000 ALTER TABLE `m_user_roles` DISABLE KEYS */;
INSERT INTO `m_user_roles` VALUES (1,1),(2,2),(6,2),(3,3),(4,3),(5,3),(7,3),(8,3);
/*!40000 ALTER TABLE `m_user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_users`
--

DROP TABLE IF EXISTS `m_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `unitId` tinyint(10) unsigned NOT NULL,
  `created` datetime NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `firstName` varchar(255) NOT NULL DEFAULT '' COMMENT 'Имя',
  `lastName` varchar(255) NOT NULL DEFAULT '' COMMENT 'Фамилия',
  `comment` varchar(2048) NOT NULL DEFAULT '',
  `lastIp` varchar(15) DEFAULT NULL,
  `lastAuth` datetime DEFAULT NULL,
  `removed` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `m_users_username` (`username`),
  KEY `m_users_unitId` (`unitId`),
  CONSTRAINT `m_users_unitId` FOREIGN KEY (`unitId`) REFERENCES `m_units` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='Пользователи системы';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_users`
--

LOCK TABLES `m_users` WRITE;
/*!40000 ALTER TABLE `m_users` DISABLE KEYS */;
INSERT INTO `m_users` VALUES (1,1,'2015-06-12 20:17:42','vkulakov','$shiro1$SHA-256$500000$kF25lJptfk3huExDfgdpBg==$IW9CdcDOTF9HJCSD36wFj6Ht+J59m+BuoVw28hXJn/c=','Вячеслав','Кулаков','Тестовый пользователь','0:0:0:0:0:0:0:1','2015-08-08 10:40:37',0),(2,2,'2015-06-15 22:34:11','ivan','$shiro1$SHA-256$500000$eTn3OWo1N93DjVZEnxLifw==$Iq5pOEPpeytDbBPBGmAgRnfO6amtVvzZm52vvFZTqOM=','Ivan','Petrov','','127.0.0.1','2015-06-19 13:20:03',0),(3,4,'2015-06-17 20:58:35','slava','$shiro1$SHA-256$500000$mvct31Qwq2AAISik2EBrYQ==$+qHmNnYdLK/vXSKM1se2Ilpxvvpc9v24l+I8FOnJiHI=','Слава','Петров','Проверка комментария 1','127.0.0.1','2015-07-03 09:20:44',0),(4,5,'2015-06-17 21:17:04','petr','$shiro1$SHA-256$500000$OdKPriXOSKPblbz0lHLkRg==$1W7iiWU/t61p1M34wVExmMvNvmkUH60tuPLjgDdSoJo=','Пётр','Иванов','',NULL,NULL,0),(5,1,'2015-06-17 21:18:31','gena36','$shiro1$SHA-256$500000$gbyS91lGYnxhsR2oVN3ZKg==$sLpr3BEn4jGGn4kMS5PjEKNJUrdTpmc+d7DMeOphn/g=','Геннадий','Петров','Нужно удалить его в ближайшее время.','127.0.0.1','2015-06-19 14:04:49',1),(6,6,'2015-06-17 21:19:08','sasha','$shiro1$SHA-256$500000$awEYJTGYuBR4B+MYYujl7A==$f/WYVseOnFKXlDBNTVpTcU208PKbgXsd6Wi3BgEoEts=','Саша','Алексеев','','0:0:0:0:0:0:0:1','2015-07-30 19:05:04',0),(7,7,'2015-06-17 21:20:07','vsergeeva','$shiro1$SHA-256$500000$xD7A9JRnK8t064D8W4cneA==$ifVy1LNpUGomMx1HqdyVnaJrqzp77FU9TLmp+7W1g7I=','Вера','Сергеева','','0:0:0:0:0:0:0:1','2015-07-30 19:02:21',0),(8,8,'2015-06-23 21:05:54','belausov','$shiro1$SHA-256$500000$g/EEwQjSpfSkrul0kh15RA==$RwXulYEK8TnEnEcZe1yFKYNIAvqvoV8CZEPreORUSG0=','Виталий','Белаусов','',NULL,NULL,0);
/*!40000 ALTER TABLE `m_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `w_action_roles`
--

DROP TABLE IF EXISTS `w_action_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `w_action_roles` (
  `actionId` tinyint(3) unsigned NOT NULL,
  `roleId` tinyint(3) unsigned NOT NULL,
  PRIMARY KEY (`actionId`,`roleId`),
  KEY `w_action_roles_actionId` (`actionId`),
  KEY `w_action_roles_roleId` (`roleId`),
  CONSTRAINT `w_action_roles_actionId` FOREIGN KEY (`actionId`) REFERENCES `w_actions` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `w_action_roles_roleId` FOREIGN KEY (`roleId`) REFERENCES `m_roles` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `w_action_roles`
--

LOCK TABLES `w_action_roles` WRITE;
/*!40000 ALTER TABLE `w_action_roles` DISABLE KEYS */;
INSERT INTO `w_action_roles` VALUES (1,1),(1,2),(1,3),(2,1),(2,2),(2,4),(3,1),(3,2),(3,3);
/*!40000 ALTER TABLE `w_action_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `w_actions`
--

DROP TABLE IF EXISTS `w_actions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `w_actions` (
  `id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `idx` varchar(45) NOT NULL,
  `shortName` varchar(45) NOT NULL,
  `fullName` varchar(255) NOT NULL,
  `order` tinyint(3) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `w_actions`
--

LOCK TABLES `w_actions` WRITE;
/*!40000 ALTER TABLE `w_actions` DISABLE KEYS */;
INSERT INTO `w_actions` VALUES (1,'issue','Выдача','Выдача чистого бланка страхователю',10),(2,'register','Регистрация','Регистрация выданного бланга',20),(3,'corrupt','Испорчен','Отметка бланка как испорченного',30);
/*!40000 ALTER TABLE `w_actions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `w_link_roles`
--

DROP TABLE IF EXISTS `w_link_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `w_link_roles` (
  `linkId` int(10) unsigned NOT NULL,
  `roleId` tinyint(3) unsigned NOT NULL,
  PRIMARY KEY (`linkId`,`roleId`),
  KEY `w_link_roles_linkId` (`linkId`),
  KEY `w_link_roles_roleId` (`roleId`),
  CONSTRAINT `w_link_roles_linkId` FOREIGN KEY (`linkId`) REFERENCES `w_links` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `w_link_roles_roleId` FOREIGN KEY (`roleId`) REFERENCES `m_roles` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `w_link_roles`
--

LOCK TABLES `w_link_roles` WRITE;
/*!40000 ALTER TABLE `w_link_roles` DISABLE KEYS */;
INSERT INTO `w_link_roles` VALUES (9,1),(10,1),(11,1),(12,1),(14,1),(15,1),(16,1),(17,1),(18,1),(19,1),(11,2),(12,2),(14,2),(15,2),(19,2),(14,3);
/*!40000 ALTER TABLE `w_link_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `w_links`
--

DROP TABLE IF EXISTS `w_links`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `w_links` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `nodeFromId` int(10) unsigned NOT NULL,
  `nodeToId` int(10) unsigned NOT NULL,
  `actTypeId` tinyint(3) unsigned DEFAULT NULL,
  `comment` varchar(2048) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `w_links_nodeFromId` (`nodeFromId`),
  KEY `w_links_nodeToId` (`nodeToId`),
  KEY `w_links_actTypeId` (`actTypeId`),
  CONSTRAINT `w_links_actTypeId` FOREIGN KEY (`actTypeId`) REFERENCES `d_act_types` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `w_links_nodeFromId` FOREIGN KEY (`nodeFromId`) REFERENCES `w_nodes` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `w_links_nodeToId` FOREIGN KEY (`nodeToId`) REFERENCES `w_nodes` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `w_links`
--

LOCK TABLES `w_links` WRITE;
/*!40000 ALTER TABLE `w_links` DISABLE KEYS */;
INSERT INTO `w_links` VALUES (9,'Занесение',2,4,1,'Занесение чистых бланков в систему'),(10,'Отправка в филиал',4,5,2,'Отправка чистых бланков по почте из центрального офиса в филиал'),(11,'Получение в филиале',5,6,2,'Получение чистых бланков на почте в филиале'),(12,'Передача в точки продаж',6,7,2,'Передача чистых бланков из филиала в точки продаж'),(14,'Передача из точки продаж',7,9,2,'Передача выданных бланков из точек продаж в филиал'),(15,'Отправка в центральный офис',9,10,2,'Отправка выданных бланков по почте из филиала в центральный офис'),(16,'Получение в центральном офисе',10,11,2,'Получение выданных бланков на почте в филиале'),(17,'Выгрузка',11,3,3,'Выгрузка выданных бланков из системы'),(18,'Передача в филиал',4,6,2,'Передача чистых бланков напрямую из центрального офиса в филиал'),(19,'Передача в центральный офис',9,11,2,'Передача выданных бланков напрямую из филиала в центральный офис');
/*!40000 ALTER TABLE `w_links` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `w_node_actions`
--

DROP TABLE IF EXISTS `w_node_actions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `w_node_actions` (
  `nodeId` int(10) unsigned NOT NULL,
  `actionId` tinyint(3) unsigned NOT NULL,
  PRIMARY KEY (`nodeId`,`actionId`),
  KEY `w_node_actions_nodeId` (`nodeId`),
  KEY `w_node_actions_actionId` (`actionId`),
  CONSTRAINT `w_node_actions_actionId` FOREIGN KEY (`actionId`) REFERENCES `w_actions` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `w_node_actions_nodeId` FOREIGN KEY (`nodeId`) REFERENCES `w_nodes` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `w_node_actions`
--

LOCK TABLES `w_node_actions` WRITE;
/*!40000 ALTER TABLE `w_node_actions` DISABLE KEYS */;
INSERT INTO `w_node_actions` VALUES (7,1),(11,2),(7,3);
/*!40000 ALTER TABLE `w_node_actions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `w_node_types`
--

DROP TABLE IF EXISTS `w_node_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `w_node_types` (
  `id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `w_node_types`
--

LOCK TABLES `w_node_types` WRITE;
/*!40000 ALTER TABLE `w_node_types` DISABLE KEYS */;
INSERT INTO `w_node_types` VALUES (1,'Первичный ввод'),(2,'Вывод'),(3,'Почта'),(4,'Подразделение');
/*!40000 ALTER TABLE `w_node_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `w_nodes`
--

DROP TABLE IF EXISTS `w_nodes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `w_nodes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `nodeTypeId` tinyint(3) unsigned NOT NULL,
  `issued` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `unitTypeId` tinyint(3) unsigned DEFAULT NULL,
  `comment` varchar(2048) DEFAULT NULL,
  `order` tinyint(3) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `w_nodes_unitTypeId` (`unitTypeId`),
  KEY `w_nodes_nodeTypeId` (`nodeTypeId`),
  CONSTRAINT `w_nodes_nodeTypeId` FOREIGN KEY (`nodeTypeId`) REFERENCES `w_node_types` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `w_nodes_unitTypeId` FOREIGN KEY (`unitTypeId`) REFERENCES `m_unit_types` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `w_nodes`
--

LOCK TABLES `w_nodes` WRITE;
/*!40000 ALTER TABLE `w_nodes` DISABLE KEYS */;
INSERT INTO `w_nodes` VALUES (2,'Получение актов в Москве',1,0,NULL,'Получение чистых бланков в Москве',10),(3,'Возврат актов в Москву',2,1,NULL,'Возврат выданных бланков в Москву',100),(4,'Центральный офис',4,0,1,'Обработка чистых бланков в центральном офисе',20),(5,'Отправка в филиал',3,0,NULL,'Отправка чистых бланков почтой в филиал',30),(6,'Филиал',4,0,2,'Обработка чистых бланков в центральном офисе филиала',40),(7,'Точка продаж',4,0,3,'Выдача чистых бланков страхователям в точках продаж',50),(9,'Филиал',4,1,2,'Обработка выданных бланков в центральном офисе филиала',70),(10,'Отправка из филиала',3,1,NULL,'Отправка выданных бланков почтой в центральный офис',80),(11,'Центральный офис',4,1,1,'Обработка выданных бланков в центральном офисе',90);
/*!40000 ALTER TABLE `w_nodes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-08-08 11:43:57
