package com.skilrock.sle.scheduler.common;

public class SchedulerDetailsBean {
	private boolean isActive ;
	private String jobDevName;
	private int jobId ;
	
	
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getJobDevName() {
		return jobDevName;
	}
	public void setJobDevName(String jobDevName) {
		this.jobDevName = jobDevName;
	}
	public int getJobId() {
		return jobId;
	}
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	
	@Override
	public String toString() {
		return "SchedulerDetailBean [isActive=" + isActive + ", jobDevName=" + jobDevName + ", jobId=" + jobId + "]";
	}
	
	
	
	
}
