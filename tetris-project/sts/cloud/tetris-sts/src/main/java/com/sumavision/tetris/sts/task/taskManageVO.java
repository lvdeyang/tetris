package com.sumavision.tetris.sts.task;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.sumavision.tetris.sts.task.tasklink.TaskLinkVO;

public class taskManageVO {

	static Logger logger = LogManager.getLogger(TaskLinkVO.class);
	
	private Integer id = 6;
	
	private String taskName="111";
	
	private String status = "run";
	
	private String taskGroupName = "222";
	
	private String sdmDeviceName = "333";
			
	private String taskDeviceIp = "10.10.40.58";
	private String sourceName = "555";
	private String programName = "666";
	
	private String output = "10.10.40.103:1258";
	
	private String url = "rtp://@10.10.40.103:1258";
	public static Logger getLogger() {
		return logger;
	}
	public static void setLogger(Logger logger) {
		taskManageVO.logger = logger;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTaskGroupName() {
		return taskGroupName;
	}
	public void setTaskGroupName(String taskGroupName) {
		this.taskGroupName = taskGroupName;
	}
	public String getSdmDeviceName() {
		return sdmDeviceName;
	}
	public void setSdmDeviceName(String sdmDeviceName) {
		this.sdmDeviceName = sdmDeviceName;
	}
	public String getTaskDeviceIp() {
		return taskDeviceIp;
	}
	public void setTaskDeviceIp(String taskDeviceIp) {
		this.taskDeviceIp = taskDeviceIp;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
