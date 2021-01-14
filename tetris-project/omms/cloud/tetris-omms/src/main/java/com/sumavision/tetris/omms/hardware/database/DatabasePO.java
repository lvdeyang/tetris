package com.sumavision.tetris.omms.hardware.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 
 * 数据库<br/>
 * <b>作者:</b>jiajun<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年11月2日 下午4:48:28
 */
@Entity
@Table(name = "TETRIS_OMMS_DATABASE")
public class DatabasePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 数据库IP */
	private String databaseIP;
	
	/** 数据库端口 */
	private String databasePort;
	
	/** 数据库名称 */
	private String databaseName;
	
	/** 用户名 */
	private String username;
	
	/** 密码 */
	private String password;
	
	/** 服务器id */
	private Long serverId;
	
	/** 路径 */
	private String path;
	
	@Column(name = "PATH")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(name = "DATABASE_IP")
	public String getDatabaseIP() {
		return databaseIP;
	}

	public void setDatabaseIP(String databaseIP) {
		this.databaseIP = databaseIP;
	}

	@Column(name = "DATABASE_PORT")
	public String getDatabasePort() {
		return databasePort;
	}

	public void setDatabasePort(String databasePort) {
		this.databasePort = databasePort;
	}

	@Column(name = "USERNAME")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "PASSWORD")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "SERVER_ID")
	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	@Column(name = "DATABASE_NAME")
	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

}
