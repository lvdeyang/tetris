package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class Encode264TotalBO implements EncodeVideoCommon, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 221541981592635978L;

	private Integer width;
	private Integer height;
	private String ratio;
	private String fps;
	private Integer gop_size;
	private String rc_mode;
	private Integer bitrate;
	private Integer max_bitrate;
	private Integer max_bframe;
	private Boolean open_gop;
	private String profile;
	private Integer level;
	private Integer ref_frames;
	private Integer lookahead;
	private Boolean scenecut;
	private Boolean mbtree;
	private String entropy_type;
	private String encoding_type;
	private Integer vbv_buffer_size;
	private Encode264Common encode264Common;
	public Encode264TotalBO(Integer width, Encode264Common encode264Common){
		super();
		this.width = width;
		this.encode264Common = encode264Common;
	}
	public Encode264TotalBO(){
		super();
	}
	public Encode264TotalBO(Integer width, Integer height, String ratio,
			String fps, Integer gop_size, String rc_mode, Integer bitrate,
			Integer max_bitrate, Integer max_bframe, Boolean open_gop,
			String profile, Integer level, Integer ref_frames,
			Integer lookahead, Boolean scenecut, Boolean mbtree,
			String entropy_type, String encoding_type, Integer vbv_buffer_size,
			Encode264Common encode264Common) {
		super();
		this.width = width;
		this.height = height;
		this.ratio = ratio;
		this.fps = fps;
		this.gop_size = gop_size;
		this.rc_mode = rc_mode;
		this.bitrate = bitrate;
		this.max_bitrate = max_bitrate;
		this.max_bframe = max_bframe;
		this.open_gop = open_gop;
		this.profile = profile;
		this.level = level;
		this.ref_frames = ref_frames;
		this.lookahead = lookahead;
		this.scenecut = scenecut;
		this.mbtree = mbtree;
		this.entropy_type = entropy_type;
		this.encoding_type = encoding_type;
		this.vbv_buffer_size = vbv_buffer_size;
		this.encode264Common = encode264Common;
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
	public Boolean getScenecut() {
		return scenecut;
	}
	public void setScenecut(Boolean scenecut) {
		this.scenecut = scenecut;
	}
	public Boolean getMbtree() {
		return mbtree;
	}
	public void setMbtree(Boolean mbtree) {
		this.mbtree = mbtree;
	}
	public String getEntropy_type() {
		return entropy_type;
	}
	public void setEntropy_type(String entropy_type) {
		this.entropy_type = entropy_type;
	}
	public String getEncoding_type() {
		return encoding_type;
	}
	public void setEncoding_type(String encoding_type) {
		this.encoding_type = encoding_type;
	}
	public Integer getVbv_buffer_size() {
		return vbv_buffer_size;
	}
	public void setVbv_buffer_size(Integer vbv_buffer_size) {
		this.vbv_buffer_size = vbv_buffer_size;
	}
	public Encode264Common getEncode264Common() {
		return encode264Common;
	}
	public void setEncode264Common(Encode264Common encode264Common) {
		this.encode264Common = encode264Common;
	}
	
	
	
}
