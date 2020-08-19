package com.sumavision.tetris.streamTranscoding.deleteTask.requestVO;

import javax.xml.bind.annotation.XmlElement;

public class TargetVO {
	private InputVO input;
	private OutputVO output;
	
	public TargetVO(){}
	
	public TargetVO(InputVO input) {
		this.input = input;
		this.output = new OutputVO();
	}
	
	@XmlElement(name = "input")
	public InputVO getInput() {
		return input;
	}
	
	public void setInput(InputVO input) {
		this.input = input;
	}
	
	@XmlElement(name = "output")
	public OutputVO getOutput() {
		return output;
	}
	
	public void setOutput(OutputVO output) {
		this.output = output;
	}
}
