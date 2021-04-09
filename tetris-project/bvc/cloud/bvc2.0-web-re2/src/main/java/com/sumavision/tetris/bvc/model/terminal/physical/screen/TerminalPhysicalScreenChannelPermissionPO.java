package com.sumavision.tetris.bvc.model.terminal.physical.screen;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 终端物理屏幕与终端解码通道关联<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月15日 下午3:00:55
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_TERMINAL_PHYSICAL_SCREEN_CHANNEL_PERMISSION")
public class TerminalPhysicalScreenChannelPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 终端id */
	private Long terminalId;
	
	/** 终端物理屏id */
	private Long terminalPhysicalScreenId;

	/** 终端通道id */
	private Long terminalChannelId;

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	@Column(name = "TERMINAL_PHYSICAL_SCREEN_ID")
	public Long getTerminalPhysicalScreenId() {
		return terminalPhysicalScreenId;
	}

	public void setTerminalPhysicalScreenId(Long terminalPhysicalScreenId) {
		this.terminalPhysicalScreenId = terminalPhysicalScreenId;
	}

	@Column(name = "TERMINAL_CHANNEL_ID")
	public Long getTerminalChannelId() {
		return terminalChannelId;
	}

	public void setTerminalChannelId(Long terminalChannelId) {
		this.terminalChannelId = terminalChannelId;
	}
	
}
