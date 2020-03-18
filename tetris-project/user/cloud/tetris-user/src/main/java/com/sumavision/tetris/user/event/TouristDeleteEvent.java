package com.sumavision.tetris.user.event;

import org.springframework.context.ApplicationEvent;

public class TouristDeleteEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;

	private String userId;
	
	private String nickname;
	
	private String userno;
	
	public String getUserId() {
		return userId;
	}

	public String getNickname() {
		return nickname;
	}

	public String getUserno() {
		return userno;
	}
	
	public TouristDeleteEvent(
			Object source,
			String userId, 
			String nickname,
			String userno) {
		super(source);
		this.userId = userId;
		this.nickname = nickname;
		this.userno = userno;
	}

}
