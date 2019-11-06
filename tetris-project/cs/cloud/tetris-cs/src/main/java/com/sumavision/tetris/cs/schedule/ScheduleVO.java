package com.sumavision.tetris.cs.schedule;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.cs.program.ProgramVO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ScheduleVO extends AbstractBaseVO<ScheduleVO, SchedulePO>{
	
	private Long channelId;
	
	private Long programId;
	
	private String broadDate;
	
	private String remark;
	
	/** 下发webSocket时使用，标记下发类型：file;stream */
	private String mediaType;
	
	/** 下发webSocket时使用，标记流udp地址 */
	private Integer streamUrlPort;

	/** 下发webSocket时使用，如果是加密的，标记密钥 */
	private String encryptKey;
	
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

	public String getMediaType() {
		return mediaType;
	}

	public ScheduleVO setMediaType(String mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	public Integer getStreamUrlPort() {
		return streamUrlPort;
	}

	public ScheduleVO setStreamUrlPort(Integer streamUrlPort) {
		this.streamUrlPort = streamUrlPort;
		return this;
	}

	public String getEncryptKey() {
		return encryptKey;
	}

	public ScheduleVO setEncryptKey(String encryptKey) {
		this.encryptKey = encryptKey;
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
