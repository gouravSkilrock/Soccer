package com.skilrock.sle.roleMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.GenericException;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;

public class UpdateRoleTableControllerImpl {
	private static final SLELogger logger = SLELogger.getLogger(UpdateRoleTableControllerImpl.class.getName());

	public void updateRoleTableWithPriviledge(String requestIp) throws GenericException{
		PreparedStatement rolePstmt=null;
		ResultSet roleRs=null;
		PreparedStatement insertRolePstmt=null;
		//PreparedStatement updateRolePstmt=null;
		PreparedStatement userPstmt=null;
		ResultSet userRs=null;
		PreparedStatement insertuserPrivPstmt=null;
		PreparedStatement insertSubmenuPstmt=null;
		//PreparedStatement updateSubMenuPstmt=null;
		Connection con = null;
		try{
			String currentTime = Util.getCurrentTimeString();

			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			rolePstmt=con.prepareStatement("select role_id,is_master,mct_id,mas.merchant_id,channel_type,tier_code from st_rm_role_master mas inner join (select mct_id,merchant_id,channel_type,tier_code,cc.tier_id from st_rm_merchant_channel_mapping aa inner join st_rm_merchant_channel_tier_mapping bb inner join st_rm_tier_master cc on aa.merchant_channel_id=bb.merchant_channel_id and bb.tier_id=cc.tier_id)mct on mas.merchant_id=mct.merchant_id and mas.tier_id=mct.tier_id");
			roleRs=rolePstmt.executeQuery();
			while(roleRs.next()){
				int roleId=roleRs.getInt("role_id");
				int mctId=roleRs.getInt("mct_id");
				int merchantId=roleRs.getInt("merchant_id");
				String channelType=roleRs.getString("channel_type");
				String tierCode=roleRs.getString("tier_code");
				boolean isRoleMaster="Y".equals(roleRs.getString("is_master"));

				if(isRoleMaster && merchantId==2 && "BO".equals(tierCode) && "WEB".equals(channelType)) {
					String query = "INSERT INTO st_rm_user_priv_history (user_id, rpm_id, action_id, STATUS, change_date, change_by, request_ip) SELECT bb.user_id, aa.rpm_id, action_id, IF(aa.status='INACTIVE',aa.status,bb.status) STATUS, change_date, change_by, request_ip FROM st_rm_role_action_mapping aa INNER JOIN st_rm_user_action_mapping bb INNER JOIN st_sl_merchant_user_master cc ON aa.rpm_id=bb.rpm_id AND bb.user_id=cc.user_id WHERE aa.status!='NA' AND bb.status!='NA' AND merchant_id=2 AND user_type='BO';";
					//System.out.println("Insert History Query - "+query);
					logger.debug(query);
					con.createStatement().executeUpdate(query);

					query = "UPDATE st_rm_user_action_mapping SET change_date='"+currentTime+"', change_by=11001, request_ip='"+requestIp+"' WHERE user_id IN (SELECT user_id FROM st_sl_merchant_user_master WHERE merchant_id=2 AND user_type='BO');";
					//System.out.println("Update Mapping Table Query - "+query);
					logger.debug(query);
					con.createStatement().executeUpdate(query);
				}

				if(isRoleMaster){
				//	insertRolePstmt=con.prepareStatement("insert into st_pms_role_priv_mapping(role_id,priv_id,status) select ?,priv_id,? from st_pms_priviledge_rep where is_start='Y' and priv_id not in (select priv_id from st_pms_role_priv_mapping where role_id=?) group by priv_id");
					insertRolePstmt=con.prepareStatement("insert into st_rm_role_action_mapping(mct_id,role_id,action_id,status) select ? mct_id,? role_id,action_id,priv.status status " +
														" from st_rm_priv_rep priv inner join st_rm_priv_action_mapping priv_action inner join st_rm_merchant_priv_mapping priv_merchant " +
														" on priv.priv_id=priv_action.priv_id and priv_merchant.priv_id=priv.priv_id where merchant_id=? and channel=? and user_type=? " +
														" and action_id not in (select action_id from st_rm_role_action_mapping where mct_id=?  and role_id=?)");
					
					insertRolePstmt.setInt(1, mctId);
					insertRolePstmt.setInt(2, roleId);
					insertRolePstmt.setInt(3, merchantId);
					insertRolePstmt.setString(4, channelType);
					insertRolePstmt.setString(5, tierCode);
					insertRolePstmt.setInt(6, mctId);
					insertRolePstmt.setInt(7, roleId);
					
					insertRolePstmt.executeUpdate();
					
				}else{
					//insertRolePstmt=con.prepareStatement("insert into st_pms_role_priv_mapping(role_id,priv_id,status) select ?,priv_id,if(is_role_head_priv='Y','INACTIVE','NA') from st_pms_priviledge_rep where is_start='Y'  and priv_id not in (select priv_id from st_pms_role_priv_mapping where role_id=?) group by priv_id");
					insertRolePstmt=con.prepareStatement("insert into st_rm_role_action_mapping(mct_id,role_id,action_id,status) select ? mct_id,? role_id,action_id,if(is_role_head_priv='Y','INACTIVE','NA') status " +
							" from st_rm_priv_rep priv inner join st_rm_priv_action_mapping priv_action inner join st_rm_merchant_priv_mapping priv_merchant " +
							" on priv.priv_id=priv_action.priv_id and priv_merchant.priv_id=priv.priv_id where merchant_id=? and channel=? and user_type=? " +
							" and action_id not in (select action_id from st_rm_role_action_mapping where mct_id=?  and role_id=?)");

						insertRolePstmt.setInt(1, mctId);
						insertRolePstmt.setInt(2, roleId);
						insertRolePstmt.setInt(3, merchantId);
						insertRolePstmt.setString(4, channelType);
						insertRolePstmt.setString(5, tierCode);
						insertRolePstmt.setInt(6, mctId);
						insertRolePstmt.setInt(7, roleId);
						insertRolePstmt.executeUpdate();
					
					/*updateRolePstmt=con.prepareStatement("update st_pms_role_priv_mapping set status='NA' where priv_id in(select priv_id from st_pms_priviledge_rep where is_start='Y' and is_role_head_priv='N') and role_id=?");
					updateRolePstmt.setInt(1, roleId);
					
					updateRolePstmt.executeUpdate();
					
					updateRolePstmt=con.prepareStatement("update st_pms_role_priv_mapping role inner join (select priv_id from st_pms_role_priv_mapping where status='NA' and priv_id not in(select priv_id from st_pms_priviledge_rep where is_start='Y' and is_role_head_priv='N')) priv on role.priv_id=priv.priv_id set status='INACTIVE' where role_id=?");
					updateRolePstmt.setInt(1, roleId);
					
					updateRolePstmt.executeUpdate();*/
					//delete bb from st_pms_role_priv_mapping bb inner join (select priv_id from st_pms_role_priv_mapping where role_id=1 and priv_id not in (select distinct(priv_id) priv_id from st_pms_priviledge_rep where is_start='Y' and is_role_head_priv='Y')) aa on bb.priv_id=aa.priv_id
				}
				
				userPstmt=con.prepareStatement("select user_id,role_id,parent_user_id,is_role_head from st_sl_merchant_user_master where user_type=? and merchant_id=? and role_id=?");
				userPstmt.setString(1, tierCode);
				userPstmt.setInt(2, merchantId);
				userPstmt.setInt(3, roleId);
				
				
				userRs=userPstmt.executeQuery();
				while(userRs.next()){
					int userId=userRs.getInt("user_id");
					
					//int parentUserId=userRs.getInt("parent_user_id");
					boolean isRoleHead="Y".equals(userRs.getString("is_role_head"));
					
					if(isRoleHead){
						insertuserPrivPstmt=con.prepareStatement("insert into st_rm_user_action_mapping(user_id,rpm_id,status)select ? user_id,rpm_id,status from st_rm_role_action_mapping where mct_id=? and rpm_id not in (select rpm_id from st_rm_user_action_mapping where user_id=?)");
						insertuserPrivPstmt.setInt(1, userId);
						insertuserPrivPstmt.setInt(2, mctId);
						insertuserPrivPstmt.setInt(3, userId);
						
						insertuserPrivPstmt.executeUpdate();
						
						/*updateRolePstmt=con.prepareStatement("update st_pms_user_priv_mapping user inner join st_pms_role_priv_mapping role on user.priv_id=role.priv_id set user.status=role.status where user_id=? and role.role_id=?");
						updateRolePstmt.setInt(1, userId);
						updateRolePstmt.setInt(2, roleId);
						
						updateRolePstmt.executeUpdate();*/
						
					}else{
						insertuserPrivPstmt=con.prepareStatement("insert into st_rm_user_action_mapping(user_id,rpm_id,status) " +
								" select ? user_id,rpm_id,if(is_sub_user_priv='Y','INACTIVE','NA')status from st_rm_role_action_mapping aa inner join st_rm_merchant_priv_mapping bb inner join st_rm_priv_action_mapping cc on aa.action_id=cc.action_id and bb.priv_id=cc.priv_id  where mct_id=? and merchant_id=? and rpm_id not in (select rpm_id from st_rm_user_action_mapping where user_id=?)");
						insertuserPrivPstmt.setInt(1, userId);
						insertuserPrivPstmt.setInt(2, mctId);
						insertuserPrivPstmt.setInt(3, merchantId);
						insertuserPrivPstmt.setInt(4, userId);
						
						insertuserPrivPstmt.executeUpdate();
						
						/*updateRolePstmt=con.prepareStatement("update st_pms_user_priv_mapping set status='NA' where priv_id in(select priv_id from st_pms_priviledge_rep where is_start='Y' and is_sub_user_priv='N') and user_id=?");
						updateRolePstmt.setInt(1, userId);
						
						updateRolePstmt.executeUpdate();
						
						updateRolePstmt=con.prepareStatement("update st_pms_user_priv_mapping user inner join (select priv_id from st_pms_user_priv_mapping where status='NA' and priv_id not in(select priv_id from st_pms_priviledge_rep where is_start='Y' and is_sub_user_priv='N')) priv on user.priv_id=priv.priv_id set status='INACTIVE' where user_id=?");
						updateRolePstmt.setInt(1, userId);
						
						updateRolePstmt.executeUpdate();*/
						
					}
					
					
				}			
				insertSubmenuPstmt=con.prepareStatement("insert into st_rm_menu_master(merchant_id,action_id,parent_menu_id,item_order,status)select merchant_id,action_id,parent_menu_id,1,'ACTIVE' from st_rm_priv_rep aa inner join st_rm_priv_action_mapping bb inner join st_rm_merchant_priv_mapping cc on aa.priv_id=bb.priv_id and aa.priv_id=cc.priv_id where merchant_id=? and is_start='Y' and action_id not in (select action_id from st_rm_menu_master where merchant_id=? )");
				insertSubmenuPstmt.setInt(1, merchantId);
				insertSubmenuPstmt.setInt(2, merchantId);
				insertSubmenuPstmt.executeUpdate();	
			}
			
			con.commit();
			/*				
				updateSubMenuPstmt=con.prepareStatement("update st_pms_user_submenu_master submenu inner join st_pms_priviledge_rep priv on submenu.action_id=priv.action_id set submenu.sub_menu_name=priv.priv_disp_name");
				updateSubMenuPstmt.executeUpdate();
			*/		
		}catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(con);
		}
	}
	
	
	public static void main(String[] args) {
		
		/**
		 * truncate table st_rm_menu_master;
truncate table st_rm_role_action_mapping;
truncate table st_rm_user_action_mapping;
		 */
		
		//Connection con = DBConnect.getPropFileCon();
		try {
			new UpdateRoleTableControllerImpl().updateRoleTableWithPriviledge("127.0.0.1");
		} catch (GenericException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
