package com.sumavision.tetris.sts.task.taskParamOutput;

import java.io.Serializable;

public class JsonHlsSubtitleBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5933095810769275364L;

	private String task_id;
	private String encode_id;
	private String codec;
	private String language;
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
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
}
