package com.sumavision.tetris.cs.program;

import java.util.List;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ProgramVO extends AbstractBaseVO<ProgramVO,ProgramPO>{
	
	private Long scheduleId;
	private Long screenId;
	private Long screenNum;
	private String orient;
	private List<ScreenVO> screenInfo;

	@Override
	public ProgramVO set(ProgramPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime() == null ? "": DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setScheduleId(entity.getScheduleId())
		.setScreenId(entity.getScreenId())
		.setScreenNum(entity.getScreenNum())
		.setOrient(entity.getOrient());
		
		return this;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public ProgramVO setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
		return this;
	}

	public Long getScreenId() {
		return screenId;
	}

	public ProgramVO setScreenId(Long screenId) {
		this.screenId = screenId;
		return this;
	}

	public Long getScreenNum() {
		return screenNum;
	}

	public ProgramVO setScreenNum(Long screenNum) {
		this.screenNum = screenNum;
		return this;
	}

	public String getOrient() {
		return orient;
	}

	public ProgramVO setOrient(String orient) {
		this.orient = orient;
		return this;
	}

	public List<ScreenVO> getScreenInfo() {
		return screenInfo;
	}

	public void setScreenInfo(List<ScreenVO> screenInfo) {
		this.screenInfo = screenInfo;
	}
}
