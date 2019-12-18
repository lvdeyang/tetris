package com.sumavision.tetris.sts.task;

import java.io.Serializable;
import java.util.Objects;

import com.sumavision.tetris.sts.task.source.AudioParamPO;

public class AudioParamBONew implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8308128072930869398L;

	private Integer pid;
	
	private int trackId;
	
	private String codec;
	
	private Integer bitrate;
	
	//采样频率
	private Integer sample_rate;
	
	private Integer sample_fmt;
	
	private Integer sample_byte;
	
	private String channel_layout;
	
	private String type;
	
	//g711a、g711u 默认时长
	private Integer ptime;

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public int getTrackId() {
		return trackId;
	}

	public void setTrackId(int trackId) {
		this.trackId = trackId;
	}

	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

	public Integer getBitrate() {
		return bitrate;
	}

	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}

	public Integer getSample_rate() {
		return sample_rate;
	}

	public void setSample_rate(Integer sample_rate) {
		this.sample_rate = sample_rate;
	}

	public Integer getSample_fmt() {
		return sample_fmt;
	}

	public void setSample_fmt(Integer sample_fmt) {
		this.sample_fmt = sample_fmt;
	}

	public Integer getSample_byte() {
		return sample_byte;
	}

	public void setSample_byte(Integer sample_byte) {
		this.sample_byte = sample_byte;
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

	public Integer getPtime() {
		return ptime;
	}

	public void setPtime(Integer ptime) {
		this.ptime = ptime;
	}

	public AudioParamPO transToAudioParamPO(){
		AudioParamPO audioParamPO = new AudioParamPO();
		audioParamPO.setPid(pid);
		audioParamPO.setTrackId(trackId);
		audioParamPO.setCodec(codec);
		audioParamPO.setSample(sample_rate.longValue());
		audioParamPO.setBitrate(bitrate.longValue());
		//audioParamPO.setVolume(volume);
		//audioParamPO.setAgcGain(agcGain);
		//audioParamPO.setAudioDupMode(audioDupMode);
		//audioParamPO.setChLayout(chLayout);
		//audioParamPO.setDenoise(denoise);
		
		return audioParamPO;
	}

}
