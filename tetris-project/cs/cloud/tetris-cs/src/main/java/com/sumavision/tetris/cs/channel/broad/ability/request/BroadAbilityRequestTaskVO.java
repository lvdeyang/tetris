/**
 * 
 */
package com.sumavision.tetris.cs.channel.broad.ability.request;

/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年12月25日 上午10:07:26
 */
public class BroadAbilityRequestTaskVO {
	
	/**索引*/
	private Integer index;
	
	/**编码类型*/
	private String codec;
	
	/**分辨率*/
	private String resolution;
	
	/**编码码率*/
	private String bitrate;

	public Integer getIndex() {
		return index;
	}

	public BroadAbilityRequestTaskVO setIndex(Integer index) {
		this.index = index;
		return this;
	}

	public String getCodec() {
		return codec;
	}

	public BroadAbilityRequestTaskVO setCodec(String codec) {
		this.codec = codec;
		return this;
	}

	public String getResolution() {
		return resolution;
	}

	public BroadAbilityRequestTaskVO setResolution(String resolution) {
		this.resolution = resolution;
		return this;
	}

	public String getBitrate() {
		return bitrate;
	}

	public BroadAbilityRequestTaskVO setBitrate(String bitrate) {
		this.bitrate = bitrate;
		return this;
	}
	
}
