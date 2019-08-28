package com.sumavision.tetris.easy.process.core;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ProcessVO extends AbstractBaseVO<ProcessVO, ProcessPO>{

	private String processId;
	
	private String name;
	
	private String type;
	
	private String remarks;
	
	private String publishTime;
	
	public String getProcessId() {
		return processId;
	}

	public ProcessVO setProcessId(String processId) {
		this.processId = processId;
		return this;
	}

	public String getName() {
		return name;
	}

	public ProcessVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getType() {
		return type;
	}

	public ProcessVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public ProcessVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}
	
	public String getPublishTime() {
		return publishTime;
	}

	public ProcessVO setPublishTime(String publishTime) {
		this.publishTime = publishTime;
		return this;
	}

	@Override
	public ProcessVO set(ProcessPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setProcessId(entity.getProcessId()==null?"-":entity.getProcessId())
			.setName(entity.getName())
			.setType(entity.getType().getName())
			.setRemarks(entity.getRemarks())
			.setPublishTime(entity.getPublishTime()==null?(ProcessType.TEMPLATE.equals(entity.getType())?"-":"未发布"):DateUtil.format(entity.getPublishTime(), DateUtil.dateTimePattern));
		return this;
	}

}
