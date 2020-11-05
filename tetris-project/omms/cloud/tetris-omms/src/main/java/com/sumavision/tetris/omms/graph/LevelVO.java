package com.sumavision.tetris.omms.graph;

import java.util.List;

public class LevelVO {

	private String id;
	
	private String name;
	
	private List<GroupVO> groups;

	public String getId() {
		return id;
	}

	public LevelVO setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public LevelVO setName(String name) {
		this.name = name;
		return this;
	}

	public List<GroupVO> getGroups() {
		return groups;
	}

	public LevelVO setGroups(List<GroupVO> groups) {
		this.groups = groups;
		return this;
	}
	
}
