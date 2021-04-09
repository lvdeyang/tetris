package com.sumavision.tetris.bvc.model.terminal.channel;

/**
 * 带有音频输出信息的音频解码通道<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年11月11日 下午1:58:34
 */
public class TerminalDecodeChannelWithTerminalAudioOutputPermissionDTO {

	/** 终端音频解码通道id */
	private Long id;
	
	/** 终端音频解码通道名称 */
	private String name;
	
	/** 终端音频解码通道类型 */
	private TerminalChannelType type;
	
	/** 终端id */
	private Long terminalId;
	
	/** 终端音频输出id */
	private Long terminalAudioOutputId;
	
	/** 终端音频解码通道与音频输出关联表id */
	private Long permissionId;

	public TerminalDecodeChannelWithTerminalAudioOutputPermissionDTO(
			Long id,
			String name,
			TerminalChannelType type,
			Long terminalId,
			Long terminalAudioOutputId,
			Long permissionId){
		this.id = id;
		this.name = name;
		this.type = type;
		this.terminalId = terminalId;
		this.terminalAudioOutputId = terminalAudioOutputId;
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

	public Long getTerminalAudioOutputId() {
		return terminalAudioOutputId;
	}

	public void setTerminalAudioOutputId(Long terminalAudioOutputId) {
		this.terminalAudioOutputId = terminalAudioOutputId;
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}
	
}
