package com.sumavision.tetris.cs.channel.broad.ability.request;

public class BroadAbilityBroadRequestInputPrevFileVO {
	
	/** 循环次数 */
	private Integer count;
	
	/** 文件播放时长 */
	private Long duration;
	
	/** seek */
	private Long seek;
	
	/** 源地址 */
	private String url;
		
	/** 播发开始的绝对时间 */
	private String startTime;
	

	public String getUrl() {
		return url;
	}

	public BroadAbilityBroadRequestInputPrevFileVO setUrl(String url) {
		this.url = url;
		return this;
	}

	public Long getSeek() {
		return seek;
	}

	public BroadAbilityBroadRequestInputPrevFileVO setSeek(Long seek) {
		this.seek = seek;
		return this;
	}

	public Integer getCount() {
		return count;
	}

	public BroadAbilityBroadRequestInputPrevFileVO setCount(Integer count) {
		this.count = count;
		return this;
	}

	public Long getDuration() {
		return duration;
	}

	public BroadAbilityBroadRequestInputPrevFileVO setDuration(Long duration) {
		this.duration = duration;
		return this;
	}

	public String getStartTime() {
		return startTime;
	}

	public BroadAbilityBroadRequestInputPrevFileVO setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}


}
