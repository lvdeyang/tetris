package com.sumavision.bvc.control.welcome;

import com.suma.venus.resource.base.bo.UserBO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class UserVO extends AbstractBaseVO<UserVO, UserBO>{

	private String name;
	
	private String userno;
	
	private boolean isAdmin;
	
	private int level;
	
	private String phone;
	
	private String email;
	
	private String createTime;
	
	private String creator;
	
	public String getName() {
		return name;
	}

	public UserVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getUserno() {
		return userno;
	}

	public UserVO setUserno(String userno) {
		this.userno = userno;
		return this;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public UserVO setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
		return this;
	}

	public int getLevel() {
		return level;
	}

	public UserVO setLevel(int level) {
		this.level = level;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public UserVO setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public UserVO setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public UserVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getCreator() {
		return creator;
	}

	public UserVO setCreator(String creator) {
		this.creator = creator;
		return this;
	}

	@Override
	public UserVO set(UserBO entity) throws Exception {
		this.setId(entity.getId())
			.setName(entity.getName())
			.setUserno(entity.getUserNo())
			.setAdmin(entity.isAdmin())
			.setLevel(entity.getLevel())
			.setPhone(entity.getPhone())
			.setEmail(entity.getEmail())
			.setCreateTime(entity.getCreateTime())
			.setCreator(entity.getCreater());
		return this;
	}
	
}
