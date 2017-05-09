package com.skilrock.sle.web.backoffice.dataMgmt.Action;

import com.skilrock.sle.common.Util;
import com.skilrock.sle.dataMgmt.controllerImpl.ReconciliationControllerImpl;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

public class ReconcileDataAction extends BaseActionWeb {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String merCode;
	
	public ReconcileDataAction() {
		super(ReconcileDataAction.class.getName());
	}
	
	public String getMerCode() {
		return merCode;
	}

	public void setMerCode(String merCode) {
		this.merCode = merCode;
	}

	public String startReconciliation() {
		MerchantInfoBean merchantInfoBean = null;

		try {
			merchantInfoBean = Util.merchantInfoMap.get(getMerCode());
			ReconciliationControllerImpl.Single.INSTANCE.getInstance().startReconciliation(merchantInfoBean);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return SUCCESS;
	}

}
