package com.sumavision.tetris.bvc.model.terminal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_BVC_MODEL_TERMINAL_BUNDLE_CHANNEL")
public class TerminalBundleChannelPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 资源城通道id */
	private String channelId;

	/** 通道编解码类型 */
	private TerminalBundleChannelType type;
	
	/** 隶属终端设备id */
	private Long terminalBundleId;

	@Column(name = "CHANNEL_ID")
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Column(name = "TYPE")
	@Enumerated(value = EnumType.STRING)
	public TerminalBundleChannelType getType() {
		return type;
	}

	public void setType(TerminalBundleChannelType type) {
		this.type = type;
	}

	@Column(name = "TERMINAL_BUNDLE_ID")
	public Long getTerminalBundleId() {
		return terminalBundleId;
	}

	public void setTerminalBundleId(Long terminalBundleId) {
		this.terminalBundleId = terminalBundleId;
	}
	
}
