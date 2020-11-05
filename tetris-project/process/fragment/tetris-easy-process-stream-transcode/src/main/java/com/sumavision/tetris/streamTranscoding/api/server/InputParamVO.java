package com.sumavision.tetris.streamTranscoding.api.server;

public class InputParamVO {
	/** 采样率 */
	private String sourSample;
	
	/** 音频采样类型 */
	private String sourPrecision;
	
	/** 声道布局 */
	private String sourChannel;
	
	public String getSourSample() {
		return sourSample;
	}
	
	public void setSourSample(String sourSample) {
		this.sourSample = sourSample;
	}
	
	public String getSourPrecision() {
		return sourPrecision;
	}
	
	public void setSourPrecision(String sourPrecision) {
		this.sourPrecision = sourPrecision;
	}
	
	public String getSourChannel() {
		return sourChannel;
	}
	
	public void setSourChannel(String sourChannel) {
		this.sourChannel = sourChannel;
	}
}
