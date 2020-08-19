package com.sumavision.tetris.transcoding.addTask.requestVO;

import javax.xml.bind.annotation.XmlElement;

public class SourceVO {
	private SrcURIVO srcURI;
	private Long startTime;
	private Long endTime;
	private SubTitleListVO subTitleList;

	public SrcURIVO getSrcURI() {
		return srcURI;
	}

	@XmlElement(name = "SrcURI")
	public void setSrcURI(SrcURIVO srcURI) {
		this.srcURI = srcURI;
	}

	public Long getStartTime() {
		return startTime;
	}

	@XmlElement(name = "StartTime")
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	@XmlElement(name = "EndTime")
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public SubTitleListVO getSubTitleList() {
		return subTitleList;
	}

	@XmlElement(name = "SubTitleList")
	public void setSubTitleList(SubTitleListVO subTitleList) {
		this.subTitleList = subTitleList;
	}
}
