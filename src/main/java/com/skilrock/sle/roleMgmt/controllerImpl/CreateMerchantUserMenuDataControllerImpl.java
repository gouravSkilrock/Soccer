package com.skilrock.sle.roleMgmt.controllerImpl;

import java.sql.Connection;
import java.util.List;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.roleMgmt.daoImpl.CreateMerchantUserMenuDataDaoImpl;
import com.skilrock.sle.roleMgmt.javaBeans.MenuDataBean;


public class CreateMerchantUserMenuDataControllerImpl {

	public List<MenuDataBean> getMenuDataForMerchantUser(MerchantInfoBean merchantInfoBean, UserInfoBean userInfoBean, String channel) throws SLEException {
		Connection con = null;
		List<MenuDataBean> menuDataList = null;
		try{
				con = DBConnect.getConnection();
				int engineUserId = CommonMethodsDaoImpl.getInstance().getEngineUserIdFromUserName(userInfoBean, con);
				menuDataList = new CreateMerchantUserMenuDataDaoImpl().getMenuDataForMerchantUser(merchantInfoBean.getMerchantId(), engineUserId, channel, con);
		}catch(SLEException se){
			se.printStackTrace();
			throw se;
		}catch(Exception e){
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(con);
		}
		return menuDataList;
	}
}