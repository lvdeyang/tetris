package com.sumavision.bvc.system.dto;

import java.util.Date;

import com.sumavision.bvc.system.po.RecordSchemePO;

public class RecordSchemeDTO {

	private Long id;
	
	private String uuid;
	
	private Date updateTime;
	
	private String name;
	
	private Long roleId;
	
	private String roleName;
	
	public RecordSchemeDTO(){}
	
	public RecordSchemeDTO(
			Long id, 
			String uuid, 
			Date updateTime, 
			String name, 
			Long roleId, 
			String roleName){
		
		this.id = id;
		this.uuid = uuid;
		this.updateTime = updateTime;
		this.name = name;
		this.roleId = roleId;
		this.roleName = roleName;
	}

	public Long getId() {
		return id;
	}

	public RecordSchemeDTO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public RecordSchemeDTO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public RecordSchemeDTO setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public String getName() {
		return name;
	}

	public RecordSchemeDTO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public RecordSchemeDTO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public RecordSchemeDTO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}
	
	public RecordSchemeDTO set(RecordSchemePO entity){
		this.setId(entity.getId())
			.setUpdateTime(entity.getUpdateTime())
			.setUuid(entity.getUuid())
			.setName(entity.getName())
			.setRoleId(entity.getRole().getId())
			.setRoleName(entity.getRole().getName());
		return this;
	}
	
}
