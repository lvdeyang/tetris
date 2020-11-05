package com.sumavision.tetris.stream.transcoding.task;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class StreamTranscodingTaskOutputPermissionVO extends AbstractBaseVO<StreamTranscodingTaskOutputPermissionVO, StreamTranscodingTaskOutputPermissionPO>{
	/** 对应任务数据库id */
	private Long messageId;
	/** 输出任务id */
	private Long outputId;
	/** 输出ip */
	private String outputIp;
	/** 输出port */
	private String outputPort;
	
	public Long getMessageId() {
		return messageId;
	}
	public StreamTranscodingTaskOutputPermissionVO setMessageId(Long messageId) {
		this.messageId = messageId;
		return this;
	}
	public Long getOutputId() {
		return outputId;
	}
	public StreamTranscodingTaskOutputPermissionVO setOutputId(Long outputId) {
		this.outputId = outputId;
		return this;
	}
	public String getOutputIp() {
		return outputIp;
	}
	public StreamTranscodingTaskOutputPermissionVO setOutputIp(String outputIp) {
		this.outputIp = outputIp;
		return this;
	}
	public String getOutputPort() {
		return outputPort;
	}
	public StreamTranscodingTaskOutputPermissionVO setOutputPort(String outputPort) {
		this.outputPort = outputPort;
		return this;
	}
	
	@Override
	public StreamTranscodingTaskOutputPermissionVO set(StreamTranscodingTaskOutputPermissionPO entity)
			throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime() == null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setMessageId(entity.getMessageId())
			.setOutputId(entity.getOutputId())
			.setOutputIp(outputIp)
			.setOutputPort(outputPort);
		return this;
	}
}
