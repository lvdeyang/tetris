package com.sumavision.tetris.transcoding.addTask.requestVO;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class SourcesVO {
	private List<SourceVO> source;
	
	private String name;

	public List<SourceVO> getSource() {
		return source;
	}

	@XmlElement(name = "Source")
	public void setSource(List<SourceVO> source) {
		this.source = source;
	}

	public String getName() {
		return name;
	}

	@XmlAttribute(name = "name")
	public void setName(String name) {
		this.name = name;
	}
}
