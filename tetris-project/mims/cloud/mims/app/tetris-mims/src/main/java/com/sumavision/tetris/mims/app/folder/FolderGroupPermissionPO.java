package com.sumavision.tetris.mims.app.folder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 用户组文件夹权限<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月21日 上午11:51:48
 */
@Entity
@Table(name = "MIMS_FOLDER_GROUP_PERMISSION")
public class FolderGroupPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 文件夹id */
	private Long folderId;
	
	/** 组id */
	private String groupId;

	@Column(name = "FOLDER_ID")
	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	@Column(name = "GROUP_ID")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
}
