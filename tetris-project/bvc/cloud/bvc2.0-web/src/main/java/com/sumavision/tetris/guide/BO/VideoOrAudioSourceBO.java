package com.sumavision.tetris.guide.BO;

public class VideoOrAudioSourceBO {
	
	private String template_source_id;

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

	public String getTemplate_source_id() {
		return template_source_id;
	}

	public VideoOrAudioSourceBO setTemplate_source_id(String template_source_id) {
		this.template_source_id = template_source_id;
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
