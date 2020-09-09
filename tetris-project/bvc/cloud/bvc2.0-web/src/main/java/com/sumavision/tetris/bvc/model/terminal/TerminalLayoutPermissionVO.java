package com.sumavision.tetris.bvc.model.terminal;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TerminalLayoutPermissionVO extends AbstractBaseVO<TerminalLayoutPermissionVO, TerminalLayoutPermissionPO>{

	/** 终端id */
	private Long terminalId;
	
	/** 布局id */
	private Long layoutId;

	/** 布局名称 */
	private String layoutName;
	
	public Long getTerminalId() {
		return terminalId;
	}

	public TerminalLayoutPermissionVO setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	public Long getLayoutId() {
		return layoutId;
	}

	public TerminalLayoutPermissionVO setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
		return this;
	}

	public String getLayoutName() {
		return layoutName;
	}

	public TerminalLayoutPermissionVO setLayoutName(String layoutName) {
		this.layoutName = layoutName;
		return this;
	}

	@Override
	public TerminalLayoutPermissionVO set(TerminalLayoutPermissionPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setTerminalId(entity.getTerminalId())
			.setLayoutId(entity.getLayoutId());
		return this;
	}

}
