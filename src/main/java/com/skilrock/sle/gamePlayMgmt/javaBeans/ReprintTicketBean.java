package com.skilrock.sle.gamePlayMgmt.javaBeans;

public class ReprintTicketBean {
	
	private boolean isPwt;
	private String ticketNumber;
    private int gameId;
    private int gameTypeId;
    private int drawId;
    private int rpc;
    private int barCodeCount;
    private SportsLotteryGamePlayBean gamePlayBean;
    private double avlBal;
    private int txnId;
    private int eventTypeId;
    private String interfaceType;
    
	public double getAvlBal() {
		return avlBal;
	}
	public void setAvlBal(double avlBal) {
		this.avlBal = avlBal;
	}
	public int getTxnId() {
		return txnId;
	}
	public void setTxnId(int txnId) {
		this.txnId = txnId;
	}
	public boolean isPwt() {
		return isPwt;
	}
	public void setPwt(boolean isPwt) {
		this.isPwt = isPwt;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public int getGameTypeId() {
		return gameTypeId;
	}
	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}
	public int getDrawId() {
		return drawId;
	}
	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}
	public void setRpc(int rpc) {
		this.rpc = rpc;
	}
	public int getRpc() {
		return rpc;
	}
	public void setBarCodeCount(int barCodeCount) {
		this.barCodeCount = barCodeCount;
	}
	public int getBarCodeCount() {
		return barCodeCount;
	}
	public void setGamePlayBean(SportsLotteryGamePlayBean gamePlayBean) {
		this.gamePlayBean = gamePlayBean;
	}
	public SportsLotteryGamePlayBean getGamePlayBean() {
		return gamePlayBean;
	}
	public void setEventTypeId(int eventTypeId) {
		this.eventTypeId = eventTypeId;
	}
	public int getEventTypeId() {
		return eventTypeId;
	}
	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}
	public String getInterfaceType() {
		return interfaceType;
	}
}
