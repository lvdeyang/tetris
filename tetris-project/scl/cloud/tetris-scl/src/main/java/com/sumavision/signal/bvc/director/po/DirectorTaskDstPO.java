package com.sumavision.signal.bvc.director.po;

import com.sumavision.tetris.orm.po.AbstractBasePO;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 转发目的
 */
@Entity
@Table(name = "BVC_DIRECTOR_DST")
public class DirectorTaskDstPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	private Long taskId;
	private String bundleId;
	private String channelId;

	public Long getTaskId() {
		return taskId;
	}

	public String getBundleId() {
		return bundleId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
}
