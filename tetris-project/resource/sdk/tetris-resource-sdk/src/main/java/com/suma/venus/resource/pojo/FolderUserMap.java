package com.suma.venus.resource.pojo;

import java.util.Comparator;

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
	
	private String userUuid;
	
	private String userName;
	
	/** 用户所在节点 */
	private String userNode;
	
	/** 用户状态--暂时用来记录ldap用户的状态 */
	private boolean userStatus;
	
	/** 用户号码--暂时用来记录ldap的用户号码 */
	private String userNo;
	
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

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
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

	public String getUserNode() {
		return userNode;
	}

	public void setUserNode(String userNode) {
		this.userNode = userNode;
	}

	public boolean isUserStatus() {
		return userStatus;
	}

	public void setUserStatus(boolean userStatus) {
		this.userStatus = userStatus;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	
	public static class FolderUserComparator implements Comparator<FolderUserMap>{

		@Override
		public int compare(FolderUserMap o1, FolderUserMap o2) {
			return Integer.parseInt(o1.getFolderIndex().toString())-Integer.parseInt(o2.getFolderIndex().toString());
		}
		
	}
	
}
