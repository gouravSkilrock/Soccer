package com.skilrock.sle.reportsMgmt.javaBeans;

import java.io.Serializable;

public class SaleDataBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String day;
	private String date;
	private int ticketCount;
	private double saleAmount;
	private double avgSalePerTkt;
	
	public SaleDataBean() {
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getTicketCount() {
		return ticketCount;
	}

	public void setTicketCount(int ticketCount) {
		this.ticketCount = ticketCount;
	}

	public double getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(double saleAmount) {
		this.saleAmount = saleAmount;
	}

	public double getAvgSalePerTkt() {
		return avgSalePerTkt;
	}

	public void setAvgSalePerTkt(double avgSalePerTkt) {
		this.avgSalePerTkt = avgSalePerTkt;
	}
	
}