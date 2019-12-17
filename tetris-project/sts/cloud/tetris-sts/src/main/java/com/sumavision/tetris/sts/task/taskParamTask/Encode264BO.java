package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

/////暂时不用了
public class Encode264BO implements EncodeCommon, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7036041472558175241L;
	
	private String resolution;
	private Integer bit_depth;
	private String profile;
	private String ratio;
	private Integer level;
	private String fps;
	private Integer bitrate;
	private Integer max_bitrate;
	private Integer keyint_min;
	private Integer keyint_max;
	private Integer gop_size;
	private Integer ref_frames;
	private Integer b_frame;
	private String refine;
	private String bframe_adaptive;
	private String bframe_reference;
	private Integer me_range;
	private Integer lookahead;
	private String open_gop;
	private String scenecut;
	private String mbtree_switch;
	private String entropy_type;
	private String rc_mode;
	private String encoding_type;
	private Integer nv_card_idx;
	private Integer stastical_id;
	private String stastical_flag;
	public Encode264BO(String resolution, Integer bit_depth, String profile,
			String ratio, Integer level, String fps, Integer bitrate,
			Integer max_bitrate, Integer keyint_min, Integer keyint_max,
			Integer gop_size, Integer ref_frames, Integer b_frame,
			String refine, String bframe_adaptive, String bframe_reference,
			Integer me_range, Integer lookahead, String open_gop,
			String scenecut, String mbtree_switch, String entropy_type,
			String rc_mode, String encoding_type, Integer nv_card_idx,
			Integer stastical_id, String stastical_flag) {
		super();
		this.resolution = resolution;
		this.bit_depth = bit_depth;
		this.profile = profile;
		this.ratio = ratio;
		this.level = level;
		this.fps = fps;
		this.bitrate = bitrate;
		this.max_bitrate = max_bitrate;
		this.keyint_min = keyint_min;
		this.keyint_max = keyint_max;
		this.gop_size = gop_size;
		this.ref_frames = ref_frames;
		this.b_frame = b_frame;
		this.refine = refine;
		this.bframe_adaptive = bframe_adaptive;
		this.bframe_reference = bframe_reference;
		this.me_range = me_range;
		this.lookahead = lookahead;
		this.open_gop = open_gop;
		this.scenecut = scenecut;
		this.mbtree_switch = mbtree_switch;
		this.entropy_type = entropy_type;
		this.rc_mode = rc_mode;
		this.encoding_type = encoding_type;
		this.nv_card_idx = nv_card_idx;
		this.stastical_id = stastical_id;
		this.stastical_flag = stastical_flag;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public Integer getBit_depth() {
		return bit_depth;
	}
	public void setBit_depth(Integer bit_depth) {
		this.bit_depth = bit_depth;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
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
	public Integer getMax_bitrate() {
		return max_bitrate;
	}
	public void setMax_bitrate(Integer max_bitrate) {
		this.max_bitrate = max_bitrate;
	}
	public Integer getKeyint_min() {
		return keyint_min;
	}
	public void setKeyint_min(Integer keyint_min) {
		this.keyint_min = keyint_min;
	}
	public Integer getKeyint_max() {
		return keyint_max;
	}
	public void setKeyint_max(Integer keyint_max) {
		this.keyint_max = keyint_max;
	}
	public Integer getGop_size() {
		return gop_size;
	}
	public void setGop_size(Integer gop_size) {
		this.gop_size = gop_size;
	}
	public Integer getRef_frames() {
		return ref_frames;
	}
	public void setRef_frames(Integer ref_frames) {
		this.ref_frames = ref_frames;
	}
	public Integer getB_frame() {
		return b_frame;
	}
	public void setB_frame(Integer b_frame) {
		this.b_frame = b_frame;
	}
	public String getRefine() {
		return refine;
	}
	public void setRefine(String refine) {
		this.refine = refine;
	}
	public String getBframe_adaptive() {
		return bframe_adaptive;
	}
	public void setBframe_adaptive(String bframe_adaptive) {
		this.bframe_adaptive = bframe_adaptive;
	}
	public String getBframe_reference() {
		return bframe_reference;
	}
	public void setBframe_reference(String bframe_reference) {
		this.bframe_reference = bframe_reference;
	}
	public Integer getMe_range() {
		return me_range;
	}
	public void setMe_range(Integer me_range) {
		this.me_range = me_range;
	}
	public Integer getLookahead() {
		return lookahead;
	}
	public void setLookahead(Integer lookahead) {
		this.lookahead = lookahead;
	}
	public String getOpen_gop() {
		return open_gop;
	}
	public void setOpen_gop(String open_gop) {
		this.open_gop = open_gop;
	}
	public String getScenecut() {
		return scenecut;
	}
	public void setScenecut(String scenecut) {
		this.scenecut = scenecut;
	}
	public String getMbtree_switch() {
		return mbtree_switch;
	}
	public void setMbtree_switch(String mbtree_switch) {
		this.mbtree_switch = mbtree_switch;
	}
	public String getEntropy_type() {
		return entropy_type;
	}
	public void setEntropy_type(String entropy_type) {
		this.entropy_type = entropy_type;
	}
	public String getRc_mode() {
		return rc_mode;
	}
	public void setRc_mode(String rc_mode) {
		this.rc_mode = rc_mode;
	}
	public String getEncoding_type() {
		return encoding_type;
	}
	public void setEncoding_type(String encoding_type) {
		this.encoding_type = encoding_type;
	}
	public Integer getNv_card_idx() {
		return nv_card_idx;
	}
	public void setNv_card_idx(Integer nv_card_idx) {
		this.nv_card_idx = nv_card_idx;
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
