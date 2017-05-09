package com.skilrock.sle.gameDataMgmt.javaBeans;

import java.io.Serializable;

public class ResultApprovalEventBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int eventId;
	private String eventDisplay;
	private String eventDescription;
	private String optionSelected1;
	private String optionSelected2;

	public ResultApprovalEventBean() {
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getEventDisplay() {
		return eventDisplay;
	}

	public void setEventDisplay(String eventDisplay) {
		this.eventDisplay = eventDisplay;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getOptionSelected1() {
		return optionSelected1;
	}

	public void setOptionSelected1(String optionSelected1) {
		this.optionSelected1 = optionSelected1;
	}

	public String getOptionSelected2() {
		return optionSelected2;
	}

	public void setOptionSelected2(String optionSelected2) {
		this.optionSelected2 = optionSelected2;
	}
}