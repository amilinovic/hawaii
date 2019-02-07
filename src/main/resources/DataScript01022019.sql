LOCK TABLES `absence` WRITE;
/*!40000 ALTER TABLE `absence` DISABLE KEYS */;
INSERT INTO `absence` VALUES (1,'ANNUAL','DEDUCTED_LEAVE',_binary '','string','icons/vacation.png','Annual leave - Vacation'),(2,'TRAINING','DEDUCTED_LEAVE',_binary '','string','icons/training.png','Training & Education'),(3,NULL,'BONUS_DAYS',_binary '','string','string','Bonus Days'),(4,NULL,'SICKNESS',_binary '','string','icons/sick-leave.png','Asthma related'),(5,NULL,'SICKNESS',_binary '','string','icons/sick-leave.png','Chest related'),(6,NULL,'SICKNESS',_binary '','string','icons/sick-leave.png','Dental Problems'),(7,NULL,'SICKNESS',_binary '','string','icons/sick-leave.png','Cold / Influenza'),(8,NULL,'SICKNESS',_binary '','string','icons/sick-leave.png','Fever'),(9,NULL,'SICKNESS',_binary '','string','icons/sick-leave.png','Food poisoing'),(10,NULL,'NON_DEDUCTED_LEAVE',_binary '','string','icons/business-travel.png','Business travel'),(11,NULL,'NON_DEDUCTED_LEAVE',_binary '','string','icons/own-wedding.png','Own wedding'),(12,NULL,'NON_DEDUCTED_LEAVE',_binary '','string','icons/maternity-leave.png','Maternity leave'),(13,NULL,'NON_DEDUCTED_LEAVE',_binary '','string','icons/work-from-home.png','Work from home'),(14,NULL,'NON_DEDUCTED_LEAVE',_binary '','string','icons/national-sport-competition.png','National sport competition');
/*!40000 ALTER TABLE `absence` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `leave_profile` WRITE;
/*!40000 ALTER TABLE `leave_profile` DISABLE KEYS */;
INSERT INTO `leave_profile`  VALUES (1,'DEFAULT',b'1','non-upgradable',160,40,40,b'0','Default profile',16),(2,'ZERO_TO_FIVE_YEARS',b'1','Upgradable',168,40,40,b'0','Tier 1',16),(3,'FIVE_TO_TEN_YEARS',b'1','Upgradable',172,40,40,b'0','Tier 2',24),(4,'TEN_TO_FIFTEEN_YEARS',b'1','Upgradable',180,40,40,b'0','Tier 3',24),(5,'FIFTEEN_TO_TWENTY_YEARS',b'1','Upgradable',188,40,40,b'0','Tier 4',24),(6,'TWENTY_TO_TWENTYFIVE_YEARS',b'1','Upgradable',196,40,40,b'0','Tier 5',24),(7,'TWENTYFIVE_TO_THIRTY_YEARS',b'1','Upgradable',204,40,40,b'0','Tier 6',24),(8,'Employees (3 days working)',b'0','Non-Upgradable',204,40,40,b'0','3 days working',24),(9,'Employees (4 days working)',b'0','Non-Upgradable',204,40,40,b'0','4 days working',24);
/*!40000 ALTER TABLE `leave_profile` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
INSERT INTO `team` VALUES (1,b'0','string','My team ONE'),(2,b'0','string','My team TWO'),(3,b'0','string','My team THREE'),(4,b'0','string','My team FOUR');
/*!40000 ALTER TABLE `team` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (id, user_status_type, email, full_name, job_title, user_role, started_working_date, started_working_at_execom_date, years_of_service, leave_profile_id, team_id) VALUES (1,'ACTIVE','bwayne@execom.eu','Bruce Wayne','Developer','HR_MANAGER','2015-01-02','2017-01-02',1,1,1),(2,'ACTIVE','merdeg@execom.eu','Monika Erdeg','Developer','HR_MANAGER','2016-02-02','2018-02-02',2,1,2),(3,'ACTIVE','idivljak@execom.eu','Ivan Divljak','Developer','HR_MANAGER','2014-03-03','2015-03-03',3,1,3),(4,'ACTIVE','sperlic@execom.eu','Stefan Perlic','Developer','HR_MANAGER','2011-01-02','2011-01-02',4,1,4),(5,'ACTIVE','dujfalusi@execom.eu','Dragan Ujfalusi','Developer','HR_MANAGER','2005-02-03','2007-07-07',10,2,4),(6,'ACTIVE','dflekac@execom.eu','Dusan Flekac','Developer','HR_MANAGER','2012-08-08','2018-08-08',10,2,1),(7,'ACTIVE','oboskov@execom.eu','Olivera Boskov','Developer','HR_MANAGER','2017-01-02','2018-01-02',10,2,1),(8,'ACTIVE','mbasicpalkovic@execom.eu','Mario Basic Palkovic','Developer','HR_MANAGER','2015-06-05','2018-12-04',3,2,1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `year` WRITE;
/*!40000 ALTER TABLE `year` DISABLE KEYS */;
INSERT INTO `year` VALUES (1,b'1',2017),(2,b'1',2018),(3,b'1',2019),(4,b'1',2020) ;
/*!40000 ALTER TABLE `year` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `allowance` WRITE;
/*!40000 ALTER TABLE `allowance` DISABLE KEYS */;
INSERT INTO `allowance` VALUES (1,160,0,0,0,0,0,0,0,0,16,1,1),(2,160,0,0,0,0,0,0,0,0,16,2,1),(3,160,0,0,0,0,0,0,0,0,16,3,1),(4,160,0,0,0,0,0,0,0,0,16,4,1),(5,160,0,0,0,0,0,0,0,0,16,3,2),(6,160,0,0,0,0,0,0,0,0,16,1,2),(7,160,0,0,0,0,0,0,0,0,16,2,3),(8,160,0,0,0,0,0,0,0,0,16,3,3),(9,160,0,0,0,0,0,0,0,0,16,1,4),(10,160,0,0,0,0,0,0,0,0,16,2,4),(11,168,0,0,0,0,0,0,0,0,16,3,5),(12,168,0,0,0,0,0,0,0,0,16,1,5),(13,168,0,0,0,0,0,0,0,0,16,2,6),(14,168,0,0,0,0,0,0,0,0,16,3,6),(15,168,0,0,0,0,0,0,0,0,16,1,7),(16,168,0,0,0,0,0,0,0,0,16,2,7),(17,168,0,0,0,0,0,0,0,0,16,3,8),(18,168,0,0,0,0,0,0,0,0,16,1,8);
/*!40000 ALTER TABLE `allowance` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `public_holiday` WRITE;
/*!40000 ALTER TABLE `public_holiday` DISABLE KEYS */;
INSERT INTO `public_holiday` VALUES (1,b'0','2018-01-01','New Year'),(2,b'0','2018-01-02','New Year'),(3,b'0','2019-01-01','New Year'),(4,b'0','2019-01-02','New Year');
/*!40000 ALTER TABLE `public_holiday` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `team_approver` WRITE;
/*!40000 ALTER TABLE `team_approver` DISABLE KEYS */;
INSERT INTO `team_approver` VALUES (1,2),(1,3),(1,4),(2,3),(3,2),(4,3),(4,2),(3,4),(2,4);
/*!40000 ALTER TABLE `team_approver` ENABLE KEYS */;
UNLOCK TABLES;
