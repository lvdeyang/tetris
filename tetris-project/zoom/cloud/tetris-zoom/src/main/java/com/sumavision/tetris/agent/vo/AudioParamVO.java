package com.sumavision.tetris.agent.vo;

public class AudioParamVO {

	private String codec = "aac";

	private String bitrate = "64000";
	
	private int sample_rate = 48000;
	
	private int channel_cnt = 1;
	
	private int bit_width = 16;

	private int gain = 25;

	public String getCodec() {
		return codec;
	}

	public AudioParamVO setCodec(String codec) {
		this.codec = codec;
		return this;
	}

	public String getBitrate() {
		return bitrate;
	}

	public AudioParamVO setBitrate(String bitrate) {
		this.bitrate = bitrate;
		return this;
	}

	public int getSample_rate() {
		return sample_rate;
	}

	public AudioParamVO setSample_rate(int sample_rate) {
		this.sample_rate = sample_rate;
		return this;
	}

	public int getChannel_cnt() {
		return channel_cnt;
	}

	public AudioParamVO setChannel_cnt(int channel_cnt) {
		this.channel_cnt = channel_cnt;
		return this;
	}

	public int getBit_width() {
		return bit_width;
	}

	public AudioParamVO setBit_width(int bit_width) {
		this.bit_width = bit_width;
		return this;
	}

	public int getGain() {
		return gain;
	}

	public AudioParamVO setGain(int gain) {
		this.gain = gain;
		return this;
	}
	
}
