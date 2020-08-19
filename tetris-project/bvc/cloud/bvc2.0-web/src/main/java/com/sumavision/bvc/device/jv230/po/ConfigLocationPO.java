package com.sumavision.bvc.device.jv230.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 大屏配置任务位置信息 
 * @Description: 每个位置对应一个Jv230
 * @author wjw
 * @date 2018年11月26日 上午10:40:54
 */
@Entity
@Table(name = "BVC_COMBINE_JV230_CONFIG_TASK_LOCATION")
public class ConfigLocationPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	//位置
	private byte[] location;
	
	/**
	 * @Fields configTask:位置对应任务
	 */
	private ConfigTaskPO configTask;

	@Column(name = "LOCATION", columnDefinition = "LONGBLOB", nullable = true)
	public byte[] getLocation() {
		return location;
	}

	public void setLocation(byte[] location) {
		this.location = location;
	}

	@ManyToOne
	@JoinColumn(name="CONFIGTASK_ID")
	public ConfigTaskPO getConfigTask() {
		return configTask;
	}

	public void setConfigTask(ConfigTaskPO configTask) {
		this.configTask = configTask;
	}
}
