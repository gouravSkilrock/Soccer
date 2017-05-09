package com.skilrock.sle.roleMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.GenericException;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.roleMgmt.javaBeans.MenuDataBean;
import com.skilrock.sle.roleMgmt.javaBeans.MenuItemActionDataBean;
import com.skilrock.sle.roleMgmt.javaBeans.MenuItemDataBean;
import com.skilrock.sle.roleMgmt.javaBeans.PriviledgeModificationDataBean;
import com.skilrock.sle.roleMgmt.javaBeans.PriviledgeModificationHeaderBean;
import com.skilrock.sle.roleMgmt.javaBeans.PriviledgeModificationMasterBean;
import com.skilrock.sle.roleMgmt.javaBeans.PrivilegeDataBean;
import com.skilrock.sle.roleMgmt.javaBeans.RolePrivilegeBean;

public class GetPriviledgeControllerImpl {
	private static final SLELogger logger = SLELogger.getLogger(GetPriviledgeControllerImpl.class.getName());
	public List<PrivilegeDataBean> getRolePrivilledge(int merchantId,int roleId) throws GenericException{
		
		PreparedStatement interfacePstmt=null;
		ResultSet interfaceRs=null;
		Statement menuStmt=null;
		ResultSet menuRs=null;
		
		Statement subMenuStmt=null;
		ResultSet subMenuRs=null;
		
		PrivilegeDataBean privilegeBean = null;
		List<PrivilegeDataBean> privilegeList = null;
		MenuDataBean menuBean = null;
		List<MenuDataBean> menuList=null;
		MenuItemDataBean menuItemBean = null;
		List<MenuItemDataBean> menuItemList = null;
		
		MenuItemActionDataBean actionDataBean = null;
		List<MenuItemActionDataBean> actionList = null;
		Connection con=DBConnect.getConnection();
		try{
			String tierCode =null;
			Statement tierStmt=con.createStatement();
			ResultSet tierRs=tierStmt.executeQuery("select tier_code from st_rm_tier_master aa inner join st_rm_role_master bb on aa.tier_id=bb.tier_id where merchant_id="+merchantId+" and merchant_role_id="+roleId);
				if(tierRs.next()){	
					tierCode=tierRs.getString("tier_code");
				}else{
					//need to through exception
				}
				
			
			interfacePstmt=con.prepareStatement("select mct_id,channel_type,channel_name from st_rm_merchant_channel_mapping aa inner join st_rm_merchant_channel_tier_mapping bb inner join st_rm_tier_master cc on aa.merchant_channel_id=bb.merchant_channel_id and bb.tier_id=cc.tier_id where merchant_id="+merchantId+" and tier_code='"+tierCode+"' and aa.status='ACTIVE' and bb.status='ACTIVE' and cc.tier_status='ACTIVE'");
			interfaceRs=interfacePstmt.executeQuery();
			privilegeList = new ArrayList<PrivilegeDataBean>();
			while(interfaceRs.next()){
				privilegeBean = new PrivilegeDataBean();
				
				int mctId=interfaceRs.getInt("mct_id");
				
				String channelType=interfaceRs.getString("channel_type");
				String channelName=interfaceRs.getString("channel_name");
				
				privilegeBean.setInterfaceDispName(channelName);
	            privilegeBean.setInterfaceDevName(channelType);
	            
	            
	            String menuStmtQuery="select menu_id,menu_disp_name from st_rm_menu_master where merchant_id ="+merchantId+" and parent_menu_id =0";
	            menuStmt=con.createStatement();
	            menuRs=menuStmt.executeQuery(menuStmtQuery);
	            menuList = new ArrayList<MenuDataBean>();
	            
	            privilegeBean.setMenuList(menuList);
	            privilegeList.add(privilegeBean);
	            while(menuRs.next()){
	            	int menuId=menuRs.getInt("menu_id");
	            	String menuDispName=menuRs.getString("menu_disp_name");
	            	
	            	menuBean = new MenuDataBean();
	            	menuBean.setMenuId(menuId);
	                menuBean.setMenuName(menuDispName);
	                
	                String subMenuStmtQuery="select merchant_priv_id,action_mapping,action_disp_name,priv_disp_name,ll.status,ll.action_id from (select action_id,bb.status from st_rm_role_master aa " +
	                				" inner join st_rm_role_action_mapping bb on aa.role_id=bb.role_id where aa.status='ACTIVE' and merchant_role_id="+roleId+" and mct_id="+mctId+" and bb.status != 'NA')ll inner join ( " +
	                						" select priv_dev_name,action_path,action_mapping,action_disp_name,action_id,parent_menu_id,merchant_priv_id,priv_disp_name from st_rm_priv_rep aa inner join  " +
	                						" st_rm_priv_action_mapping bb inner join st_rm_merchant_priv_mapping cc on aa.priv_id=bb.priv_id and aa.priv_id=cc.priv_id where merchant_id ="+merchantId+" and aa.status='ACTIVE' and bb.status='ACTIVE' and cc.status='ACTIVE' and parent_menu_id="+menuId+")mm on ll.action_id=mm.action_id  order by merchant_priv_id";
		            subMenuStmt=con.createStatement();
		            subMenuRs=subMenuStmt.executeQuery(subMenuStmtQuery);
		            
	                menuItemList = new ArrayList<MenuItemDataBean>();
	                List<Integer> privIdList=new ArrayList<Integer>();
	                menuBean.setMenuItems(menuItemList);
	                if(subMenuRs.next()){
	                	menuList.add(menuBean);
	                }
	                subMenuRs.first();
	                while(subMenuRs.next()){	                	
		               
	                	 Integer privId=subMenuRs.getInt("merchant_priv_id");
	                	 
	                	 if(privIdList.contains(privId)){
	                		 menuItemBean.setIsAssign("ACTIVE".equals(subMenuRs.getString("status")));
	                	 }else{
	                		 privIdList = new ArrayList<Integer>();
	                		 privIdList.add(privId);
	                		 actionList = new ArrayList<MenuItemActionDataBean>();
	                		 menuItemBean = new MenuItemDataBean();
	                		 menuItemBean.setMenuItemId(privId);
	                		 menuItemBean.setMenuItemName(subMenuRs.getString("priv_disp_name"));
	                		 menuItemBean.setIsAssign("ACTIVE".equals(subMenuRs.getString("status")));
	                		 menuItemBean.setActionItems(actionList);
		                     menuItemList.add(menuItemBean);
		                     
		                     
	                	 }
	                	 actionDataBean = new MenuItemActionDataBean();
	                	 actionDataBean.setActionId(subMenuRs.getInt("action_id"));
	                	 actionDataBean.setActionName(subMenuRs.getString("action_disp_name"));
	                	 actionDataBean.setIsAssign("ACTIVE".equals(subMenuRs.getString("status")));
	                	 
	                	 actionList.add(actionDataBean);
	                }
	                
	            }
	            
			}
		
		
		
		
		/*JsonArray data = new JsonParser().parse(privilegeJson).getAsJsonArray();
        for(JsonElement interfaceData : data) {
            privilegeData = interfaceData.getAsJsonObject();
            privilegeBean = new PrivilegeDataBean();
            privilegeBean.setInterfaceDispName(privilegeData.get("interfaceDispName").getAsString());
            privilegeBean.setInterfaceDevName(privilegeData.get("interfaceDevName").getAsString());
            menuList = new ArrayList<MenuDataBean>();
            privilegeBean.setMenuList(menuList);
            menuArray = privilegeData.getAsJsonArray("menuData");
            for(JsonElement menuData : menuArray) {
                menu = menuData.getAsJsonObject();
                menuBean = new MenuDataBean();
                menuBean.setMenuId(menu.get("menuId").getAsInt());
                menuBean.setMenuName(menu.get("menuName").getAsString());
                menuItemList = new ArrayList<MenuItemDataBean>();
                menuBean.setMenuItems(menuItemList);
                menuItemArray = menu.getAsJsonArray("menuItemData");
                for(JsonElement menuItemData : menuItemArray) {
                    menuItem = menuItemData.getAsJsonObject();
                    menuItemBean = new MenuItemDataBean();
                    menuItemBean.setMenuItemId(menuItem.get("menuItemId").getAsInt());
                    menuItemBean.setMenuItemName(menuItem.get("menuItemName").getAsString());
                    menuItemBean.setAssign(menuItem.get("isAssign").getAsBoolean());
                    menuItemList.add(menuItemBean);
                }
                menuList.add(menuBean);
            }
            privilegeList.add(privilegeBean);
        }*/
			/*Gson gson = new Gson();
			String json = gson.toJson(privilegeList);
			System.out.println("LMS Response data {}"+json);*/
		}catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(con);
		}
		return privilegeList;
	}
        
	
	
	
	public List<PrivilegeDataBean> getSubUserPrivilledge(int merchantId,int userId) throws GenericException{
		
		PreparedStatement interfacePstmt=null;
		ResultSet interfaceRs=null;
		Statement menuStmt=null;
		ResultSet menuRs=null;
		
		Statement subMenuStmt=null;
		ResultSet subMenuRs=null;
		
		PrivilegeDataBean privilegeBean = null;
		List<PrivilegeDataBean> privilegeList = null;
		MenuDataBean menuBean = null;
		List<MenuDataBean> menuList=null;
		MenuItemDataBean menuItemBean = null;
		List<MenuItemDataBean> menuItemList = null;
		
		MenuItemActionDataBean actionDataBean = null;
		List<MenuItemActionDataBean> actionList = null;
		Connection con=DBConnect.getConnection();
		try{
			String tierCode =null;
			Statement tierStmt=con.createStatement();
			ResultSet tierRs=tierStmt.executeQuery("select user_type from st_sl_merchant_user_master where merchant_id="+merchantId+" and merchant_user_id="+userId);
				if(tierRs.next()){	
					tierCode=tierRs.getString("user_type");
				}else{
					//need to through exception
				}
				
			interfacePstmt=con.prepareStatement("select mct_id,channel_type,channel_name from st_rm_merchant_channel_mapping aa inner join st_rm_merchant_channel_tier_mapping bb inner join st_rm_tier_master cc on aa.merchant_channel_id=bb.merchant_channel_id and bb.tier_id=cc.tier_id where merchant_id="+merchantId+" and tier_code='"+tierCode+"' and aa.status='ACTIVE' and bb.status='ACTIVE' and cc.tier_status='ACTIVE'");
			interfaceRs=interfacePstmt.executeQuery();
			privilegeList = new ArrayList<PrivilegeDataBean>();
			while(interfaceRs.next()){
				privilegeBean = new PrivilegeDataBean();
				
				int mctId=interfaceRs.getInt("mct_id");
				
				String channelType=interfaceRs.getString("channel_type");
				String channelName=interfaceRs.getString("channel_name");
				
				privilegeBean.setInterfaceDispName(channelName);
	            privilegeBean.setInterfaceDevName(channelType);
	            
	            
	            String menuStmtQuery="select menu_id,menu_disp_name from st_rm_menu_master where merchant_id ="+merchantId+" and parent_menu_id =0";
	            menuStmt=con.createStatement();
	            menuRs=menuStmt.executeQuery(menuStmtQuery);
	            menuList = new ArrayList<MenuDataBean>();
	            
	            privilegeBean.setMenuList(menuList);
	            privilegeList.add(privilegeBean);
	            while(menuRs.next()){
	            	int menuId=menuRs.getInt("menu_id");
	            	String menuDispName=menuRs.getString("menu_disp_name");
	            	
	            	menuBean = new MenuDataBean();
	            	menuBean.setMenuId(menuId);
	                menuBean.setMenuName(menuDispName);
	                
	                String subMenuStmtQuery="select merchant_priv_id,action_mapping,action_disp_name,priv_disp_name,ll.status,ll.action_id from (select action_id,if(aa.status='INACTIVE',aa.status,bb.status)status from st_rm_role_action_mapping aa  inner join st_rm_user_action_mapping bb  inner join st_sl_merchant_user_master cc on aa.rpm_id=bb.rpm_id and bb.user_id=cc.user_id where aa.status != 'NA'  and mct_id="+mctId+"  and bb.status != 'NA' and cc.merchant_user_id="+userId+")ll inner join (  select priv_dev_name,action_path,action_mapping,action_disp_name,action_id,parent_menu_id,merchant_priv_id,priv_disp_name from st_rm_priv_rep aa inner join   st_rm_priv_action_mapping bb inner join st_rm_merchant_priv_mapping cc on aa.priv_id=bb.priv_id and aa.priv_id=cc.priv_id where merchant_id ="+merchantId+" and aa.status='ACTIVE' and bb.status='ACTIVE' and cc.status='ACTIVE' and parent_menu_id="+menuId+")mm on ll.action_id=mm.action_id  order by merchant_priv_id";
		            subMenuStmt=con.createStatement();
		            subMenuRs=subMenuStmt.executeQuery(subMenuStmtQuery);
		            
	                menuItemList = new ArrayList<MenuItemDataBean>();
	                List<Integer> privIdList=new ArrayList<Integer>();
	                menuBean.setMenuItems(menuItemList);
	                if(subMenuRs.next()){
	                	menuList.add(menuBean);
	                }
	                subMenuRs.beforeFirst();
	                while(subMenuRs.next()){
	                
	                	 Integer privId=subMenuRs.getInt("merchant_priv_id");
	                	 
	                	 if(privIdList.contains(privId)){
	                		 menuItemBean.setIsAssign("ACTIVE".equals(subMenuRs.getString("status")));
	                	 }else{
	                		 privIdList = new ArrayList<Integer>();
	                		 privIdList.add(privId);
	                		 actionList = new ArrayList<MenuItemActionDataBean>();
	                		 menuItemBean = new MenuItemDataBean();
	                		 menuItemBean.setMenuItemId(subMenuRs.getInt("merchant_priv_id"));
	                		 menuItemBean.setMenuItemName(subMenuRs.getString("priv_disp_name"));
	                		 menuItemBean.setIsAssign("ACTIVE".equals(subMenuRs.getString("status")));
	                		 menuItemBean.setActionItems(actionList);
		                     menuItemList.add(menuItemBean);
		                     
		                     
	                	 }
	                	 actionDataBean = new MenuItemActionDataBean();
	                	 actionDataBean.setActionId(subMenuRs.getInt("action_id"));
	                	 actionDataBean.setActionName(subMenuRs.getString("action_disp_name"));
	                	 actionDataBean.setIsAssign("ACTIVE".equals(subMenuRs.getString("status")));
	                	 
	                	 actionList.add(actionDataBean);
	                }
	                
	            }
	            
			}
		
		
		
		
		/*JsonArray data = new JsonParser().parse(privilegeJson).getAsJsonArray();
        for(JsonElement interfaceData : data) {
            privilegeData = interfaceData.getAsJsonObject();
            privilegeBean = new PrivilegeDataBean();
            privilegeBean.setInterfaceDispName(privilegeData.get("interfaceDispName").getAsString());
            privilegeBean.setInterfaceDevName(privilegeData.get("interfaceDevName").getAsString());
            menuList = new ArrayList<MenuDataBean>();
            privilegeBean.setMenuList(menuList);
            menuArray = privilegeData.getAsJsonArray("menuData");
            for(JsonElement menuData : menuArray) {
                menu = menuData.getAsJsonObject();
                menuBean = new MenuDataBean();
                menuBean.setMenuId(menu.get("menuId").getAsInt());
                menuBean.setMenuName(menu.get("menuName").getAsString());
                menuItemList = new ArrayList<MenuItemDataBean>();
                menuBean.setMenuItems(menuItemList);
                menuItemArray = menu.getAsJsonArray("menuItemData");
                for(JsonElement menuItemData : menuItemArray) {
                    menuItem = menuItemData.getAsJsonObject();
                    menuItemBean = new MenuItemDataBean();
                    menuItemBean.setMenuItemId(menuItem.get("menuItemId").getAsInt());
                    menuItemBean.setMenuItemName(menuItem.get("menuItemName").getAsString());
                    menuItemBean.setAssign(menuItem.get("isAssign").getAsBoolean());
                    menuItemList.add(menuItemBean);
                }
                menuList.add(menuBean);
            }
            privilegeList.add(privilegeBean);
        }*/
			Gson gson = new Gson();
			String json = gson.toJson(privilegeList);
			System.out.println("LMS Response data {}"+json);
		}catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(con);
		}
		return privilegeList;
	}
	
	
	public List<PrivilegeDataBean> getRetailerPrivilege(int userId,String merCode) throws GenericException{
		
		PreparedStatement interfacePstmt=null;
		ResultSet interfaceRs=null;
		
		Statement subMenuStmt=null;
		ResultSet subMenuRs=null;
		
		PrivilegeDataBean privilegeBean = null;
		List<PrivilegeDataBean> privilegeList = null;
		MenuDataBean menuBean = null;
		List<MenuDataBean> menuList=null;
		MenuItemDataBean menuItemBean = null;
		List<MenuItemDataBean> menuItemList = null;
		
		List<MenuItemActionDataBean> actionList = null;
		Connection con=DBConnect.getConnection();
		int merchantId=Util.merchantInfoMap.get(merCode).getMerchantId();
		try{
			String tierCode =null;
			Statement tierStmt=con.createStatement();
			ResultSet tierRs=tierStmt.executeQuery("select user_type from st_sl_merchant_user_master where merchant_id="+merchantId+" and merchant_user_id="+userId);
			if(tierRs.next()){	
				tierCode=tierRs.getString("user_type");
			}
				
			interfacePstmt=con.prepareStatement("select mct_id,channel_type,channel_name from st_rm_merchant_channel_mapping aa inner join st_rm_merchant_channel_tier_mapping bb inner join st_rm_tier_master cc on aa.merchant_channel_id=bb.merchant_channel_id and bb.tier_id=cc.tier_id where merchant_id="+merchantId+" and tier_code='"+tierCode+"' and aa.status='ACTIVE' and bb.status='ACTIVE' and cc.tier_status='ACTIVE' and channel_name='Terminal'");
			interfaceRs=interfacePstmt.executeQuery();
			privilegeList = new ArrayList<PrivilegeDataBean>();
			while(interfaceRs.next()){
				privilegeBean = new PrivilegeDataBean();
				
				int mctId=interfaceRs.getInt("mct_id");
				
				String channelType=interfaceRs.getString("channel_type");
				String channelName=interfaceRs.getString("channel_name");
				
				privilegeBean.setInterfaceDispName(channelName);
	            privilegeBean.setInterfaceDevName(channelType);
	            

	            menuList = new ArrayList<MenuDataBean>();
	            
	            privilegeBean.setMenuList(menuList);
	            privilegeList.add(privilegeBean);
	            	
            	menuBean = new MenuDataBean();
            	menuBean.setMenuId(1);
                menuBean.setMenuName("All");
                
                String subMenuStmtQuery="select merchant_priv_id,priv_disp_name ,uam.status from st_rm_user_action_mapping uam INNER JOIN  st_rm_role_action_mapping ram on  ram.rpm_id=uam.rpm_id  INNER JOIN st_rm_priv_action_mapping pam on pam.action_id=ram.action_id INNER JOIN st_rm_merchant_priv_mapping mpm on pam.priv_id=mpm.priv_id INNER JOIN st_rm_priv_rep rpr on rpr.priv_id=pam.priv_id INNER JOIN st_sl_merchant_user_master mum on uam.user_id=mum.user_id where merchant_user_id="+userId+" and mct_id="+mctId+" and  rpr.status='ACTIVE' and mpm.status='ACTIVE' and mum.merchant_id="+merchantId;
	            subMenuStmt=con.createStatement();
	            subMenuRs=subMenuStmt.executeQuery(subMenuStmtQuery);
	            
                menuItemList = new ArrayList<MenuItemDataBean>();
                List<Integer> privIdList=new ArrayList<Integer>();
                menuBean.setMenuItems(menuItemList);
                if(subMenuRs.next()){
                	menuList.add(menuBean);
                }
                subMenuRs.beforeFirst();
                while(subMenuRs.next()){
                
                	 Integer privId=subMenuRs.getInt("merchant_priv_id");
                	 
                	 if(privIdList.contains(privId)){
                		 menuItemBean.setIsAssign("ACTIVE".equals(subMenuRs.getString("status")));
                	 }else{
                		 privIdList = new ArrayList<Integer>();
                		 privIdList.add(privId);
                		 actionList = new ArrayList<MenuItemActionDataBean>();
                		 menuItemBean = new MenuItemDataBean();
                		 menuItemBean.setMenuItemId(subMenuRs.getInt("merchant_priv_id"));
                		 menuItemBean.setMenuItemName(subMenuRs.getString("priv_disp_name"));
                		 menuItemBean.setIsAssign("ACTIVE".equals(subMenuRs.getString("status")));
                		 menuItemBean.setActionItems(actionList);
	                     menuItemList.add(menuItemBean);
	                     
	                     
                	 }
                }
	                
	         }
		
			Gson gson = new Gson();
			String json = gson.toJson(privilegeList);
			logger.info("LMS Response get retailer priv data {}"+json);
		}catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(con);
		}
		return privilegeList;
	}
	
	
	public boolean updateRetailerPrivilege(RolePrivilegeBean retailerRolePrivBean) throws GenericException {
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement updateUserPrivPstmt = null;
		Connection con = null;
		boolean isSuccess=false;

		int userId = 0;
		int roleId = 0;
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			stmt = con.createStatement();
			rs = stmt.executeQuery("select user_id,role_id from st_sl_merchant_user_master where merchant_user_id="+retailerRolePrivBean.getUserId()+" and merchant_id="+Util.merchantInfoMap.get("RMS").getMerchantId());
			if(rs.next()) {	
				userId = rs.getInt("user_id");
				roleId = rs.getInt("role_id");
			}

			String query = "INSERT INTO st_rm_user_priv_history (user_id, rpm_id, action_id, status, change_date, change_by, request_ip) SELECT "+userId+" user_id, aa.rpm_id, action_id, IF(aa.status='INACTIVE',aa.status,bb.status) status, change_date, change_by, request_ip FROM st_rm_role_action_mapping aa INNER JOIN st_rm_user_action_mapping bb INNER JOIN st_sl_merchant_user_master cc ON aa.rpm_id=bb.rpm_id AND bb.user_id=cc.user_id WHERE aa.status!='NA' AND bb.status!='NA' AND cc.merchant_user_id="+retailerRolePrivBean.getUserId()+";";
			stmt.executeUpdate(query);

			String currentTime = Util.getCurrentTimeString();
			List<PrivilegeDataBean> privBeanList=retailerRolePrivBean.getPrivilegeList();
			updateUserPrivPstmt = con.prepareStatement("update st_rm_user_action_mapping aa inner join (select rpm_id from st_rm_merchant_priv_mapping ll inner join st_rm_priv_action_mapping mm inner join st_rm_role_action_mapping nn on ll.priv_id=mm.priv_id and mm.action_id=nn.action_id where merchant_priv_id=? and role_id=?)bb on aa.rpm_id=bb.rpm_id set aa.status=?, change_date='"+currentTime+"', change_by="+retailerRolePrivBean.getCreatorUserId()+", request_ip='"+retailerRolePrivBean.getRequestIp()+"' where aa.user_id=?");
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
			isSuccess=true;
			
		}catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(con);
		}
		return isSuccess;
	}
	
	
	
	
	public List<PrivilegeDataBean> getCreateUserPriviledge(int merchantId,int userId,boolean isCreateRole) throws GenericException{
		
		PreparedStatement interfacePstmt=null;
		ResultSet interfaceRs=null;
		Statement menuStmt=null;
		ResultSet menuRs=null;
		
		Statement subMenuStmt=null;
		ResultSet subMenuRs=null;
		
		PrivilegeDataBean privilegeBean = null;
		List<PrivilegeDataBean> privilegeList = null;
		MenuDataBean menuBean = null;
		List<MenuDataBean> menuList=null;
		MenuItemDataBean menuItemBean = null;
		List<MenuItemDataBean> menuItemList = null;
		
		MenuItemActionDataBean actionDataBean = null;
		List<MenuItemActionDataBean> actionList = null;
		Connection con=DBConnect.getConnection();
		try{
			String tierCode =null;
			Statement tierStmt=con.createStatement();
			ResultSet tierRs=tierStmt.executeQuery("select user_type from st_sl_merchant_user_master where merchant_id="+merchantId+" and merchant_user_id="+userId);
				if(tierRs.next()){	
					tierCode=tierRs.getString("user_type");
				}else{
					//need to through exception
				}
			
			interfacePstmt=con.prepareStatement("select mct_id,channel_type,channel_name from st_rm_merchant_channel_mapping aa inner join st_rm_merchant_channel_tier_mapping bb inner join st_rm_tier_master cc on aa.merchant_channel_id=bb.merchant_channel_id and bb.tier_id=cc.tier_id where merchant_id="+merchantId+" and tier_code='"+tierCode+"' and aa.status='ACTIVE' and bb.status='ACTIVE' and cc.tier_status='ACTIVE'");
			interfaceRs=interfacePstmt.executeQuery();
			privilegeList = new ArrayList<PrivilegeDataBean>();
			while(interfaceRs.next()){
				privilegeBean = new PrivilegeDataBean();
				
				int mctId=interfaceRs.getInt("mct_id");
				
				String channelType=interfaceRs.getString("channel_type");
				String channelName=interfaceRs.getString("channel_name");
				
				privilegeBean.setInterfaceDispName(channelName);
	            privilegeBean.setInterfaceDevName(channelType);
	            
	            
	            
	            String menuStmtQuery="select menu_id,menu_disp_name from st_rm_menu_master where merchant_id ="+merchantId+" and parent_menu_id =0";
	            menuStmt=con.createStatement();
	            menuRs=menuStmt.executeQuery(menuStmtQuery);
	            menuList = new ArrayList<MenuDataBean>();
	            
	            privilegeBean.setMenuList(menuList);
	            privilegeList.add(privilegeBean);
	            while(menuRs.next()){
	            	int menuId=menuRs.getInt("menu_id");
	            	String menuDispName=menuRs.getString("menu_disp_name");
	            	menuBean = new MenuDataBean();
	            	menuBean.setMenuId(menuId);
	                menuBean.setMenuName(menuDispName);
	            	
	                String privQuery ="";
	                if(isCreateRole){
	                	privQuery="is_role_head_priv='Y'";
	                }else{
	                	privQuery ="is_sub_user_priv='Y'";
	                }
	                
	                
	                String subMenuStmtQuery="select merchant_priv_id,action_mapping,action_disp_name,priv_disp_name,ll.status,ll.action_id from (select action_id,aa.status from st_rm_role_action_mapping aa  inner join st_rm_user_action_mapping bb  inner join st_sl_merchant_user_master cc on aa.rpm_id=bb.rpm_id and bb.user_id=cc.user_id where aa.status = 'ACTIVE'  and mct_id="+mctId+"  and bb.status = 'ACTIVE' and cc.merchant_user_id="+userId+")ll inner join (  select priv_dev_name,action_path,action_mapping,action_disp_name,action_id,parent_menu_id,merchant_priv_id,priv_disp_name from st_rm_priv_rep aa inner join   st_rm_priv_action_mapping bb inner join st_rm_merchant_priv_mapping cc on aa.priv_id=bb.priv_id and aa.priv_id=cc.priv_id where merchant_id ="+merchantId+" and aa.status='ACTIVE' and bb.status='ACTIVE' and cc.status='ACTIVE' and parent_menu_id="+menuId+" and "+privQuery+")mm on ll.action_id=mm.action_id  order by merchant_priv_id";
		            subMenuStmt=con.createStatement();
		            subMenuRs=subMenuStmt.executeQuery(subMenuStmtQuery);
		            
	                menuItemList = new ArrayList<MenuItemDataBean>();
	                List<Integer> privIdList=new ArrayList<Integer>();
	                menuBean.setMenuItems(menuItemList);
	                if(subMenuRs.next()){
	                	menuList.add(menuBean);
	                }
	                subMenuRs.first();
	                while(subMenuRs.next()){
	                	
	                	 Integer privId=subMenuRs.getInt("merchant_priv_id");
	                	 
	                	 if(privIdList.contains(privId)){
	                		 //menuItemBean.setIsAssign("ACTIVE".equals(subMenuRs.getString("status")));
	                	 }else{
	                		 privIdList = new ArrayList<Integer>();
	                		 privIdList.add(privId);
	                		 actionList = new ArrayList<MenuItemActionDataBean>();
	                		 menuItemBean = new MenuItemDataBean();
	                		 menuItemBean.setMenuItemId(subMenuRs.getInt("merchant_priv_id"));
	                		 menuItemBean.setMenuItemName(subMenuRs.getString("priv_disp_name"));
	                		 if("ACTIVE".equals(subMenuRs.getString("status"))){
	                			 menuItemBean.setIsAssign(false);
	                		 }else{
	                			 menuItemBean.setIsAssign(true);
	                		 }
	                		 
	                		 
	                		 
	                		 menuItemBean.setActionItems(actionList);
		                     menuItemList.add(menuItemBean);
		                     
		                     
	                	 }
	                	 actionDataBean = new MenuItemActionDataBean();
	                	 actionDataBean.setActionId(subMenuRs.getInt("action_id"));
	                	 actionDataBean.setActionName(subMenuRs.getString("action_disp_name"));
	                	 actionDataBean.setIsAssign("ACTIVE".equals(subMenuRs.getString("status")));
	                	 
	                	 actionList.add(actionDataBean);
	                }
	                
	            }
	            
			}
		
		
		
		
		/*JsonArray data = new JsonParser().parse(privilegeJson).getAsJsonArray();
        for(JsonElement interfaceData : data) {
            privilegeData = interfaceData.getAsJsonObject();
            privilegeBean = new PrivilegeDataBean();
            privilegeBean.setInterfaceDispName(privilegeData.get("interfaceDispName").getAsString());
            privilegeBean.setInterfaceDevName(privilegeData.get("interfaceDevName").getAsString());
            menuList = new ArrayList<MenuDataBean>();
            privilegeBean.setMenuList(menuList);
            menuArray = privilegeData.getAsJsonArray("menuData");
            for(JsonElement menuData : menuArray) {
                menu = menuData.getAsJsonObject();
                menuBean = new MenuDataBean();
                menuBean.setMenuId(menu.get("menuId").getAsInt());
                menuBean.setMenuName(menu.get("menuName").getAsString());
                menuItemList = new ArrayList<MenuItemDataBean>();
                menuBean.setMenuItems(menuItemList);
                menuItemArray = menu.getAsJsonArray("menuItemData");
                for(JsonElement menuItemData : menuItemArray) {
                    menuItem = menuItemData.getAsJsonObject();
                    menuItemBean = new MenuItemDataBean();
                    menuItemBean.setMenuItemId(menuItem.get("menuItemId").getAsInt());
                    menuItemBean.setMenuItemName(menuItem.get("menuItemName").getAsString());
                    menuItemBean.setAssign(menuItem.get("isAssign").getAsBoolean());
                    menuItemList.add(menuItemBean);
                }
                menuList.add(menuBean);
            }
            privilegeList.add(privilegeBean);
        }*/
			Gson gson = new Gson();
			String json = gson.toJson(privilegeList);
			System.out.println("LMS Response data {}"+json);
		}catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(con);
		}
		return privilegeList;
	}

	public PriviledgeModificationMasterBean fetchUserPriviledgeHistory(int merchantId, int userId, String startDate, String endDate) throws GenericException {
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		PriviledgeModificationMasterBean masterBean = new PriviledgeModificationMasterBean();
		List<PriviledgeModificationHeaderBean> headerList = new ArrayList<PriviledgeModificationHeaderBean>();
		PriviledgeModificationHeaderBean headerBean = null;
		Map<String, Map<String, List<PriviledgeModificationDataBean>>> serviceMap = new HashMap<String, Map<String,List<PriviledgeModificationDataBean>>>();
		Map<String, List<PriviledgeModificationDataBean>> dataMap = new TreeMap<String, List<PriviledgeModificationDataBean>>();
		List<PriviledgeModificationDataBean> priviledgeList = null;
		PriviledgeModificationDataBean dataBean = null;
		try {
			serviceMap.put("Sports Lottery", dataMap);
			masterBean.setHeaderList(headerList);
			masterBean.setServiceMap(serviceMap);

			con = DBConnect.getConnection();
			stmt = con.createStatement();
			stmt.execute("SET SESSION group_concat_max_len=1000000;");

			String query = null;
			SimpleDateFormat compareFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDate = compareFormat.format(new Date().getTime());
			query = "SELECT user_id, data_value, change_date, change_by, user_name, request_ip FROM (" + ((currentDate.equals(endDate.split(" ")[0])) ? "SELECT user_id, GROUP_CONCAT(CONCAT(mpm.priv_id,'~',priv_disp_name,'~',uam.status) SEPARATOR '#') data_value, change_date, change_by, (SELECT user_name FROM st_sl_merchant_user_master WHERE merchant_user_id=change_by) user_name, request_ip FROM st_rm_user_action_mapping uam INNER JOIN st_rm_role_action_mapping ram ON uam.rpm_id=ram.rpm_id INNER JOIN st_rm_priv_action_mapping pam ON ram.action_id=pam.action_id INNER JOIN st_rm_merchant_priv_mapping mpm ON mpm.priv_id=pam.priv_id WHERE user_id=(SELECT user_id FROM st_sl_merchant_user_master WHERE merchant_id="+merchantId+" AND merchant_user_id="+userId+") AND merchant_id="+merchantId+" AND ram.role_id=(SELECT role_id FROM st_sl_merchant_user_master WHERE merchant_id="+merchantId+" AND merchant_user_id="+userId+") AND is_start='Y' AND uam.status<>'NA' UNION " : "");

			int dateCount = 0;
			boolean isHeaderSet = false;
			query += "SELECT user_id, data_value, change_date, change_by, (SELECT user_name FROM st_sl_merchant_user_master WHERE merchant_user_id=change_by) user_name, request_ip FROM (SELECT user_id, GROUP_CONCAT(CONCAT(mpm.priv_id,'~',priv_disp_name,'~',hist.status) SEPARATOR '#') data_value, change_date, change_by, request_ip FROM st_rm_user_priv_history hist INNER JOIN st_rm_role_action_mapping ram ON hist.rpm_id=ram.rpm_id INNER JOIN st_rm_priv_action_mapping pam ON hist.action_id=pam.action_id INNER JOIN st_rm_merchant_priv_mapping mpm ON pam.priv_id=mpm.priv_id WHERE user_id=(SELECT user_id FROM st_sl_merchant_user_master WHERE merchant_id="+merchantId+" AND merchant_user_id="+userId+") AND ram.role_id=(SELECT role_id FROM st_sl_merchant_user_master WHERE merchant_id="+merchantId+" AND merchant_user_id="+userId+") AND change_date>='"+startDate+"' AND change_date<='"+endDate+"' AND is_start='Y' AND merchant_id="+merchantId+" GROUP BY change_date ORDER BY change_date, mpm.priv_id)aa ORDER BY change_date DESC LIMIT 5) aa ORDER BY change_date ASC;";
			System.out.println("fetchUserPriviledgeHistory Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				dateCount++;
				if(isHeaderSet == false) {
					headerBean = new PriviledgeModificationHeaderBean();
					headerBean.setChangeTime(dateFormat.format(rs.getTimestamp("change_date")));
					headerBean.setDoneByUser(rs.getString("user_name"));
					headerBean.setDoneByIP(rs.getString("request_ip"));
					headerList.add(headerBean);
				}

				String dataValues = rs.getString("data_value");
				for(String dataValue : dataValues.split("#")) {
					String privName = dataValue.split("~")[1];
					priviledgeList = dataMap.get(privName);
					if(priviledgeList == null) {
						priviledgeList = new ArrayList<PriviledgeModificationDataBean>();
						dataMap.put(privName, priviledgeList);

						for(int i=1; i<dateCount; i++) {
							priviledgeList.add(new PriviledgeModificationDataBean());
						}
					}

					dataBean = new PriviledgeModificationDataBean();
					dataBean.setPrivId(Integer.parseInt(dataValue.split("~")[0]));
					dataBean.setPrivName(privName);
					dataBean.setStatus(dataValue.split("~")[2]);
					priviledgeList.add(dataBean);
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}

		return masterBean;
	}
	
	public String fetchRetailerPrivList(int merchantUserId,int merchantId) throws SLEException{
		List<String> privList=new ArrayList<String>();
		String query=null;
		Statement stmt=null;
		ResultSet rs=null;
		Connection con=null;
		try{
			con=DBConnect.getConnection();
			query="select merchant_priv_id,priv_disp_name ,uam.status,priv_code from st_rm_user_action_mapping uam INNER JOIN  st_rm_role_action_mapping ram on  ram.rpm_id=uam.rpm_id  INNER JOIN st_rm_priv_action_mapping pam on pam.action_id=ram.action_id INNER JOIN st_rm_merchant_priv_mapping mpm on pam.priv_id=mpm.priv_id INNER JOIN st_rm_priv_rep rpr on rpr.priv_id=pam.priv_id INNER JOIN st_sl_merchant_user_master mum on uam.user_id=mum.user_id where merchant_user_id="+merchantUserId+" and mct_id=4 and  rpr.status='ACTIVE' and mpm.status='ACTIVE' and ram.status='ACTIVE' and mpm.merchant_id="+merchantId+";";
			stmt=con.createStatement();
			rs=stmt.executeQuery(query);
			while(rs.next()){
				if("INACTIVE".equalsIgnoreCase(rs.getString("status"))){
					if(!privList.contains(rs.getString("priv_code"))){
						privList.add(rs.getString("priv_code"));
					}
				}
			}
			query="select merchant_priv_id, rpr.status,priv_code from  st_rm_priv_rep rpr  INNER JOIN st_rm_merchant_priv_mapping mpm on mpm.priv_id=rpr.priv_id where rpr.channel='TERMINAL' and user_type='RETAILER'";
			stmt=con.createStatement();
			rs=stmt.executeQuery(query);
			while(rs.next()){
				if("INACTIVE".equalsIgnoreCase(rs.getString("status"))){
					if(!privList.contains(rs.getString("priv_code"))){
						privList.add(rs.getString("priv_code"));
					}
				}
			}
		}catch (Exception e) {
			new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(con, stmt, rs);
		}
		return privList.toString().replace("[", "").replace("]", "").replace(",", "").trim().replace(" ", "");
	} 
	
	
}