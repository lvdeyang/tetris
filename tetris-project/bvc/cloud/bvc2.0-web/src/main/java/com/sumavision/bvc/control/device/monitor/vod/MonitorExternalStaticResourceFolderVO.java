package com.sumavision.bvc.control.device.monitor.vod;

import com.sumavision.bvc.device.monitor.vod.MonitorExternalStaticResourceFolderPO;
import com.sumavision.bvc.device.monitor.vod.ProtocolType;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MonitorExternalStaticResourceFolderVO extends AbstractBaseVO<MonitorExternalStaticResourceFolderVO, MonitorExternalStaticResourceFolderPO>{

	private String name;
	
	private String ip;
	
	private String port;
	
	private String folderPath;
	
	private String protocolType;
	
	private String username;
	
	private String password;
	
	private String fullPath;
	
	private String createUsername;
	
	public String getName() {
		return name;
	}

	public MonitorExternalStaticResourceFolderVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getIp() {
		return ip;
	}

	public MonitorExternalStaticResourceFolderVO setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public String getPort() {
		return port;
	}

	public MonitorExternalStaticResourceFolderVO setPort(String port) {
		this.port = port;
		return this;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public MonitorExternalStaticResourceFolderVO setFolderPath(String folderPath) {
		this.folderPath = folderPath;
		return this;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public MonitorExternalStaticResourceFolderVO setProtocolType(String protocolType) {
		this.protocolType = protocolType;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public MonitorExternalStaticResourceFolderVO setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public MonitorExternalStaticResourceFolderVO setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getFullPath() {
		return fullPath;
	}

	public MonitorExternalStaticResourceFolderVO setFullPath(String fullPath) {
		this.fullPath = fullPath;
		return this;
	}
	
	public MonitorExternalStaticResourceFolderVO setFullPath(MonitorExternalStaticResourceFolderPO entity){
		this.fullPath = new StringBufferWrapper().append(entity.getProtocolType().equals(ProtocolType.HTTP)?"http://":"")
												 .append(entity.getIp())
												 .append(":")
												 .append(entity.getPort())
												 .append(entity.getFolderPath())
												 .toString();
		return this;
	}
	
	public String getCreateUsername() {
		return createUsername;
	}

	public MonitorExternalStaticResourceFolderVO setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
		return this;
	}

	@Override
	public MonitorExternalStaticResourceFolderVO set(MonitorExternalStaticResourceFolderPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setIp(entity.getIp())
			.setPort(entity.getPort())
			.setFolderPath(entity.getFolderPath())
			.setProtocolType(entity.getProtocolType().getName())
			.setUsername(entity.getUsername())
			.setPassword(entity.getPassword())
			.setCreateUsername(entity.getCreateUsername());
		return this;
	}

}
