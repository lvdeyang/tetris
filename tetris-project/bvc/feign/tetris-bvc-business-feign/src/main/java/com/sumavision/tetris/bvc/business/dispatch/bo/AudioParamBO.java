package com.sumavision.tetris.bvc.business.dispatch.bo;

public class AudioParamBO {

	private String codec = "";

	private String bitrate = "64000";
	
	private int sample_rate = 48000;
	
	private int channel_cnt = 1;
	
	private int bit_width = 16;
	
	/** 音量增益，音量调节0-100，协议范围为0-3000*/
	private int gain = 0;

	public String getCodec() {
		return codec;
	}

	public AudioParamBO setCodec(String codec) {
		this.codec = codec;
		return this;
	}

	public String getBitrate() {
		return bitrate;
	}

	public AudioParamBO setBitrate(String bitrate) {
		this.bitrate = bitrate;
		return this;
	}

	public int getSample_rate() {
		return sample_rate;
	}

	public AudioParamBO setSample_rate(int sample_rate) {
		this.sample_rate = sample_rate;
		return this;
	}

	public int getChannel_cnt() {
		return channel_cnt;
	}

	public AudioParamBO setChannel_cnt(int channel_cnt) {
		this.channel_cnt = channel_cnt;
		return this;
	}

	public int getBit_width() {
		return bit_width;
	}

	public AudioParamBO setBit_width(int bit_width) {
		this.bit_width = bit_width;
		return this;
	}

	public int getGain() {
		return gain;
	}

	public AudioParamBO setGain(int gain) {
		this.gain = gain;
		return this;
	}	
}
