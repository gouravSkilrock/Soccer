package com.skilrock.sle.web.merchantUser.userMgmt.Action;

import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.roleMgmt.controllerImpl.CreateMerchantUserMenuDataControllerImpl;
import com.skilrock.sle.roleMgmt.javaBeans.MenuDataBean;

public class CreateMerchantUserMenuDataAction extends BaseActionWeb{
	private static final long serialVersionUID = 1L;

	public CreateMerchantUserMenuDataAction() {
		super(CreateMerchantUserMenuDataAction.class.getName());
	}

	public void getMenuDataForMerchantUser() throws SLEException {
		response.setContentType("application/json");
		PrintWriter out = null;
		MerchantInfoBean merchantInfoBean = null;
		UserInfoBean userInfoBean = null;
		try {
				out = response.getWriter();
				merchantInfoBean =  Util.merchantInfoMap.get(getMerCode());
				userInfoBean = new UserInfoBean();
				userInfoBean.setUserName(getUserName());
				List<MenuDataBean> menuDataBeanList = new CreateMerchantUserMenuDataControllerImpl().getMenuDataForMerchantUser(merchantInfoBean, userInfoBean,"WEB");
				String menuData = new Gson().toJson(menuDataBeanList);
				out.print(menuData);
				return;
		} catch (SLEException se) {
			logger.info("ErrorCode:"+se.getErrorCode()+" ErrorMessage:"+se.getErrorMessage());
			return;
		} catch (Exception e) {
			logger.info("ErrorCode:"+SLEErrors.GENERAL_EXCEPTION_ERROR_CODE+" ErrorMessage:"+SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			return;
		} finally {
			out.flush();
			out.close();
		}
	}
}