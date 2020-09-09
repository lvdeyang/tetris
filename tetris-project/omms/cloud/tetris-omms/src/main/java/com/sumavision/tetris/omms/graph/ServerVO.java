package com.sumavision.tetris.omms.graph;

import java.util.List;

public class ServerVO {

	private String id;
	
	private String ip;
	
	private String icon;
	
	private String status;
	
	private List<String> refs;

	public String getId() {
		return id;
	}

	public ServerVO setId(String id) {
		this.id = id;
		return this;
	}

	public String getIp() {
		return ip;
	}

	public ServerVO setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public ServerVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public ServerVO setStatus(String status) {
		this.status = status;
		return this;
	}

	public List<String> getRefs() {
		return refs;
	}

	public ServerVO setRefs(List<String> refs) {
		this.refs = refs;
		return this;
	}
	
}
