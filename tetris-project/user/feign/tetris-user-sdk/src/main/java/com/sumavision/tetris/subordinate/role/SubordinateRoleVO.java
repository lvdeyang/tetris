package com.sumavision.tetris.subordinate.role;

import java.util.Date;

import com.sumavision.tetris.commons.util.date.DateUtil;


/**
 * 公司角色<br/>
 * <b>作者:</b>ql<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月11日 上午10:43:34
 */
public class SubordinateRoleVO {

	private Long id;
	
	private String uuid;
	
	private String updateTime;
	
	private String name;
	
	private Long companyId;
	
	private String userId;

	public Long getId() {
		return id;
	}

	public SubordinateRoleVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public SubordinateRoleVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public SubordinateRoleVO setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
		return this;
	}
	
	public SubordinateRoleVO setUpdateTime(Date updateTime){
		return setUpdateTime(DateUtil.format(updateTime, DateUtil.dateTimePattern));
	}
	
	public String getName() {
		return name;
	}

	public SubordinateRoleVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public SubordinateRoleVO setCompanyId(Long companyId) {
		this.companyId = companyId;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public SubordinateRoleVO setUserId(String userId) {
		this.userId = userId;
		return this;
	}
}
