package com.suma.venus.resource.base.bo;

public class AccessNodeBO {

	private String nodeUid;
	
	private String ip;

	private String port;
	
	private String downloadPort;

	public String getNodeUid() {
		return nodeUid;
	}

	public void setNodeUid(String nodeUid) {
		this.nodeUid = nodeUid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getDownloadPort() {
		return downloadPort;
	}

	public void setDownloadPort(String downloadPort) {
		this.downloadPort = downloadPort;
	}
	
}
