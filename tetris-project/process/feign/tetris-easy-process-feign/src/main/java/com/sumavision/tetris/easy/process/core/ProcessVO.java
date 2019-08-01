package com.sumavision.tetris.easy.process.core;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ProcessVO extends AbstractBaseVO<ProcessVO, Object>{

	private String processId;
	
	private String name;
	
	private String type;
	
	private String remarks;
	
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

	@Override
	public ProcessVO set(Object entity) throws Exception {
		return this;
	}

}
