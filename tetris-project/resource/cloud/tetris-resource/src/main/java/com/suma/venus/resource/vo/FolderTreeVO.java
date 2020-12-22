package com.suma.venus.resource.vo;

import java.util.List;

import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;

/**
 * 分组树VO
 * 
 * @author lxw
 * 
 * @author chenmo
 *
 */
public class FolderTreeVO {

	/** 节点ID */
	private Long id;

	/** 父节点ID */
	private Long parentId;

	/** 节点显示名称 */
	private String name;

	/** 是否文件夹节点 */
	@Deprecated
	private boolean beFolder;

	/** 标记文件夹节点是否上传ldap **/
	private Boolean toLdap;

	/** 设备Uid，当beFolder为false时有效，设备节点时有效 */
	private String bundleId;

	/** 用户名，当beFolder为false时有效，用户节点时有效 **/
	private String username;

	/** 文件夹的类型 **/
	private String folderType;

	/** 子节点集合 */
	private List<FolderTreeVO> children;

	/** trur表示本系统的数据，false来自外部同步的数据 **/
	private Boolean systemSourceType;

	private Integer folderIndex;

	private String nodeType;
	
	private ONLINE_STATUS onlineStatus;
	
	public ONLINE_STATUS getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(ONLINE_STATUS onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FolderTreeVO> getChildren() {
		return children;
	}

	public void setChildren(List<FolderTreeVO> children) {
		this.children = children;
	}

	public boolean isBeFolder() {
		return beFolder;
	}

	public void setBeFolder(boolean beFolder) {
		this.beFolder = beFolder;
	}

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getToLdap() {
		return toLdap;
	}

	public void setToLdap(Boolean toLdap) {
		this.toLdap = toLdap;
	}

	public Boolean getSystemSourceType() {
		return systemSourceType;
	}

	public void setSystemSourceType(Boolean systemSourceType) {
		this.systemSourceType = systemSourceType;
	}

	public Integer getFolderIndex() {
		return folderIndex;
	}

	public void setFolderIndex(Integer folderIndex) {
		this.folderIndex = folderIndex;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getFolderType() {
		return folderType;
	}

	public void setFolderType(String folderType) {
		this.folderType = folderType;
	}

}
