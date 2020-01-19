package com.sumavision.signal.bvc.entity.vo;

import com.sumavision.signal.bvc.mq.bo.BaseParamBO;

public class TranscodeVO {

	private String ip;
	
	private Long port;
	
	private String udp;
	
	private String bundleId;
	
	private BaseParamBO video;
	
	private BaseParamBO audio;

	public String getIp() {
		return ip;
	}

	public TranscodeVO setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public Long getPort() {
		return port;
	}

	public TranscodeVO setPort(Long port) {
		this.port = port;
		return this;
	}

	public String getUdp() {
		return udp;
	}

	public TranscodeVO setUdp(String udp) {
		this.udp = udp;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public TranscodeVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public BaseParamBO getVideo() {
		return video;
	}

	public TranscodeVO setVideo(BaseParamBO video) {
		this.video = video;
		return this;
	}

	public BaseParamBO getAudio() {
		return audio;
	}

	public TranscodeVO setAudio(BaseParamBO audio) {
		this.audio = audio;
		return this;
	}
	
}
