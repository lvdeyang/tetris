package com.sumavision.tetris.cs.schedule.api.server;

import java.util.List;

public class ApiServerScheduleVO {
	/** 节目单开始时间 */
	private String broadDate;
	
	/** 节目排期 */
	private List<String> assetPaths;

	public String getBroadDate() {
		return broadDate;
	}

	public void setBroadDate(String broadDate) {
		this.broadDate = broadDate;
	}

	public List<String> getAssetPaths() {
		return assetPaths;
	}

	public void setAssetPaths(List<String> assetPaths) {
		this.assetPaths = assetPaths;
	}
	
	
}
