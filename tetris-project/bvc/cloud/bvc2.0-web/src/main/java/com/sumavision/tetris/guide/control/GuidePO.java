package com.sumavision.tetris.guide.control;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 
 * 导播任务表<br/>
 * <p>
 * 详细描述
 * </p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月2日 下午5:18:35
 */
@Entity
@Table(name = "TETRIS_GUIDE_PO")
public class GuidePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 名称 */
	private String taskName;

	/** 直播时长 */
	private String liveBroadcastDuration;

	/** 创建时间 */
	private Date creationTime;
	
	/** 直播状态 */
	private Status status;

	@Column(name = "TASK_NAME")
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	@Column(name = "LIVE_BROADCAST_DURATION")
	public String getLiveBroadcastDuration() {
		return liveBroadcastDuration;
	}

	public void setLiveBroadcastDuration(String liveBroadcastDuration) {
		this.liveBroadcastDuration = liveBroadcastDuration;
	}
	
	@Column(name = "CREATION_TIME")
	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
