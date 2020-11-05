package com.sumavision.tetris.transcoding.addTask.requestVO;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class SubTitleListVO {
	private List<SubTitleVO> subTitle;

	public List<SubTitleVO> getSubTitle() {
		return subTitle;
	}

	@XmlElement(name = "SubTitle")
	public void setSubTitle(List<SubTitleVO> subTitle) {
		this.subTitle = subTitle;
	}
}
