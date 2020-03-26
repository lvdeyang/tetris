package com.sumavision.tetris.bvc.business.dispatch.bo;

public class VideoParamBO {

	private String resolution = "1920x1080";
	
	private String codec = "h264";
	
	private String bit_depth = "8bit";
	
	private String chroma_subsampling = "YUV420";
	
	private String fps = "25.0";
	
	private int bitrate = 0;
	
	private String profile = "main";

	public String getResolution() {
		return resolution;
	}

	public VideoParamBO setResolution(String resolution) {
		this.resolution = resolution;
		return this;
	}

	public String getCodec() {
		return codec;
	}

	public VideoParamBO setCodec(String codec) {
		this.codec = codec;
		return this;
	}

	public String getBit_depth() {
		return bit_depth;
	}

	public VideoParamBO setBit_depth(String bit_depth) {
		this.bit_depth = bit_depth;
		return this;
	}

	public String getChroma_subsampling() {
		return chroma_subsampling;
	}

	public VideoParamBO setChroma_subsampling(String chroma_subsampling) {
		this.chroma_subsampling = chroma_subsampling;
		return this;
	}

	public String getFps() {
		return fps;
	}

	public VideoParamBO setFps(String fps) {
		this.fps = fps;
		return this;
	}

	public int getBitrate() {
		return bitrate;
	}

//	public String getBitrate() {
//		return String.valueOf(bitrate);
//	}

	public VideoParamBO setBitrate(String bitrate) {
		this.bitrate = Integer.parseInt(bitrate);
		return this;
	}

	public String getProfile() {
		return profile;
	}

	public VideoParamBO setProfile(String profile) {
		this.profile = profile;
		return this;
	}
	
}
