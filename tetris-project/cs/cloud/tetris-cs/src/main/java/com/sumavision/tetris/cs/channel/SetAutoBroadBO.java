package com.sumavision.tetris.cs.channel;

public class SetAutoBroadBO {
	/** 是否智能播发 */
	private Boolean autoBroad;
	
	/** 是否列表乱序 */
	private Boolean autoBroadShuffle;
	
	/** 生效天数 */
	private Integer autoBroadDuration;
	
	/** 开始播发时间点 */
	private String autoBroadStart;

	public Boolean getAutoBroad() {
		return autoBroad;
	}

	public SetAutoBroadBO setAutoBroad(Boolean autoBroad) {
		this.autoBroad = autoBroad;
		return this;
	}

	public Boolean getAutoBroadShuffle() {
		return autoBroadShuffle;
	}

	public SetAutoBroadBO setAutoBroadShuffle(Boolean autoBroadShuffle) {
		this.autoBroadShuffle = autoBroadShuffle;
		return this;
	}

	public Integer getAutoBroadDuration() {
		return autoBroadDuration;
	}

	public SetAutoBroadBO setAutoBroadDuration(Integer autoBroadDuration) {
		this.autoBroadDuration = autoBroadDuration;
		return this;
	}

	public String getAutoBroadStart() {
		return autoBroadStart;
	}

	public SetAutoBroadBO setAutoBroadStart(String autoBroadStart) {
		this.autoBroadStart = autoBroadStart;
		return this;
	}
}
