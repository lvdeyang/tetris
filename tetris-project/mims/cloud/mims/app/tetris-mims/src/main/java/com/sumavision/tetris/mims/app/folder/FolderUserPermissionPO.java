package com.sumavision.tetris.mims.app.folder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 用户文件夹权限<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月21日 上午11:48:59
 */
@Entity
@Table(name = "MIMS_FOLDER_USER_PERMISSION")
public class FolderUserPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 文件夹id */
	private Long folderId;
	
	/** 用户id */
	private String userId;
	
	@Column(name = "FOLDER_ID")
	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
