package com.sumavision.signal.bvc.entity.po.director;

import com.sumavision.signal.bvc.entity.enumeration.director.RateControlType;
import com.sumavision.signal.bvc.entity.enumeration.director.SwitchType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 转发源
 */
@Entity
@Table(name = "BVC_DIRECTOR_SRC")
public class DirectorTaskSrcPO extends AbstractBasePO{
	
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
