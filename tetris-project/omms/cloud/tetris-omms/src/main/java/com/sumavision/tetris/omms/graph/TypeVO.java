package com.sumavision.tetris.omms.graph;

import java.util.ArrayList;
import java.util.List;

public class TypeVO {

	private String id;
	
	private String name;
	
	private ServerType serverType;
	
	private List<ServerVO> servers;

	public String getId() {
		return id;
	}

	public TypeVO setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public TypeVO setName(String name) {
		this.name = name;
		return this;
	}

	public ServerType getServerType() {
		return serverType;
	}

	public TypeVO setServerType(ServerType serverType) {
		this.serverType = serverType;
		return this;
	}

	public List<ServerVO> getServers() {
		return servers;
	}

	public TypeVO setServers(List<ServerVO> servers) {
		this.servers = servers;
		return this;
	}
	
	public TypeVO set(ServerType serverType){
		this.setId(serverType.getId())
			.setName(serverType.getName())
			.setServerType(serverType)
			.setServers(new ArrayList<ServerVO>());
		return this;
	}
	
}
