package com.sumavision.bvc.device.group.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 设备组关联的录制方案 （区别于系统级录制方案数据）
 * @author zy
 * @date 2018年7月31日 上午10:41:07 
 */
@Entity
@Table(name="BVC_DEVICE_GROUP_RECORD_SCHEME")
public class DeviceGroupRecordSchemePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 录制名称 */
	private String name;
	
	/** 录制的角色id */
	private Long roleId;
	
	/** 录制的角色名称 */
	private String roleName;

	/** 关联的设备组 */
	private DeviceGroupPO group;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "ROLE_ID")
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "ROLE_NAME")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@ManyToOne
	@JoinColumn(name = "GROUP_ID")
	public DeviceGroupPO getGroup() {
		return group;
	}

	public void setGroup(DeviceGroupPO group) {
		this.group = group;
	}
	
	/**
	 * @Title: 从系统资源中复制数据 
	 * @param entity 系统资源
	 * @return RecordSchemePO 设备组资源
	 */
	public DeviceGroupRecordSchemePO set(com.sumavision.bvc.system.po.RecordSchemePO entity){
		this.setUuid(entity.getUuid());
		this.setUpdateTime(new Date());
		this.setName(entity.getName());
		return this;
	}
	
}
