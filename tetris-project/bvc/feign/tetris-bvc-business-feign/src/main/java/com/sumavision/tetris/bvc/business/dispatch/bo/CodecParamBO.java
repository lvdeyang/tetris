package com.sumavision.tetris.bvc.business.dispatch.bo;

public class CodecParamBO {

	private AudioParamBO audio_param = new AudioParamBO();
	
	private VideoParamBO video_param = new VideoParamBO();

	public AudioParamBO getAudio_param() {
		return audio_param;
	}

	public CodecParamBO setAudio_param(AudioParamBO audio_param) {
		this.audio_param = audio_param;
		return this;
	}

	public VideoParamBO getVideo_param() {
		return video_param;
	}

	public CodecParamBO setVideo_param(VideoParamBO video_param) {
		this.video_param = video_param;
		return this;
	}
	
}
