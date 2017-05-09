package com.skilrock.sle.dataMgmt.javaBeans;

import com.skilrock.sle.embedded.playMgmt.common.javaBeans.CancelTicketBean;

public class CancelTransactionAPIBean extends CancelTicketBean {
	private String channelType;
	private int userId;
	private int purchaseTableName;
	private String refTransId;
	private String refSaleTransId;

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPurchaseTableName() {
		return purchaseTableName;
	}

	public void setPurchaseTableName(int purchaseTableName) {
		this.purchaseTableName = purchaseTableName;
	}

	public String getRefTransId() {
		return refTransId;
	}

	public void setRefTransId(String refTransId) {
		this.refTransId = refTransId;
	}

	public String getRefSaleTransId() {
		return refSaleTransId;
	}

	public void setRefSaleTransId(String refSaleTransId) {
		this.refSaleTransId = refSaleTransId;
	}

	@Override
	public String toString() {
		return "CancelTransactionAPIBean [channelType=" + channelType
				+ ", userId=" + userId + ", purchaseTableName="
				+ purchaseTableName + ", refTransId=" + refTransId
				+ ", refSaleTransId=" + refSaleTransId + "]";
	}

}
