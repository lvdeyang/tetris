package com.sumavision.tetris.cms.region;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CMS_REGION_USER_PERMISSION")
public class RegionUserPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 地区id */
	private Long regionId;
	
	/** 地区名称  */
	private String regionName;
	
	/** 地区编码 */
	private String regionCode;
	
	/** 用户id */
	private String userId;
	
	/** 用户名 */
	private String userName;
	
	/** 组织id */
	private String groupId;
	
	/** 组织名 */
	private String groupName;

	@Column(name = "REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	@Column(name = "REGION_NAME")
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	@Column(name = "REGION_CODE")
	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "USER_NAME")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "GROUP_ID")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Column(name = "GROUP_NAME")
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}
