--liquibase formatted sql

--changeset ClientSchemaSLE:1


DROP TABLE IF EXISTS `st_sl_draw_details_1_1_2016`;

CREATE TABLE `st_sl_draw_details_1_1_2016` (
  `serial_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `draw_id` int(11) DEFAULT NULL,
  `merchant_id` int(11) DEFAULT NULL,
  `draw_datetime` datetime DEFAULT NULL,
  `draw_name` varchar(50) DEFAULT NULL,
  `draw_status` enum('ACTIVE','CANCEL','FREEZE','PERFORMED','INACTIVE','HOLD','CLAIM ALLOW','CLAIM HOLD','RANK CHK') NOT NULL DEFAULT 'ACTIVE',
  `game_id` int(10) unsigned NOT NULL,
  `game_type_id` int(10) unsigned NOT NULL,
  `total_sale_value` decimal(20,2) NOT NULL,
  `total_sale_tickets` int(11) NOT NULL,
  `total_cancel_tickets` int(11) NOT NULL,
  `total_cancel_amount` decimal(20,2) NOT NULL,
  `total_winning_amount` decimal(20,2) NOT NULL,
  `total_winning_tickets` int(11) NOT NULL,
  `total_claimed_tickets` bigint(20) unsigned NOT NULL,
  `total_claimed_amount` double(10,2) NOT NULL,
  PRIMARY KEY (`serial_id`)
) ENGINE=MyISAM  ;

/*Table structure for table `st_sl_draw_details_1_2_2016` */

DROP TABLE IF EXISTS `st_sl_draw_details_1_2_2016`;

CREATE TABLE `st_sl_draw_details_1_2_2016` (
  `serial_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `draw_id` int(11) DEFAULT NULL,
  `merchant_id` int(11) DEFAULT NULL,
  `draw_datetime` datetime DEFAULT NULL,
  `draw_name` varchar(50) DEFAULT NULL,
  `draw_status` enum('ACTIVE','CANCEL','FREEZE','PERFORMED','INACTIVE','HOLD','CLAIM ALLOW','CLAIM HOLD','RANK CHK') NOT NULL DEFAULT 'ACTIVE',
  `game_id` int(10) unsigned NOT NULL,
  `game_type_id` int(10) unsigned NOT NULL,
  `total_sale_value` decimal(20,2) NOT NULL,
  `total_sale_tickets` int(11) NOT NULL,
  `total_cancel_tickets` int(11) NOT NULL,
  `total_cancel_amount` decimal(20,2) NOT NULL,
  `total_winning_amount` decimal(20,2) NOT NULL,
  `total_winning_tickets` int(11) NOT NULL,
  `total_claimed_tickets` bigint(20) unsigned NOT NULL,
  `total_claimed_amount` double(10,2) NOT NULL,
  PRIMARY KEY (`serial_id`)
) ENGINE=MyISAM  ;

/*Table structure for table `st_sl_draw_details_1_3_2016` */

DROP TABLE IF EXISTS `st_sl_draw_details_1_3_2016`;

