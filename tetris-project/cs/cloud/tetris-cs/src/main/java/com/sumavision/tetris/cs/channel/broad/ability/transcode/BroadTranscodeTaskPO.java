package com.sumavision.tetris.cs.channel.broad.ability.transcode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CS_BROAD_TRANSCODE_TASK_PERMISSION")
public class BroadTranscodeTaskPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 频道id */
	private Long channelId;
	
	/** 任务id */
	private String taskId;
	
	/** 任务类型 */
	private String transcodeType;

	@Column(name = "CHANNEL_ID")
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	@Column(name = "TASK_ID")
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Column(name = "TRANSCODE_TYPE")
	public String getTranscodeType() {
		return transcodeType;
	}

	public void setTranscodeType(String transcodeType) {
		this.transcodeType = transcodeType;
	}
}
