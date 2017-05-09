package com.skilrock.sle.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class CallUrl extends Thread {
	private String urlString;
	private int gameId;
	private int gameTypeId;
	private int drawId;
	
	private String activityType;

	public CallUrl(String urlString, int gameId, int gameTypeId, int drawId, String activityType) {
		this.urlString = urlString;
		this.gameId = gameId;
		this.gameTypeId = gameTypeId;
		this.drawId = drawId;
		this.activityType = activityType;
	}

	public static void main(String[] args) throws IOException {
		URL url = new URL("http://192.168.124.80:8080/PMS/com/skilrock/pms/sportsLottery/drawMgmt/Action/slResultNotify.action?gameId=1&gameTypeId=1&drawId=1");
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		in.close();
	}

	public void sendCallOnResultSubmission() throws IOException {
		URL url = new URL(urlString+"gameId="+gameId+"&gameTypeId="+gameTypeId+"&drawId="+drawId);
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		in.close();
	}

	public void sendCallOnDrawFreeze() throws IOException {
		URL url = new URL(urlString+"gameNo="+gameId+"&gameTypeId="+gameTypeId);
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		in.close();
	}

	public void run() {
		try {
			if ("RESULT_SUBMISSION".equals(activityType)) {
				sendCallOnResultSubmission();
			} else if ("DRAW_FREEZE".equals(activityType)) {
				sendCallOnDrawFreeze();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}