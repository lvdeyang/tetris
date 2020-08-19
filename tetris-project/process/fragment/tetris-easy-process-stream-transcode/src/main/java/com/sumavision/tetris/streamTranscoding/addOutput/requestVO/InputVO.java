package com.sumavision.tetris.streamTranscoding.addOutput.requestVO;

import javax.xml.bind.annotation.XmlAttribute;

public class InputVO {
	private Long id;
	
	public InputVO(){}
	
	public InputVO(Long id) {
		this.id = id;
	}

	@XmlAttribute(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
