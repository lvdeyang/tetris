package com.sumavision.bvc.control.device.monitor.live;

public class ExternalUserVO {

	/** 用户号码 */
	private String username;
	
	/** 用户名 */
	private String name;
	
	/** 用户是否登录 */
	private boolean logined;

	public String getUsername() {
		return username;
	}

	public ExternalUserVO setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getName() {
		return name;
	}

	public ExternalUserVO setName(String name) {
		this.name = name;
		return this;
	}

	public boolean isLogined() {
		return logined;
	}

	public ExternalUserVO setLogined(boolean logined) {
		this.logined = logined;
		return this;
	}
	
}
