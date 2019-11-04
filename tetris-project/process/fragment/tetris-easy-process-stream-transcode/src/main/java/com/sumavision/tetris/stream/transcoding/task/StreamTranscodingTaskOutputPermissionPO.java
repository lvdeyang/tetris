package com.sumavision.tetris.stream.transcoding.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_STREAM_TRANSCODING_TASK_OUTPUT_PERMISSION")
public class StreamTranscodingTaskOutputPermissionPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 对应任务数据库id */
	private Long messageId;
	/** 输出任务id */
	private Long outputId;
	/** 输出ip */
	private String outputIp;
	/** 输出port */
	private String outputPort;
	
	@Column(name = "MESSAGE_ID")
	public Long getMessageId() {
		return messageId;
	}
	
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	
	@Column(name = "OUTPUT_ID")
	public Long getOutputId() {
		return outputId;
	}
	
	public void setOutputId(Long outputId) {
		this.outputId = outputId;
	}

	@Column(name = "OUTPUT_IP")
	public String getOutputIp() {
		return outputIp;
	}

	public void setOutputIp(String outputIp) {
		this.outputIp = outputIp;
	}

	@Column(name = "OUTPUT_PORT")
	public String getOutputPort() {
		return outputPort;
	}

	public void setOutputPort(String outputPort) {
		this.outputPort = outputPort;
	}
}
