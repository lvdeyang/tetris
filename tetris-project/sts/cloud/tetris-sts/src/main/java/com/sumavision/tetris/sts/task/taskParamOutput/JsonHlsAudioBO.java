package com.sumavision.tetris.sts.task.taskParamOutput;

import java.io.Serializable;

public class JsonHlsAudioBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7128796559152976798L;

	private String task_id;
	private String encode_id;
	private String codec;
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
	public Integer getBitrate() {
		return bitrate;
	}
	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}
	
}
