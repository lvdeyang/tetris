package com.sumavision.tetris.bvc.model.terminal.channel;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 终端通道与终端设备通道关联--带参数<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月23日 下午2:52:28
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_CHANNEL_BUNDLE_CHANNEL_PERMISSION")
public class TerminalChannelBundleChannelPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 终端通道id */
	private Long terminalChannelId;
	
	/** 终端id */
	private Long terminalId;
	
	/** 终端设备id */
	private Long terminalBundleId;
	
	/** 终端设备通道id */
	private Long terminalBundleChannelId;
	
	/** 自适应通道参数 */
	private ChannelParamsType channelParamsType;

	@Column(name = "TERMINAL_CHANNEL_ID")
	public Long getTerminalChannelId() {
		return terminalChannelId;
	}

	public void setTerminalChannelId(Long terminalChannelId) {
		this.terminalChannelId = terminalChannelId;
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

	@Enumerated(value = EnumType.STRING)
	@Column(name = "CHANNEL_PARAMS_TYPE")
	public ChannelParamsType getChannelParamsType() {
		return channelParamsType;
	}

	public void setChannelParamsType(ChannelParamsType channelParamsType) {
		this.channelParamsType = channelParamsType;
	}
	
}
