package com.sumavision.tetris.business.common.bo;

import java.util.List;

import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.output.OutputBO;
import com.sumavision.tetris.capacity.bo.task.TaskBO;

public class CapacityDeleteBO {

	private String capacityIp;
	
	private List<InputBO> inputs;
	
	private List<TaskBO> tasks;
	
	private List<OutputBO> outputs;

	public String getCapacityIp() {
		return capacityIp;
	}

	public void setCapacityIp(String capacityIp) {
		this.capacityIp = capacityIp;
	}

	public List<InputBO> getInputs() {
		return inputs;
	}

	public void setInputs(List<InputBO> inputs) {
		this.inputs = inputs;
	}

	public List<TaskBO> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskBO> tasks) {
		this.tasks = tasks;
	}

	public List<OutputBO> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<OutputBO> outputs) {
		this.outputs = outputs;
	}
	
}
