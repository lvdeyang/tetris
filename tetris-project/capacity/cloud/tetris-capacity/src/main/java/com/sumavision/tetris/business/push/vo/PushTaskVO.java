package com.sumavision.tetris.business.push.vo;

import java.util.List;

public class PushTaskVO {

	/** 转码模块ip */
	private String deviceIp;
	
	/** 转码备份模式 auto、manual */
	private String exchangeMode;
	
	/** 媒体类型 audio、video */
	private String mediaType;
	
	/** 输入源 */
	private List<PushInputVO> input;
	
	/** 输出 */
	private List<PushOutputVO> output;

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public String getExchangeMode() {
		return exchangeMode;
	}

	public void setExchangeMode(String exchangeMode) {
		this.exchangeMode = exchangeMode;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public List<PushInputVO> getInput() {
		return input;
	}

	public void setInput(List<PushInputVO> input) {
		this.input = input;
	}

	public List<PushOutputVO> getOutput() {
		return output;
	}

	public void setOutput(List<PushOutputVO> output) {
		this.output = output;
	}
	
}
