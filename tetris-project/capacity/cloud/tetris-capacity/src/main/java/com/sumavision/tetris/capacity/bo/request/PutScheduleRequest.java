package com.sumavision.tetris.capacity.bo.request;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.capacity.bo.input.InputBO;

public class PutScheduleRequest {
	
	private String msg_id;

	private List<InputBO> input_array;
	
	private ScheduleRequest schedule;
	
	private JSONObject delete_input;

	public String getMsg_id() {
		return msg_id;
	}

	public PutScheduleRequest setMsg_id(String msg_id) {
		this.msg_id = msg_id;
		return this;
	}

	public List<InputBO> getInput_array() {
		return input_array;
	}

	public PutScheduleRequest setInput_array(List<InputBO> input_array) {
		this.input_array = input_array;
		return this;
	}

	public ScheduleRequest getSchedule() {
		return schedule;
	}

	public PutScheduleRequest setSchedule(ScheduleRequest schedule) {
		this.schedule = schedule;
		return this;
	}

	public JSONObject getDelete_input() {
		return delete_input;
	}

	public PutScheduleRequest setDelete_input(JSONObject delete_input) {
		this.delete_input = delete_input;
		return this;
	}
	
}
