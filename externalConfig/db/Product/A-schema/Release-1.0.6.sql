--liquibase formatted sql

--changeset Nikhil:1  
CREATE TABLE `st_sl_generic_scheduler_master` ( 
`id` tinyint(4) unsigned NOT NULL AUTO_INCREMENT, 
`scheduler_dev_name` varchar(50) NOT NULL, 
`scheduler_display_name` varchar(50) DEFAULT NULL, 
`jobGroup` varchar(50) DEFAULT NULL, 
`status` enum('ACTIVE','INACTIVE') DEFAULT NULL, 
`scheduled_Time` varchar(20) DEFAULT NULL, 
`last_start_time` datetime DEFAULT NULL, 
`last_end_time` datetime DEFAULT NULL, 
`last_status` enum('DONE','RUNNING','ERROR') DEFAULT NULL, 
`last_success_time` datetime DEFAULT NULL, 
`estimated_time` int(6) DEFAULT NULL COMMENT 'Scheuler Estimated Duration In Seconds', 
`status_msg` varchar(100) DEFAULT NULL, 
PRIMARY KEY (`id`), 
UNIQUE KEY `scheduler_dev_name` (`scheduler_dev_name`) 
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

insert into `st_sl_generic_scheduler_master`(`id`,`scheduler_dev_name`,`scheduler_display_name`,`jobGroup`,`status`,`scheduled_Time`,`last_start_time`,`last_end_time`,`last_status`,`last_success_time`,`estimated_time`,`status_msg`) values ( NULL,'ticketCountCronExpr','TicketCountUpdateScheduler','ticketCountCronExpr','ACTIVE','0 30 15 1/1 * ? *',NULL,NULL,NULL,NULL,NULL,NULL);

insert into `st_sl_generic_scheduler_master`(`id`,`scheduler_dev_name`,`scheduler_display_name`,`jobGroup`,`status`,`scheduled_Time`,`last_start_time`,`last_end_time`,`last_status`,`last_success_time`,`estimated_time`,`status_msg`) values ( NULL,'settlementJobCronExpr','SettlementScheduler','settlementJobCronExpr','ACTIVE','0 30 15 1/1 * ? *',NULL,NULL,NULL,NULL,NULL,NULL);

--rollback Drop table st_sl_generic_scheduler_master; delete from st_sl_generic_scheduler_master;
