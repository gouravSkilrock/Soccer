--liquibase formatted sql
--changeset GK:1
alter table `st_sl_venue_master` change `venue_display_name` `venue_display_name` varchar (50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL;
--rollback
alter table `st_sl_venue_master` change `venue_display_name` `venue_display_name` varchar (20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL;
