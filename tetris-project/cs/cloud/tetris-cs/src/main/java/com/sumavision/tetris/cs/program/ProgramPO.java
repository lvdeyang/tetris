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

	/** 排期id */
	private Long scheduleId;
	
	/** 分屏数 */
	private Long screenNum;

	@Column(name="SCHEDULE_ID")
	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	@Column(name="SCREEN_NUM")
	public Long getScreenNum() {
		return screenNum;
	}

	public void setScreenNum(Long screenNum) {
		this.screenNum = screenNum;
	}

}