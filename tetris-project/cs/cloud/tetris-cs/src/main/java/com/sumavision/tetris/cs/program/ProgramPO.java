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
	
	/** 分屏id */
	private Long screenId;
	
	/** 分屏数 */
	private Long screenNum;
	
	/** 屏幕方向 */
	private String orient;
	
	@Column(name="SCHEDULE_ID")
	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	@Column(name = "SCREEN_ID")
	public Long getScreenId() {
		return screenId;
	}

	public void setScreenId(Long screenId) {
		this.screenId = screenId;
	}

	@Column(name="SCREEN_NUM")
	public Long getScreenNum() {
		return screenNum;
	}

	public void setScreenNum(Long screenNum) {
		this.screenNum = screenNum;
	}

	@Column(name = "ORIENT")
	public String getOrient() {
		return orient;
	}

	public void setOrient(String orient) {
		this.orient = orient;
	}

}
