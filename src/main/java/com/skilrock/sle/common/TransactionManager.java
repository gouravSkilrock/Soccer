package com.skilrock.sle.common;


public class TransactionManager {
	private static final ThreadLocal<LocalThreadData> context = new ThreadLocal<LocalThreadData>();

	public static void startTransaction() {
		// logic to start a transaction
		// ...
		LocalThreadData localThread = new LocalThreadData();
		localThread.setAuditId(Util.getAuditTrailId());
		localThread.setAuditTime(System.currentTimeMillis());

		context.set(localThread);
	}

	public static long getAuditId() {
		return context.get().getAuditId();
	}

	public static long getAuditTime() {
		return context.get().getAuditTime();
	}

	public static int getMerchantId() {
		return context.get().getMerchantId();
	}

	public static void setMerchantId(int merchantId) {
		context.get().setMerchantId(merchantId);
	}

	public static void endTransaction() {

		context.remove();
	}

}