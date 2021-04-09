package com.sumavision.bvc.control.stb.vo;

import com.sumavision.bvc.device.group.enumeration.GroupStatus;

public class DeviceGroupStbVO{

	private Long id;
	
	private String name;
	
	private Long userId;
	
	private String userName;
	
	//private Long systemTplId;
	
	//private String systemTplName;
	
	private GroupStatus status;
	
	public DeviceGroupStbVO(){}
	
	public DeviceGroupStbVO(
			Long id,
			String name,
			Long userId,
			String userName,
			GroupStatus status) {
		this.id = id;
		this.name = name;
		this.userId = userId;
		this.userName = userName;
		this.status = status;
	}

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public GroupStatus getStatus() {
		return status;
	}

	public void setStatus(GroupStatus status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "{" + "groupId:" + id + "," + 
			   "name:" + name + "," + 
			   "userName:" + userName + "," +
			   "userId:" + userId + "," + 
			   "status:" + status + "}";
	}
	
}
