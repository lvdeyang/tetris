package com.sumavision.tetris.mims.app.folder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 角色文件夹权限<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月21日 下午12:06:14
 */
@Entity
@Table(name = "MIMS_FOLDER_ROLE_PERMISSION")
public class FolderRolePermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 角色id */
	private Long roleId;
	
	/** 文件夹id */
	private Long folderId;

	@Column(name = "ROLE_ID")
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "FOLDER_ID")
	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

}
