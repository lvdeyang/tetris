package com.sumavision.tetris.sts.device.group;


import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import com.sumavision.tetris.sts.common.CommonConstants.GroupType;
import com.sumavision.tetris.sts.common.CommonPO;

import javax.persistence.*;

import java.io.Serializable;


/**
 * 设备分组
 * 此PO同时能代表SDM以及SDM分组的PO，根据GroupType划分。
 * SDM分组是SDM的上级信息
 */
@Entity
@Table
public class DeviceGroupPO extends CommonPO<DeviceGroupPO> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5264600071692351179L;

	/**
	 * 分组名称
	 */

	@Length(max=20, message="不能大于20字符")
	private String name;
	/**
	 * 设备类型
	 */
	private GroupType type;

	private String dataNetIds;

	private Long groupId;
	
	//转发网卡分组id（转发工作模式）
	private Long transmitNetId;

	/**
	 * 分组级别备份标志
	 */
	private Boolean autoBackupFlag = true;

	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column
	@Enumerated(EnumType.STRING)
	public GroupType getType() {
		return type;
	}

	public void setType(GroupType type) {
		this.type = type;
	}
	@Column
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Column
	public String getDataNetIds() {
		return dataNetIds;
	}

	public void setDataNetIds(String dataNetIds) {
		this.dataNetIds = dataNetIds;
	}
	@Column
	public Long getTransmitNetId() {
		return transmitNetId;
	}
	public void setTransmitNetId(Long transmitNetId) {
		this.transmitNetId = transmitNetId;
	}

	@Column
	@Type(type = "yes_no")
	public Boolean getAutoBackupFlag() {
		return autoBackupFlag;
	}

	public void setAutoBackupFlag(Boolean autoBackupFlag) {
		this.autoBackupFlag = autoBackupFlag;
	}
}
