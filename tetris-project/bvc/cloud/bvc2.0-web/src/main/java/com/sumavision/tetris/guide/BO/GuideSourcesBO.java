package com.sumavision.tetris.guide.BO;

public class GuideSourcesBO {
	
	private VideoOrAudioSourceBO video_source;
	
	private VideoOrAudioSourceBO audio_source;

	public VideoOrAudioSourceBO getVideo_source() {
		return video_source;
	}

	public GuideSourcesBO setVideo_source(VideoOrAudioSourceBO video_source) {
		this.video_source = video_source;
		return this;
	}

	public VideoOrAudioSourceBO getAudio_source() {
		return audio_source;
	}

	public GuideSourcesBO setAudio_source(VideoOrAudioSourceBO audio_source) {
		this.audio_source = audio_source;
		return this;
	}

}
