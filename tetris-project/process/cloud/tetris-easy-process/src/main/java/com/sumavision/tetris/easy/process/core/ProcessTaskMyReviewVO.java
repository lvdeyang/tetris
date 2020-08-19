package com.sumavision.tetris.easy.process.core;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * 待审核任务<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年7月23日 下午3:53:11
 */
public class ProcessTaskMyReviewVO extends AbstractBaseVO<ProcessTaskMyReviewVO, Object>{

	/** 流程定义id */
	private String processDefinitionId;
	
	/** 流程实例id */
	private String processInstanceId;
	
	/** 任务定义id */
	private String taskDefinitionKey;
	
	/** 任务id */
	private String taskId;
	
	/** 流程定义名称 */
	private String processName;
	
	/** 流程开始时间 */
	private String startTime;
	
	/** 流程开始用户 */
	private String startUser;
	
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public ProcessTaskMyReviewVO setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
		return this;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public ProcessTaskMyReviewVO setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
		return this;
	}
	
	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public ProcessTaskMyReviewVO setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
		return this;
	}

	public String getTaskId() {
		return taskId;
	}

	public ProcessTaskMyReviewVO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getProcessName() {
		return processName;
	}

	public ProcessTaskMyReviewVO setProcessName(String processName) {
		this.processName = processName;
		return this;
	}

	public String getStartTime() {
		return startTime;
	}

	public ProcessTaskMyReviewVO setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public String getStartUser() {
		return startUser;
	}

	public ProcessTaskMyReviewVO setStartUser(String startUser) {
		this.startUser = startUser;
		return this;
	}

	@Override
	public ProcessTaskMyReviewVO set(Object entity) throws Exception {
		return this;
	}
	
}
