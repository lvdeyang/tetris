package com.sumavision.bvc.control.device.group.vo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupBusinessRoleVO extends AbstractBaseVO<DeviceGroupBusinessRoleVO, DeviceGroupBusinessRolePO>{

	private Long id;
	
	private String uuid;
	
	private String name;
	
	/** 特殊字段:枚举类型,比如唯一性 */
	private BusinessRoleSpecial special;
	
	/** 角色业务类型：默认，可录制*/
	private BusinessRoleType type;
	
	private List<DeviceGroupMemberVO> members;
	
	private List<ChannelNameVO> channels;
	
	private List<ScreenNameVO> screens;

	public Long getId() {
		return id;
	}

	public DeviceGroupBusinessRoleVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public DeviceGroupBusinessRoleVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getName() {
		return name;
	}

	public DeviceGroupBusinessRoleVO setName(String name) {
		this.name = name;
		return this;
	}

	public BusinessRoleSpecial getSpecial() {
		return special;
	}

	public DeviceGroupBusinessRoleVO setSpecial(BusinessRoleSpecial special) {
		this.special = special;
		return this;
	}

	public BusinessRoleType getType() {
		return type;
	}

	public DeviceGroupBusinessRoleVO setType(BusinessRoleType type) {
		this.type = type;
		return this;
	}

	public List<DeviceGroupMemberVO> getMembers() {
		return members;
	}

	public DeviceGroupBusinessRoleVO setMembers(List<DeviceGroupMemberVO> members) {
		this.members = members;
		return this;
	}

	public List<ChannelNameVO> getChannels() {
		return channels;
	}

	public DeviceGroupBusinessRoleVO setChannels(List<ChannelNameVO> channels) {
		this.channels = channels;
		return this;
	}

	public List<ScreenNameVO> getScreens() {
		return screens;
	}

	public DeviceGroupBusinessRoleVO setScreens(List<ScreenNameVO> screens) {
		this.screens = screens;
		return this;
	}

	@Override
	public DeviceGroupBusinessRoleVO set(DeviceGroupBusinessRolePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setName(entity.getName())
			.setSpecial(entity.getSpecial())
			.setType(entity.getType())
			.setMembers(new ArrayList<DeviceGroupMemberVO>())
			.setChannels(new ArrayList<ChannelNameVO>())
			.setScreens(new ArrayList<ScreenNameVO>());
		
		return this;
	}
	
	/**
	 * 发言人[|主席]排序<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月18日 下午4:51:17
	 */
	public static final class SpokesmanSorter implements Comparator<DeviceGroupBusinessRoleVO> {

		@Override
		public int compare(DeviceGroupBusinessRoleVO arg0, DeviceGroupBusinessRoleVO arg1) {
			if(BusinessRoleSpecial.CHAIRMAN.equals(arg0.getSpecial())){
				return -1;
			}else if(BusinessRoleSpecial.CHAIRMAN.equals(arg1.getSpecial())){
				return 1;
			}else{
				return arg0.getName().compareTo(arg1.getName());
			}
		}
		
	}
	
}
