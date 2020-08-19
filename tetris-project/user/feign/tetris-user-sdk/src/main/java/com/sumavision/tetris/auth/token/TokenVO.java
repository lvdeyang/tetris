package com.sumavision.tetris.auth.token;

public class TokenVO{
	
	private Long id;
	
	private String uuid;
	
	private String updateTime;

	private String type;
	
	private String ip;
	
	private String status;
	
	private Long userId;
	
	public Long getId() {
		return id;
	}

	public TokenVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public TokenVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public TokenVO setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
		return this;
	}

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

}