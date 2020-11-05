package com.sumavision.signal.bvc.capacity.bo.task;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 编码参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月30日 上午9:58:08
 */
public class EncodeBO {

	/** 编码预处理 */
	private List<PreProcessingBO> process_array;

	private String encode_id;

	private JSONObject h264;

	private JSONObject hevc;

	private JSONObject mpeg2;

	private JSONObject avs2;
	
	@JSONField(name = "avs+")
	private AvsPlusBO avs_plus;

	private JSONObject aac;

	private JSONObject dolby;

	private G711BO g711a;

	private G711BO g711u;

	private JSONObject mp3;

	private JSONObject mp2;

	private JSONObject passby;

	public List<PreProcessingBO> getProcess_array() {
		return process_array;
	}

	public EncodeBO setProcess_array(List<PreProcessingBO> process_array) {
		this.process_array = process_array;
		return this;
	}

	public String getEncode_id() {
		return encode_id;
	}

	public EncodeBO setEncode_id(String encode_id) {
		this.encode_id = encode_id;
		return this;
	}

	public JSONObject getH264() {
		return h264;
	}

	public EncodeBO setH264(JSONObject h264) {
		this.h264 = h264;
		return this;
	}

	public JSONObject getHevc() {
		return hevc;
	}

	public EncodeBO setHevc(JSONObject hevc) {
		this.hevc = hevc;
		return this;
	}

	public JSONObject getMpeg2() {
		return mpeg2;
	}

	public EncodeBO setMpeg2(JSONObject mpeg2) {
		this.mpeg2 = mpeg2;
		return this;
	}

	public AvsPlusBO getAvs_plus() {
		return avs_plus;
	}

	public EncodeBO setAvs_plus(AvsPlusBO avs_plus) {
		this.avs_plus = avs_plus;
		return this;
	}

	public JSONObject getAac() {
		return aac;
	}

	public EncodeBO setAac(JSONObject aac) {
		this.aac = aac;
		return this;
	}

	public JSONObject getDolby() {
		return dolby;
	}

	public EncodeBO setDolby(JSONObject dolby) {
		this.dolby = dolby;
		return this;
	}

	public G711BO getG711a() {
		return g711a;
	}

	public EncodeBO setG711a(G711BO g711a) {
		this.g711a = g711a;
		return this;
	}

	public G711BO getG711u() {
		return g711u;
	}

	public EncodeBO setG711u(G711BO g711u) {
		this.g711u = g711u;
		return this;
	}

	public JSONObject getMp3() {
		return mp3;
	}

	public EncodeBO setMp3(JSONObject mp3) {
		this.mp3 = mp3;
		return this;
	}

	public JSONObject getMp2() {
		return mp2;
	}

	public EncodeBO setMp2(JSONObject mp2) {
		this.mp2 = mp2;
		return this;
	}

	public JSONObject getPassby() {
		return passby;
	}

	public EncodeBO setPassby(JSONObject passby) {
		this.passby = passby;
		return this;
	}

}
