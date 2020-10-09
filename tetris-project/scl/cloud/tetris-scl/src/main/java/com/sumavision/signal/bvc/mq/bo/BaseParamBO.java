package com.sumavision.signal.bvc.mq.bo;

import java.util.List;

public class BaseParamBO {

	private String codec;
	
	private String profile;
	
	private String fps;
	
	private Long bitrate;
	
	private String chroma_subsampling;
	
	private String bit_depth;
	
	private String resolution;
	
	private Long sample_rate;
	
	private String channel_cnt;
	
	private String bit_width;
	
	private String gain;
	
	private SourceBO source;

	private String output_interface;

	private String input_interface;

	private List<SourceBO> sources;

	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getFps() {
		return fps;
	}

	public void setFps(String fps) {
		this.fps = fps;
	}

	public Long getBitrate() {
		return bitrate;
	}

	public void setBitrate(Long bitrate) {
		this.bitrate = bitrate;
	}

	public String getChroma_subsampling() {
		return chroma_subsampling;
	}

	public void setChroma_subsampling(String chroma_subsampling) {
		this.chroma_subsampling = chroma_subsampling;
	}

	public String getBit_depth() {
		return bit_depth;
	}

	public void setBit_depth(String bit_depth) {
		this.bit_depth = bit_depth;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public Long getSample_rate() {
		return sample_rate;
	}

	public void setSample_rate(Long sample_rate) {
		this.sample_rate = sample_rate;
	}

	public String getChannel_cnt() {
		return channel_cnt;
	}

	public void setChannel_cnt(String channel_cnt) {
		this.channel_cnt = channel_cnt;
	}

	public String getBit_width() {
		return bit_width;
	}

	public void setBit_width(String bit_width) {
		this.bit_width = bit_width;
	}

	public String getGain() {
		return gain;
	}

	public void setGain(String gain) {
		this.gain = gain;
	}

	public SourceBO getSource() {
		return source;
	}

	public void setSource(SourceBO source) {
		this.source = source;
	}

	public String getOutput_interface() {
		return output_interface;
	}

	public void setOutput_interface(String output_interface) {
		this.output_interface = output_interface;
	}

	public String getInput_interface() {
		return input_interface;
	}

	public void setInput_interface(String input_interface) {
		this.input_interface = input_interface;
	}

	public List<SourceBO> getSources() {
		return sources;
	}

	public void setSources(List<SourceBO> sources) {
		this.sources = sources;
	}
}
