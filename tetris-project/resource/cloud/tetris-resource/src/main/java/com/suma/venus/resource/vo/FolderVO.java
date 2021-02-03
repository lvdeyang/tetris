package com.suma.venus.resource.vo;

import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderPO.FolderType;

public class FolderVO {
	
	private Long id;
	
	private String uuid;
	
	private String name;

	/** 父级文件夹id */
	private String parentId;

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
	
	private String folderFactInfo;
	
	public static FolderVO fromFolderPO(FolderPO po){
		FolderVO vo = new FolderVO();
		vo.setId(po.getId());
		vo.setName(po.getName());
		vo.setParentId(po.getParentId().toString());
		vo.setParentPath(po.getParentPath()==null? null:po.getParentPath());
		vo.setFolderType(po.getFolderType()==null? null:po.getFolderType());
		vo.setFolderIndex(po.getFolderIndex());
		vo.setUuid(po.getUuid());
		vo.setBeBvcRoot(po.getBeBvcRoot());
		vo.setSourceType(po.getSourceType());
		vo.setFolderFactInfo(po.getFolderFactInfo());
		return vo;
	}
	
	public FolderPO toPo(){
		FolderPO po = new FolderPO();
		po.setUuid(this.getUuid());
		po.setName(this.getName());
		po.setParentId(Long.valueOf(this.getParentId()));
		po.setParentPath(this.getParentPath()==null? null:this.getParentPath());
		po.setFolderType(this.getFolderType()==null? null:this.getFolderType());
		po.setFolderIndex(this.getFolderIndex());
		po.setUuid(this.getUuid());
		po.setBeBvcRoot(this.getBeBvcRoot());
		po.setSourceType(this.getSourceType());
		po.setFolderFactInfo(this.getFolderFactInfo());
		return po;
	}

	public String getFolderFactInfo() {
		return folderFactInfo;
	}

	public void setFolderFactInfo(String folderFactInfo) {
		this.folderFactInfo = folderFactInfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

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

	public FolderType getFolderType() {
		return folderType;
	}

	public void setFolderType(FolderType folderType) {
		this.folderType = folderType;
	}

	public Boolean getBeBvcRoot() {
		return beBvcRoot;
	}

	public void setBeBvcRoot(Boolean beBvcRoot) {
		this.beBvcRoot = beBvcRoot;
	}

	public Boolean getToLdap() {
		return toLdap;
	}

	public void setToLdap(Boolean toLdap) {
		this.toLdap = toLdap;
	}

	public SYNC_STATUS getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(SYNC_STATUS syncStatus) {
		this.syncStatus = syncStatus;
	}

	public SOURCE_TYPE getSourceType() {
		return sourceType;
	}

	public void setSourceType(SOURCE_TYPE sourceType) {
		this.sourceType = sourceType;
	}
	
	

}
