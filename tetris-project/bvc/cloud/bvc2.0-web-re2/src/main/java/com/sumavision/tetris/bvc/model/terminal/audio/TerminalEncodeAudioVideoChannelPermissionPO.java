package com.sumavision.tetris.bvc.model.terminal.audio;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 终端音视频编码通道映射<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年11月3日 下午6:18:27
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_TERMINAL_ENCODE_AUDIO_VIDEO_CHANNEL_PERMISSION")
public class TerminalEncodeAudioVideoChannelPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 终端id */
	private Long terminalId;
	
	/** 视频通道id */
	private Long terminalVideoChannelId;
	
	/** 音频通道id */
	private Long terminalAudioChannelId;

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	@Column(name = "TERMINAL_VIDEO_CHANNEL_ID")
	public Long getTerminalVideoChannelId() {
		return terminalVideoChannelId;
	}

	public void setTerminalVideoChannelId(Long terminalVideoChannelId) {
		this.terminalVideoChannelId = terminalVideoChannelId;
	}

	@Column(name = "TERMINAL_AUDIO_CHANNEL_ID")
	public Long getTerminalAudioChannelId() {
		return terminalAudioChannelId;
	}

	public void setTerminalAudioChannelId(Long terminalAudioChannelId) {
		this.terminalAudioChannelId = terminalAudioChannelId;
	}
	
}
