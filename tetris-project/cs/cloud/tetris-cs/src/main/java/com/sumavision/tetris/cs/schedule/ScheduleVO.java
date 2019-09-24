package com.sumavision.tetris.cs.schedule;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.cs.program.ProgramVO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ScheduleVO extends AbstractBaseVO<ScheduleVO, SchedulePO>{
	
	private Long channelId;
	
	private Long programId;
	
	private String broadDate;
	
	private String remark;
	
	private ProgramVO program;

	public Long getChannelId() {
		return channelId;
	}

	public ScheduleVO setChannelId(Long channelId) {
		this.channelId = channelId;
		return this;
	}

	public Long getProgramId() {
		return programId;
	}

	public ScheduleVO setProgramId(Long programId) {
		this.programId = programId;
		return this;
	}
	
	public String getBroadDate() {
		return broadDate;
	}

	public ScheduleVO setBroadDate(String broadDate) {
		this.broadDate = broadDate;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public ScheduleVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public ProgramVO getProgram() {
		return program;
	}

	public ScheduleVO setProgram(ProgramVO program) {
		this.program = program;
		return this;
	}

	@Override
	public ScheduleVO set(SchedulePO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime() == null ? "": DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setChannelId(entity.getChannelId())
		.setProgramId(entity.getProgramId())
		.setBroadDate(entity.getBroadDate())
		.setRemark(entity.getRemark());
		return this;
	}
}
