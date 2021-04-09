package com.sumavision.tetris.bvc.model.terminal.channel;

/**
 * 带有物理屏幕信息的终端解码通道<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年11月11日 下午1:24:21
 */
public class TerminalDecodeChannelWithTerminalPhysicalScreenPermissionDTO {

	/** 终端视频解码通道id */
	private Long id;
	
	/** 终端视频解码通道名称 */
	private String name;
	
	/** 终端视频解码通道类型 */
	private TerminalChannelType type;
	
	/** 终端id */
	private Long terminalId;
	
	/** 终端物理屏id */
	private Long terminalPhysicalScreenId;
	
	/** 终端视频解码通道与物理屏幕关联表id */
	private Long permissionId;

	public TerminalDecodeChannelWithTerminalPhysicalScreenPermissionDTO(
			Long id,
			String name,
			TerminalChannelType type,
			Long terminalId,
			Long terminalPhysicalScreenId,
			Long permissionId){
		this.id = id;
		this.name = name;
		this.type = type;
		this.terminalId = terminalId;
		this.terminalPhysicalScreenId = terminalPhysicalScreenId;
		this.permissionId = permissionId;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TerminalChannelType getType() {
		return type;
	}

	public void setType(TerminalChannelType type) {
		this.type = type;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	public Long getTerminalPhysicalScreenId() {
		return terminalPhysicalScreenId;
	}

	public void setTerminalPhysicalScreenId(Long terminalPhysicalScreenId) {
		this.terminalPhysicalScreenId = terminalPhysicalScreenId;
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}
	
}
