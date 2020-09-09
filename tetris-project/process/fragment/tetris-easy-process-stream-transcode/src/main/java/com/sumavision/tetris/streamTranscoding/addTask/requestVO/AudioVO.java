package com.sumavision.tetris.streamTranscoding.addTask.requestVO;

import javax.xml.bind.annotation.XmlAttribute;

public class AudioVO {
	private String codec;
	
	private Long sample;
	
	private Long bitrate;
	
	private Integer pid;
	
	private String agcGain;
	
	private String denoise;
	
	private String chLayout;
	
	private Integer volume;
	
	public AudioVO(){}
	
	public AudioVO(String codec){
		this(codec, 48000l, 64000l, 256, "off", "off", "stereo", 0);
	}
	
	public AudioVO(String codec, Long sample, Long bitrate, String chLayout){
		this(codec, sample, bitrate, 256, "off", "off", chLayout, 0);
	}
	
	public AudioVO(
			String codec,
			Long sample,
			Long bitrate,
			Integer pid,
			String agcGain,
			String denoise,
			String chLayout,
			Integer volume) {
		this.codec = codec;
		this.sample = sample;
		this.bitrate = bitrate;
		this.pid = pid;
		this.agcGain = agcGain;
		this.denoise = denoise;
		this.chLayout = chLayout;
		this.volume = volume;
	}

	public String getCodec() {
		return codec;
	}
	
	@XmlAttribute(name = "codec")
	public void setCodec(String codec) {
		this.codec = codec;
	}

	
	public Long getSample() {
		return sample;
	}

	@XmlAttribute(name = "sample")
	public void setSample(Long sample) {
		this.sample = sample;
	}

	public Long getBitrate() {
		return bitrate;
	}

	@XmlAttribute(name = "bitrate")
	public void setBitrate(Long bitrate) {
		this.bitrate = bitrate;
	}

	public Integer getPid() {
		return pid;
	}

	@XmlAttribute(name = "pid")
	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getAgcGain() {
		return agcGain;
	}

	@XmlAttribute(name = "agc-gain")
	public void setAgcGain(String agcGain) {
		this.agcGain = agcGain;
	}

	public String getDenoise() {
		return denoise;
	}

	@XmlAttribute(name = "denoise")
	public void setDenoise(String denoise) {
		this.denoise = denoise;
	}

	public String getChLayout() {
		return chLayout;
	}

	@XmlAttribute(name = "ch-layout")
	public void setChLayout(String chLayout) {
		this.chLayout = chLayout;
	}

	public Integer getVolume() {
		return volume;
	}

	@XmlAttribute(name = "volume")
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	
	public AudioVO copy() {
		return new AudioVO(this.getCodec(), this.getSample(), this.getBitrate(), this.getChLayout());
	}
}
