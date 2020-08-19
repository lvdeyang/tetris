package com.sumavision.tetris.business.push.vo;

public class PushStreamVO {

	private String url;
	
	/** 流类型 */
	private String pcm;
	
	private String startTime;
	
	private String endTime;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPcm() {
		return pcm;
	}

	public void setPcm(String pcm) {
		this.pcm = pcm;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
