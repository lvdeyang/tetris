package com.sumavision.bvc.device.group.bo;

import com.sumavision.tetris.bvc.business.dispatch.po.DispatchAudioParamPO;

public class AudioParamBO {

	private String codec = "";

	private int bitrate = 64000;
	
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

	public int getBitrate() {
		return bitrate;
	}

	public AudioParamBO setBitrate(int bitrate) {
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
	
	public AudioParamBO set(DispatchAudioParamPO auidoParam){
		this.bit_width = auidoParam.getBit_width();
		this.bitrate = auidoParam.getBitrate();
		this.channel_cnt = auidoParam.getChannel_cnt();
		this.codec = auidoParam.getCodec();
		this.gain = auidoParam.getGain();
		this.sample_rate = auidoParam.getSample_rate();
		return this;
	}
	
	public AudioParamBO copy(AudioParamBO auidoParam){
		this.bit_width = auidoParam.getBit_width();
		this.bitrate = auidoParam.getBitrate();
		this.channel_cnt = auidoParam.getChannel_cnt();
		this.codec = auidoParam.getCodec();
		this.gain = auidoParam.getGain();
		this.sample_rate = auidoParam.getSample_rate();
		return this;
	}
}
