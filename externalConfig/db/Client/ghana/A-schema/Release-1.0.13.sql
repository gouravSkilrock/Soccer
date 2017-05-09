--liquibase formatted sql

-- changeset Vatsal_8053:1
CREATE TABLE `st_sl_simnet_history_prize_detail_1_1` (
  `sim_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `draw_id` int(10) DEFAULT NULL,
  `merchant_id` int(11) NOT NULL,
  `prize_rank` tinyint(4) NOT NULL,
  `prize_amount` double(10,2) DEFAULT NULL,
  `unit_price` double(10,2) DEFAULT NULL,
  `no_of_winners` bigint(20) DEFAULT NULL,
  `no_of_match` int(11) DEFAULT NULL,
  `user_id` int(10) NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`sim_id`)
) ENGINE=InnoDB 
-- rollback DROP TABLE st_sl_simnet_history_prize_detail_1_1;


-- changeset Vatsal_8053:2
CREATE TABLE `st_sl_simnet_history_prize_distribution_1` (
  `sim_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `game_type_id` int(11) DEFAULT NULL,
  `draw_id` int(10) DEFAULT NULL,
  `total_sale` double(20,2) DEFAULT NULL,
  `10_match_winners` bigint(20) DEFAULT NULL,	
  `11_match_winners` bigint(20) DEFAULT NULL,
  `12_match_winners` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL, 
  `user_id` int(10) NOT NULL,
  PRIMARY KEY (`sim_id`)
) ENGINE=InnoDB 
-- rollback DROP TABLE st_sl_simnet_history_prize_distribution_1;

-- changeset Vatsal_8053:3
CREATE TABLE `st_sl_miscellaneous_simnet_1_1` (                   
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
   KEY `draw_id` (`draw_id`)                                
 ) ENGINE=InnoDB 
-- rollback DROP TABLE st_sl_miscellaneous_simnet_1_1;

-- changeset vatsal_8053:5
 DROP TABLE IF EXISTS `st_sl_miscellaneous_simnet_1_1`;
 
 CREATE TABLE `st_sl_miscellaneous_simnet_1_1` (           
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
  KEY `draw_id` (`draw_id`)                               
) ENGINE=MyISAM
-- rollback DROP TABLE st_sl_miscellaneous_simnet_1_1;


  