CREATE TABLE `st_sl_draw_details_1_3_2016` (
  `serial_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `draw_id` int(11) DEFAULT NULL,
  `merchant_id` int(11) DEFAULT NULL,
  `draw_datetime` datetime DEFAULT NULL,
  `draw_name` varchar(50) DEFAULT NULL,
  `draw_status` enum('ACTIVE','CANCEL','FREEZE','PERFORMED','INACTIVE','HOLD','CLAIM ALLOW','CLAIM HOLD','RANK CHK') NOT NULL DEFAULT 'ACTIVE',
  `game_id` int(10) unsigned NOT NULL,
  `game_type_id` int(10) unsigned NOT NULL,
  `total_sale_value` decimal(20,2) NOT NULL,
  `total_sale_tickets` int(11) NOT NULL,
  `total_cancel_tickets` int(11) NOT NULL,
  `total_cancel_amount` decimal(20,2) NOT NULL,
  `total_winning_amount` decimal(20,2) NOT NULL,
  `total_winning_tickets` int(11) NOT NULL,
  `total_claimed_tickets` bigint(20) unsigned NOT NULL,
  `total_claimed_amount` double(10,2) NOT NULL,
  PRIMARY KEY (`serial_id`)
) ENGINE=MyISAM  ;

/*Table structure for table `st_sl_draw_details_1_4_2016` */

DROP TABLE IF EXISTS `st_sl_draw_details_1_4_2016`;

CREATE TABLE `st_sl_draw_details_1_4_2016` (
  `serial_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `draw_id` int(11) DEFAULT NULL,
  `merchant_id` int(11) DEFAULT NULL,
  `draw_datetime` datetime DEFAULT NULL,
  `draw_name` varchar(50) DEFAULT NULL,
  `draw_status` enum('ACTIVE','CANCEL','FREEZE','PERFORMED','INACTIVE','HOLD','CLAIM ALLOW','CLAIM HOLD','RANK CHK') NOT NULL DEFAULT 'ACTIVE',
  `game_id` int(10) unsigned NOT NULL,
  `game_type_id` int(10) unsigned NOT NULL,
  `total_sale_value` decimal(20,2) NOT NULL,
  `total_sale_tickets` int(11) NOT NULL,
  `total_cancel_tickets` int(11) NOT NULL,
  `total_cancel_amount` decimal(20,2) NOT NULL,
  `total_winning_amount` decimal(20,2) NOT NULL,
  `total_winning_tickets` int(11) NOT NULL,
  `total_claimed_tickets` bigint(20) unsigned NOT NULL,
  `total_claimed_amount` double(10,2) NOT NULL,
  PRIMARY KEY (`serial_id`)
) ENGINE=MyISAM  ;

/*Table structure for table `st_sl_draw_event_mapping_1` */

DROP TABLE IF EXISTS `st_sl_draw_event_mapping_1`;

CREATE TABLE `st_sl_draw_event_mapping_1` (
  `event_draw_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(11) DEFAULT NULL,
  `draw_id` int(11) DEFAULT NULL,
  `event_order` int(11) DEFAULT NULL,
  `evt_opt_id` int(11) DEFAULT NULL COMMENT 'winning option',
  PRIMARY KEY (`event_draw_id`)
) ENGINE=InnoDB  ;

/*Table structure for table `st_sl_draw_master_1` */

DROP TABLE IF EXISTS `st_sl_draw_master_1`;

CREATE TABLE `st_sl_draw_master_1` (
  `draw_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `game_type_id` int(11) DEFAULT NULL,
  `draw_no` int(11) DEFAULT NULL,
  `draw_datetime` datetime DEFAULT NULL,
  `draw_freeze_time` datetime DEFAULT NULL,
  `sale_start_time` datetime DEFAULT NULL,
  `draw_status` enum('ACTIVE','CANCEL','FREEZE','PERFORMED','INACTIVE','HOLD','CLAIM ALLOW','CLAIM HOLD','RANK CHK') DEFAULT NULL,
  `purchase_table_name` int(11) DEFAULT NULL,
  PRIMARY KEY (`draw_id`)
) ENGINE=InnoDB  ;

/*Table structure for table `st_sl_draw_merchant_mapping_1` */

DROP TABLE IF EXISTS `st_sl_draw_merchant_mapping_1`;

CREATE TABLE `st_sl_draw_merchant_mapping_1` (
  `draw_id` int(10) unsigned NOT NULL DEFAULT '0',
  `merchant_id` int(11) DEFAULT NULL,
  `draw_name` varchar(100) DEFAULT NULL,
  `draw_display_type` enum('DRAW_NAME','DRAW_DATETIME','NAME_DATETIME') DEFAULT NULL,
  `claim_start_date` datetime DEFAULT NULL,
  `claim_end_date` datetime DEFAULT NULL,
  `verification_date` datetime DEFAULT NULL
) ENGINE=InnoDB ;

/*Table structure for table `st_sl_game_tickets_1_1` */

DROP TABLE IF EXISTS `st_sl_game_tickets_1_1`;

