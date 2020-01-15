package com.suma.venus.resource.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 文件夹用户映射<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年12月26日 下午3:52:53
 */
@Entity
@Table(name = "folder_user_map")
public class FolderUserMap extends CommonPO<FolderUserMap>{

	private Long folderId;
	
	private String folderUuid;
	
	private Long userId;
	
	private String userName;
	
	/** 索引顺序编号 */ 
	private Long folderIndex;
	
	/** 同步状态,0：未同步；1：已同步 */
	private Integer syncStatus;
	
	private String creator;

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public String getFolderUuid() {
		return folderUuid;
	}

	public void setFolderUuid(String folderUuid) {
		this.folderUuid = folderUuid;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getFolderIndex() {
		return folderIndex;
	}

	public void setFolderIndex(Long folderIndex) {
		this.folderIndex = folderIndex;
	}

	public Integer getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(Integer syncStatus) {
		this.syncStatus = syncStatus;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
}
