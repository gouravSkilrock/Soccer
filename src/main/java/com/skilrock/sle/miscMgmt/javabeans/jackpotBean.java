package com.skilrock.sle.miscMgmt.javabeans;

import java.io.Serializable;

public class jackpotBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String status;
	private double carryForward;
	private double saleAmt;
	private double jackpotAmt;

public double getJackpotAmt() {
		return jackpotAmt;
	}
	public void setJackpotAmt(double jackpotAmt) {
		this.jackpotAmt = jackpotAmt;
	}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public double getCarryForward() {
	return carryForward;
}
public void setCarryForward(double carryForward) {
	this.carryForward = carryForward;
}
public double getSaleAmt() {
	return saleAmt;
}
public void setSaleAmt(double saleAmt) {
	this.saleAmt = saleAmt;
}


}
