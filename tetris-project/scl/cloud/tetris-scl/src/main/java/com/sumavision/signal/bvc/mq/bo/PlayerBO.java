package com.sumavision.signal.bvc.mq.bo;

public class PlayerBO {

	private String udpUrl;
	
	private SourceBO source;
	
	private BaseParamBO video_param;
	
	private BaseParamBO audio_param;

	public String getUdpUrl() {
		return udpUrl;
	}

	public void setUdpUrl(String udpUrl) {
		this.udpUrl = udpUrl;
	}

	public SourceBO getSource() {
		return source;
	}

	public void setSource(SourceBO source) {
		this.source = source;
	}

	public BaseParamBO getVideo_param() {
		return video_param;
	}

	public void setVideo_param(BaseParamBO video_param) {
		this.video_param = video_param;
	}

	public BaseParamBO getAudio_param() {
		return audio_param;
	}

	public void setAudio_param(BaseParamBO audio_param) {
		this.audio_param = audio_param;
	}
	
}
