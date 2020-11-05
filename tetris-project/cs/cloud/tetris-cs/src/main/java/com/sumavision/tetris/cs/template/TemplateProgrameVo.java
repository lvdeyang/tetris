package com.sumavision.tetris.cs.template;

import java.util.Date;

import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TemplateProgrameVo extends AbstractBaseVO<TemplateProgrameVo, TemplateProgramePO> {
    private long templateId;
	
	private Date startTime;
	
	private Date endTime;
	
	private String startTimeStr;
	
	private String endTimeStr;
	
    private String programeType;
    
    private String labelIds;//多个标签支持，逗号分隔
    
    private String labelNames;//多个标签支持，逗号分隔
    
    private String mimsId;
    
    private String mimsName;
    
    private String url;
    
	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

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

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public String getProgrameType() {
		return programeType;
	}

	public void setProgrameType(String programeType) {
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

	@Override
	public TemplateProgrameVo set(TemplateProgramePO entity) throws Exception {
		// TODO Auto-generated method stub
		this.setEndTime(entity.getEndTime());
		this.setStartTime(entity.getStartTime());
		this.setId(entity.getId());
		this.setLabelIds(entity.getLabelIds());
		this.setLabelNames(entity.getLabelNames());
		this.setMimsId(entity.getMimsId());
		this.setMimsName(entity.getMimsName());
		this.setProgrameType(entity.getProgrameType().getName());
		this.setStartTimeStr(DateUtil.format(entity.getStartTime(),"HH:mm:ss"));
		this.setEndTimeStr(DateUtil.format(entity.getEndTime(),"HH:mm:ss"));
		this.setTemplateId(entity.getTemplateId());
		return this;
	}

}
