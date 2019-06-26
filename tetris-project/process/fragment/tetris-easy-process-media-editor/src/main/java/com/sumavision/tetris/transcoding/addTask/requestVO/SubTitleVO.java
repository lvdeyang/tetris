package com.sumavision.tetris.transcoding.addTask.requestVO;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class SubTitleVO {
	private String value;
	
	private String mode;
	private String type;
	
	@XmlValue
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@XmlAttribute(name = "mode")
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	@XmlAttribute(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
