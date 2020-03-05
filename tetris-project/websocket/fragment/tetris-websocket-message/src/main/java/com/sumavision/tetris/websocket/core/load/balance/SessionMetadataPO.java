package com.sumavision.tetris.websocket.core.load.balance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_WEBSOCKET_SESSION_METADATA")
public class SessionMetadataPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 用户id */
	private String userId;
	
	/** 用户名称 */
	private String username;
	
	/** 用户websocket链接服务器ip */
	private String serverIp;
	
	/** 用户websocket链接服务器端口 */
	private String serverPort;
	
	/** websocket session id */
	private String sessionId;
	
	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name = "USERNAME")
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name = "SERVER_IP")
	public String getServerIp() {
		return serverIp;
	}
	
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	
	@Column(name = "SERVER_PORT")
	public String getServerPort() {
		return serverPort;
	}
	
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	
	@Column(name = "SESSION_ID")
	public String getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
