package com.sumavision.tetris.transcoding.addTask.requestVO;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class TranscodeJobsVO {
	private List<TranscodeVO> transcode;

	public List<TranscodeVO> getTranscode() {
		return transcode;
	}

	@XmlElement(name = "Transcode")
	public void setTranscode(List<TranscodeVO> transcode) {
		this.transcode = transcode;
	}
}
