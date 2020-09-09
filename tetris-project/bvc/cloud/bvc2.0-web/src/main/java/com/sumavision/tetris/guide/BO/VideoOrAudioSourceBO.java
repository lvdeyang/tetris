package com.sumavision.tetris.guide.BO;

public class VideoOrAudioSourceBO {

	private String layer_id;
	
	private String bundle_id;
	
	private String channel_id;

	public String getLayer_id() {
		return layer_id;
	}

	public VideoOrAudioSourceBO setLayer_id(String layer_id) {
		this.layer_id = layer_id;
		return this;
	}

	public String getBundle_id() {
		return bundle_id;
	}

	public VideoOrAudioSourceBO setBundle_id(String bundle_id) {
		this.bundle_id = bundle_id;
		return this;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public VideoOrAudioSourceBO setChannel_id(String channel_id) {
		this.channel_id = channel_id;
		return this;
	}

	@Override
	public String toString() {
		return "VideoOrAudioSourceBO [layer_id=" + layer_id + ", bundle_id=" + bundle_id + ", channel_id=" + channel_id + "]";
	}
}
