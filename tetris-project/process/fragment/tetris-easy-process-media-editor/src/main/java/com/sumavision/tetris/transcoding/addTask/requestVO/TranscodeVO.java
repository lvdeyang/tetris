package com.sumavision.tetris.transcoding.addTask.requestVO;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class TranscodeVO {
	private List<SourceVO> source;
	private TargetVO target;
	
	private String id;
	private String type;
	private String priority;

	public List<SourceVO> getSource() {
		return source;
	}

	@XmlElement(name = "Source")
	public void setSource(List<SourceVO> source) {
		this.source = source;
	}

	public TargetVO getTarget() {
		return target;
	}

	@XmlElement(name = "Target")
	public void setTarget(TargetVO target) {
		this.target = target;
	}

	public String getId() {
		return id;
	}

	@XmlAttribute(name = "id")
	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	@XmlAttribute(name = "type")
	public void setType(String type) {
		this.type = type;
	}

	public String getPriority() {
		return priority;
	}

	@XmlAttribute(name = "priority")
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
}
