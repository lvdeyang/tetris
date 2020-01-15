package com.sumavision.bvc.control.device.group.vo;

import com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoSmallScreenSrcDTO;
import com.sumavision.bvc.device.group.enumeration.ForwardSrcType;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoSmallSrcPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupAgendaVideoSmallScreenSrcVO extends AbstractBaseVO<DeviceGroupAgendaVideoSmallScreenSrcVO, DeviceGroupConfigVideoSmallScreenSrcDTO>{
	
	private String type;
	
	private Long roleId;
	
	private String roleName;
	
	private String roleChannelType;
	
	private Long memberId;
	
	private Long memberChannelId;
	
	private String memberChannelName;
	
	private String layerId;
	
	private String channelId;
	
	private String channelName;
	
	private String bundleId;
	
	private String bundleName;
	
	private String virtualUuid;
	
	private String virtualName;

	public String getType() {
		return type;
	}

	public DeviceGroupAgendaVideoSmallScreenSrcVO setType(String type) {
		this.type = type;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public DeviceGroupAgendaVideoSmallScreenSrcVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public DeviceGroupAgendaVideoSmallScreenSrcVO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public String getRoleChannelType() {
		return roleChannelType;
	}

	public DeviceGroupAgendaVideoSmallScreenSrcVO setRoleChannelType(String roleChannelType) {
		this.roleChannelType = roleChannelType;
		return this;
	}

	public Long getMemberId() {
		return memberId;
	}

	public DeviceGroupAgendaVideoSmallScreenSrcVO setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	public Long getMemberChannelId() {
		return memberChannelId;
	}

	public DeviceGroupAgendaVideoSmallScreenSrcVO setMemberChannelId(Long memberChannelId) {
		this.memberChannelId = memberChannelId;
		return this;
	}
	
	public String getMemberChannelName() {
		return memberChannelName;
	}

	public DeviceGroupAgendaVideoSmallScreenSrcVO setMemberChannelName(String memberChannelName) {
		this.memberChannelName = memberChannelName;
		return this;
	}
	
	public String getLayerId() {
		return layerId;
	}

	public DeviceGroupAgendaVideoSmallScreenSrcVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public DeviceGroupAgendaVideoSmallScreenSrcVO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public DeviceGroupAgendaVideoSmallScreenSrcVO setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public DeviceGroupAgendaVideoSmallScreenSrcVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public DeviceGroupAgendaVideoSmallScreenSrcVO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getVirtualUuid() {
		return virtualUuid;
	}

	public DeviceGroupAgendaVideoSmallScreenSrcVO setVirtualUuid(String virtualUuid) {
		this.virtualUuid = virtualUuid;
		return this;
	}

	public String getVirtualName() {
		return virtualName;
	}

	public DeviceGroupAgendaVideoSmallScreenSrcVO setVirtualName(String virtualName) {
		this.virtualName = virtualName;
		return this;
	}

	@Override
	public DeviceGroupAgendaVideoSmallScreenSrcVO set(DeviceGroupConfigVideoSmallScreenSrcDTO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setType(entity.getType()==null?ForwardSrcType.CHANNEL.toString():entity.getType().toString())
			.setRoleId(entity.getRoleId())
			.setRoleName(entity.getRoleName())
			.setRoleChannelType(entity.getRoleChannelType()==null?"":entity.getRoleChannelType().toString())
			.setMemberId(entity.getMemberId())
			.setMemberChannelId(entity.getMemberChannelId())
			.setLayerId(entity.getLayerId())
			.setMemberChannelName(entity.getMemberChannelName())
			.setChannelId(entity.getChannelId())
			.setChannelName(entity.getChannelName())
			.setBundleId(entity.getBundleId())
			.setBundleName(entity.getBundleName())
			.setVirtualUuid(entity.getVirtualUuid())
			.setVirtualName(entity.getVirtualName());
		return this;
	}

	public DeviceGroupAgendaVideoSmallScreenSrcVO set(DeviceGroupConfigVideoSmallSrcPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setType(entity.getType()==null?ForwardSrcType.CHANNEL.toString():entity.getType().toString())
			.setRoleId(entity.getRoleId())
			.setRoleName(entity.getRoleName())
			.setRoleChannelType(entity.getRoleChannelType()==null?"":entity.getRoleChannelType().toString())
			.setMemberId(entity.getMemberId())
			.setMemberChannelId(entity.getMemberChannelId())
			.setLayerId(entity.getLayerId())
			.setMemberChannelName(entity.getMemberChannelName())
			.setChannelId(entity.getChannelId())
			.setChannelName(entity.getChannelName())
			.setBundleId(entity.getBundleId())
			.setBundleName(entity.getBundleName())
			.setVirtualUuid(entity.getVirtualUuid())
			.setVirtualName(entity.getVirtualName());
		return this;
	}
	
}
