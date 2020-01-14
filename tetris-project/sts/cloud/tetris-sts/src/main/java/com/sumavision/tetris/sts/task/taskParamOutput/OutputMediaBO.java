package com.sumavision.tetris.sts.task.taskParamOutput;

import java.io.Serializable;

public class OutputMediaBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2742043661468361962L;
	private String task_id;
	private String encode_id;
	private Integer pid;
	private Integer payload_type;
	private String type;
	public OutputMediaBO(String task_id, String encode_id, Integer pid,
			Integer payload_type, String type) {
		super();
		this.task_id = task_id;
		this.encode_id = encode_id;
		this.pid = pid;
		this.payload_type = payload_type;
		this.type = type;
	}
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
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public Integer getPayload_type() {
		return payload_type;
	}
	public void setPayload_type(Integer payload_type) {
		this.payload_type = payload_type;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
