package com.skilrock.sle.reportsMgmt.javaBeans;

import java.io.Serializable;
import java.util.List;

public class SaleTrendDataBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<SaleDataBean> saleBeansDayWise;
	private WinningDataBean winningBean;
	private DrawDataBean drawInfoBean;
	
	
	
	public List<SaleDataBean> getSaleBeansDayWise() {
		return saleBeansDayWise;
	}



	public void setSaleBeansDayWise(List<SaleDataBean> saleBeansDayWise) {
		this.saleBeansDayWise = saleBeansDayWise;
	}



	public WinningDataBean getWinningBean() {
		return winningBean;
	}



	public void setWinningBean(WinningDataBean winningBean) {
		this.winningBean = winningBean;
	}



	public SaleTrendDataBean() {
	}



	public void setDrawInfoBean(DrawDataBean drawInfoBean) {
		this.drawInfoBean = drawInfoBean;
	}



	public DrawDataBean getDrawInfoBean() {
		return drawInfoBean;
	}
}