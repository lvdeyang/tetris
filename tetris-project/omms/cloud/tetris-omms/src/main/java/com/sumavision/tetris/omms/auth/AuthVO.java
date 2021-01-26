package com.sumavision.tetris.omms.auth;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class AuthVO extends AbstractBaseVO<AuthVO, AuthPO>{
	private String name;
	private String content;//json
	private String deviceId;
	
	public String getName() {
		return name;
	}

	public AuthVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getContent() {
		return content;
	}

	public AuthVO setContent(String content) {
		this.content = content;
		return this;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public AuthVO setDeviceId(String deviceId) {
		this.deviceId = deviceId;
		return this;
	}

	@Override
	public AuthVO set(AuthPO entity) throws Exception {
		// TODO Auto-generated method stub
		this.setName(entity.getName())
		    .setId(entity.getId())
		    .setContent(entity.getContent())
		    .setDeviceId(entity.getDeviceId());
		return this;
	}

	
}
