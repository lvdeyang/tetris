package com.sumavision.tetris.business.transcode.vo;

public class TaskVO {

	private String task_id;
	
	private Integer system_type;
	
	private String url;

	public String getTask_id() {
		return task_id;
	}

	public TaskVO setTask_id(String task_id) {
		this.task_id = task_id;
		return this;
	}

	public Integer getSystem_type() {
		return system_type;
	}

	public TaskVO setSystem_type(Integer system_type) {
		this.system_type = system_type;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public TaskVO setUrl(String url) {
		this.url = url;
		return this;
	}
	
}
