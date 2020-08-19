package com.sumavision.tetris.media.editor.task;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaEditorTaskRatePermissionVO extends AbstractBaseVO<MediaEditorTaskRatePermissionVO, MediaEditorTaskRatePermissionPO>{

	/** 流程任务id */
	private Long taskId;
	
	/** 转码任务id */
	private String transcodeId;
	
	/** 进度 */
	private int rate;
	
	/** 存储地址 */
	private String saveUrl;
	
	/** 存储目录Id */
	private Long folderId;
	
	/** 媒资标签 */
	private String tags;
	
	@Override
	public MediaEditorTaskRatePermissionVO set(MediaEditorTaskRatePermissionPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setTaskId(entity.getTaskId())
		.setTranscodeId(entity.getTranscodeId())
		.setSaveUrl(entity.getSaveUrl())
		.setRate(entity.getRate())
		.setFolderId(entity.getFolderId())
		.setTags(entity.getTags());
		return this;
	}

	public Long getTaskId() {
		return taskId;
	}

	public MediaEditorTaskRatePermissionVO setTaskId(Long taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getTranscodeId() {
		return transcodeId;
	}

	public MediaEditorTaskRatePermissionVO setTranscodeId(String transcodeId) {
		this.transcodeId = transcodeId;
		return this;
	}

	public int getRate() {
		return rate;
	}

	public MediaEditorTaskRatePermissionVO setRate(int rate) {
		this.rate = rate;
		return this;
	}

	public String getSaveUrl() {
		return saveUrl;
	}

	public MediaEditorTaskRatePermissionVO setSaveUrl(String saveUrl) {
		this.saveUrl = saveUrl;
		return this;
	}

	public Long getFolderId() {
		return folderId;
	}

	public MediaEditorTaskRatePermissionVO setFolderId(Long folderId) {
		this.folderId = folderId;
		return this;
	}

	public String getTags() {
		return tags;
	}

	public MediaEditorTaskRatePermissionVO setTags(String tags) {
		this.tags = tags;
		return this;
	}
}
