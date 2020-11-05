package com.sumavision.bvc.command.system.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name="BVC_COMMAND_TITLE")
public class CommandSystemTitlePO extends AbstractBasePO{
	
	/** */
	private static final long serialVersionUID = 1L;
	
	/** 任务title名字*/
	private String titleName;

	/** 开始时间*/
	private Date beginTime;
	
	/** 是否为当前任务 false 否 ； true 是*/
	private Boolean currentTask=false;
	
	/** 创建用户id*/
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public CommandSystemTitlePO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	@Column(name="TITLE_NAME")
	public String getTitleName() {
		return titleName;
	}

	public CommandSystemTitlePO setTitleName(String titleName) {
		this.titleName = titleName;
		return this;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="BEGINTIME")
	public Date getBeginTime() {
		return beginTime;
	}

	public CommandSystemTitlePO setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
		return this;
	}

	@Column(name="CURRENT_TASK")
	public Boolean getCurrentTask() {
		return currentTask;
	}

	public CommandSystemTitlePO setCurrentTask(Boolean currentTask) {
		this.currentTask = currentTask;
		return this;
	}
	
}
