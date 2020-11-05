package com.sumavision.tetris.streamTranscoding.deleteOutput.responseVO;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sumavision.tetris.streamTranscoding.deleteOutput.requestVO.TargetVO;
import com.sumavision.tetris.streamTranscoding.deleteTask.requestVO.BodyVO;

@XmlRootElement(name = "response")
public class ResponseVO {
	private Long id;
	private String cmd;
	private Integer ack;
	private TargetVO target;
	private BodyVO body;
	
	@XmlAttribute(name = "id")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@XmlAttribute(name = "cmd")
	public String getCmd() {
		return cmd;
	}
	
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	@XmlAttribute(name = "ack")
	public Integer getAck() {
		return ack;
	}

	public void setAck(Integer ack) {
		this.ack = ack;
	}

	@XmlElement(name = "target")
	public TargetVO getTarget() {
		return target;
	}

	public void setTarget(TargetVO target) {
		this.target = target;
	}

	@XmlElement(name = "body")
	public BodyVO getBody() {
		return body;
	}

	public void setBody(BodyVO body) {
		this.body = body;
	}
	
	
}
