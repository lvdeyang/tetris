package com.sumavision.tetris.cs.schedule.api.qt.response;

import java.util.List;

public class ApiQtScheduleVO {
	private List<ApiQtScheduleScreenVO> screens;
	
	/** 生效时间 */
	private String effectTime;

	public List<ApiQtScheduleScreenVO> getScreens() {
		return screens;
	}

	public ApiQtScheduleVO setScreens(List<ApiQtScheduleScreenVO> screens) {
		this.screens = screens;
		return this;
	}

	public String getEffectTime() {
		return effectTime;
	}

	public ApiQtScheduleVO setEffectTime(String effectTime) {
		this.effectTime = effectTime;
		return this;
	}
}
