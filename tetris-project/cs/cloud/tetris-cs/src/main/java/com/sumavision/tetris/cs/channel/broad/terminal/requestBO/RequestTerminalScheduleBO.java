package com.sumavision.tetris.cs.channel.broad.terminal.requestBO;

import java.util.List;

public class RequestTerminalScheduleBO {
	/** 单个排期单起始时间 */
	private String effectTime;
	
	/** 单个排期单停止时间 */
	private String endTime;
	
	/** 排期单屏幕方向 */
	private String screensOrient;
	
	/** 排期单内分屏描述 */
	private List<RequestTerminalScheduleScreenBO> screens;

	public String getEffectTime() {
		return effectTime;
	}

	public RequestTerminalScheduleBO setEffectTime(String effectTime) {
		this.effectTime = effectTime;
		return this;
	}

	public String getEndTime() {
		return endTime;
	}

	public RequestTerminalScheduleBO setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}

	public String getScreensOrient() {
		return screensOrient;
	}

	public RequestTerminalScheduleBO setScreensOrient(String screensOrient) {
		this.screensOrient = screensOrient;
		return this;
	}

	public List<RequestTerminalScheduleScreenBO> getScreens() {
		return screens;
	}

	public RequestTerminalScheduleBO setScreens(List<RequestTerminalScheduleScreenBO> screens) {
		this.screens = screens;
		return this;
	}
}
