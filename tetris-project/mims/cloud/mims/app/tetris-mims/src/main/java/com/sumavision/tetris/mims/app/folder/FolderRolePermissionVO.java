package com.sumavision.tetris.mims.app.folder;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class FolderRolePermissionVO extends AbstractBaseVO<FolderRolePermissionVO, FolderRolePermissionPO>{

	private Long roleId;
	
	private String roleName;
	
	private Long folderId;
	
	private Boolean autoGeneration;
	
	public Long getRoleId() {
		return roleId;
	}

	public FolderRolePermissionVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public FolderRolePermissionVO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public Long getFolderId() {
		return folderId;
	}

	public FolderRolePermissionVO setFolderId(Long folderId) {
		this.folderId = folderId;
		return this;
	}

	public Boolean getAutoGeneration() {
		return autoGeneration;
	}

	public FolderRolePermissionVO setAutoGeneration(Boolean autoGeneration) {
		this.autoGeneration = autoGeneration;
		return this;
	}

	@Override
	public FolderRolePermissionVO set(FolderRolePermissionPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setRoleId(entity.getRoleId())
			.setRoleName(entity.getRoleName())
			.setFolderId(entity.getFolderId())
			.setAutoGeneration(entity.getAutoGeneration());
		return this;
	}
	
}
