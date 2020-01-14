package com.sumavision.tetris.sts.task.taskParamOutput;

import java.io.Serializable;

public class JsonHlsVideoBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2953821138238851539L;
	private String task_id;
	private String encode_id;
	private String codec;
	private String resolution;
	private Integer bitrate;
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	public String getEncode_id() {
		return encode_id;
	}
	public void setEncode_id(String encode_id) {
		this.encode_id = encode_id;
	}
	public String getCodec() {
		return codec;
	}
	public void setCodec(String codec) {
		this.codec = codec;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public Integer getBitrate() {
		return bitrate;
	}
	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}
	

}
