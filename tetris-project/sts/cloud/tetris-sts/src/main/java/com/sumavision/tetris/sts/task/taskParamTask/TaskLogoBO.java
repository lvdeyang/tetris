package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class TaskLogoBO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 225792461529288390L;
	
	private String position;
	private String zone;
	private String url;
	private Integer mode;
	public TaskLogoBO(String position, String zone,String url, Integer mode){
		super();
		this.position = position;
		this.zone = zone;
		this.url = zone;
		this.mode = mode;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	
}
