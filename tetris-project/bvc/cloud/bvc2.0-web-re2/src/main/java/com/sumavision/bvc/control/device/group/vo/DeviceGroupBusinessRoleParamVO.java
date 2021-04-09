package com.sumavision.bvc.control.device.group.vo;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.bvc.common.group.po.CommonBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupBusinessRoleParamVO extends AbstractBaseVO<DeviceGroupBusinessRoleParamVO, DeviceGroupBusinessRolePO> {

	private Long id;
	
	private String uuid;
	
	private String name;
	
	private String special;
	
	private String type;
	
	private List<DeviceGroupMemberVO> members;
	
	private List<ChannelNameVO> channels;

	public Long getId() {
		return id;
	}

	public DeviceGroupBusinessRoleParamVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public DeviceGroupBusinessRoleParamVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getName() {
		return name;
	}

	public DeviceGroupBusinessRoleParamVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getSpecial() {
		return special;
	}

	public DeviceGroupBusinessRoleParamVO setSpecial(String special) {
		this.special = special;
		return this;
	}

	public String getType() {
		return type;
	}

	public DeviceGroupBusinessRoleParamVO setType(String type) {
		this.type = type;
		return this;
	}

	public List<DeviceGroupMemberVO> getMembers() {
		return members;
	}

	public DeviceGroupBusinessRoleParamVO setMembers(List<DeviceGroupMemberVO> members) {
		this.members = members;
		return this;
	}

	public List<ChannelNameVO> getChannels() {
		return channels;
	}

	public DeviceGroupBusinessRoleParamVO setChannels(List<ChannelNameVO> channels) {
		this.channels = channels;
		return this;
	}

	@Override
	public DeviceGroupBusinessRoleParamVO set(DeviceGroupBusinessRolePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setName(entity.getName())
			.setSpecial(entity.getSpecial() == null ? "" : entity.getSpecial().getName())
			.setType(entity.getType() == null ? "" : entity.getType().getName())
			.setMembers(new ArrayList<DeviceGroupMemberVO>())
			.setChannels(new ArrayList<ChannelNameVO>());
		
		return this;
	}

	public DeviceGroupBusinessRoleParamVO set(CommonBusinessRolePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setName(entity.getName())
			.setSpecial(entity.getSpecial() == null ? "" : entity.getSpecial().getName())
			.setType(entity.getType() == null ? "" : entity.getType().getName())
			.setMembers(new ArrayList<DeviceGroupMemberVO>())
			.setChannels(new ArrayList<ChannelNameVO>());
		
		return this;
	}
	
}
