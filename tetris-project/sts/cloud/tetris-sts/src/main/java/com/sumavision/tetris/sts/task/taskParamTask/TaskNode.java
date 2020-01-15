package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;
import java.util.ArrayList;



public class TaskNode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4991360813173778870L;
	private String id;
	private String type;
	private SourceCommon sourceCommon;
	private ArrayList<DecodeProcess> decode_process_array;   
	private ArrayList<Encode> encode_array;
	private String result_msg;
	private Integer result_code;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public SourceCommon getSourceCommon() {
		return sourceCommon;
	}
	public void setSourceCommon(SourceCommon sourceCommon) {
		this.sourceCommon = sourceCommon;
	}
	public ArrayList<DecodeProcess> getDecode_process_array() {
		return decode_process_array;
	}
	public void setDecode_process_array(ArrayList<DecodeProcess> decode_process_array) {
		this.decode_process_array = decode_process_array;
	}
	public ArrayList<Encode> getEncode_array() {
		return encode_array;
	}
	public void setEncode_array(ArrayList<Encode> encode_array) {
		this.encode_array = encode_array;
	}
	public String getResult_msg() {
		return result_msg;
	}
	public void setResult_msg(String result_msg) {
		this.result_msg = result_msg;
	}
	public Integer getResult_code() {
		return result_code;
	}
	public void setResult_code(Integer result_code) {
		this.result_code = result_code;
	} 
	

}
