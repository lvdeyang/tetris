package com.sumavision.tetris.bvc.business.terminal.screen;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 用户自定义终端屏幕与终端通道的绑定关系<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:34:14
 */
@Entity
@Table(name = "TETRIS_BVC_BUSINESS_TERMINAL_SCREEN_USER_SETTINGS")
public class ScreenUserSettingsPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 屏幕id */
	private Long screenId;
	
	/** 终端id */
	private Long terminalId;
	
	/** 终端设备id */
	private Long terminalBundleId;
	
	/** 终端设备通道id */
	private Long terminalBundleChannelId;
	
	/** 用户id */
	private String userId;

	@Column(name = "SCREEN_ID")
	public Long getScreenId() {
		return screenId;
	}

	public void setScreenId(Long screenId) {
		this.screenId = screenId;
	}

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	@Column(name = "TERMINAL_BUNDLE_ID")
	public Long getTerminalBundleId() {
		return terminalBundleId;
	}

	public void setTerminalBundleId(Long terminalBundleId) {
		this.terminalBundleId = terminalBundleId;
	}

	@Column(name = "TERMINAL_BUNDLE_CHANNEL_ID")
	public Long getTerminalBundleChannelId() {
		return terminalBundleChannelId;
	}

	public void setTerminalBundleChannelId(Long terminalBundleChannelId) {
		this.terminalBundleChannelId = terminalBundleChannelId;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
