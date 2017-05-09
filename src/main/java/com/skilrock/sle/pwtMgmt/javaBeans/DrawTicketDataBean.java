package com.skilrock.sle.pwtMgmt.javaBeans;

import java.io.Serializable;

public class DrawTicketDataBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int drawId;
	private long taskId;
	private long transactionId;
	private double winningAmt;
	private int purTbleName;

	public DrawTicketDataBean() {
	}

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public double getWinningAmt() {
		return winningAmt;
	}

	public void setWinningAmt(double winningAmt) {
		this.winningAmt = winningAmt;
	}

	public int getPurTbleName() {
		return purTbleName;
	}

	public void setPurTbleName(int purTbleName) {
		this.purTbleName = purTbleName;
	}
}