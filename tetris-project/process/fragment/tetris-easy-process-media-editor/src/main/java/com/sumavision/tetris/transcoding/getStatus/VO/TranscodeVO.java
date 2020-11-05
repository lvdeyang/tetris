package com.sumavision.tetris.transcoding.getStatus.VO;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class TranscodeVO {
	private int completeRate;
	private String taskStatus;
	private String resultString;
	
	private String id;

	@XmlElement(name = "CompleteRate")
	public int getCompleteRate() {
		return completeRate;
	}

	public void setCompleteRate(int completeRate) {
		this.completeRate = completeRate;
	}

	@XmlElement(name = "TaskStatus")
	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	@XmlElement(name = "ResultString")
	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	@XmlAttribute(name = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
