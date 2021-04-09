package com.sumavision.tetris.bvc.model.terminal.channel;

/**
 * 带音频关联信息的终端视频编码通道<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年11月12日 上午9:27:37
 */
public class TerminalEncodeVideoChannelWithAudioChannelPermissionDTO {

	/** 终端视频编码通道id */
	private Long id;
	
	/** 终端视频编码通道名称 */
	private String name;
	
	/** 终端视频编码通道类型 */
	private TerminalChannelType type;
	
	/** 终端id */
	private Long terminalId;
	
	/** 终端音频编码通道 */
	private Long terminalAudioChannelId;
	
	/** 终端音视频通道关联表id */
	private Long permissionId;

	public TerminalEncodeVideoChannelWithAudioChannelPermissionDTO(
			Long id,
			String name,
			TerminalChannelType type,
			Long terminalId,
			Long terminalAudioChannelId,
			Long permissionId){
		this.id = id;
		this.name = name;
		this.type = type;
		this.terminalId = terminalId;
		this.terminalAudioChannelId = terminalAudioChannelId;
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

	public Long getTerminalAudioChannelId() {
		return terminalAudioChannelId;
	}

	public void setTerminalAudioChannelId(Long terminalAudioChannelId) {
		this.terminalAudioChannelId = terminalAudioChannelId;
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}
	
}
