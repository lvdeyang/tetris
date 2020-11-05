/**
 * 
 */
package com.sumavision.tetris.omms.hardware.database;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DatabaseVO extends AbstractBaseVO<DatabaseVO, DatabasePO>{
	
	private Long id;
	
	/** 数据库IP */
	private String databaseIP;
	
	/** 数据库端口 */
	private String databasePort;
	
	/** 用户名 */
	private String username;
	
	/** 密码 */
	private String password;
	
	/** 服务器id */
	private Long serverId;

	public Long getId() {
		return id;
	}

	public DatabaseVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getDatabaseIP() {
		return databaseIP;
	}

	public DatabaseVO setDatabaseIP(String databaseIP) {
		this.databaseIP = databaseIP;
		return this;
	}

	public String getDatabasePort() {
		return databasePort;
	}

	public DatabaseVO setDatabasePort(String databasePort) {
		this.databasePort = databasePort;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public DatabaseVO setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public DatabaseVO setPassword(String password) {
		this.password = password;
		return this;
	}

	public Long getServerId() {
		return serverId;
	}

	public DatabaseVO setServerId(Long serverId) {
		this.serverId = serverId;
		return this;
	}

	@Override
	public DatabaseVO set(DatabasePO entity) throws Exception {
		this.setId(entity.getId())
			.setDatabaseIP(entity.getDatabaseIP())
			.setDatabasePort(entity.getDatabasePort())
			.setUsername(entity.getUsername())
			.setPassword(entity.getPassword())
			.setServerId(entity.getServerId());
		return this;
	}
}
