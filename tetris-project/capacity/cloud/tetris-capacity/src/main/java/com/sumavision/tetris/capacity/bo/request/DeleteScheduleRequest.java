package com.sumavision.tetris.capacity.bo.request;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.capacity.bo.input.InputBO;

import java.util.List;

public class DeleteScheduleRequest {
	
	private String msg_id;

	private List<InputIdRequest> delete_inputs;

	public String getMsg_id() {
		return msg_id;
	}

	public DeleteScheduleRequest setMsg_id(String msg_id) {
		this.msg_id = msg_id;
		return this;
	}

	public List<InputIdRequest> getDelete_inputs() {
		return delete_inputs;
	}

	public void setDelete_inputs(List<InputIdRequest> delete_inputs) {
		this.delete_inputs = delete_inputs;
	}
}
