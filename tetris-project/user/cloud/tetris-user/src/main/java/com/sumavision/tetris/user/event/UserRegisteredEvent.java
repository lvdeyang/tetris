package com.sumavision.tetris.user.event;

import org.springframework.context.ApplicationEvent;

public class UserRegisteredEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;

	private String userId;
	
	private String nickname;
	
	private String companyId;
	
	private String companyName;
	
	private String roleId;
	
	private String roleName;
	
	private String userno;
	
	private String worknodeUid;
	
	public String getWorknodeUid() {
		return worknodeUid;
	}

	public String getUserId() {
		return userId;
	}

	public String getNickname() {
		return nickname;
	}

	public String getCompanyId() {
		return companyId;
	}

	public String getCompanyName() {
		return companyName;
	}
	
	public String getRoleId() {
		return roleId;
	}

	public String getRoleName() {
		return roleName;
	}
	
	public String getUserno() {
		return userno;
	}

	public UserRegisteredEvent(
			Object source, 
			String userId, 
			String nickname, 
			String companyId, 
			String companyName,
			String roleId,
			String roleName,
			String worknodeUid) {
		
		super(source);
		
		this.userId = userId;
		this.nickname = nickname;
		this.companyId = companyId;
		this.companyName = companyName;
		this.roleId = roleId;
		this.roleName = roleName;
		this.worknodeUid = worknodeUid;
	}
	
	public UserRegisteredEvent(
			Object source, 
			String userId, 
			String nickname, 
			String companyId, 
			String companyName,
			String userno,
			String worknodeUid) {
		
		super(source);
		
		this.userId = userId;
		this.nickname = nickname;
		this.companyId = companyId;
		this.companyName = companyName;
		this.userno = userno;
		this.worknodeUid = worknodeUid;
	}
	
	public UserRegisteredEvent(
			Object source, 
			String userId, 
			String nickname, 
			String companyId, 
			String companyName,
			String worknodeUid) {
		
		super(source);
		
		this.userId = userId;
		this.nickname = nickname;
		this.companyId = companyId;
		this.companyName = companyName;
		this.worknodeUid = worknodeUid;
	}
	
	public UserRegisteredEvent(
			Object source, 
			String userId, 
			String nickname,
			String worknodeUid) {
		
		super(source);
		
		this.userId = userId;
		this.nickname = nickname;
		this.worknodeUid = worknodeUid;
	}

}
