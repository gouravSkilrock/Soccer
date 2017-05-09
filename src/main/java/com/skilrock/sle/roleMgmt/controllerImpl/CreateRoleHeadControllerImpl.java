package com.skilrock.sle.roleMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.GenericException;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.roleMgmt.javaBeans.MenuDataBean;
import com.skilrock.sle.roleMgmt.javaBeans.MenuItemDataBean;
import com.skilrock.sle.roleMgmt.javaBeans.PrivilegeDataBean;
import com.skilrock.sle.roleMgmt.javaBeans.RoleHeadRegistrationBean;
import com.skilrock.sle.roleMgmt.javaBeans.RolePrivilegeBean;
import com.skilrock.sle.roleMgmt.javaBeans.SubUserRegistrationBean;

public class CreateRoleHeadControllerImpl {

	
	public void createRoleSave(RolePrivilegeBean rolePrivBean,int merchantId) throws GenericException {
		PreparedStatement insertRolePstmt=null;
		ResultSet roleRs=null;
		PreparedStatement interfacePstmt=null;
		ResultSet interfaceRs=null;
		PreparedStatement updateUserPrivPstmt=null;
		
		Connection con = null;
		try{
			con=DBConnect.getConnection();
			con.setAutoCommit(false);
			int tierId=0;
			int ownerUserId=0;
			Statement tierStmt=con.createStatement();
			ResultSet tierRs=tierStmt.executeQuery("select user_id,tier_id from st_sl_merchant_user_master aa inner join st_rm_tier_master bb on aa.user_type=bb.tier_code where merchant_user_id="+rolePrivBean.getCreatorUserId()+" and merchant_id="+merchantId);
				if(tierRs.next()){	
					tierId=tierRs.getInt("tier_id");
					ownerUserId=tierRs.getInt("user_id");
				}else{
					//need to through exception
				}
			
			insertRolePstmt=con.prepareStatement("insert into st_rm_role_master(merchant_id,merchant_role_id,role_name,role_description,owner_user_id,tier_id,is_master,status)values(?,?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
			insertRolePstmt.setInt(1, merchantId);
			insertRolePstmt.setInt(2, rolePrivBean.getRoleId());
			insertRolePstmt.setString(3, rolePrivBean.getRoleName());
			insertRolePstmt.setString(4, rolePrivBean.getRoleDescription());
			
			insertRolePstmt.setInt(5, ownerUserId);
			insertRolePstmt.setInt(6, tierId);
			insertRolePstmt.setString(7, "N");
			insertRolePstmt.setString(8, "ACTIVE");
			
			//logger.info("Insert Role Master:"+insertRolePstmt);
			insertRolePstmt.executeUpdate();
			
			roleRs=insertRolePstmt.getGeneratedKeys();
			if(roleRs.next()){
			int roleId=roleRs.getInt(1);
			
			
			interfacePstmt=con.prepareStatement("select mct_id,channel_type,tier_code,channel_name from st_rm_merchant_channel_mapping aa inner join st_rm_merchant_channel_tier_mapping bb inner join st_rm_tier_master cc on aa.merchant_channel_id=bb.merchant_channel_id and bb.tier_id=cc.tier_id where merchant_id="+merchantId+" and cc.tier_id='"+tierId+"' and aa.status='ACTIVE' and bb.status='ACTIVE' and cc.tier_status='ACTIVE'");
			interfaceRs=interfacePstmt.executeQuery();
			while(interfaceRs.next()){
				int mctId=interfaceRs.getInt("mct_id");
				String channelType=interfaceRs.getString("channel_type");
				String tierCode=interfaceRs.getString("tier_code");
				
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
					
					
			
			
			}
			
			List<PrivilegeDataBean> privBeanList=rolePrivBean.getPrivilegeList();
			
			updateUserPrivPstmt = con.prepareStatement("update st_rm_role_action_mapping aa inner join (select merchant_priv_id,action_id from st_rm_merchant_priv_mapping ll inner join st_rm_priv_action_mapping mm on ll.priv_id=mm.priv_id where merchant_priv_id=?) bb on aa.action_id=bb.action_id set aa.status='ACTIVE' where role_id=?");
			for(PrivilegeDataBean privBean : privBeanList){
				for(MenuDataBean menuBean :   privBean.getMenuList()){
					for(MenuItemDataBean menuItemBean : menuBean.getMenuItems()){
						if(menuItemBean.getIsAssign()){
							updateUserPrivPstmt.setInt(1, menuItemBean.getMenuItemId());
							updateUserPrivPstmt.setInt(2, roleId);
							updateUserPrivPstmt.executeUpdate();
						}
					}
				}
			}
			
			}
			
			con.commit();
			
		}catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(con);
		}
	}
	
