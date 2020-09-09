package com.sumavision.tetris.capacity.bo.task;

/**
 * dolby参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月1日 上午9:17:14
 */
public class DolbyBO extends BaseAudioEncodeBO<DolbyBO>{

	private String sample_fmt;
	
	/** ac3/eac3 */
	private String type = "ac3";	

	public String getSample_fmt() {
		return sample_fmt;
	}

	public DolbyBO setSample_fmt(String sample_fmt) {
		this.sample_fmt = sample_fmt;
		return this;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
