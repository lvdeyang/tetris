package com.sumavision.tetris.streamTranscoding.deleteOutput.requestVO;

import javax.xml.bind.annotation.XmlElement;

import com.sumavision.tetris.streamTranscoding.addOutput.requestVO.InputVO;

public class TargetVO {
	private InputVO input;
	private InputVO prog;
	private InputVO task;
	private InputVO output;
	
	public TargetVO(){}
	
	public TargetVO(InputVO input, InputVO prog, InputVO task, InputVO output) {
		this.input = input;
		this.prog = prog;
		this.task = task;
		this.output = output;
	}
	
	@XmlElement(name = "input")
	public InputVO getInput() {
		return input;
	}
	
	public void setInput(InputVO input) {
		this.input = input;
	}
	
	@XmlElement(name = "prog")
	public InputVO getProg() {
		return prog;
	}
	
	public void setProg(InputVO prog) {
		this.prog = prog;
	}
	
	@XmlElement(name = "task")
	public InputVO getTask() {
		return task;
	}
	
	public void setTask(InputVO task) {
		this.task = task;
	}
	
	@XmlElement(name = "output")
	public InputVO getOutput() {
		return output;
	}
	
	public void setOutput(InputVO output) {
		this.output = output;
	}
}
