--liquibase formatted sql

-- changeset Vatsal_8053:1
insert into st_rm_priv_rep (priv_dev_name,channel ,user_type,status)values("SIMNET_RESULT", "WEB","BO","ACTIVE");
-- rollback delete  from st_rm_priv_rep where priv_dev_name ="SIMNET_RESULT";

-- changeset Vatsal_8053:2
INSERT INTO st_rm_priv_action_mapping (priv_id ,action_path ,action_mapping  ,action_disp_name ,is_start ,is_audit_trail_display,status ) select priv_id ,"/com/skilrock/sle/web/merchantUser/drawMgmt/Action/","boSimnetResultSubmissionMenu.action","Simnet Result" ,"Y","N","Active" from st_rm_priv_rep  where  priv_dev_name="SIMNET_RESULT"  ;                          
-- rollback delete from st_rm_priv_action_mapping where action_disp_name= "Simnet Result";


-- changeset Vatsal_8053:3
insert into st_rm_merchant_priv_mapping (merchant_id ,priv_id ,priv_disp_name,is_default,check_login,is_role_head_priv , is_sub_user_priv,parent_menu_id ,priv_code,hidden  ,status)  select 2,priv_id ,"Simnet Result","Y","Y","Y","Y",10,null,"N","ACTIVE" from st_rm_priv_rep  where  priv_dev_name="SIMNET_RESULT";

-- rollback delete from st_rm_merchant_priv_mapping where priv_disp_name =  "Simnet Result";


-- changeset Vatsal_8053:4
update `st_sl_schedular_merchant_mapping` set `draw_data`='{\r\n  \"WEEKLY\": {\r\n    \"MON\": {\r\n      \"1\": {\r\n        \"drawName\": \"WEEKEND\",\r\n        \"verificationDays\": -7,\r\n        \"claimStartDays\": -7,\r\n        \"claimEndDays\": -22,\r\n        \"drawDisplayType\": \"DRAW_NAME\",\r\n        \"eventStartId\": 1\r\n      }\r\n    }, \"FRI\": {\r\n      \"2\": {\r\n        \"drawName\": \"MIDWEEK\",\r\n        \"verificationDays\": -7,\r\n        \"claimStartDays\": -7,\r\n        \"claimEndDays\": -22,\r\n        \"drawDisplayType\": \"DRAW_NAME\",\r\n        \"eventStartId\": 1\r\n      }\r\n    }\r\n  }\r\n}' where `merchant_id`='2';
-- rollback update `st_sl_schedular_merchant_mapping` set `draw_data`='{\r\n  \"WEEKLY\": {\r\n    \"MON\": {\r\n      \"1\": {\r\n        \"drawName\": \"WEEKEND\",\r\n        \"verificationDays\": -1,\r\n        \"claimStartDays\": -1,\r\n        \"claimEndDays\": -15,\r\n        \"drawDisplayType\": \"DRAW_NAME\",\r\n        \"eventStartId\": 1\r\n      }\r\n    }, \"FRI\": {\r\n      \"2\": {\r\n        \"drawName\": \"MIDWEEK\",\r\n        \"verificationDays\": -1,\r\n        \"claimStartDays\": -1,\r\n        \"claimEndDays\": -15,\r\n        \"drawDisplayType\": \"DRAW_NAME\",\r\n        \"eventStartId\": 1\r\n      }\r\n    }\r\n  }\r\n}' where `merchant_id`='2';

