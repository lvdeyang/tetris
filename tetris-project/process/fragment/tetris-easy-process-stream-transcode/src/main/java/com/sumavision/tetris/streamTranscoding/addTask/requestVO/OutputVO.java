package com.sumavision.tetris.streamTranscoding.addTask.requestVO;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class OutputVO {
	private Long id;
	
	private String name;
	
	private String type;
	
	private String dstType;
	
	private String vidSelect;
	
	private String audSelect;
	
	private TSUDPVO TSUDPVO;
	
	private TSUDPVO ESUDPVO;
	
	private TSUDPVO RTPUDPVO;
	
	public OutputVO(){}
	
	public OutputVO(Long id, String type, TSUDPVO tsudpvo, TSUDPVO esudpvo, TSUDPVO rtpudpvo){
		this(id, "pcm-output", type, "1", "", "1", tsudpvo, esudpvo, rtpudpvo);
	}
	
	public OutputVO(
			Long id,
			String name,
			String type,
			String dstType,
			String vidSelect,
			String audSelect,
			TSUDPVO TSUDPVO,
			TSUDPVO ESUDPVO,
			TSUDPVO RTPUDPVO) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.dstType = dstType;
		this.vidSelect = vidSelect;
		this.audSelect = audSelect;
		if (TSUDPVO != null) this.TSUDPVO = TSUDPVO;
		if (ESUDPVO != null) this.ESUDPVO = ESUDPVO;
		if (RTPUDPVO != null) this.RTPUDPVO = RTPUDPVO;
	}

	public Long getId() {
		return id;
	}

	@XmlAttribute(name = "id")
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	@XmlAttribute(name = "name")
	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	@XmlAttribute(name = "type")
	public void setType(String type) {
		this.type = type;
	}

	public String getDstType() {
		return dstType;
	}

	@XmlAttribute(name = "dst-type")
	public void setDstType(String dstType) {
		this.dstType = dstType;
	}

	public String getVidSelect() {
		return vidSelect;
	}

	@XmlAttribute(name = "vid-select")
	public void setVidSelect(String vidSelect) {
		this.vidSelect = vidSelect;
	}

	public String getAudSelect() {
		return audSelect;
	}

	@XmlAttribute(name = "aud-select")
	public void setAudSelect(String audSelect) {
		this.audSelect = audSelect;
	}

	public TSUDPVO getTSUDPVO() {
		return TSUDPVO;
	}

	@XmlElement(name = "TS-UDP")
	public void setTSUDPVO(TSUDPVO tSUDPVO) {
		TSUDPVO = tSUDPVO;
	}

	public TSUDPVO getESUDPVO() {
		return ESUDPVO;
	}

	@XmlElement(name = "ES-UDP")
	public void setESUDPVO(TSUDPVO eSUDPVO) {
		ESUDPVO = eSUDPVO;
	}

	public TSUDPVO getRTPUDPVO() {
		return RTPUDPVO;
	}

	@XmlElement(name = "RTP-UDP")
	public void setRTPUDPVO(TSUDPVO rTPUDPVO) {
		RTPUDPVO = rTPUDPVO;
	}
}
