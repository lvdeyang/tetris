package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class EncodeMpeg2BO implements EncodeVideoCommon, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2751951329274206940L;
	private Integer width;
	private Integer height;
	private String ratio;
	private String fps;
	private String rc_mode;
	private Integer bitrate;
	private Integer max_bitrate;
	private Integer max_bframe;
	private Integer gop_size;
	private Boolean open_gop;
	private EncodeMpeg2M2vBO m2v;
	public EncodeMpeg2BO(){
		super();
	}
	
	public EncodeMpeg2BO(Integer width, Integer height, String ratio,
			String fps, String rc_mode, Integer bitrate, Integer max_bitrate,
			Integer max_bframe, Integer gop_size, Boolean open_gop,
			EncodeMpeg2M2vBO m2v) {
		super();
		this.width = width;
		this.height = height;
		this.ratio = ratio;
		this.fps = fps;
		this.rc_mode = rc_mode;
		this.bitrate = bitrate;
		this.max_bitrate = max_bitrate;
		this.max_bframe = max_bframe;
		this.gop_size = gop_size;
		this.open_gop = open_gop;
		this.m2v = m2v;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public String getFps() {
		return fps;
	}
	public void setFps(String fps) {
		this.fps = fps;
	}
	public String getRc_mode() {
		return rc_mode;
	}
	public void setRc_mode(String rc_mode) {
		this.rc_mode = rc_mode;
	}
	public Integer getBitrate() {
		return bitrate;
	}
	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}
	public Integer getMax_bitrate() {
		return max_bitrate;
	}
	public void setMax_bitrate(Integer max_bitrate) {
		this.max_bitrate = max_bitrate;
	}
	public Integer getMax_bframe() {
		return max_bframe;
	}
	public void setMax_bframe(Integer max_bframe) {
		this.max_bframe = max_bframe;
	}
	public Integer getGop_size() {
		return gop_size;
	}
	public void setGop_size(Integer gop_size) {
		this.gop_size = gop_size;
	}
	public Boolean getOpen_gop() {
		return open_gop;
	}
	public void setOpen_gop(Boolean open_gop) {
		this.open_gop = open_gop;
	}
	public EncodeMpeg2M2vBO getM2v() {
		return m2v;
	}
	public void setM2v(EncodeMpeg2M2vBO m2v) {
		this.m2v = m2v;
	}
	
	
	
	
	
}
