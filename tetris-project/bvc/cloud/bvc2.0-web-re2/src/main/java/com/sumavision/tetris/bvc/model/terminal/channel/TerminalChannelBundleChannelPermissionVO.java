package com.sumavision.tetris.bvc.model.terminal.channel;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TerminalChannelBundleChannelPermissionVO  extends AbstractBaseVO<TerminalChannelBundleChannelPermissionVO, TerminalChannelBundleChannelPermissionPO>{

	/** 终端通道id */
	private Long terminalChannelId;
	
	/**终端设备名称*/
	private String name;
	
	/** 终端id */
	private Long terminalId;
	
	/** 终端设备id */
	private Long terminalBundleId;
	
	/** 终端设备通道id */
	private Long terminalBundleChannelId;
	
	/** 自适应通道参数 */
	private String channelParamsType;
	
	public String getName() {
		return name;
	}

	public TerminalChannelBundleChannelPermissionVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getTerminalChannelId() {
		return terminalChannelId;
	}

	public TerminalChannelBundleChannelPermissionVO setTerminalChannelId(Long terminalChannelId) {
		this.terminalChannelId = terminalChannelId;
		return this;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public TerminalChannelBundleChannelPermissionVO setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	public Long getTerminalBundleId() {
		return terminalBundleId;
	}

	public TerminalChannelBundleChannelPermissionVO setTerminalBundleId(Long terminalBundleId) {
		this.terminalBundleId = terminalBundleId;
		return this;
	}

	public Long getTerminalBundleChannelId() {
		return terminalBundleChannelId;
	}

	public TerminalChannelBundleChannelPermissionVO setTerminalBundleChannelId(Long terminalBundleChannelId) {
		this.terminalBundleChannelId = terminalBundleChannelId;
		return this;
	}

	public String getChannelParamsType() {
		return channelParamsType;
	}

	public TerminalChannelBundleChannelPermissionVO setChannelParamsType(String channelParamsType) {
		this.channelParamsType = channelParamsType;
		return this;
	}

	@Override
	public TerminalChannelBundleChannelPermissionVO set(TerminalChannelBundleChannelPermissionPO entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
