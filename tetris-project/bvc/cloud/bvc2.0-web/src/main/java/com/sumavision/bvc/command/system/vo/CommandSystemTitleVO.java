package com.sumavision.bvc.command.system.vo;

import java.util.Date;

public class CommandSystemTitleVO {
	
	/** 主键*/
	private Long id;
	
	/** 任务title名字*/
	private String titleName;

	/** 开始时间*/
	private String beginTime;
	
	/** 是否为当前任务 false 否 ； true 是*/
	private Boolean currentTask;
	
	/** 创建用户id*/
	private Long userId;

	public Long getId() {
		return id;
	}

	public CommandSystemTitleVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getTitleName() {
		return titleName;
	}

	public CommandSystemTitleVO setTitleName(String titleName) {
		this.titleName = titleName;
		return this;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public CommandSystemTitleVO setBeginTime(String beginTime) {
		this.beginTime = beginTime;
		return this;
	}

	public Boolean getCurrentTask() {
		return currentTask;
	}

	public CommandSystemTitleVO setCurrentTask(Boolean currentTask) {
		this.currentTask = currentTask;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public CommandSystemTitleVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}
	
	
}
