package com.sumavision.tetris.user.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.sumavision.tetris.user.UserVO;

public class UserDeletedEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;
	
	private List<UserVO> users;

	public List<UserVO> getUsers() {
		return users;
	}

	public void setUsers(List<UserVO> users) {
		this.users = users;
	}
	
	public UserDeletedEvent(
			Object source,
			List<UserVO> users) {
		super(source);
		this.users = users;
	}
	
}
