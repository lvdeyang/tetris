package com.sumavision.bvc.device.monitor.vod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "BVC_MONITOR_EXTERNAL_STATIC_RESOURCE_FOLDER")
public class MonitorExternalStaticResourceFolderPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 别名 */
	private String name;
	
	/** 外部文件夹ip */
	private String ip;
	
	/** 外部文件夹端口 */
	private String port;
	
	/** 外部文件夹路径 */
	private String folderPath;
	
	/** 协议类型 */
	private ProtocolType protocolType;
	
	/** 文件夹访问用户名 */
	private String username;
	
	/** 文件夹访问密码 */
	private String password;
	
	/** 创建用户id */
	private String createUserId;
	
	/** 创建用户名 */
	private String createUsername;

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "IP")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "PORT")
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Column(name = "FOLDER_PATH")
	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "PROTOCOL_TYPE")
	public ProtocolType getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(ProtocolType protocolType) {
		this.protocolType = protocolType;
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

	@Column(name = "CREATE_USER_ID")
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "CREATE_USERNAME")
	public String getCreateUsername() {
		return createUsername;
	}

	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}

}
