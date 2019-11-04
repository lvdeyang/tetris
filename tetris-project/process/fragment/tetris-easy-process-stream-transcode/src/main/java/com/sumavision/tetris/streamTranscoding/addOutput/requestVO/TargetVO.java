package com.sumavision.tetris.streamTranscoding.addOutput.requestVO;

import javax.xml.bind.annotation.XmlElement;

public class TargetVO {
	private InputVO input;
	private InputVO prog;
	private InputVO task;
	
	public TargetVO(){}
	
	public TargetVO(InputVO input, InputVO prog, InputVO task) {
		this.input = input;
		this.prog = prog;
		this.task = task;
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
}
