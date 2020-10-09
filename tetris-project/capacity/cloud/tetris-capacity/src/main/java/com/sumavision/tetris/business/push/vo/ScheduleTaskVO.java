package com.sumavision.tetris.business.push.vo;

import java.util.List;

public class ScheduleTaskVO {

	private String deviceIp;

	private String mediaType;
	
	private ScheduleInputVO input;
	
	private List<PushOutputVO> output;

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public ScheduleInputVO getInput() {
		return input;
	}

	public void setInput(ScheduleInputVO input) {
		this.input = input;
	}

	public List<PushOutputVO> getOutput() {
		return output;
	}

	public void setOutput(List<PushOutputVO> output) {
		this.output = output;
	}
	
}
