package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class EncodeAvsPlusBO implements EncodeVideoCommon, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6367340232250979723L;
	
	private String resolution;
	private String ratio;
	private Integer keyint_max;
	private Integer keyint_min;
	private Integer b_frame;
	private Integer ref_frames;
	private String encoding_type;
	private String refine;
	private Integer bitrate;
	private String rc_model;
	private String scenecut;
	private String bframe_adaptive;
	private Integer lookahead;
	private Integer me_range;
	private String two_pass;
	private String fps;
	private Integer stastical_id;
	private String stastical_flag;
	public EncodeAvsPlusBO(String resolution, String ratio, Integer keyint_max,
			Integer keyint_min, Integer b_frame, Integer ref_frames,
			String encoding_type, String refine, Integer bitrate,
			String rc_model, String scenecut, String bframe_adaptive,
			Integer lookahead, Integer me_range, String two_pass, String fps,
			Integer stastical_id, String stastical_flag) {
		super();
		this.resolution = resolution;
		this.ratio = ratio;
		this.keyint_max = keyint_max;
		this.keyint_min = keyint_min;
		this.b_frame = b_frame;
		this.ref_frames = ref_frames;
		this.encoding_type = encoding_type;
		this.refine = refine;
		this.bitrate = bitrate;
		this.rc_model = rc_model;
		this.scenecut = scenecut;
		this.bframe_adaptive = bframe_adaptive;
		this.lookahead = lookahead;
		this.me_range = me_range;
		this.two_pass = two_pass;
		this.fps = fps;
		this.stastical_id = stastical_id;
		this.stastical_flag = stastical_flag;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public Integer getKeyint_max() {
		return keyint_max;
	}
	public void setKeyint_max(Integer keyint_max) {
		this.keyint_max = keyint_max;
	}
	public Integer getKeyint_min() {
		return keyint_min;
	}
	public void setKeyint_min(Integer keyint_min) {
		this.keyint_min = keyint_min;
	}
	public Integer getB_frame() {
		return b_frame;
	}
	public void setB_frame(Integer b_frame) {
		this.b_frame = b_frame;
	}
	public Integer getRef_frames() {
		return ref_frames;
	}
	public void setRef_frames(Integer ref_frames) {
		this.ref_frames = ref_frames;
	}
	public String getEncoding_type() {
		return encoding_type;
	}
	public void setEncoding_type(String encoding_type) {
		this.encoding_type = encoding_type;
	}
	public String getRefine() {
		return refine;
	}
	public void setRefine(String refine) {
		this.refine = refine;
	}
	public Integer getBitrate() {
		return bitrate;
	}
	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}
	public String getRc_model() {
		return rc_model;
	}
	public void setRc_model(String rc_model) {
		this.rc_model = rc_model;
	}
	public String getScenecut() {
		return scenecut;
	}
	public void setScenecut(String scenecut) {
		this.scenecut = scenecut;
	}
	public String getBframe_adaptive() {
		return bframe_adaptive;
	}
	public void setBframe_adaptive(String bframe_adaptive) {
		this.bframe_adaptive = bframe_adaptive;
	}
	public Integer getLookahead() {
		return lookahead;
	}
	public void setLookahead(Integer lookahead) {
		this.lookahead = lookahead;
	}
	public Integer getMe_range() {
		return me_range;
	}
	public void setMe_range(Integer me_range) {
		this.me_range = me_range;
	}
	public String getTwo_pass() {
		return two_pass;
	}
	public void setTwo_pass(String two_pass) {
		this.two_pass = two_pass;
	}
	public String getFps() {
		return fps;
	}
	public void setFps(String fps) {
		this.fps = fps;
	}
	public Integer getStastical_id() {
		return stastical_id;
	}
	public void setStastical_id(Integer stastical_id) {
		this.stastical_id = stastical_id;
	}
	public String getStastical_flag() {
		return stastical_flag;
	}
	public void setStastical_flag(String stastical_flag) {
		this.stastical_flag = stastical_flag;
	}
	

}
