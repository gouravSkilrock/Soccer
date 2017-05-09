--liquibase formatted sql

--changeset Rachit-9382:3
CREATE TABLE `arch_drop_table_history` (
  `game_id` tinyint(3) unsigned DEFAULT NULL,
  `game_type_id` tinyint(3) unsigned DEFAULT NULL,
  `last_draw_id` int(10) unsigned NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `arch_drop_table_history`;

--changeset Rachit-9382:4
CREATE TABLE `archiving_history` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `processing_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `processing_duration` int(10) unsigned NOT NULL DEFAULT '0' COMMENT 'No of seconds in archiving',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `archiving_history`;

--changeset Rachit-9382:5
CREATE TABLE `datestore` (
  `alldate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `datestore`;

--changeset Rachit-9382:6
CREATE TABLE `draw_master2302` (
  `draw_id` int(10) unsigned NOT NULL DEFAULT '0',
  `game_type_id` int(11) DEFAULT NULL,
  `draw_no` int(11) DEFAULT NULL,
  `draw_datetime` datetime DEFAULT NULL,
  `draw_freeze_time` datetime DEFAULT NULL,
  `sale_start_time` datetime DEFAULT NULL,
  `draw_status` enum('ACTIVE','CANCEL','FREEZE','PERFORMED','INACTIVE','HOLD','CLAIM ALLOW','CLAIM HOLD','RANK CHK') DEFAULT NULL,
  `purchase_table_name` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `draw_master2302`;

--changeset Rachit-9382:7
CREATE TABLE `st_lms_audit_user_access_history` (
  `id` bigint(18) unsigned NOT NULL AUTO_INCREMENT,
  `audit_id` bigint(18) unsigned NOT NULL,
  `merchant_id` int(10) NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `access_ip` varchar(20) NOT NULL,
  `request_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  `priv_id` int(11) DEFAULT NULL,
  `is_audit_trail_display` enum('Y','N') DEFAULT NULL,
  `service_type` enum('HOME','DG','SLE','MGMT') DEFAULT NULL,
  `interface` enum('WEB','TERMINAL','MOBILE') DEFAULT NULL,
  `response_time` datetime NOT NULL,
  `entry_time` datetime NOT NULL COMMENT 'Entry Time while inserting entry in database through scheduler',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_audit_user_access_history`;

--changeset Rachit-9382:8
CREATE TABLE `st_rm_menu_master` (
  `menu_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `merchant_id` int(11) DEFAULT NULL,
  `menu_disp_name` varchar(100) DEFAULT NULL,
  `action_id` int(11) DEFAULT NULL,
  `parent_menu_id` int(11) DEFAULT NULL,
  `item_order` int(4) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`menu_id`),
  UNIQUE KEY `merchant_id` (`merchant_id`,`action_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_rm_menu_master`;

--changeset Rachit-9382:9
CREATE TABLE `st_rm_merchant_channel_mapping` (
  `merchant_channel_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `merchant_id` int(11) DEFAULT NULL,
  `channel_type` enum('WEB','TERMINAL','MOBILE','SMS') DEFAULT NULL,
  `channel_name` varchar(200) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`merchant_channel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_rm_merchant_channel_mapping`;

--changeset Rachit-9382:10
CREATE TABLE `st_rm_merchant_channel_tier_mapping` (
  `mct_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `merchant_channel_id` int(11) DEFAULT NULL,
  `tier_id` int(11) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`mct_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_rm_merchant_channel_tier_mapping`;

--changeset Rachit-9382:11
CREATE TABLE `st_rm_merchant_priv_mapping` (
  `merchant_priv_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `merchant_id` int(11) DEFAULT NULL,
  `priv_id` int(10) unsigned NOT NULL,
  `priv_disp_name` varchar(50) DEFAULT NULL,
  `is_default` enum('Y','N') DEFAULT NULL,
  `check_login` enum('Y','N') DEFAULT NULL,
  `is_role_head_priv` enum('Y','N') DEFAULT NULL,
  `is_sub_user_priv` enum('Y','N') DEFAULT NULL,
  `parent_menu_id` int(11) DEFAULT NULL,
  `priv_code` varchar(20) DEFAULT NULL,
  `hidden` enum('Y','N') DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`merchant_priv_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_rm_merchant_priv_mapping`;

CREATE TABLE `st_rm_priv_action_mapping` (
  `action_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `priv_id` int(11) DEFAULT NULL,
  `action_path` varchar(500) DEFAULT NULL,
  `action_mapping` varchar(200) DEFAULT NULL,
  `action_disp_name` varchar(200) DEFAULT NULL,
  `is_start` enum('Y','N') DEFAULT NULL,
  `is_audit_trail_display` enum('Y','N') DEFAULT 'N',
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`action_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_rm_priv_action_mapping`;

CREATE TABLE `st_rm_priv_rep` (
  `priv_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `priv_dev_name` varchar(100) DEFAULT NULL,
  `channel` enum('WEB','TERMINAL','MOBILE','SMS') DEFAULT NULL,
  `user_type` enum('BO','AGENT','RETAILER','PLAYER') DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`priv_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_rm_priv_rep`;

CREATE TABLE `st_rm_priv_rep_bak` (
  `priv_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `priv_dev_name` varchar(100) DEFAULT NULL,
  `channel` enum('WEB','TERMINAL','MOBILE','SMS') DEFAULT NULL,
  `user_type` enum('BO','AGENT','RETAILER','PLAYER') DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`priv_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_rm_priv_rep_bak`;

CREATE TABLE `st_rm_role_action_mapping` (
  `rpm_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `mct_id` int(11) DEFAULT NULL,
  `role_id` int(10) NOT NULL,
  `action_id` int(10) NOT NULL,
  `status` enum('ACTIVE','INACTIVE','NA') NOT NULL,
  PRIMARY KEY (`rpm_id`),
  UNIQUE KEY `role_id` (`action_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
--rollback drop table `st_rm_role_action_mapping`;

CREATE TABLE `st_rm_role_master` (
  `role_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `merchant_id` int(11) DEFAULT NULL,
  `merchant_role_id` int(11) DEFAULT NULL,
  `role_name` varchar(30) NOT NULL,
  `role_description` varchar(30) DEFAULT NULL,
  `owner_user_id` int(11) DEFAULT NULL,
  `tier_id` int(11) DEFAULT NULL,
  `is_master` enum('Y','N') DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_rm_role_master`;

CREATE TABLE `st_rm_tier_master` (
  `tier_id` tinyint(4) unsigned NOT NULL AUTO_INCREMENT,
  `tier_name` varchar(20) DEFAULT NULL,
  `tier_code` varchar(10) DEFAULT NULL,
  `tier_status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `parent_tier_id` tinyint(4) unsigned NOT NULL,
  PRIMARY KEY (`tier_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_rm_tier_master`;

CREATE TABLE `st_rm_user_action_mapping` (
  `user_id` int(10) unsigned NOT NULL,
  `rpm_id` int(11) NOT NULL,
  `status` enum('ACTIVE','INACTIVE','NA') NOT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_by` int(11) DEFAULT NULL,
  `request_ip` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`rpm_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_rm_user_action_mapping`;

CREATE TABLE `st_rm_user_priv_history` (
  `user_id` int(11) DEFAULT NULL,
  `rpm_id` int(11) DEFAULT NULL,
  `action_id` int(11) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','NA') DEFAULT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_by` int(11) DEFAULT NULL,
  `request_ip` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rm_user_priv_history`;

CREATE TABLE `st_sl_calendar` (
  `calendar_date` date NOT NULL,
  `day_name` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`calendar_date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_calendar`;

CREATE TABLE `st_sl_city_master` (
  `city_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `state_id` int(11) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL,
  `city_code` varchar(15) DEFAULT NULL,
  `city_name` varchar(30) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') NOT NULL,
  PRIMARY KEY (`city_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_sl_city_master`;

CREATE TABLE `st_sl_client_info_master` (
  `client_id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `client_access_url` varchar(150) DEFAULT NULL,
  `on_activity` enum('RESULT_SUBMISSION','DRAW_FREEZE') DEFAULT NULL,
  `STATUS` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_client_info_master`;

CREATE TABLE `st_sl_country_master` (
  `country_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `country_code` varchar(5) NOT NULL,
  `country_name` varchar(50) NOT NULL DEFAULT '',
  `status` enum('ACTIVE','INACTIVE') NOT NULL,
  PRIMARY KEY (`country_id`),
  KEY `country_code` (`country_code`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_sl_country_master`;

CREATE TABLE `st_sl_draw_details_1_1_2015` (
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
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_draw_details_1_1_2015`;

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
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_draw_details_1_1_2016`;

CREATE TABLE `st_sl_draw_details_1_2_2015` (
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
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_draw_details_1_2_2015`;

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
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_draw_details_1_2_2016`;

CREATE TABLE `st_sl_draw_details_1_3_2015` (
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
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_draw_details_1_3_2015`;

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
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_draw_details_1_3_2016`;

CREATE TABLE `st_sl_draw_details_1_4_2015` (
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
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_draw_details_1_4_2015`;

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
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_draw_details_1_4_2016`;

CREATE TABLE `st_sl_draw_details_change_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `draw_id` int(11) NOT NULL,
  `game_id` int(10) NOT NULL,
  `game_type_id` int(10) DEFAULT NULL,
  `merchant_id` int(10) NOT NULL,
  `user_id` int(11) NOT NULL,
  `action` enum('FREEZE','CHANGE DRAW TIME','CHANGE FREEZE TIME','CANCEL','CLAIM ALLOW','CLAIM HOLD','CHANGE SALE TIME') NOT NULL,
  `date` datetime NOT NULL,
  `remarks` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_draw_details_change_history`;

CREATE TABLE `st_sl_draw_event_mapping_1` (
  `event_draw_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(11) DEFAULT NULL,
  `draw_id` int(11) DEFAULT NULL,
  `event_order` int(11) DEFAULT NULL,
  `evt_opt_id` int(11) DEFAULT NULL COMMENT 'winning option',
  PRIMARY KEY (`event_draw_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_draw_event_mapping_1`;

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
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_draw_master_1`;

CREATE TABLE `st_sl_draw_merchant_mapping_1` (
  `draw_id` int(10) unsigned NOT NULL DEFAULT '0',
  `merchant_id` int(11) DEFAULT NULL,
  `draw_name` varchar(100) DEFAULT NULL,
  `draw_display_type` enum('DRAW_NAME','DRAW_DATETIME','NAME_DATETIME') DEFAULT NULL,
  `claim_start_date` datetime DEFAULT NULL,
  `claim_end_date` datetime DEFAULT NULL,
  `verification_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_draw_merchant_mapping_1`;

CREATE TABLE `st_sl_event_details_change_history` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(10) unsigned NOT NULL,
  `game_id` int(11) DEFAULT NULL,
  `event_display` varchar(50) DEFAULT NULL,
  `event_description` varchar(200) DEFAULT NULL,
  `league_id` int(11) DEFAULT NULL,
  `home_team_id` int(11) DEFAULT NULL,
  `away_team_id` int(11) DEFAULT NULL,
  `home_team_odds` varchar(10) DEFAULT NULL,
  `away_team_odds` varchar(10) DEFAULT NULL,
  `draw_odds` varchar(10) DEFAULT NULL,
  `venue_id` int(11) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `user_id` varchar(20) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_event_details_change_history`;

CREATE TABLE `st_sl_event_master` (
  `event_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `game_id` int(11) DEFAULT NULL,
  `event_display` varchar(50) DEFAULT NULL,
  `event_description` varchar(200) DEFAULT NULL,
  `league_id` int(11) DEFAULT NULL,
  `home_team_id` int(11) DEFAULT NULL,
  `away_team_id` int(11) DEFAULT NULL,
  `home_team_odds` varchar(10) DEFAULT NULL,
  `away_team_odds` varchar(10) DEFAULT NULL,
  `draw_odds` varchar(10) DEFAULT NULL,
  `venue_id` int(11) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `entry_time` datetime DEFAULT NULL,
  PRIMARY KEY (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_event_master`;

CREATE TABLE `st_sl_event_option_mapping` (
  `evt_opt_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `game_id` int(11) DEFAULT NULL,
  `game_type_id` int(11) DEFAULT NULL,
  `event_id` int(11) DEFAULT NULL,
  `option_name` varchar(100) DEFAULT NULL,
  `option_code` varchar(50) DEFAULT NULL,
  `is_displayable` enum('YES','NO') DEFAULT NULL,
  PRIMARY KEY (`evt_opt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_event_option_mapping`;

CREATE TABLE `st_sl_game_master` (
  `game_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `game_dev_name` varchar(50) DEFAULT NULL,
  `game_status` enum('SALE_OPEN','SALE_HOLD','SALE_CLOSE') DEFAULT NULL,
  PRIMARY KEY (`game_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_game_master`;

CREATE TABLE `st_sl_game_merchant_mapping` (
  `game_id` int(10) unsigned NOT NULL,
  `merchant_id` int(11) NOT NULL,
  `game_disp_name` varchar(50) DEFAULT NULL,
  `max_ticket_amount` decimal(20,2) DEFAULT NULL,
  `thersold_ticket_amount` decimal(20,2) DEFAULT NULL,
  `max_board_count` int(11) DEFAULT NULL,
  `min_board_count` int(11) DEFAULT NULL,
  `display_order` int(11) DEFAULT NULL,
  `game_status` enum('SALE_OPEN','SALE_HOLD','SALE_CLOSE') DEFAULT NULL,
  PRIMARY KEY (`game_id`,`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_game_merchant_mapping`;

CREATE TABLE `st_sl_game_team_master` (
  `team_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `game_id` int(11) DEFAULT NULL,
  `team_name` varchar(100) DEFAULT NULL,
  `team_code` varchar(50) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`team_id`),
  UNIQUE KEY `game_id` (`game_id`,`team_name`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_game_team_master`;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_game_tickets_1_1`;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_game_tickets_1_2`;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_game_tickets_1_3`;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_game_tickets_1_4`;

CREATE TABLE `st_sl_game_type_master` (
  `game_type_id` int(10) NOT NULL,
  `game_id` int(11) DEFAULT NULL,
  `type_dev_name` varchar(50) DEFAULT NULL,
  `no_of_events` enum('2','4','6','8','12','13','10') DEFAULT NULL,
  `event_type` enum('SIMPLE') DEFAULT NULL,
  `table_query` blob,
  `type_status` enum('SALE_OPEN','SALE_HOLD','SALE_CLOSE') DEFAULT NULL,
  `jackpot_sale_percent` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_game_type_master`;

CREATE TABLE `st_sl_game_type_merchant_mapping` (
  `game_type_id` int(10) unsigned NOT NULL DEFAULT '0',
  `merchant_id` int(11) DEFAULT NULL,
  `type_disp_name` varchar(50) DEFAULT NULL,
  `unit_price` decimal(10,2) DEFAULT NULL,
  `max_bet_amount_multiple` int(11) DEFAULT NULL,
  `prize_rank_xml` blob,
  `prize_distribution_xml` blob,
  `prize_amt_percentage` decimal(5,2) DEFAULT NULL,
  `jackpot_message_display` enum('YES','NO') DEFAULT NULL,
  `sale_start_time` datetime DEFAULT NULL,
  `sale_end_time` datetime DEFAULT NULL,
  `display_order` int(11) DEFAULT NULL,
  `type_status` enum('SALE_OPEN','SALE_HOLD','SALE_CLOSE') DEFAULT NULL,
  `event_selection_type` enum('SINGLE','MULTIPLE') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_game_type_merchant_mapping`;

CREATE TABLE `st_sl_game_type_option_mapping` (
  `game_type_opt_id` int(10) NOT NULL AUTO_INCREMENT,
  `game_id` int(11) DEFAULT NULL,
  `game_type_id` int(11) DEFAULT NULL,
  `option_id` int(10) DEFAULT NULL,
  `display_order` tinyint(11) DEFAULT NULL,
  PRIMARY KEY (`game_type_opt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_game_type_option_mapping`;

CREATE TABLE `st_sl_generate_ticketno` (
  `user_id` bigint(11) unsigned NOT NULL,
  `ticket_count` int(11) DEFAULT NULL,
  `service_id` int(11) DEFAULT NULL,
  `merchant_user_ticket_id` int(11) DEFAULT NULL,
  `merchant_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `merchant_id` (`merchant_id`,`merchant_user_ticket_id`),
  UNIQUE KEY `merchant_id_2` (`merchant_id`,`merchant_user_ticket_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_generate_ticketno`;

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
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_generic_scheduler_master`;

CREATE TABLE `st_sl_league_master` (
  `league_id` int(11) NOT NULL AUTO_INCREMENT,
  `league_type` varchar(20) DEFAULT NULL,
  `league_dev_name` varchar(50) DEFAULT NULL,
  `league_display_name` varchar(50) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`league_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_league_master`;

CREATE TABLE `st_sl_league_team_mapping` (
  `league_team_id` int(11) NOT NULL AUTO_INCREMENT,
  `league_id` int(11) DEFAULT NULL,
  `team_id` int(11) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`league_team_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_league_team_mapping`;

CREATE TABLE `st_sl_merchant_master` (
  `merchant_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `merchant_name` varchar(100) DEFAULT NULL,
  `merchant_dev_name` varchar(20) DEFAULT NULL,
  `service_id` int(11) DEFAULT NULL,
  `registration_date` datetime DEFAULT NULL,
  `protocol` varchar(20) DEFAULT NULL,
  `merchant_ip` varchar(50) DEFAULT NULL,
  `port` varchar(10) DEFAULT NULL,
  `project_name` varchar(20) DEFAULT NULL,
  `merchant_user_name` varchar(100) DEFAULT NULL,
  `merchant_password` varchar(50) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','TERMINATE') DEFAULT NULL,
  `alias_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`merchant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_merchant_master`;

CREATE TABLE `st_sl_merchant_player_master` (
  `user_id` bigint(10) unsigned NOT NULL AUTO_INCREMENT,
  `merchant_id` int(11) DEFAULT NULL,
  `merchant_user_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_merchant_player_master`;

CREATE TABLE `st_sl_merchant_retailer_master` (
  `user_id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `merchant_id` int(11) DEFAULT NULL,
  `merchant_user_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_merchant_retailer_master`;

CREATE TABLE `st_sl_merchant_user_authentication_mapping` (
  `user_id` bigint(20) unsigned NOT NULL,
  `merchant_id` int(10) DEFAULT NULL,
  `merchant_user_id` int(11) DEFAULT NULL,
  `session_id` char(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `merchant_id` (`merchant_id`,`merchant_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_merchant_user_authentication_mapping`;

CREATE TABLE `st_sl_merchant_user_master` (
  `user_id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `parent_user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `merchant_id` int(11) DEFAULT NULL,
  `merchant_user_id` varchar(50) DEFAULT NULL,
  `merchant_user_mapping_id` int(11) DEFAULT NULL,
  `user_name` varchar(100) DEFAULT NULL,
  `user_type` enum('RETAILER','AGENT','BO','PLAYER') DEFAULT NULL,
  `is_role_head` enum('Y','N') DEFAULT NULL,
  `first_name` varchar(30) DEFAULT NULL,
  `last_name` varchar(30) DEFAULT NULL,
  `address` varchar(90) DEFAULT NULL,
  `mobile_nbr` varchar(20) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `email_id` varchar(65) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  `state` varchar(20) DEFAULT NULL,
  `country` varchar(20) DEFAULT NULL,
  `org_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_merchant_user_master`;

CREATE TABLE `st_sl_merchant_wise_game_details` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `rep_date` date DEFAULT NULL,
  `merchant_id` tinyint(3) unsigned DEFAULT NULL,
  `game_id` tinyint(3) unsigned DEFAULT NULL,
  `game_type_id` tinyint(3) unsigned DEFAULT NULL,
  `total_sale_amount` double(20,2) NOT NULL DEFAULT '0.00',
  `total_refund_amount` double(20,2) NOT NULL DEFAULT '0.00',
  `total_winning_amount` double(20,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_merchant_wise_game_details`;

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
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_miscellaneous_1_1`;

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
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_miscellaneous_1_2`;

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
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_miscellaneous_1_3`;

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
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_miscellaneous_1_4`;

CREATE TABLE `st_sl_option_master` (
  `option_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `option_dev_name` varchar(50) DEFAULT NULL,
  `option_disp_name` varchar(50) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`option_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_option_master`;

CREATE TABLE `st_sl_pending_cancel` (
  `sale_ref_transaction_id` bigint(20) unsigned NOT NULL,
  `ticket_nbr` bigint(20) DEFAULT NULL,
  `mrp_amt` decimal(20,2) DEFAULT NULL,
  `game_id` int(11) DEFAULT NULL,
  `game_type_id` int(11) DEFAULT NULL,
  `cancel_attempt_time` datetime DEFAULT NULL,
  `txn_date` datetime DEFAULT NULL,
  `reason` enum('CANCEL_EXPIRED','AUTO_CANCEL') DEFAULT NULL,
  `settlement_ref_trans_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`sale_ref_transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_pending_cancel`;

CREATE TABLE `st_sl_prize_details_1_1` (
  `draw_id` int(10) unsigned NOT NULL,
  `merchant_id` int(11) NOT NULL,
  `prize_rank` tinyint(4) NOT NULL,
  `prize_amount` double(10,2) DEFAULT NULL,
  `unit_price` double(10,2) DEFAULT NULL,
  `no_of_winners` bigint(20) DEFAULT NULL,
  `no_of_match` int(11) DEFAULT NULL,
  PRIMARY KEY (`draw_id`,`merchant_id`,`prize_rank`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_prize_details_1_1`;

CREATE TABLE `st_sl_prize_details_1_2` (
  `draw_id` int(10) unsigned NOT NULL,
  `merchant_id` int(11) NOT NULL,
  `prize_rank` tinyint(4) NOT NULL,
  `prize_amount` double(10,2) DEFAULT NULL,
  `unit_price` double(10,2) DEFAULT NULL,
  `no_of_winners` bigint(20) DEFAULT NULL,
  `no_of_match` int(11) DEFAULT NULL,
  PRIMARY KEY (`draw_id`,`merchant_id`,`prize_rank`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_prize_details_1_2`;

CREATE TABLE `st_sl_prize_details_1_3` (
  `draw_id` int(10) unsigned NOT NULL,
  `merchant_id` int(11) NOT NULL,
  `prize_rank` tinyint(4) NOT NULL,
  `prize_amount` double(10,2) DEFAULT NULL,
  `unit_price` double(10,2) DEFAULT NULL,
  `no_of_winners` bigint(20) DEFAULT NULL,
  `no_of_match` int(11) DEFAULT NULL,
  PRIMARY KEY (`draw_id`,`merchant_id`,`prize_rank`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_prize_details_1_3`;

CREATE TABLE `st_sl_prize_details_1_4` (
  `draw_id` int(10) unsigned NOT NULL,
  `merchant_id` int(11) NOT NULL,
  `prize_rank` tinyint(4) NOT NULL,
  `prize_amount` double(10,2) DEFAULT NULL,
  `unit_price` double(10,2) DEFAULT NULL,
  `no_of_winners` bigint(20) DEFAULT NULL,
  `no_of_match` int(11) DEFAULT NULL,
  PRIMARY KEY (`draw_id`,`merchant_id`,`prize_rank`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_prize_details_1_4`;

CREATE TABLE `st_sl_property_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `property_code` varchar(50) DEFAULT NULL,
  `property_dev_name` varchar(50) DEFAULT NULL,
  `property_display_name` varchar(50) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `editable` enum('YES','NO') DEFAULT NULL,
  `value` varchar(100) DEFAULT NULL,
  `value_type` varchar(50) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `property_code` (`property_code`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_property_master`;

CREATE TABLE `st_sl_property_master_bak` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `property_code` varchar(50) DEFAULT NULL,
  `property_dev_name` varchar(50) DEFAULT NULL,
  `property_display_name` varchar(50) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `editable` enum('YES','NO') DEFAULT NULL,
  `value` varchar(100) DEFAULT NULL,
  `value_type` varchar(50) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `property_code` (`property_code`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_property_master_bak`;

CREATE TABLE `st_sl_property_master_history` (
  `property_code` varchar(50) DEFAULT NULL,
  `property_display_name` varchar(50) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `value` varchar(100) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by_user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_property_master_history`;

CREATE TABLE `st_sl_result_sub_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_id` int(11) DEFAULT NULL,
  `game_id` int(11) DEFAULT NULL,
  `game_type_id` int(11) DEFAULT NULL,
  `user1_id` int(11) DEFAULT NULL,
  `user2_id` int(11) DEFAULT NULL,
  `user3_id` int(11) DEFAULT NULL,
  `user4_id` int(11) DEFAULT NULL,
  `user5_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_result_sub_master`;

CREATE TABLE `st_sl_result_submission_user_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `game_id` int(11) DEFAULT NULL,
  `game_type_id` int(11) DEFAULT NULL,
  `draw_id` int(11) DEFAULT NULL,
  `user1_id` int(11) DEFAULT NULL,
  `user1_result` varchar(250) DEFAULT NULL,
  `user1_update_time` datetime DEFAULT NULL,
  `user2_id` int(11) DEFAULT NULL,
  `user2_result` varchar(250) DEFAULT NULL,
  `user2_update_time` datetime DEFAULT NULL,
  `bo_user_id` int(11) DEFAULT NULL,
  `bo_result` varchar(250) DEFAULT NULL,
  `bo_update_time` datetime DEFAULT NULL,
  `status` enum('PENDING','UPDATED','MATCHED','UNMATCHED','RESOLVED') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_result_submission_user_mapping`;

CREATE TABLE `st_sl_retailer_match_list_count` (
  `user_name` varchar(45) DEFAULT NULL,
  `match_list_count` int(5) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_retailer_match_list_count`;

CREATE TABLE `st_sl_sale_refund_txn_master` (
  `trans_id` bigint(20) unsigned NOT NULL,
  `merchant_id` int(11) DEFAULT NULL,
  `channel_type` enum('WEB','TERMINAL','MOBILE','SMS') DEFAULT NULL,
  `game_id` int(11) DEFAULT NULL,
  `game_type_id` int(11) DEFAULT NULL,
  `ticket_nbr` bigint(20) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `merchant_user_id` int(11) DEFAULT NULL,
  `is_auto_cancel` enum('Y','N') DEFAULT NULL,
  `cancel_type` enum('PAPER_OUT','RESPONSE_TIME_OUT','CANCEL_MANUAL','DATA_ERROR','CANCEL_MISMATCH','CANCEL_PRINTER') DEFAULT NULL,
  `trans_date` datetime DEFAULT NULL,
  `amount` double(20,2) DEFAULT NULL,
  `status` enum('INITIATED','FAILED','DONE') DEFAULT NULL,
  `merchant_trans_id` varchar(30) DEFAULT NULL,
  `sale_trans_id` bigint(20) DEFAULT NULL,
  `settlement_date` datetime DEFAULT NULL,
  `settlement_status` enum('PENDING','DONE') DEFAULT NULL,
  PRIMARY KEY (`trans_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_sale_refund_txn_master`;

CREATE TABLE `st_sl_sale_txn_master` (
  `trans_id` bigint(20) unsigned NOT NULL,
  `merchant_id` int(11) DEFAULT NULL,
  `channel_type` enum('WEB','TERMINAL','MOBILE','SMS','PORTAL') DEFAULT NULL,
  `game_id` int(11) DEFAULT NULL,
  `game_type_id` int(11) DEFAULT NULL,
  `ticket_nbr` bigint(20) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `merchant_user_id` int(11) DEFAULT NULL,
  `trans_date` datetime DEFAULT NULL,
  `amount` decimal(20,2) DEFAULT NULL,
  `status` enum('INITIATED','FAILED','DONE') DEFAULT NULL,
  `merchant_trans_id` varchar(30) DEFAULT NULL,
  `settlement_date` datetime DEFAULT NULL,
  `settlement_status` enum('PENDING','DONE') DEFAULT NULL,
  `is_cancel` enum('Y','N') DEFAULT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`trans_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_sale_txn_master`;

CREATE TABLE `st_sl_sale_winning_claim_condition` (
  `sale_merchant_id` int(11) DEFAULT NULL,
  `winning_merchant_id` int(11) DEFAULT NULL,
  `is_date_bypass` enum('YES','NO') DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_sale_winning_claim_condition`;

CREATE TABLE `st_sl_schedular_merchant_mapping` (
  `map_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `schedular_id` int(11) DEFAULT NULL,
  `merchant_id` int(11) DEFAULT NULL,
  `draw_data` blob,
  PRIMARY KEY (`map_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_schedular_merchant_mapping`;

CREATE TABLE `st_sl_schedular_merchant_mapping_bak` (
  `map_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `schedular_id` int(11) DEFAULT NULL,
  `merchant_id` int(11) DEFAULT NULL,
  `draw_data` blob,
  PRIMARY KEY (`map_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_schedular_merchant_mapping_bak`;

CREATE TABLE `st_sl_scheduler_master` (
  `scheduler_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `game_type_id` int(11) DEFAULT NULL,
  `game_id` int(11) DEFAULT NULL,
  `scheduler_draw_data` blob,
  `max_advance_draws` int(11) DEFAULT NULL,
  `additional_draws` int(11) DEFAULT NULL,
  `entry_per_table` int(11) DEFAULT NULL,
  `draw_frequency` enum('DAILY','WEEKLY') DEFAULT NULL,
  `scheduler_status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`scheduler_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_scheduler_master`;

CREATE TABLE `st_sl_settlement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_id` int(11) NOT NULL,
  `last_txn_id` bigint(20) DEFAULT NULL,
  `request_time` datetime DEFAULT NULL,
  `response_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_settlement`;

CREATE TABLE `st_sl_state_master` (
  `state_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `state_code` varchar(10) NOT NULL,
  `country_id` int(11) NOT NULL,
  `state_name` varchar(50) NOT NULL,
  `status` enum('ACTIVE','INACTIVE') NOT NULL,
  PRIMARY KEY (`state_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_sl_state_master`;

CREATE TABLE `st_sl_txn_master` (
  `trans_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `merchant_id` int(11) DEFAULT NULL,
  `trans_type` enum('SALE','SALE_REFUND','PWT') DEFAULT NULL,
  PRIMARY KEY (`trans_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_txn_master`;

CREATE TABLE `st_sl_venue_master` (
  `venue_id` int(11) NOT NULL AUTO_INCREMENT,
  `venue_display_name` varchar(50) DEFAULT NULL,
  `venue_code` varchar(20) DEFAULT NULL,
  `city_id` int(11) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`venue_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_venue_master`;

CREATE TABLE `st_sl_winning_txn_master` (
  `trans_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `merchant_id` int(11) DEFAULT NULL,
  `channel_type` enum('WEB','TERMINAL','MOBILE','SMS','API') DEFAULT NULL,
  `game_id` int(11) DEFAULT NULL,
  `game_type_id` int(11) DEFAULT NULL,
  `draw_id` int(11) DEFAULT NULL,
  `ticket_nbr` bigint(20) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `merchant_user_id` int(11) DEFAULT NULL,
  `trans_date` datetime DEFAULT NULL,
  `amount` double(10,2) DEFAULT NULL,
  `status` enum('INITIATED','PENDING','DONE','FAILED') DEFAULT NULL,
  `pay_type` enum('NORMAL_PAY','HIGH_PRIZE','MAS_APPROVAL','API_PAY') DEFAULT NULL,
  `merchant_trans_id` varchar(30) DEFAULT NULL,
  `request_id` varchar(30) DEFAULT NULL,
  `settlement_date` datetime DEFAULT NULL,
  `settlement_status` enum('PENDING','DONE') DEFAULT NULL,
  `tax_amt` decimal(20,2) NOT NULL,
  `net_amount` decimal(20,2) NOT NULL,
  PRIMARY KEY (`trans_id`),
  UNIQUE KEY `merchant_trans_id` (`merchant_trans_id`),
  UNIQUE KEY `merchant_trans_id_2` (`merchant_trans_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sl_winning_txn_master`;

CREATE TABLE `temp_arch_trans_pwt` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `trans_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
--rollback drop table `temp_arch_trans_pwt`;

CREATE TABLE `temp_arch_trans_sale` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `trans_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
--rollback drop table `temp_arch_trans_sale`;

CREATE TABLE `temp_arch_trans_sale_refund` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `trans_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
--rollback drop table `temp_arch_trans_sale_refund`;