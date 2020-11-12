package com.sumavision.tetris.cs.template;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;


import com.sumavision.tetris.orm.po.AbstractBasePO;

@Table
@Entity(name = "TETRIS_CS_TEMPLATE_PROGRAME")
public class TemplateProgramePO extends AbstractBasePO {
	private static final long serialVersionUID = 1L;
	
	/** 唯一标识 */
	public static final String VERSION_OF_ORIGIN = "0.0";

	private long templateId;
	
	private Date startTime;
	
	private Date endTime;
	
    private ProgrameType programeType;
    
    private String labelIds;//多个标签支持，逗号分隔
    
    private String labelNames;//多个标签支持，逗号分隔
    
    private String mimsId;
    
    private String mimsName;
    
    private String url;
    
    private String duration;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@Enumerated(value = EnumType.STRING)
	public ProgrameType getProgrameType() {
		return programeType;
	}

	public void setProgrameType(ProgrameType programeType) {
		this.programeType = programeType;
	}

	public String getLabelIds() {
		return labelIds;
	}

	public void setLabelIds(String labelIds) {
		this.labelIds = labelIds;
	}

	public String getLabelNames() {
		return labelNames;
	}

	public void setLabelNames(String labelNames) {
		this.labelNames = labelNames;
	}

	public String getMimsId() {
		return mimsId;
	}

	public void setMimsId(String mimsId) {
		this.mimsId = mimsId;
	}

	public String getMimsName() {
		return mimsName;
	}

	public void setMimsName(String mimsName) {
		this.mimsName = mimsName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
    
    
	
}
