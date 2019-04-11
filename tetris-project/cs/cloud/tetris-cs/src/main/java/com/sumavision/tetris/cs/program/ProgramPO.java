package com.sumavision.tetris.cs.program;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CS_PROGRAM")
public class ProgramPO extends AbstractBasePO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long channelId;
	private Long screenNum;

	@Column(name="CHANNEL_ID")
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	@Column(name="SCREEN_NUM")
	public Long getScreenNum() {
		return screenNum;
	}

	public void setScreenNum(Long screenNum) {
		this.screenNum = screenNum;
	}

}
