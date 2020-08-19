package com.sumavision.tetris.omms.graph;

import java.util.ArrayList;
import java.util.List;

public class GroupVO {

	private String id;
	
	private String name;
	
	private GroupType groupType;
	
	private List<TypeVO> types;

	public String getId() {
		return id;
	}

	public GroupVO setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public GroupVO setName(String name) {
		this.name = name;
		return this;
	}

	public GroupType getGroupType() {
		return groupType;
	}

	public GroupVO setGroupType(GroupType groupType) {
		this.groupType = groupType;
		return this;
	}

	public List<TypeVO> getTypes() {
		return types;
	}

	public GroupVO setTypes(List<TypeVO> types) {
		this.types = types;
		return this;
	}
	
	public GroupVO set(GroupType groupType){
		this.setId(groupType.getId())
			.setName(groupType.getName())
			.setGroupType(groupType)
			.setTypes(new ArrayList<TypeVO>());
		return this;
	}
	
}
