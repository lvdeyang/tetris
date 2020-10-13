package com.sumavision.signal.bvc.entity.po;

import javax.persistence.*;

import com.sumavision.signal.bvc.common.enumeration.CommonConstants.ProtocolType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

import java.util.ArrayList;
import java.util.List;

/**
 * 能力绑定端口信息<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年12月16日 上午10:09:40
 */
@Entity
@Table(name = "BVC_CAPACITY_PERMISSION_PORT")
public class CapacityPermissionPortPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 设备bundleId */
	private String bundleId;


	/** 接入层layerId */
	private String layerId;

	/** 设备channelId */
	private String channelId;
	
	/** 设备ip */
	private String bundleIp;
	
	/** 转码能力ip */
	private String capacityIp;
	
	/** 转码能力端口 */
	private Long capacityPort;


	/** 该源的转码任务id */
	private String taskId;

	private List<SourcePO> sourcePOs = new ArrayList();

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getBundleIp() {
		return bundleIp;
	}

	public void setBundleIp(String bundleIp) {
		this.bundleIp = bundleIp;
	}

	public String getCapacityIp() {
		return capacityIp;
	}

	public void setCapacityIp(String capacityIp) {
		this.capacityIp = capacityIp;
	}

	public Long getCapacityPort() {
		return capacityPort;
	}

	public void setCapacityPort(Long capacityPort) {
		this.capacityPort = capacityPort;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	@OneToMany(fetch= FetchType.EAGER , cascade=CascadeType.ALL,orphanRemoval = true)
	@JoinColumn(name="bvcCapacityPermissionId")
	public List<SourcePO> getSourcePOs() {
		return sourcePOs;
	}

	public void setSourcePOs(List<SourcePO> sourcePOs) {
		this.sourcePOs = sourcePOs;
	}
}
