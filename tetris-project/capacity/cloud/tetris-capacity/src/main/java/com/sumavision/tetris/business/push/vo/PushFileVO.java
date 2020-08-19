package com.sumavision.tetris.business.push.vo;

public class PushFileVO {

	private String url;
	
	private Long seek;
	
	private Long count;
	
	private Long duration;
	
	private String startTime;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getSeek() {
		return seek;
	}

	public void setSeek(Long seek) {
		this.seek = seek;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
}
