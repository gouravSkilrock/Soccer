--liquibase formatted sql

-- changeset Rachit_Bhandari_LMS_47:2
insert into `st_sl_property_master` (`property_code`, `property_dev_name`, `property_display_name`, `status`, `editable`, `value`, `value_type`, `description`) values('AUTO_APPROVED_WINNING_AMT_LIMIT','AUTO_APPROVED_WINNING_AMT_LIMIT','AUTO APPROVED WINNING AMT LIMIT','ACTIVE','YES','426','Double','Winning amount limit for auto winning transfer');
-- rollback delete from st_sl_property_master where property_code = 'AUTO_APPROVED_WINNING_AMT_LIMIT';