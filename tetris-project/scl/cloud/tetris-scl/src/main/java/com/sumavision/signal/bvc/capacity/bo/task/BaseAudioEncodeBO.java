package com.sumavision.signal.bvc.capacity.bo.task;

/**
 * 音频编码通用参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月1日 上午9:55:59
 */
public class BaseAudioEncodeBO <V extends BaseAudioEncodeBO>{

	private String bitrate = "192";
	
	private String sample_rate = "44.1";

	private Integer sample_byte = 2;
	
	private String channel_layout = "stereo";
	
	public String getBitrate() {
		return bitrate;
	}

	public V setBitrate(String bitrate) {
		this.bitrate = bitrate;
		return (V)this;
	}

	public String getSample_rate() {
		return sample_rate;
	}

	public V setSample_rate(String sample_rate) {
		this.sample_rate = sample_rate;
		return (V)this;
	}

	public Integer getSample_byte() {
		return sample_byte;
	}

	public V setSample_byte(Integer sample_byte) {
		this.sample_byte = sample_byte;
		return (V)this;
	}

	public String getChannel_layout() {
		return channel_layout;
	}

	public V setChannel_layout(String channel_layout) {
		this.channel_layout = channel_layout;
		return (V)this;
	}
	
}
