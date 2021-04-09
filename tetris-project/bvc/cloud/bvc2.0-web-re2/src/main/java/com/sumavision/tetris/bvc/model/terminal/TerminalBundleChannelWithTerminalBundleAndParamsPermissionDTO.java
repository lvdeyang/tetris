package com.sumavision.tetris.bvc.model.terminal;

import com.sumavision.tetris.bvc.model.terminal.channel.ChannelParamsType;

/**
 * 带有终端设备信息以及参数映射信息的终端设备通道<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年11月11日 下午2:40:18
 */
public class TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO {

	/** 终端设备通道id */
	private Long id;
	
	/** 真实设备通道id */
	private String channelId;
	
	/** 终端设备通道类型 */
	private TerminalBundleChannelType type;
	
	/** 终端设备id */
	private Long terminalBundleId;
	
	/** 终端设备名称 */
	private String bundleName;
	
	/** 真实设备类型 */
	private String deviceMode;
	
	/** 终端设备类型 */
	private TerminalBundleType bundleType;
	
	/** 终端id */
	private Long terminalId;
	
	/** 终端通道id */
	private Long terminalChannelId;
	
	/** 终端通道与终端设备通道映射参数 */
	private ChannelParamsType channelParamsType;
	
	/** 终端通道与终端设备通道关联表id */
	private Long paramsPermissionId;

	public TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO(
			Long id,
			String channelId,
			TerminalBundleChannelType type,
			Long terminalBundleId,
			String bundleName,
			String deviceMode,
			TerminalBundleType bundleType){
		this.id = id;
		this.channelId = channelId;
		this.type = type;
		this.terminalBundleId = terminalBundleId;
		this.bundleName = bundleName;
		this.deviceMode = deviceMode;
		this.bundleType = bundleType;
	}
	
	public TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO(
			Long id,
			String channelId,
			TerminalBundleChannelType type,
			Long terminalBundleId,
			String bundleName,
			String deviceMode,
			TerminalBundleType bundleType,
			Long terminalId,
			Long terminalChannelId,
			ChannelParamsType channelParamsType,
			Long paramsPermissionId){
		this.id = id;
		this.channelId = channelId;
		this.type = type;
		this.terminalBundleId = terminalBundleId;
		this.bundleName = bundleName;
		this.deviceMode = deviceMode;
		this.bundleType = bundleType;
		this.terminalId = terminalId;
		this.terminalChannelId = terminalChannelId;
		this.channelParamsType = channelParamsType;
		this.paramsPermissionId = paramsPermissionId;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public TerminalBundleChannelType getType() {
		return type;
	}

	public void setType(TerminalBundleChannelType type) {
		this.type = type;
	}

	public Long getTerminalBundleId() {
		return terminalBundleId;
	}

	public void setTerminalBundleId(Long terminalBundleId) {
		this.terminalBundleId = terminalBundleId;
	}

	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	public String getDeviceMode() {
		return deviceMode;
	}

	public void setDeviceMode(String deviceMode) {
		this.deviceMode = deviceMode;
	}

	public TerminalBundleType getBundleType() {
		return bundleType;
	}

	public void setBundleType(TerminalBundleType bundleType) {
		this.bundleType = bundleType;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	public Long getTerminalChannelId() {
		return terminalChannelId;
	}

	public void setTerminalChannelId(Long terminalChannelId) {
		this.terminalChannelId = terminalChannelId;
	}

	public ChannelParamsType getChannelParamsType() {
		return channelParamsType;
	}

	public void setChannelParamsType(ChannelParamsType channelParamsType) {
		this.channelParamsType = channelParamsType;
	}

	public Long getParamsPermissionId() {
		return paramsPermissionId;
	}

	public void setParamsPermissionId(Long paramsPermissionId) {
		this.paramsPermissionId = paramsPermissionId;
	}
	
}