	public void editRoleSave(RolePrivilegeBean rolePrivBean,int merchantId) throws GenericException {
		Statement rolePstmt=null;
		ResultSet roleRs=null;
		PreparedStatement updateUserPrivPstmt=null;
		Connection con = null;
		try{
			con=DBConnect.getConnection();
			con.setAutoCommit(false);
			int roleId=0;
			rolePstmt=con.createStatement();
			roleRs=rolePstmt.executeQuery("SELECT rm.role_id, user_id FROM st_rm_role_master rm INNER JOIN st_sl_merchant_user_master um ON rm.role_id=um.role_id WHERE merchant_role_id="+rolePrivBean.getRoleId()+" AND rm.merchant_id="+merchantId+";");
			if(roleRs.next()){	
				roleId=roleRs.getInt("role_id");
			}

			int userId = 0;
			String currentTime = Util.getCurrentTimeString();
			ResultSet histRs = rolePstmt.executeQuery("SELECT rm.role_id, user_id FROM st_rm_role_master rm INNER JOIN st_sl_merchant_user_master um ON rm.role_id=um.role_id WHERE merchant_role_id="+rolePrivBean.getRoleId()+" AND rm.merchant_id="+merchantId+";");
			while(histRs.next()) {
				userId = histRs.getInt("user_id");
				String query = "INSERT INTO st_rm_user_priv_history (user_id, rpm_id, action_id, STATUS, change_date, change_by, request_ip) SELECT "+userId+" user_id, rpm_id, pam.action_id, ram.status, '"+currentTime+"', "+rolePrivBean.getCreatorUserId()+", '"+rolePrivBean.getRequestIp()+"' FROM st_rm_role_action_mapping ram INNER JOIN st_rm_priv_action_mapping pam ON ram.action_id=pam.action_id INNER JOIN st_rm_merchant_priv_mapping mpm ON pam.priv_id=mpm.priv_id WHERE role_id="+roleId+" AND is_start='Y' AND merchant_id="+merchantId+";";
				con.createStatement().executeUpdate(query);
			}

			List<PrivilegeDataBean> privBeanList=rolePrivBean.getPrivilegeList();
			
			updateUserPrivPstmt = con.prepareStatement("update st_rm_role_action_mapping aa inner join (select merchant_priv_id,action_id from st_rm_merchant_priv_mapping ll inner join st_rm_priv_action_mapping mm on ll.priv_id=mm.priv_id where merchant_priv_id=?) bb on aa.action_id=bb.action_id set aa.status=? where role_id=?");
			for(PrivilegeDataBean privBean : privBeanList){
				for(MenuDataBean menuBean :   privBean.getMenuList()){
					for (MenuItemDataBean menuItemBean : menuBean
							.getMenuItems()) {
						updateUserPrivPstmt.setInt(1,
								menuItemBean.getMenuItemId());
						if (menuItemBean.getIsAssign()) {
							updateUserPrivPstmt.setString(2, "ACTIVE");
						} else {
							updateUserPrivPstmt.setString(2, "INACTIVE");
						}
						updateUserPrivPstmt.setInt(3, roleId);
						updateUserPrivPstmt.executeUpdate();
					}
			}
			
			}
			
			con.commit();
			
		}catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(con);
		}
	}
	
	public void createRoleHeadUserSave(RoleHeadRegistrationBean registrationBean,int merchantId) throws GenericException {
		PreparedStatement insertRolePstmt=null;
		ResultSet roleRs=null;
		
		Connection con = null;
		try{
			con=DBConnect.getConnection();
			con.setAutoCommit(false);
			int parentUserId=0;
			Statement userStmt=con.createStatement();
			ResultSet userRs=userStmt.executeQuery("select user_id from st_sl_merchant_user_master where merchant_user_id="+registrationBean.getCreatorUserId()+" and merchant_id="+merchantId);
				if(userRs.next()){	
					parentUserId=userRs.getInt("user_id");
				}else{
					//need to through exception
				}
				int roleId=0;
				Statement rolePstmt=con.createStatement();
				 roleRs=rolePstmt.executeQuery("select role_id from st_rm_role_master where merchant_role_id="+registrationBean.getRoleId()+" and merchant_id="+merchantId);
					if(roleRs.next()){	
						roleId=roleRs.getInt("role_id");
					}else{
						//need to through exception
					}
			insertRolePstmt=con.prepareStatement("insert into st_sl_merchant_user_master(parent_user_id,role_id,merchant_id,merchant_user_id,merchant_user_mapping_id, user_name,user_type,is_role_head,first_name,last_name,mobile_nbr,email_id)values(?,?,?,?,?,?,?,?,?,?,?,?);",PreparedStatement.RETURN_GENERATED_KEYS);
			insertRolePstmt.setInt(1, parentUserId);
			insertRolePstmt.setInt(2, roleId);
			insertRolePstmt.setInt(3, merchantId);
			insertRolePstmt.setInt(4, registrationBean.getUserId());
			insertRolePstmt.setInt(5, registrationBean.getUserMappingId());
			insertRolePstmt.setString(6, registrationBean.getUserName());
			insertRolePstmt.setString(7, registrationBean.getUserType());
			insertRolePstmt.setString(8, "Y");
			insertRolePstmt.setString(9, registrationBean.getFirstName());
			insertRolePstmt.setString(10, registrationBean.getLastName());
			insertRolePstmt.setString(11, registrationBean.getMobileNbr());
			insertRolePstmt.setString(12, registrationBean.getEmailId());
			
			//logger.info("Insert Role Master:"+insertRolePstmt);
			insertRolePstmt.executeUpdate();
			roleRs=insertRolePstmt.getGeneratedKeys();
			if(roleRs.next()){
				int userId=roleRs.getInt(1);
				PreparedStatement pStmt = con.prepareStatement("INSERT INTO st_sl_merchant_user_authentication_mapping(user_id, merchant_user_id, merchant_id) VALUES(?, ?, ?)");
				pStmt.setLong(1, userId);
				pStmt.setInt(2, registrationBean.getUserId());
				pStmt.setInt(3, merchantId);
				int isUpdated = pStmt.executeUpdate();

				if(isUpdated == 0)
					throw new GenericException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);

				String currentTime = Util.getCurrentTimeString();
				String insertPrivQuery="insert into st_rm_user_action_mapping (user_id, rpm_id, status, change_date, change_by, request_ip) select user_id,rpm_id,aa.status, '"+currentTime+"', "+registrationBean.getCreatorUserId()+", '"+registrationBean.getRequestIp()+"' from st_rm_role_action_mapping aa inner join st_sl_merchant_user_master bb inner join st_rm_role_master cc on aa.role_id=bb.role_id and bb.role_id=cc.role_id and bb.merchant_id=cc.merchant_id where user_id="+userId;
				Statement stmt=con.createStatement();
				stmt.executeUpdate(insertPrivQuery);
			
			}
			con.commit();
			
		}catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(con);
		}
	}
	
	public void createSubUserSave(SubUserRegistrationBean registrationBean,int merchantId) throws GenericException {
		PreparedStatement insertRolePstmt=null;
		ResultSet roleRs=null;
		
		Connection con = null;
		try{
			con=DBConnect.getConnection();
			con.setAutoCommit(false);
			int parentUserId=0;
			int roleId=0;
			Statement userStmt=con.createStatement();
			ResultSet userRs=userStmt.executeQuery("select user_id,role_id from st_sl_merchant_user_master where merchant_user_id="+registrationBean.getCreatorUserId()+" and merchant_id="+merchantId);
				if(userRs.next()){	
					parentUserId=userRs.getInt("user_id");
					roleId=userRs.getInt("role_id");
				}else{
					//need to through exception
				}
				
			insertRolePstmt=con.prepareStatement("insert into st_sl_merchant_user_master(parent_user_id,role_id,merchant_id,merchant_user_id,merchant_user_mapping_id,user_name,user_type,is_role_head,first_name,last_name,mobile_nbr,email_id)values(?,?,?,?,?,?,?,?,?,?,?,?);",PreparedStatement.RETURN_GENERATED_KEYS);
			insertRolePstmt.setInt(1, parentUserId);
			insertRolePstmt.setInt(2, roleId);
			insertRolePstmt.setInt(3, merchantId);
			insertRolePstmt.setInt(4, registrationBean.getUserId());
			insertRolePstmt.setInt(5, registrationBean.getUserMappingId());
			insertRolePstmt.setString(6, registrationBean.getUserName());
			insertRolePstmt.setString(7, registrationBean.getUserType());
			insertRolePstmt.setString(8, "N");
			insertRolePstmt.setString(9, registrationBean.getFirstName());
			insertRolePstmt.setString(10, registrationBean.getLastName());
			insertRolePstmt.setString(11, registrationBean.getMobileNbr());
			insertRolePstmt.setString(12, registrationBean.getEmailId());
			
			//logger.info("Insert Role Master:"+insertRolePstmt);
			insertRolePstmt.executeUpdate();
			roleRs=insertRolePstmt.getGeneratedKeys();
			if(roleRs.next()){
				int userId=roleRs.getInt(1);			
				PreparedStatement pStmt = con.prepareStatement("INSERT INTO st_sl_merchant_user_authentication_mapping(user_id, merchant_user_id, merchant_id) VALUES(?, ?, ?)");
				pStmt.setLong(1, userId);
				pStmt.setInt(2, registrationBean.getUserId());
				pStmt.setInt(3, merchantId);
				int isUpdated = pStmt.executeUpdate();

				if(isUpdated == 0)
					throw new GenericException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
			
				
				List<PrivilegeDataBean> privBeanList=registrationBean.getPrivilegeList();

				String currentTime = Util.getCurrentTimeString();
				PreparedStatement updateUserPrivPstmt = con.prepareStatement("insert into st_rm_user_action_mapping (user_id, rpm_id, status, change_date, change_by, request_ip) select ?,rpm_id,? status, '"+currentTime+"', "+registrationBean.getCreatorUserId()+", '"+registrationBean.getRequestIp()+"' from st_rm_merchant_priv_mapping ll inner join st_rm_priv_action_mapping mm inner join st_rm_role_action_mapping nn on ll.priv_id=mm.priv_id and mm.action_id=nn.action_id where merchant_priv_id=? and role_id=?");
				for(PrivilegeDataBean privBean : privBeanList){
					for(MenuDataBean menuBean :   privBean.getMenuList()){
						for (MenuItemDataBean menuItemBean : menuBean.getMenuItems()) {
							updateUserPrivPstmt.setInt(1,userId);
							if (menuItemBean.getIsAssign()) {
								updateUserPrivPstmt.setString(2, "ACTIVE");
							} else {
								updateUserPrivPstmt.setString(2, "INACTIVE");
							}
							updateUserPrivPstmt.setInt(3,menuItemBean.getMenuItemId());
							updateUserPrivPstmt.setInt(4, roleId);
							updateUserPrivPstmt.executeUpdate();
						}
					}			
				}
			}			
			con.commit();			
		}catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(con);
		}
	}
	
	
	public void editSubUser(SubUserRegistrationBean registrationBean, int merchantId) throws GenericException {
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement updateUserPrivPstmt = null;
		Connection con = null;

		int userId = 0;
		int roleId = 0;
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			stmt = con.createStatement();
			rs = stmt.executeQuery("select user_id,role_id from st_sl_merchant_user_master where merchant_user_id="+registrationBean.getUserId()+" and merchant_id="+merchantId);
			if(rs.next()) {	
				userId = rs.getInt("user_id");
				roleId = rs.getInt("role_id");
			}

			String query = "INSERT INTO st_rm_user_priv_history (user_id, rpm_id, action_id, status, change_date, change_by, request_ip) SELECT "+userId+" user_id, aa.rpm_id, action_id, IF(aa.status='INACTIVE',aa.status,bb.status) status, change_date, change_by, request_ip FROM st_rm_role_action_mapping aa INNER JOIN st_rm_user_action_mapping bb INNER JOIN st_sl_merchant_user_master cc ON aa.rpm_id=bb.rpm_id AND bb.user_id=cc.user_id WHERE aa.status!='NA' AND bb.status!='NA' AND cc.merchant_user_id="+registrationBean.getUserId()+";";
			stmt.executeUpdate(query);

			String currentTime = Util.getCurrentTimeString();
			List<PrivilegeDataBean> privBeanList=registrationBean.getPrivilegeList();
			updateUserPrivPstmt = con.prepareStatement("update st_rm_user_action_mapping aa inner join (select rpm_id from st_rm_merchant_priv_mapping ll inner join st_rm_priv_action_mapping mm inner join st_rm_role_action_mapping nn on ll.priv_id=mm.priv_id and mm.action_id=nn.action_id where merchant_priv_id=? and role_id=?)bb on aa.rpm_id=bb.rpm_id set aa.status=?, change_date='"+currentTime+"', change_by="+registrationBean.getCreatorUserId()+", request_ip='"+registrationBean.getRequestIp()+"' where aa.user_id=?");
			for(PrivilegeDataBean privBean : privBeanList){
				for(MenuDataBean menuBean :   privBean.getMenuList()){
					for (MenuItemDataBean menuItemBean : menuBean
							.getMenuItems()) {
						updateUserPrivPstmt.setInt(1,
								menuItemBean.getMenuItemId());
						
						updateUserPrivPstmt.setInt(2, roleId);
						if (menuItemBean.getIsAssign()) {
							updateUserPrivPstmt.setString(3, "ACTIVE");
						} else {
							updateUserPrivPstmt.setString(3, "INACTIVE");
						}
						updateUserPrivPstmt.setInt(4,userId);
						updateUserPrivPstmt.executeUpdate();
					}
			}
			
			}
			
			con.commit();
			
		}catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(con);
		}
	}
}