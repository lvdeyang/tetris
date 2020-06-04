package com.sumavision.tetris.bvc.model.terminal;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 记录终端可以有哪几种布局<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:10:45
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_TERMINAL_LAYOUT_PERMISSION")
public class TerminalLayoutPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 终端id */
	private Long terminalId;
	
	/** 布局id */
	private Long layoutId;

	@Column(name = "TERMINAL")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	@Column(name = "LAYOUT_ID")
	public Long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
	}
	
}
