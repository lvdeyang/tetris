package com.sumavision.tetris.cs.channel.api;

public class ApiServerScheduleCastVO {
	/** 资源id */
	private Long assetId;
	
	/** 资源类型 */
	private String assetType;
	
	/** 资源播放次数 */
	private Long playTime;
	
	/** 开始时间 */
	private String startTime;
	
	/** 结束时间 */
	private String endTime;

	public Long getAssetId() {
		return assetId;
	}

	public ApiServerScheduleCastVO setAssetId(Long assetId) {
		this.assetId = assetId;
		return this;
	}

	public String getAssetType() {
		return assetType;
	}

	public ApiServerScheduleCastVO setAssetType(String assetType) {
		this.assetType = assetType;
		return this;
	}

	public Long getPlayTime() {
		return playTime;
	}

	public ApiServerScheduleCastVO setPlayTime(Long playTime) {
		this.playTime = playTime;
		return this;
	}

	public String getStartTime() {
		return startTime;
	}

	public ApiServerScheduleCastVO setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public String getEndTime() {
		return endTime;
	}

	public ApiServerScheduleCastVO setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}
}
