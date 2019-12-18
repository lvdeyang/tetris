package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;
import java.util.ArrayList;

public class Encode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3510210010789604573L;
	private ArrayList<TaskEncodeProcess> process_array;
	private String encode_id;
	private EncodeCommon encodeCommon;
	
	public ArrayList<TaskEncodeProcess> getProcess_array() {
		return process_array;
	}
	public void setProcess_array(ArrayList<TaskEncodeProcess> process_array) {
		this.process_array = process_array;
	}
	public String getEncode_id() {
		return encode_id;
	}
	public void setEncode_id(String encode_id) {
		this.encode_id = encode_id;
	}
	public EncodeCommon getEncodeCommon() {
		return encodeCommon;
	}
	public void setEncodeCommon(EncodeCommon encodeCommon) {
		this.encodeCommon = encodeCommon;
	}
	
}
