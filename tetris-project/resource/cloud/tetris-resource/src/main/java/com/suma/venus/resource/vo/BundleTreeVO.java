package com.suma.venus.resource.vo;

import java.util.List;

import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;

public class BundleTreeVO {
	
	/** 根目录节点id */
	public static final long FOLDERID_ROOT = -1l;

	/**folderId**/
	private String groupId;
	
	/** 父节点ID */
	private Long parentId;
	
	/**folderName**/
	private String groupname;
	
	/** 是否文件夹节点 */
	@Deprecated
	private boolean beFolder = false;
	
	/** 文件夹的类型 **/
	private String folderType;
	
	/**bundleId**/
	private String emrid;
	
	/**bundleName**/
	private String emrname;
	
	/**经度**/
	private String longitude;
	
	/**纬度**/
	private String latitude;
	
	/**在线状态**/
	private String status;
	
	private List<BundleTreeVO> children;

	public String getGroupId() {
		return groupId;
	}

	public BundleTreeVO setGroupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public Long getParentId() {
		return parentId;
	}

	public BundleTreeVO setParentId(Long parentId) {
		this.parentId = parentId;
		return this;
	}

	public String getGroupname() {
		return groupname;
	}

	public BundleTreeVO setGroupname(String groupname) {
		this.groupname = groupname;
		return this;
	}

	public boolean isBeFolder() {
		return beFolder;
	}

	public BundleTreeVO setBeFolder(boolean beFolder) {
		this.beFolder = beFolder;
		return this;
	}

	public String getFolderType() {
		return folderType;
	}

	public BundleTreeVO setFolderType(String folderType) {
		this.folderType = folderType;
		return this;
	}

	public String getEmrid() {
		return emrid;
	}

	public BundleTreeVO setEmrid(String emrid) {
		this.emrid = emrid;
		return this;
	}

	public String getEmrname() {
		return emrname;
	}

	public BundleTreeVO setEmrname(String emrname) {
		this.emrname = emrname;
		return this;
	}

	public String getLongitude() {
		return longitude;
	}

	public BundleTreeVO setLongitude(String longitude) {
		this.longitude = longitude;
		return this;
	}

	public String getLatitude() {
		return latitude;
	}

	public BundleTreeVO setLatitude(String latitude) {
		this.latitude = latitude;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public BundleTreeVO setStatus(String status) {
		this.status = status;
		return this;
	}

	public List<BundleTreeVO> getChildren() {
		return children;
	}

	public BundleTreeVO setChildren(List<BundleTreeVO> children) {
		this.children = children;
		return this;
	}
	
	public BundleTreeVO set(FolderPO folderPO){
		this.setGroupId(folderPO.getId().toString())
		.setGroupname(folderPO.getName())
		.setBeFolder(true)		
		.setFolderType(folderPO.getFolderType()==null? null : folderPO.getFolderType().toString());
		
		return this;
	}
	
	public BundleTreeVO set(BundlePO bundlePO){
		this.setGroupId(bundlePO.getFolderId().toString())
		.setBeFolder(false)
		.setEmrid(bundlePO.getId().toString())
		.setEmrname(bundlePO.getBundleName())
		.setLongitude(bundlePO.getLongitude())
		.setLatitude(bundlePO.getLatitude());
//		.setStatus(bundlePO.getOnlineStatus().toString());
		
		if(null != bundlePO.getOnlineStatus()){
			switch (bundlePO.getOnlineStatus().toString()){
			case "ONLINE":
				this.setStatus("bundle-online");
				break;
			case "OFFLINE":
				this.setStatus("bundle-offline");
				break;
			default:
				break;
			}			
		}
		
		return this;
	}
}
