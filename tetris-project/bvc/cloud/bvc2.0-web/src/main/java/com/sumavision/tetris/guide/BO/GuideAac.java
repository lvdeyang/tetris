package com.sumavision.tetris.guide.BO;

public class GuideAac {
	
	/**采样率 */
	private String sample_rate;
	
	private String sample_fmt;
	
	/**码率 */
	private int bitrate;
	
	/**声道类型 */
	private String channel_layout;
	
	/**编码类型mpeg4-aac-lc,mpeg4-he-aac-lc,mpeg4-he-aac-v2-lc;ac3,eac3 */
	private String type;

	public String getSample_rate() {
		return sample_rate;
	}

	public void setSample_rate(String sample_rate) {
		this.sample_rate = sample_rate;
	}

	public String getSample_fmt() {
		return sample_fmt;
	}

	public void setSample_fmt(String sample_fmt) {
		this.sample_fmt = sample_fmt;
	}

	public int getBitrate() {
		return bitrate;
	}

	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}

	public String getChannel_layout() {
		return channel_layout;
	}

	public void setChannel_layout(String channel_layout) {
		this.channel_layout = channel_layout;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
