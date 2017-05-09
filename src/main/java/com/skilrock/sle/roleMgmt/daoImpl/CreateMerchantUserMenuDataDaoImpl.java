package com.skilrock.sle.roleMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.roleMgmt.javaBeans.MenuDataBean;
import com.skilrock.sle.roleMgmt.javaBeans.MenuItemActionDataBean;
import com.skilrock.sle.roleMgmt.javaBeans.MenuItemDataBean;


public class CreateMerchantUserMenuDataDaoImpl {

	private static final SLELogger logger = SLELogger.getLogger(CreateMerchantUserMenuDataDaoImpl.class.getName());
	
	
	public List<MenuDataBean> getMenuDataForMerchantUser(int merchantId, int engineMerchantUserId, String channel, Connection con) throws SLEException {
		Map<Integer, MenuDataBean> menuDataMap = null;
		List<MenuDataBean> menuDataList = null;
		MenuDataBean menuDataBean = null;
		List<MenuItemDataBean> menuItemDataList = null;
		MenuItemDataBean menuItemDataBean = null;
		List<MenuItemActionDataBean> menuItemActionDataList = null;
		MenuItemActionDataBean menuItemActionDataBean = null;
		StringBuilder fetchMenuDataQuery = null;
		Statement stmt = null;
		ResultSet rs = null;
		int menuId = 0;
		try {				
				fetchMenuDataQuery = new StringBuilder();
				fetchMenuDataQuery.append("select menu_id, menu_disp_name,  bbb.priv_id, priv_disp_name, bbb.action_id, actionName from st_rm_menu_master aaa");
				fetchMenuDataQuery.append(" inner join");
				fetchMenuDataQuery.append(" (select aa.priv_id, bb.action_id, priv_disp_name, actionName, parent_menu_id from st_rm_merchant_priv_mapping aa");
				fetchMenuDataQuery.append(" inner join");
				fetchMenuDataQuery.append(" (select action_id, priv_id, concat(action_path,action_mapping) actionName from st_rm_priv_action_mapping where action_id in (select action_id from st_rm_user_action_mapping a inner join st_rm_role_action_mapping b on a.rpm_id = b.rpm_id where user_id = ").append(engineMerchantUserId).append( " and a.status = 'ACTIVE' and b.status = 'ACTIVE') and is_start = 'Y') bb");
				fetchMenuDataQuery.append(" inner join");
				fetchMenuDataQuery.append(" st_rm_priv_rep cc");
				fetchMenuDataQuery.append(" on aa.priv_id = bb.priv_id and cc.channel = '").append(channel).append("' where merchant_id = ").append(merchantId).append( " and cc.priv_id = aa.priv_id and cc.priv_id = bb.priv_id and cc.status = 'ACTIVE' and aa.status='ACTIVE') bbb");
				fetchMenuDataQuery.append(" on aaa.menu_id = bbb.parent_menu_id where merchant_id = ").append(merchantId).append( " and status = 'ACTIVE' order by aaa.item_order");
				stmt = con.createStatement();
				logger.debug("MenuDataFetchQry : "+fetchMenuDataQuery.toString());
				rs = stmt.executeQuery(fetchMenuDataQuery.toString());
				menuDataMap = new LinkedHashMap<Integer, MenuDataBean>();
				while(rs.next()){
					
				menuId = rs.getInt("menu_id");
					
				menuItemActionDataList = new LinkedList<MenuItemActionDataBean>();
					menuItemActionDataBean = new MenuItemActionDataBean();
						menuItemActionDataBean.setActionId(rs.getInt("action_id"));
						menuItemActionDataBean.setActionName(rs.getString("actionName"));
							menuItemActionDataList.add(menuItemActionDataBean);
							
					menuItemDataBean = new MenuItemDataBean();
						menuItemDataBean.setMenuItemId(rs.getInt("priv_id"));
						menuItemDataBean.setMenuItemName(rs.getString("priv_disp_name"));
						menuItemDataBean.setActionItems(menuItemActionDataList);
					
					if(menuDataMap.containsKey(menuId)){
						 menuDataMap.get(menuId).getMenuItems().add(menuItemDataBean);						
					}else{
						
						menuItemDataList = new LinkedList<MenuItemDataBean>();
						menuItemDataList.add(menuItemDataBean);
								
							menuDataBean = new MenuDataBean();
								menuDataBean.setMenuId(rs.getInt("menu_id"));
								menuDataBean.setMenuName(rs.getString("menu_disp_name"));
								menuDataBean.setMenuItems(menuItemDataList);
								
						menuDataMap.put(rs.getInt("menu_id"), menuDataBean);
					}
					
				}
				
				//logger.info("MerchantUserMenuData : "+menuDataMap);
				menuDataList = new LinkedList<MenuDataBean>();
				for(Map.Entry<Integer, MenuDataBean> entry : menuDataMap.entrySet()){
					menuDataList.add(entry.getValue());
				}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
		return menuDataList;
	}
	
	public static void main(String args[]) throws SLEException{
		Connection con = DBConnect.getPropFileCon();
		new CreateMerchantUserMenuDataDaoImpl().getMenuDataForMerchantUser(1,1,"WEB",con);
	}
}