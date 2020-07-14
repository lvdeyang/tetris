package com.sumavision.tetris.bvc.model.terminal;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TerminalBundleVO extends AbstractBaseVO<TerminalBundleVO, TerminalBundlePO>{

	private String name;
	
	private String bundleType;
	
	private String type;
	
	private Long terminalId;
	
	public String getName() {
		return name;
	}

	public TerminalBundleVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getBundleType() {
		return bundleType;
	}

	public TerminalBundleVO setBundleType(String bundleType) {
		this.bundleType = bundleType;
		return this;
	}

	public String getType() {
		return type;
	}

	public TerminalBundleVO setType(String type) {
		this.type = type;
		return this;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public TerminalBundleVO setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	@Override
	public TerminalBundleVO set(TerminalBundlePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setBundleType(entity.getBundleType())
			.setType(entity.getType().getName())
			.setTerminalId(entity.getTerminalId());
		return this;
	}

}
