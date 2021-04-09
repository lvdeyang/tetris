package com.sumavision.tetris.bvc.model.terminal.audio;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 终端音频输出<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年11月3日 下午6:21:04
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_TERMINAL_AUDIO_OUTPUT")
public class TerminalAudioOutputPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 名称 */
	private String name;
	
	/** 隶属终端id */
	private Long terminalId;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}
	
}
