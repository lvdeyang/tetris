package com.sumavision.tetris.streamTranscoding.addTask.requestVO;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class BodyVO {
	private InputVO input;
	private List<TaskVO> tasks;
	private List<OutputVO> outputs;
	
	public BodyVO(){}
	
	public BodyVO(InputVO input, List<TaskVO> tasks, List<OutputVO> outputs) {
		if (input != null) this.input = input;
		if (tasks != null) this.tasks = tasks;
		this.outputs = outputs;
	}
	
	public InputVO getInput() {
		return input;
	}
	
	@XmlElement(name = "input")
	public void setInput(InputVO input) {
		this.input = input;
	}
	
	public List<TaskVO> getTasks() {
		return tasks;
	}
	
	@XmlElement(name = "task")
	public void setTasks(List<TaskVO> tasks) {
		this.tasks = tasks;
	}
	
	public List<OutputVO> getOutputs() {
		return outputs;
	}
	
	@XmlElement(name = "output")
	public void setOutputs(List<OutputVO> outputs) {
		this.outputs = outputs;
	}
}
