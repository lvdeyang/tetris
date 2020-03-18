package com.sumavision.tetris.agent.vo;

public class CodecParamVO {

	private VideoParamVO video_param;
	
	private AudioParamVO audio_param;

	public VideoParamVO getVideo_param() {
		return video_param;
	}

	public CodecParamVO setVideo_param(VideoParamVO video_param) {
		this.video_param = video_param;
		return this;
	}

	public AudioParamVO getAudio_param() {
		return audio_param;
	}

	public CodecParamVO setAudio_param(AudioParamVO audio_param) {
		this.audio_param = audio_param;
		return this;
	}
	
}
