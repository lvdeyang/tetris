package com.suma.venus.resource.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.Type;

import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;

/**
 * 分组(文件夹形式)pojo
 * 
 * @author lxw
 *
 */
@Entity
public class FolderPO extends CommonPO<FolderPO> {

	private String uuid = BundlePO.createBundleId();

	/** 文件夹显示名称 */
	private String name;

	/** 父级文件夹id */
	private Long parentId;

	/** 当前文件夹所有父级，格式：/id/id/id */
	private String parentPath;

	private Integer folderIndex;

	/** 文件夹类型 **/
	private FolderType folderType;

	/** 是否是bvc系统内的根目录 */
	private Boolean beBvcRoot = false;

	/** 上否上传ldap,标识这条分组数据及其分组下的设备和用户是否上传到ldap */
	private Boolean toLdap = false;

	private SYNC_STATUS syncStatus = SYNC_STATUS.ASYNC;

	private SOURCE_TYPE sourceType = SOURCE_TYPE.SYSTEM;

	public enum FolderType {
		TERMINAL, // 终端文件夹
		MONITOR, // 监控文件夹
		LIVE, // 直播文件夹
		ON_DEMAND, // 点播文件夹
		USER // 用户文件夹
	}

	@Column
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Column
	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public Integer getFolderIndex() {
		return folderIndex;
	}

	public void setFolderIndex(Integer folderIndex) {
		this.folderIndex = folderIndex;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public FolderType getFolderType() {
		return folderType;
	}

	public void setFolderType(FolderType folderType) {
		this.folderType = folderType;
	}

	@Column
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(name = "sync_status")
	@Enumerated(EnumType.STRING)
	public SYNC_STATUS getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(SYNC_STATUS syncStatus) {
		this.syncStatus = syncStatus;
	}

	@Column(name = "source_type")
	@Enumerated(EnumType.STRING)
	public SOURCE_TYPE getSourceType() {
		return sourceType;
	}

	public void setSourceType(SOURCE_TYPE sourceType) {
		this.sourceType = sourceType;
	}
	
	@Type(type = "yes_no")
	@Column
	public Boolean getBeBvcRoot() {
		return beBvcRoot;
	}

	public void setBeBvcRoot(Boolean beBvcRoot) {
		this.beBvcRoot = beBvcRoot;
	}

	@Type(type = "yes_no")
	@Column
	public Boolean getToLdap() {
		return toLdap;
	}

	public void setToLdap(Boolean toLdap) {
		this.toLdap = toLdap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((beBvcRoot == null) ? 0 : beBvcRoot.hashCode());
		result = prime * result + ((folderIndex == null) ? 0 : folderIndex.hashCode());
		result = prime * result + ((folderType == null) ? 0 : folderType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
		result = prime * result + ((parentPath == null) ? 0 : parentPath.hashCode());
		result = prime * result + ((sourceType == null) ? 0 : sourceType.hashCode());
		result = prime * result + ((syncStatus == null) ? 0 : syncStatus.hashCode());
		result = prime * result + ((toLdap == null) ? 0 : toLdap.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FolderPO other = (FolderPO) obj;
		if (beBvcRoot == null) {
			if (other.beBvcRoot != null)
				return false;
		} else if (!beBvcRoot.equals(other.beBvcRoot))
			return false;
		if (folderIndex == null) {
			if (other.folderIndex != null)
				return false;
		} else if (!folderIndex.equals(other.folderIndex))
			return false;
		if (folderType != other.folderType)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parentId == null) {
			if (other.parentId != null)
				return false;
		} else if (!parentId.equals(other.parentId))
			return false;
		if (parentPath == null) {
			if (other.parentPath != null)
				return false;
		} else if (!parentPath.equals(other.parentPath))
			return false;
		if (sourceType != other.sourceType)
			return false;
		if (syncStatus != other.syncStatus)
			return false;
		if (toLdap == null) {
			if (other.toLdap != null)
				return false;
		} else if (!toLdap.equals(other.toLdap))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

}
