package com.sumavision.tetris.bvc.business.terminal.hall;

import java.util.HashMap;
import java.util.Map;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ConferenceHallVO extends AbstractBaseVO<ConferenceHallVO, ConferenceHallPO>{

	private String name;
	
	private Long folderId;
	
	private String folderName;
	
	private Long terminalId;
	
	private String terminalName;
	
	private Map<String, String> permissions;
	
	public String getName() {
		return name;
	}

	public ConferenceHallVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getFolderId() {
		return folderId;
	}

	public ConferenceHallVO setFolderId(Long folderId) {
		this.folderId = folderId;
		return this;
	}

	public String getFolderName() {
		return folderName;
	}

	public ConferenceHallVO setFolderName(String folderName) {
		this.folderName = folderName;
		return this;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public ConferenceHallVO setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public ConferenceHallVO setTerminalName(String terminalName) {
		this.terminalName = terminalName;
		return this;
	}

	public Map<String, String> getPermissions() {
		return permissions;
	}

	public ConferenceHallVO setPermissions(Map<String, String> permissions) {
		this.permissions = permissions;
		return this;
	}
	
	public ConferenceHallVO addPermission(PrivilegeType privilegeType) {
		if(this.permissions == null) this.permissions = new HashMap<String, String>();
		this.permissions.put(privilegeType.toString(), privilegeType.getName());
		return this;
	}

	@Override
	public ConferenceHallVO set(ConferenceHallPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setFolderId(entity.getFolderId())
			.setTerminalId(entity.getTerminalId());
		return this;
	}

}
