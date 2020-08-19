package com.sumavision.tetris.streamTranscoding.addTask.requestVO;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.JSONObject;

@XmlRootElement(name = "message")
public class MessageVO extends JSONObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String cmd;
	private TargetVO target;
	private BodyVO body;
	
	public MessageVO(){}
	
	public MessageVO(Long id, BodyVO body){
		this(id, "create-inout", body);
	}
	
	public MessageVO(Long id, String cmd, BodyVO body){
		this.id = id;
		this.cmd = cmd;
		this.target = new TargetVO();
		this.body = body;
	}
	
	public Long getId() {
		return id;
	}
	
	@XmlAttribute(name = "id")
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCmd() {
		return cmd;
	}
	
	@XmlAttribute(name = "cmd")
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	
	public TargetVO getTarget() {
		return target;
	}
	
	@XmlElement(name = "target")
	public void setTarget(TargetVO target) {
		this.target = target;
	}
	
	public BodyVO getBody() {
		return body;
	}
	
	@XmlElement(name = "body")
	public void setBody(BodyVO body) {
		this.body = body;
	}
}
