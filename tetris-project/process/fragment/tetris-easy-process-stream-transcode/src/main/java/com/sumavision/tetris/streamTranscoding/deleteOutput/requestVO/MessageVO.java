package com.sumavision.tetris.streamTranscoding.deleteOutput.requestVO;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sumavision.tetris.streamTranscoding.deleteTask.requestVO.BodyVO;

@XmlRootElement(name = "message")
public class MessageVO {
	private Long id;
	private String cmd;
	private TargetVO target;
	private BodyVO body;
	
	public MessageVO(){}
	
	public MessageVO(Long id, TargetVO target) {
		this.id = id;
		this.cmd = "delete-output";
		this.target = target;
		this.body = new BodyVO();
	}
	
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
