package com.sumavision.tetris.sts.task;

import java.io.Serializable;

public class ScreenCapBO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3038086313678639651L;

	private String ftpUrl;
	
	private Long timeSpan;
	
	private Integer maxBuf;
	
	private String jpegPre;

	public String getFtpUrl() {
		return ftpUrl;
	}

	public void setFtpUrl(String ftpUrl) {
		this.ftpUrl = ftpUrl;
	}

	public Long getTimeSpan() {
		return timeSpan;
	}

	public void setTimeSpan(Long timeSpan) {
		this.timeSpan = timeSpan;
	}

	public Integer getMaxBuf() {
		return maxBuf;
	}

	public void setMaxBuf(Integer maxBuf) {
		this.maxBuf = maxBuf;
	}

	public String getJpegPre() {
		return jpegPre;
	}

	public void setJpegPre(String jpegPre) {
		this.jpegPre = jpegPre;
	}
}
