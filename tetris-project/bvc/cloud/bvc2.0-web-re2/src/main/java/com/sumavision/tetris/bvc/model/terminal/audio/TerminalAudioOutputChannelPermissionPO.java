package com.sumavision.tetris.bvc.model.terminal.audio;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 终端音频输出与音频通道关联<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年11月3日 下午6:22:54
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_TERMINAL_AUDIO_OUTPUT_CHANNEL_PERMISSION")
public class TerminalAudioOutputChannelPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 终端id */
	private Long terminalId;
	
	/** 终端音频通道id */
	private Long terminalAudioChannelId;
	
	/** 终端音频输出id */
	private Long terminalAudioOutputId;

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	@Column(name = "TERMINAL_AUDIO_CHANNEL_ID")
	public Long getTerminalAudioChannelId() {
		return terminalAudioChannelId;
	}

	public void setTerminalAudioChannelId(Long terminalAudioChannelId) {
		this.terminalAudioChannelId = terminalAudioChannelId;
	}

	@Column(name = "TERMINAL_AUDIO_OUTPUT_ID")
	public Long getTerminalAudioOutputId() {
		return terminalAudioOutputId;
	}

	public void setTerminalAudioOutputId(Long terminalAudioOutputId) {
		this.terminalAudioOutputId = terminalAudioOutputId;
	}

}
