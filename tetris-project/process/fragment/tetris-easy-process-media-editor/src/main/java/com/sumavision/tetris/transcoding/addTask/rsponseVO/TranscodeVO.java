package com.sumavision.tetris.transcoding.addTask.rsponseVO;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class TranscodeVO {
	private int resultCode;
	private String resultString;
	
	private String id;

	@XmlElement(name = "ResultCode")
	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
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
