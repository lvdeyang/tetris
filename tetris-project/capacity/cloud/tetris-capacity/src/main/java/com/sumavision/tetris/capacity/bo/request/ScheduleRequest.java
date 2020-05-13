package com.sumavision.tetris.capacity.bo.request;

import java.util.List;

import com.sumavision.tetris.capacity.bo.input.ScheduleProgramBO;

public class ScheduleRequest {

	private List<ScheduleProgramBO> source_array;

	public List<ScheduleProgramBO> getSource_array() {
		return source_array;
	}

	public void setSource_array(List<ScheduleProgramBO> source_array) {
		this.source_array = source_array;
	}
	
}
