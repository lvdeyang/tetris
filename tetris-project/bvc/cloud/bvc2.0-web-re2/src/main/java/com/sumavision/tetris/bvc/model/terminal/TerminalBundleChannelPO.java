package com.sumavision.tetris.bvc.model.terminal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 记录终端设备真实的通道id<br/>
 * <p>
 * 	通道id根据现有规则自动生成<br/>
 * </p>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:08:17
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_TERMINAL_BUNDLE_CHANNEL")
public class TerminalBundleChannelPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 资源层真实通道id */
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
