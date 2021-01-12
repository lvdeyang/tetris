package com.sumavision.tetris.cs.channel.broad.ability.request;

public class BroadAbilityBroadRequestInputPrevStreamVO {

	/** 源地址 */
	private String url;
	
	/** 源封装 */
	private String pcm;
	
	/** 时长 */
	private Long duration;
	
	/** 流开始绝对时间 */
	private String startTime;
	
	/** 流结束绝对时间 */
	private String endTime;
	
	/** 收流网口*/
	private String localIp;

	/** 直播流类型*/
	private String type;
	
	public String getUrl() {
		return url;
	}

	public BroadAbilityBroadRequestInputPrevStreamVO setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getPcm() {
		return pcm;
	}

	public BroadAbilityBroadRequestInputPrevStreamVO setPcm(String pcm) {
		this.pcm = pcm;
		return this;
	}

	public Long getDuration() {
		return duration;
	}

	public BroadAbilityBroadRequestInputPrevStreamVO setDuration(Long duration) {
		this.duration = duration;
		return this;
	}

	public String getStartTime() {
		return startTime;
	}

	public BroadAbilityBroadRequestInputPrevStreamVO setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public String getEndTime() {
		return endTime;
	}

	public BroadAbilityBroadRequestInputPrevStreamVO setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}

	public String getLocalIp() {
		return localIp;
	}

	public BroadAbilityBroadRequestInputPrevStreamVO setLocalIp(String localIp) {
		this.localIp = localIp;
		return this;
	}

	public String getType() {
		return type;
	}

	public BroadAbilityBroadRequestInputPrevStreamVO setType(String type) {
		this.type = type;
		return this;
	}
}
