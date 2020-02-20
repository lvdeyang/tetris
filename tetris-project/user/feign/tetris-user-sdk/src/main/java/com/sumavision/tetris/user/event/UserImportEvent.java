package com.sumavision.tetris.user.event;

import org.springframework.context.ApplicationEvent;

public class UserImportEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;

	private String userId;
	
	private String nickname;
	
	private String companyId;
	
	private String companyName;
	
	private String roleId;
	
	private String roleName;
	
	private String userno;
	
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
	
	public UserImportEvent(
			Object source,
			String userId, 
			String nickname, 
			String companyId, 
			String companyName) {
		super(source);
		this.userId = userId;
		this.nickname = nickname;
		this.companyId = companyId;
		this.companyName = companyName;
	}

}
