package com.sumavision.tetris.sts.device;

import java.io.Serializable;

import javax.persistence.*;

import com.sumavision.tetris.sts.common.CommonConstants.BackType;
import com.sumavision.tetris.sts.common.CommonConstants.DeviceType;
import com.sumavision.tetris.sts.common.CommonPO;


/**
 * 设备表，目前设备主要分为三种类型：SDM2.0/SDM3.0/Server；每个设备上可能有一个或多个设备节点
 * 
 * @author lxw 
 */
@Entity
@Table
public class DevicePO extends CommonPO<DevicePO> implements Serializable{

	private static final long serialVersionUID = 7941293982638029068L;

	//设备名称
	private String name;
	
	//所属设备分组的ID
	private Long groupId;
	
	//设备类型，分为SDM2.0、SDM3.0、服务器
	private DeviceType deviceType;
	
	private BackType backType = BackType.MAIN;

	@Column
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public BackType getBackType() {
		return backType;
	}

	public void setBackType(BackType backType) {
		this.backType = backType;
	}
}
