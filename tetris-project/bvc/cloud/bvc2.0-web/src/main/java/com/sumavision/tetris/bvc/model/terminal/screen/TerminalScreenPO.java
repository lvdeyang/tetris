package com.sumavision.tetris.bvc.model.terminal.screen;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 终端屏幕，终端从屏幕主键中选择生成终端屏幕<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:13:24
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_TERMINAL_SCREEN")
public class TerminalScreenPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 屏幕名称 */
	private String name;
	
	/** 屏幕primaryKey */
	private String screenPrimaryKey;
	
	/** 终端id */
	private Long terminalId;
	
	/** 与终端通道一一对应 */
	private Long terminalChannelId;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SCREEN_PRIMARY_KEY")
	public String getScreenPrimaryKey() {
		return screenPrimaryKey;
	}

	public void setScreenPrimaryKey(String screenPrimaryKey) {
		this.screenPrimaryKey = screenPrimaryKey;
	}

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	@Column(name = "TERMINAL_CHANNEL_ID")
	public Long getTerminalChannelId() {
		return terminalChannelId;
	}

	public void setTerminalChannelId(Long terminalChannelId) {
		this.terminalChannelId = terminalChannelId;
	}
	
}
