package com.sumavision.tetris.easy.process.core;

import java.util.List;
import com.sumavision.tetris.easy.process.access.service.rest.RestServicePO;

public class GroupEntryVO{

	private String name;
	
	private List<EntryVO> entries;

	public String getName() {
		return name;
	}

	public GroupEntryVO setName(String name) {
		this.name = name;
		return this;
	}

	public List<EntryVO> getEntries() {
		return entries;
	}

	public GroupEntryVO setEntries(List<EntryVO> entries) {
		this.entries = entries;
		return this;
	}
	
	public GroupEntryVO set(RestServicePO entity){
		this.setName(entity.getName());
		return this;	
	}

}
