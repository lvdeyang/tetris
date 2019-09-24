package com.sumavision.tetris.easy.process.core;

public class ProcessMyStartVO {

	/** 流程定义id */
	private String processDefinitionKey;
	
	/** 流程实例id */
	private String processInstanceId;
	
	/** 开始时间 */
	private String startTime;
	
	/** 结束时间 */
	private String endTime;
	
	/** 流程定义名称 */
	private String name;
	
	/** 流程实例主题 */
	private String category;
	
	/** 流程实例承载的业务内容 */
	private String business;
	
	/** 流程状态 */
	private String status;

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public ProcessMyStartVO setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
		return this;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public ProcessMyStartVO setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
		return this;
	}

	public String getStartTime() {
		return startTime;
	}

	public ProcessMyStartVO setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public String getEndTime() {
		return endTime;
	}

	public ProcessMyStartVO setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}

	public String getName() {
		return name;
	}

	public ProcessMyStartVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getCategory() {
		return category;
	}

	public ProcessMyStartVO setCategory(String category) {
		this.category = category;
		return this;
	}

	public String getBusiness() {
		return business;
	}

	public ProcessMyStartVO setBusiness(String business) {
		this.business = business;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public ProcessMyStartVO setStatus(String status) {
		this.status = status;
		return this;
	}
	
}
