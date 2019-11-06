package com.sumavision.tetris.streamTranscoding.addTask.requestVO;

import javax.xml.bind.annotation.XmlAttribute;

public class InputVO {
	private Long id;
	private String type;
	private String url;
	private Integer cycleCnt;
	
	public InputVO(){}
	
	public InputVO(Long id, String url, Integer cycleCnt) {
		this(id, "stream", url, cycleCnt);
	}
	
	public InputVO(Long id, String type, String url, Integer cycleCnt) {
		this.id = id;
		this.type = type;
		this.url = url;
		if (cycleCnt != 0) this.cycleCnt = cycleCnt;
	}
	
	@XmlAttribute(name = "id")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@XmlAttribute(name = "type")
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@XmlAttribute(name = "url")
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	@XmlAttribute(name = "cycle-cnt")
	public Integer getSycleCnt() {
		return cycleCnt;
	}

	public void setSycleCnt(Integer cycleCnt) {
		this.cycleCnt = cycleCnt;
	}
	
	public InputVO copy(){
		InputVO copy = new InputVO(this.getId(), this.getUrl(), this.getSycleCnt());
		return copy;
	}
}
