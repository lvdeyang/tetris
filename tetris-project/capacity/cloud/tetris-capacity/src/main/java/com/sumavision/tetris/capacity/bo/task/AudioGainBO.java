package com.sumavision.tetris.capacity.bo.task;

/**
 * 重采样参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月30日 上午9:27:46
 */
public class AudioGainBO {

	/** 采样率 16-96000 */
	private Integer volume = 0;
	
	/** 协议中该字段去了，这里留着看映射关系 */
	private String gain_mode = "auto";

	public Integer getVolume() {
		return volume;
	}

	public AudioGainBO setVolume(Integer volume) {
		this.volume = volume;
		return this;
	}

	public String getGain_mode() {
		return gain_mode;
	}

	public AudioGainBO setGain_mode(String gain_mode) {
		this.gain_mode = gain_mode;
		return this;
	}
}
