package com.sumavision.tetris.cs.schedule.api.server;

import java.util.List;

import com.sumavision.tetris.cs.program.ScreenVO;

public class ApiServerScheduleVO {
	/** 节目单开始时间 */
	private String broadDate;
	
	/** 节目排期 */
	private List<ScreenVO> screens;

	public String getBroadDate() {
		return broadDate;
	}

	public void setBroadDate(String broadDate) {
		this.broadDate = broadDate;
	}

	public List<ScreenVO> getAssetScreens() {
		return screens;
	}

	public void setScreens(List<ScreenVO> screens) {
		this.screens = screens;
	}
	
	
}
