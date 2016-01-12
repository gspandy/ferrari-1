package com.cip.ferrari.admin.core.model;

import java.util.Date;

/**
 * ferrari log, used to track trigger process
 * @author xuxueli  2015-12-19 23:19:09
 */
public class FerrariJobLog{
	
	private int id;
	private Date addTime;
	private Date updateTime;
	
	// job info
	private String jobName;
	private String jobCron;
	private String jobClass;
	private String jobData;
	
	// trigger info
	private Date triggerTime;
	private String triggerStatus;
	private String triggerMsg;
	private String triggerHost;
	
	// handle info
	private Date handleTime;
	private String handleStatus;
	private String handleMsg;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobCron() {
		return jobCron;
	}
	public void setJobCron(String jobCron) {
		this.jobCron = jobCron;
	}
	public String getJobClass() {
		return jobClass;
	}
	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}
	public String getJobData() {
		return jobData;
	}
	public void setJobData(String jobData) {
		this.jobData = jobData;
	}
	public Date getTriggerTime() {
		return triggerTime;
	}
	public void setTriggerTime(Date triggerTime) {
		this.triggerTime = triggerTime;
	}
	public String getTriggerStatus() {
		return triggerStatus;
	}
	public void setTriggerStatus(String triggerStatus) {
		this.triggerStatus = triggerStatus;
	}
	public String getTriggerMsg() {
		return triggerMsg;
	}
	public void setTriggerMsg(String triggerMsg) {
		this.triggerMsg = triggerMsg;
	}
	public String getTriggerHost() {
		return triggerHost;
	}
	public void setTriggerHost(String triggerHost) {
		this.triggerHost = triggerHost;
	}
	public Date getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	public String getHandleStatus() {
		return handleStatus;
	}
	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
	}
	public String getHandleMsg() {
		return handleMsg;
	}
	public void setHandleMsg(String handleMsg) {
		this.handleMsg = handleMsg;
	}
	
	@Override
	public String toString() {
		return "FerrariJobLog [id=" + id + ", jobName=" + jobName + ", jobCron=" + jobCron + ", jobClass=" + jobClass
				+ ", jobData=" + jobData + ", triggerTime=" + triggerTime + ", triggerStatus=" + triggerStatus
				+ ", triggerMsg=" + triggerMsg + ", handleTime=" + handleTime + ", handleStatus=" + handleStatus
				+ ", handleMsg=" + handleMsg + "]";
	}
	
}
