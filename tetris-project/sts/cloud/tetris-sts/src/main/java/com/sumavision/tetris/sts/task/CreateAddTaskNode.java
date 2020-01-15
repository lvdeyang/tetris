package com.sumavision.tetris.sts.task;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.sts.task.taskParamInput.InputNode;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputNode;
import com.sumavision.tetris.sts.task.taskParamTask.TaskNode;

public class CreateAddTaskNode {
	
	/**任务链路的名称 */
	private String task_name;
	
	/**选择设备 */
	private String device_ip;
	
	/**0: 纯网关任务; 1:转码任务 */
	private Integer task_type;
	
	/**任务id */
	private Long task_id;
	
	/**输入参数 */
	private JSONObject input_array;
	
	/**任务参数 */
	private ArrayList<TaskNode> task_array;
	
	/**输出参数 */
	private ArrayList<OutputNode> output_array;

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public String getDevice_ip() {
		return device_ip;
	}

	public void setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
	}

	public Integer getTask_type() {
		return task_type;
	}

	public void setTask_type(Integer task_type) {
		this.task_type = task_type;
	}

	public Long getTask_id() {
		return task_id;
	}

	public void setTask_id(Long task_id) {
		this.task_id = task_id;
	}



	public JSONObject getInput_array() {
		return input_array;
	}

	public void setInput_array(JSONObject input_array) {
		this.input_array = input_array;
	}

	public ArrayList<TaskNode> getTask_array() {
		return task_array;
	}

	public void setTask_array(ArrayList<TaskNode> task_array) {
		this.task_array = task_array;
	}

	public ArrayList<OutputNode> getOutput_array() {
		return output_array;
	}

	public void setOutput_array(ArrayList<OutputNode> output_array) {
		this.output_array = output_array;
	}
}
	
