package com.sumavision.tetris.capacity.bo.input;

/**
 * 音频参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月28日 下午3:54:00
 */
public class ProgramAudioBO {

	/** 音频pid 0~8191 */
	private Integer pid;
	
	/** 音频编码格式 mp1/mp2/mp3/aac/heaac/ac3/eac3
	 * /pcm_alaw/pac_mulaw/g729a/g729/adpcm */
	private String type;
	
	private Integer bitrate;
	
	/** 解码方式 cpu */
	private String decode_mode;
	
	private Integer nv_card_idx;

	private Integer sample_rate;

	private String sample_fmt;

	private String channel_num;

	private String channel_layout;

	private String language;

	private Integer bit_depth;

	private String backup_mode;

	private Integer cutoff_time;

	public Integer getPid() {
		return pid;
	}

	public ProgramAudioBO setPid(Integer pid) {
		this.pid = pid;
		return this;
	}

	public String getType() {
		return type;
	}

	public ProgramAudioBO setType(String type) {
		this.type = type;
		return this;
	}

	public Integer getBitrate() {
		return bitrate;
	}

	public ProgramAudioBO setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
		return this;
	}

	public String getDecode_mode() {
		return decode_mode;
	}

	public ProgramAudioBO setDecode_mode(String decode_mode) {
		this.decode_mode = decode_mode;
		return this;
	}

	public Integer getNv_card_idx() {
		return nv_card_idx;
	}

	public ProgramAudioBO setNv_card_idx(Integer nv_card_idx) {
		this.nv_card_idx = nv_card_idx;
		return this;
	}


	public Integer getSample_rate() {
		return sample_rate;
	}

	public void setSample_rate(Integer sample_rate) {
		this.sample_rate = sample_rate;
	}

	public String getSample_fmt() {
		return sample_fmt;
	}

	public void setSample_fmt(String sample_fmt) {
		this.sample_fmt = sample_fmt;
	}

	public String getChannel_num() {
		return channel_num;
	}

	public void setChannel_num(String channel_num) {
		this.channel_num = channel_num;
	}

	public String getChannel_layout() {
		return channel_layout;
	}

	public void setChannel_layout(String channel_layout) {
		this.channel_layout = channel_layout;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getBit_depth() {
		return bit_depth;
	}

	public void setBit_depth(Integer bit_depth) {
		this.bit_depth = bit_depth;
	}

	public String getBackup_mode() {
		return backup_mode;
	}

	public ProgramAudioBO setBackup_mode(String backup_mode) {
		this.backup_mode = backup_mode;
		return this;
	}

	public Integer getCutoff_time() {
		return cutoff_time;
	}

	public ProgramAudioBO setCutoff_time(Integer cutoff_time) {
		this.cutoff_time = cutoff_time;
		return this;
	}
}
