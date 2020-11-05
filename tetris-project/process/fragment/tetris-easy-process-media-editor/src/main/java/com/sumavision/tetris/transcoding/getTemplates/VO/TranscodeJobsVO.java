package com.sumavision.tetris.transcoding.getTemplates.VO;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class TranscodeJobsVO {
	private List<String> templateNames;

	@XmlElement(name = "TemplateName")
	public List<String> getTemplateNames() {
		return templateNames;
	}

	public void setTemplateNames(List<String> templateNames) {
		this.templateNames = templateNames;
	}
}
