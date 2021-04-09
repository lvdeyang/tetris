package com.sumavision.bvc.control.device.group.vo;

import com.sumavision.bvc.device.group.po.DeviceGroupRecordSchemePO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;


public class DeviceGroupRecordSchemeVO extends AbstractBaseVO<DeviceGroupRecordSchemeVO, DeviceGroupRecordSchemePO>{

	private Long id;
	
	private String uuid;
	
	private String name;
	
	private Long roleId;
	
	private String roleName;

	public String getName() {
		return name;
	}

	public DeviceGroupRecordSchemeVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public DeviceGroupRecordSchemeVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public DeviceGroupRecordSchemeVO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}
	
	public String getUuid() {
		return uuid;
	}

	public DeviceGroupRecordSchemeVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public Long getId() {
		return id;
	}

	public DeviceGroupRecordSchemeVO setId(Long id) {
		this.id = id;
		return this;
	}

	public DeviceGroupRecordSchemeVO set(DeviceGroupRecordSchemePO entity){
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setName(entity.getName())
			.setRoleId(entity.getRoleId())
			.setRoleName(entity.getRoleName());
		return this;
			
	}
	
	
}
