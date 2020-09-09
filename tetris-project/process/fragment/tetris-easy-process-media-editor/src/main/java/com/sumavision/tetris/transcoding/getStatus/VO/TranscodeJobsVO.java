package com.sumavision.tetris.transcoding.getStatus.VO;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class TranscodeJobsVO {
	private List<TranscodeVO> transcodes;

	@XmlElement(name = "Transcode")
	public List<TranscodeVO> getTranscodes() {
		return transcodes;
	}

	public void setTranscodes(List<TranscodeVO> transcodes) {
		this.transcodes = transcodes;
	}
}
