package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class Encode265TotalBO implements EncodeVideoCommon, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 981680029046187461L;
	private Integer width;
	private Integer height;
	private String ratio;
	private String fps;
	private Integer max_bframe;
	private Integer gop_size;
	private String rc_mode;
	private Integer bitrate;
	private Integer max_bitrate;
	private Boolean open_gop;
	private String profile;
	private Integer level;
	private Boolean mbtree;
	private Integer ref_frames;
	private Integer lookahead;
	private Boolean bframe_adaptive;
	private Integer vbv_buffer_size;
	private Encode265Common encode265Common;
	public Encode265TotalBO(){
		super();
	}
	
	public Encode265TotalBO(Integer width, Integer height, String ratio,
			String fps, Integer max_bframe, Integer gop_size, String rc_mode,
			Integer bitrate, Integer max_bitrate, Boolean open_gop,
			String profile, Integer level, Boolean mbtree, Integer ref_frames,
			Integer lookahead, Boolean bframe_adaptive,
			Integer vbv_buffer_size, Encode265Common encode265Common) {
		super();
		this.width = width;
		this.height = height;
		this.ratio = ratio;
		this.fps = fps;
		this.max_bframe = max_bframe;
		this.gop_size = gop_size;
		this.rc_mode = rc_mode;
		this.bitrate = bitrate;
		this.max_bitrate = max_bitrate;
		this.open_gop = open_gop;
		this.profile = profile;
		this.level = level;
		this.mbtree = mbtree;
		this.ref_frames = ref_frames;
		this.lookahead = lookahead;
		this.bframe_adaptive = bframe_adaptive;
		this.vbv_buffer_size = vbv_buffer_size;
		this.encode265Common = encode265Common;
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
	public Integer getBitrate() {
		return bitrate;
	}
	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}
	public Integer getMax_bframe() {
		return max_bframe;
	}
	public void setMax_bframe(Integer max_bframe) {
		this.max_bframe = max_bframe;
	}
	public Integer getMax_bitrate() {
		return max_bitrate;
	}
	public void setMax_bitrate(Integer max_bitrate) {
		this.max_bitrate = max_bitrate;
	}
	public Integer getGop_size() {
		return gop_size;
	}
	public void setGop_size(Integer gop_size) {
		this.gop_size = gop_size;
	}
	public String getRc_mode() {
		return rc_mode;
	}
	public void setRc_mode(String rc_mode) {
		this.rc_mode = rc_mode;
	}
	public Boolean getOpen_gop() {
		return open_gop;
	}
	public void setOpen_gop(Boolean open_gop) {
		this.open_gop = open_gop;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Boolean getMbtree() {
		return mbtree;
	}
	public void setMbtree(Boolean mbtree) {
		this.mbtree = mbtree;
	}
	public Integer getRef_frames() {
		return ref_frames;
	}
	public void setRef_frames(Integer ref_frames) {
		this.ref_frames = ref_frames;
	}
	public Integer getLookahead() {
		return lookahead;
	}
	public void setLookahead(Integer lookahead) {
		this.lookahead = lookahead;
	}
	public Boolean getBframe_adaptive() {
		return bframe_adaptive;
	}
	public void setBframe_adaptive(Boolean bframe_adaptive) {
		this.bframe_adaptive = bframe_adaptive;
	}
	public Integer getVbv_buffer_size() {
		return vbv_buffer_size;
	}
	public void setVbv_buffer_size(Integer vbv_buffer_size) {
		this.vbv_buffer_size = vbv_buffer_size;
	}
	public Encode265Common getEncode265Common() {
		return encode265Common;
	}
	public void setEncode265Common(Encode265Common encode265Common) {
		this.encode265Common = encode265Common;
	}
	

}
