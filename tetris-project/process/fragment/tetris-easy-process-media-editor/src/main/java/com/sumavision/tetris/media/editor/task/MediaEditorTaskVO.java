package com.sumavision.tetris.media.editor.task;

import java.util.List;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaEditorTaskVO extends AbstractBaseVO<MediaEditorTaskVO, MediaEditorTaskPO>{

	/** 创建任务的用户 */
	private String userId;
	
	/** 拟定一个标题 */
	private String title;
	
	/** 媒体编辑任务状态 */
	private MediaEditorTaskStatus status;
	
	/** 备注 */
	private String remarks;
	
	/** 流程实例id */
	private String processInstanceId;
	
	/** 节点id */
	private Long accessPointId;
	
	/** 任务创建时间 */
	private String createTime;
	
	/** 任务进度 */
	private String completeRate;
	
	/** 流程任务下的转码任务id */
	private List<String> transcodeIds;
	
	/** 流程任务下的转码任务 */
	private List<MediaEditorTaskRatePermissionVO>  transcodes;

	public String getUserId() {
		return userId;
	}

	public MediaEditorTaskVO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public MediaEditorTaskVO setTitle(String title) {
		this.title = title;
		return this;
	}

	public MediaEditorTaskStatus getStatus() {
		return status;
	}

	public MediaEditorTaskVO setStatus(MediaEditorTaskStatus status) {
		this.status = status;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public MediaEditorTaskVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public MediaEditorTaskVO setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
		return this;
	}

	public Long getAccessPointId() {
		return accessPointId;
	}

	public MediaEditorTaskVO setAccessPointId(Long accessPointId) {
		this.accessPointId = accessPointId;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public MediaEditorTaskVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getCompleteRate() {
		return completeRate;
	}

	public MediaEditorTaskVO setCompleteRate(String completeRate) {
		this.completeRate = completeRate;
		return this;
	}

	public List<String> getTranscodeIds() {
		return transcodeIds;
	}

	public MediaEditorTaskVO setTranscodeIds(List<String> transcodeIds) {
		this.transcodeIds = transcodeIds;
		return this;
	}

	public List<MediaEditorTaskRatePermissionVO> getTranscodes() {
		return transcodes;
	}

	public MediaEditorTaskVO setTranscodes(List<MediaEditorTaskRatePermissionVO> transcodes) {
		this.transcodes = transcodes;
		return this;
	}

	@Override
	public MediaEditorTaskVO set(MediaEditorTaskPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setUserId(entity.getUserId())
		.setTitle(entity.getTitle())
		.setStatus(entity.getStatus())
		.setRemarks(entity.getRemarks())
		.setProcessInstanceId(entity.getProcessInstanceId())
		.setAccessPointId(entity.getAccessPointId())
		.setCreateTime(entity.getCreateTime()==null?"":DateUtil.format(entity.getCreateTime(), DateUtil.dateTimePattern))
		.setCompleteRate(entity.getCompleteRate());
		return this;
	}

}
