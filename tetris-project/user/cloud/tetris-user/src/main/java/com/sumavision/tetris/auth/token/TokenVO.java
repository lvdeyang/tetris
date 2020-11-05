package com.sumavision.tetris.auth.token;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TokenVO extends AbstractBaseVO<TokenVO, TokenPO>{

	private String type;
	
	private String ip;
	
	private String status;
	
	private Long userId;
	
	public String getType() {
		return type;
	}

	public TokenVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getIp() {
		return ip;
	}

	public TokenVO setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public TokenVO setStatus(String status) {
		this.status = status;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public TokenVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	@Override
	public TokenVO set(TokenPO entity) throws Exception {
		this.setId(entity.getId())
			.setIp(entity.getIp())
			.setType(entity.getType().getName())
			.setStatus(entity.getStatus().toString())
			.setUserId(entity.getUserId());
		return this;
	}

}
