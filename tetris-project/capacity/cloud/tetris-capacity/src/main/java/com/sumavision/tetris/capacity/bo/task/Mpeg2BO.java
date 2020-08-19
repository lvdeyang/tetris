package com.sumavision.tetris.capacity.bo.task;

import com.alibaba.fastjson.JSONObject;

/**
 * mpeg2参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月30日 下午4:45:40
 */
public class Mpeg2BO extends BaseEncodeBO<Mpeg2BO>{
	
	private Integer level;
	
	private String profile;
	
	private Integer ref_frames;
	
	private String encoding_type;

	private Mpeg2ObjectBO m2v;
	
	private JSONObject msdk_encode;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
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

	public Mpeg2ObjectBO getM2v() {
		return m2v;
	}

	public Mpeg2BO setM2v(Mpeg2ObjectBO m2v) {
		this.m2v = m2v;
		return this;
	}

	public JSONObject getMsdk_encode() {
		return msdk_encode;
	}

	public Mpeg2BO setMsdk_encode(JSONObject msdk_encode) {
		this.msdk_encode = msdk_encode;
		return this;
	}
	
}
