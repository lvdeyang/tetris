package com.sumavision.tetris.media.editor.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_MEDIA_EDITOR_TASK_RATE_PERMISSION")
public class MediaEditorTaskRatePermissionPO extends AbstractBasePO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 流程任务id */
	private Long taskId;
	
	/** 转码任务id */
	private String transcodeId;
	
	/** 进度 */
	private int rate;
	
	/** 存储地址 */
	private String saveUrl;

	@Column(name = "TASK_ID")
	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	@Column(name = "TRANSCODE_ID")
	public String getTranscodeId() {
		return transcodeId;
	}

	public void setTranscodeId(String transcodeId) {
		this.transcodeId = transcodeId;
	}

	@Column(name = "RATE")
	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	@Column(name = "SAVE_URL")
	public String getSaveUrl() {
		return saveUrl;
	}

	public void setSaveUrl(String saveUrl) {
		this.saveUrl = saveUrl;
	}
}
