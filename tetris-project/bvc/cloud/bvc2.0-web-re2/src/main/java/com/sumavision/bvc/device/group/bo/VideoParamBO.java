package com.sumavision.bvc.device.group.bo;

import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.tetris.bvc.business.dispatch.po.DispatchVideoParamPO;

public class VideoParamBO {

	private String resolution = "";
	
	private String codec = "";
	
	private String bit_depth = "8bit";
	
	private String chroma_subsampling = "YUV420";
	
	private String fps = "25.0";
	
	private String gop_size = "25.0";
	
	private int bitrate = 0;
	
	private String profile = "main";
	
	private String input_interface = "SDI_4X3G";//COLOR_BAR
	
	private String output_interface = "CTRL";
	
	private boolean transcode = false;//解码器是否需要先转码

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

	public String getGop_size() {
		return gop_size;
	}

	public VideoParamBO setGop_size(String gop_size) {
		this.gop_size = gop_size;
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
	
	public String getInput_interface() {
		return input_interface;
	}

	public VideoParamBO setInput_interface(String input_interface) {
		this.input_interface = input_interface;
		return this;
	}

	public String getOutput_interface() {
		return output_interface;
	}

	public VideoParamBO setOutput_interface(String output_interface) {
		this.output_interface = output_interface;
		return this;
	}

	public boolean isTranscode() {
		return transcode;
	}

	public VideoParamBO setTranscode(boolean transcode) {
		this.transcode = transcode;
		return this;
	}

	public VideoParamBO set(DispatchVideoParamPO videoParam){
		this.bit_depth = videoParam.getBit_depth();
		this.bitrate = videoParam.getBitrate();
		this.chroma_subsampling = videoParam.getChroma_subsampling();
		this.codec = videoParam.getCodec();
		this.fps = videoParam.getFps();
		this.profile = videoParam.getProfile();
		this.resolution = videoParam.getResolution();
		return this;
	}

	public VideoParamBO copy(VideoParamBO videoParam){
		this.transcode = videoParam.isTranscode();
		this.bit_depth = videoParam.getBit_depth();
		this.bitrate = videoParam.getBitrate();
		this.chroma_subsampling = videoParam.getChroma_subsampling();
		this.codec = videoParam.getCodec();
		this.fps = videoParam.getFps();
		this.profile = videoParam.getProfile();
		this.resolution = videoParam.getResolution();
		this.input_interface = videoParam.getInput_interface();
		this.output_interface = videoParam.getOutput_interface();
		return this;
	}

	/**
	 * 根据gears数据生成对象
	 * @param gearsPO
	 * @return
	 */
	public VideoParamBO set(DeviceGroupAvtplGearsPO gearsPO){
		this.bitrate = Integer.parseInt(gearsPO.getVideoBitRate());
		this.codec = gearsPO.getVideoFormat().getName();
		this.fps = gearsPO.getFps();
		this.resolution = gearsPO.getVideoResolution().getName();
		return this;
	}
	
}