CREATE TABLE `st_sl_game_tickets_1_1` (
  `trans_id` bigint(20) unsigned NOT NULL,
  `draw_id` int(11) DEFAULT NULL,
  `game_type_id` int(11) DEFAULT NULL,
  `board_id` int(11) DEFAULT NULL,
  `ticket_number` bigint(20) DEFAULT NULL,
  `no_of_lines` int(11) DEFAULT NULL,
  `event_1` char(20) DEFAULT NULL,
  `event_2` char(20) DEFAULT NULL,
  `event_3` char(20) DEFAULT NULL,
  `event_4` char(20) DEFAULT NULL,
  `event_5` char(20) DEFAULT NULL,
  `event_6` char(20) DEFAULT NULL,
  `event_7` char(20) DEFAULT NULL,
  `event_8` char(20) DEFAULT NULL,
  `event_9` char(20) DEFAULT NULL,
  `event_10` char(20) DEFAULT NULL,
  `event_11` char(20) DEFAULT NULL,
  `event_12` char(20) CHARACTER SET latin1 COLLATE latin1_spanish_ci DEFAULT NULL,
  `event_13` char(20) DEFAULT NULL,
  `no_fixture` int(11) DEFAULT NULL,
  `rpc_total` int(11) DEFAULT '0',
  `reprint_datetime` datetime DEFAULT NULL,
  `bet_amount_multiple` int(11) DEFAULT NULL,
  `barcode_count` int(11) DEFAULT NULL,
  `total_amount` decimal(20,2) DEFAULT NULL,
  `sale_type` enum('PROMO','SALE') DEFAULT NULL,
  PRIMARY KEY (`trans_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_sl_game_tickets_1_2` */

DROP TABLE IF EXISTS `st_sl_game_tickets_1_2`;

CREATE TABLE `st_sl_game_tickets_1_2` (
  `trans_id` bigint(20) unsigned NOT NULL,
  `draw_id` int(11) DEFAULT NULL,
  `game_type_id` int(11) DEFAULT NULL,
  `board_id` int(11) DEFAULT NULL,
  `ticket_number` bigint(20) DEFAULT NULL,
  `no_of_lines` int(11) DEFAULT NULL,
  `event_1` char(20) DEFAULT NULL,
  `event_2` char(20) DEFAULT NULL,
  `event_3` char(20) DEFAULT NULL,
  `event_4` char(20) DEFAULT NULL,
  `event_5` char(20) DEFAULT NULL,
  `event_6` char(20) DEFAULT NULL,
  `event_7` char(20) DEFAULT NULL,
  `event_8` char(20) DEFAULT NULL,
  `event_9` char(20) DEFAULT NULL,
  `event_10` char(20) DEFAULT NULL,
  `no_fixture` int(11) DEFAULT NULL,
  `rpc_total` int(11) DEFAULT '0',
  `reprint_datetime` datetime DEFAULT NULL,
  `bet_amount_multiple` int(11) DEFAULT NULL,
  `barcode_count` int(11) DEFAULT NULL,
  `total_amount` decimal(20,2) DEFAULT NULL,
  `sale_type` enum('PROMO','SALE') DEFAULT NULL,
  PRIMARY KEY (`trans_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_sl_game_tickets_1_3` */

DROP TABLE IF EXISTS `st_sl_game_tickets_1_3`;

CREATE TABLE `st_sl_game_tickets_1_3` (
  `trans_id` bigint(20) unsigned NOT NULL,
  `draw_id` int(11) DEFAULT NULL,
  `game_type_id` int(11) DEFAULT NULL,
  `board_id` int(11) DEFAULT NULL,
  `ticket_number` bigint(20) DEFAULT NULL,
  `no_of_lines` int(11) DEFAULT NULL,
  `event_1` char(50) DEFAULT NULL,
  `event_2` char(50) DEFAULT NULL,
  `event_3` char(50) DEFAULT NULL,
  `event_4` char(50) DEFAULT NULL,
  `event_5` char(50) DEFAULT NULL,
  `event_6` char(50) DEFAULT NULL,
  `no_fixture` int(11) DEFAULT NULL,
  `rpc_total` int(11) DEFAULT '0',
  `reprint_datetime` datetime DEFAULT NULL,
  `bet_amount_multiple` int(11) DEFAULT NULL,
  `barcode_count` int(11) DEFAULT NULL,
  `total_amount` decimal(20,2) DEFAULT NULL,
  `sale_type` enum('PROMO','SALE') DEFAULT NULL,
  PRIMARY KEY (`trans_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_sl_game_tickets_1_4` */

DROP TABLE IF EXISTS `st_sl_game_tickets_1_4`;

CREATE TABLE `st_sl_game_tickets_1_4` (
  `trans_id` bigint(20) unsigned NOT NULL,
  `draw_id` int(11) DEFAULT NULL,
  `game_type_id` int(11) DEFAULT NULL,
  `board_id` int(11) DEFAULT NULL,
  `ticket_number` bigint(20) DEFAULT NULL,
  `no_of_lines` int(11) DEFAULT NULL,
  `event_1` char(50) DEFAULT NULL,
  `event_2` char(50) DEFAULT NULL,
  `event_3` char(50) DEFAULT NULL,
  `event_4` char(50) DEFAULT NULL,
  `no_fixture` int(11) DEFAULT NULL,
  `rpc_total` int(11) DEFAULT '0',
  `reprint_datetime` datetime DEFAULT NULL,
  `bet_amount_multiple` int(11) DEFAULT NULL,
  `barcode_count` int(11) DEFAULT NULL,
  `total_amount` decimal(20,2) DEFAULT NULL,
  `sale_type` enum('PROMO','SALE') DEFAULT NULL,
  PRIMARY KEY (`trans_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_sl_miscellaneous_1_1` */

DROP TABLE IF EXISTS `st_sl_miscellaneous_1_1`;

CREATE TABLE `st_sl_miscellaneous_1_1` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `draw_id` int(10) DEFAULT NULL,
  `roll_over_count` int(10) DEFAULT NULL,
  `carried_over_RSR` double(10,2) DEFAULT NULL,
  `carried_over_jackpot` double(10,2) DEFAULT NULL,
  `prize_fund` double(10,2) DEFAULT NULL,
  `RSR_for_this_draw` double(10,2) DEFAULT NULL,
  `total_available_RSR` double(10,2) DEFAULT NULL,
  `RSR_utilized_in_this_draw` double(10,2) DEFAULT NULL,
  `jackpot_for_this_draw` double(10,2) DEFAULT NULL,
  `total_available_jackpot` double(10,2) DEFAULT NULL,
  `fixed_prizes_fund` double(10,2) DEFAULT NULL,
  `remaining_prize_fund` double(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `draw_id` (`draw_id`)
) ENGINE=MyISAM  ;

/*Table structure for table `st_sl_miscellaneous_1_2` */

DROP TABLE IF EXISTS `st_sl_miscellaneous_1_2`;

CREATE TABLE `st_sl_miscellaneous_1_2` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `draw_id` int(10) DEFAULT NULL,
  `roll_over_count` int(10) DEFAULT NULL,
  `carried_over_RSR` double(10,2) DEFAULT NULL,
  `carried_over_jackpot` double(10,2) DEFAULT NULL,
  `prize_fund` double(10,2) DEFAULT NULL,
  `RSR_for_this_draw` double(10,2) DEFAULT NULL,
  `total_available_RSR` double(10,2) DEFAULT NULL,
  `RSR_utilized_in_this_draw` double(10,2) DEFAULT NULL,
  `jackpot_for_this_draw` double(10,2) DEFAULT NULL,
  `total_available_jackpot` double(10,2) DEFAULT NULL,
  `fixed_prizes_fund` double(10,2) DEFAULT NULL,
  `remaining_prize_fund` double(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `draw_id` (`draw_id`)
) ENGINE=MyISAM  ;

/*Table structure for table `st_sl_miscellaneous_1_3` */

DROP TABLE IF EXISTS `st_sl_miscellaneous_1_3`;

CREATE TABLE `st_sl_miscellaneous_1_3` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `draw_id` int(10) DEFAULT NULL,
  `roll_over_count` int(10) DEFAULT NULL,
  `carried_over_RSR` double(10,2) DEFAULT NULL,
  `carried_over_jackpot` double(10,2) DEFAULT NULL,
  `prize_fund` double(10,2) DEFAULT NULL,
  `RSR_for_this_draw` double(10,2) DEFAULT NULL,
  `total_available_RSR` double(10,2) DEFAULT NULL,
  `RSR_utilized_in_this_draw` double(10,2) DEFAULT NULL,
  `jackpot_for_this_draw` double(10,2) DEFAULT NULL,
  `total_available_jackpot` double(10,2) DEFAULT NULL,
  `fixed_prizes_fund` double(10,2) DEFAULT NULL,
  `remaining_prize_fund` double(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `draw_id` (`draw_id`)
) ENGINE=MyISAM ;

/*Table structure for table `st_sl_miscellaneous_1_4` */

DROP TABLE IF EXISTS `st_sl_miscellaneous_1_4`;

CREATE TABLE `st_sl_miscellaneous_1_4` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `draw_id` int(10) DEFAULT NULL,
  `roll_over_count` int(10) DEFAULT NULL,
  `carried_over_RSR` double(10,2) DEFAULT NULL,
  `carried_over_jackpot` double(10,2) DEFAULT NULL,
  `prize_fund` double(10,2) DEFAULT NULL,
  `RSR_for_this_draw` double(10,2) DEFAULT NULL,
  `total_available_RSR` double(10,2) DEFAULT NULL,
  `RSR_utilized_in_this_draw` double(10,2) DEFAULT NULL,
  `jackpot_for_this_draw` double(10,2) DEFAULT NULL,
  `total_available_jackpot` double(10,2) DEFAULT NULL,
  `fixed_prizes_fund` double(10,2) DEFAULT NULL,
  `remaining_prize_fund` double(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `draw_id` (`draw_id`)
) ENGINE=MyISAM ;

/*Table structure for table `st_sl_prize_details_1_1` */

DROP TABLE IF EXISTS `st_sl_prize_details_1_1`;

CREATE TABLE `st_sl_prize_details_1_1` (
  `draw_id` int(10) unsigned NOT NULL,
  `merchant_id` int(11) NOT NULL,
  `prize_rank` tinyint(4) NOT NULL,
  `prize_amount` double(10,2) DEFAULT NULL,
  `unit_price` double(10,2) DEFAULT NULL,
  `no_of_winners` bigint(20) DEFAULT NULL,
  `no_of_match` int(11) DEFAULT NULL,
  PRIMARY KEY (`draw_id`,`merchant_id`,`prize_rank`)
) ENGINE=InnoDB ;

/*Table structure for table `st_sl_prize_details_1_2` */

DROP TABLE IF EXISTS `st_sl_prize_details_1_2`;

CREATE TABLE `st_sl_prize_details_1_2` (
  `draw_id` int(10) unsigned NOT NULL,
  `merchant_id` int(11) NOT NULL,
  `prize_rank` tinyint(4) NOT NULL,
  `prize_amount` double(10,2) DEFAULT NULL,
  `unit_price` double(10,2) DEFAULT NULL,
  `no_of_winners` bigint(20) DEFAULT NULL,
  `no_of_match` int(11) DEFAULT NULL,
  PRIMARY KEY (`draw_id`,`merchant_id`,`prize_rank`)
) ENGINE=InnoDB ;

/*Table structure for table `st_sl_prize_details_1_3` */

DROP TABLE IF EXISTS `st_sl_prize_details_1_3`;

CREATE TABLE `st_sl_prize_details_1_3` (
  `draw_id` int(10) unsigned NOT NULL,
  `merchant_id` int(11) NOT NULL,
  `prize_rank` tinyint(4) NOT NULL,
  `prize_amount` double(10,2) DEFAULT NULL,
  `unit_price` double(10,2) DEFAULT NULL,
  `no_of_winners` bigint(20) DEFAULT NULL,
  `no_of_match` int(11) DEFAULT NULL,
  PRIMARY KEY (`draw_id`,`merchant_id`,`prize_rank`)
) ENGINE=InnoDB ;

/*Table structure for table `st_sl_prize_details_1_4` */

DROP TABLE IF EXISTS `st_sl_prize_details_1_4`;

CREATE TABLE `st_sl_prize_details_1_4` (
  `draw_id` int(10) unsigned NOT NULL,
  `merchant_id` int(11) NOT NULL,
  `prize_rank` tinyint(4) NOT NULL,
  `prize_amount` double(10,2) DEFAULT NULL,
  `unit_price` double(10,2) DEFAULT NULL,
  `no_of_winners` bigint(20) DEFAULT NULL,
  `no_of_match` int(11) DEFAULT NULL,
  PRIMARY KEY (`draw_id`,`merchant_id`,`prize_rank`)
) ENGINE=InnoDB ;

