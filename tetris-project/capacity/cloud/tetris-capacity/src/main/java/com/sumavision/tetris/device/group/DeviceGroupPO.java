package com.sumavision.tetris.device.group;


import com.sumavision.tetris.business.common.enumeration.BackupStrategy;
import com.sumavision.tetris.orm.po.AbstractBasePO;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;


/**
 * 设备分组
 * 此PO同时能代表SDM以及SDM分组的PO，根据GroupType划分。
 * SDM分组是SDM的上级信息
 */
@Entity
@Table(name="TETRIS_CAPACITY_DEVICE_GROUP")
public class DeviceGroupPO extends AbstractBasePO {

	/**
	 * 分组名称
	 */
	private String name;

	private BackupStrategy backupStrategy;

	/**
	 * 分组级别备份标志
	 */
	private Boolean autoBackupFlag = true;

	private String dataNetIds;


	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column
	@Type(type = "yes_no")
	public Boolean getAutoBackupFlag() {
		return autoBackupFlag;
	}

	public void setAutoBackupFlag(Boolean autoBackupFlag) {
		this.autoBackupFlag = autoBackupFlag;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public BackupStrategy getBackupStrategy() {
		return backupStrategy;
	}

	public void setBackupStrategy(BackupStrategy backupStrategy) {
		this.backupStrategy = backupStrategy;
	}

	@Column
	public String getDataNetIds() {
		return dataNetIds;
	}

	public void setDataNetIds(String dataNetIds) {
		this.dataNetIds = dataNetIds;
	}
}
