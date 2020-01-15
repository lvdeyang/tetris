package com.sumavision.bvc.control.device.group.vo;

import com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoDstDTO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.ForwardDstType;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoDstPO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupAgendaVideoDstVO extends AbstractBaseVO<DeviceGroupAgendaVideoDstVO, DeviceGroupConfigVideoDstDTO>{

	private ForwardDstType type;
	
	private Long roleId;

	private String roleName;
	
	private BusinessRoleSpecial special;
	
	private BusinessRoleType roleType;
	
	private ChannelType roleChannelType;
	
	private Long memberId;
	
	private Long memberChannelId;
	
	private String layerId;
	
	private String channelId;
	
	private String channelName;
	
	private String bundleId;
	
	private String bundleName;
	
	private String bundleType;
	
	private String screenId;
	
	private String roleScreenId;
	
	private Long memberScreenId;
	
	private String memberScreenName;

	public ForwardDstType getType() {
		return type;
	}

	public DeviceGroupAgendaVideoDstVO setType(ForwardDstType type) {
		this.type = type;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public DeviceGroupAgendaVideoDstVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public DeviceGroupAgendaVideoDstVO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public BusinessRoleSpecial getSpecial() {
		return special;
	}

	public DeviceGroupAgendaVideoDstVO setSpecial(BusinessRoleSpecial special) {
		this.special = special;
		return this;
	}

	public BusinessRoleType getRoleType() {
		return roleType;
	}

	public DeviceGroupAgendaVideoDstVO setRoleType(BusinessRoleType roleType) {
		this.roleType = roleType;
		return this;
	}

	public ChannelType getRoleChannelType() {
		return roleChannelType;
	}

	public DeviceGroupAgendaVideoDstVO setRoleChannelType(ChannelType roleChannelType) {
		this.roleChannelType = roleChannelType;
		return this;
	}

	public Long getMemberId() {
		return memberId;
	}

	public DeviceGroupAgendaVideoDstVO setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	public Long getMemberChannelId() {
		return memberChannelId;
	}

	public DeviceGroupAgendaVideoDstVO setMemberChannelId(Long memberChannelId) {
		this.memberChannelId = memberChannelId;
		return this;
	}
	
	public String getLayerId() {
		return layerId;
	}

	public DeviceGroupAgendaVideoDstVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public DeviceGroupAgendaVideoDstVO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public DeviceGroupAgendaVideoDstVO setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public DeviceGroupAgendaVideoDstVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public DeviceGroupAgendaVideoDstVO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getBundleType() {
		return bundleType;
	}

	public DeviceGroupAgendaVideoDstVO setBundleType(String bundleType) {
		this.bundleType = bundleType;
		return this;
	}

	public String getScreenId() {
		return screenId;
	}

	public DeviceGroupAgendaVideoDstVO setScreenId(String screenId) {
		this.screenId = screenId;
		return this;
	}

	public String getRoleScreenId() {
		return roleScreenId;
	}

	public DeviceGroupAgendaVideoDstVO setRoleScreenId(String roleScreenId) {
		this.roleScreenId = roleScreenId;
		return this;
	}

	public Long getMemberScreenId() {
		return memberScreenId;
	}

	public DeviceGroupAgendaVideoDstVO setMemberScreenId(Long memberScreenId) {
		this.memberScreenId = memberScreenId;
		return this;
	}

	public String getMemberScreenName() {
		return memberScreenName;
	}

	public DeviceGroupAgendaVideoDstVO setMemberScreenName(String memberScreenName) {
		this.memberScreenName = memberScreenName;
		return this;
	}

	@Override
	public DeviceGroupAgendaVideoDstVO set(DeviceGroupConfigVideoDstDTO entity) throws Exception {
		this.setId(entity.getMemberScreenId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime())
			.setType(entity.getType())
			.setRoleId(entity.getRoleId())
			.setRoleName(entity.getRoleName())
			.setRoleChannelType(entity.getRoleChannelType())
			.setMemberId(entity.getMemberId())
			.setMemberChannelId(entity.getMemberChannelId())
			.setLayerId(entity.getLayerId())
			.setChannelId(entity.getChannelId())
			.setChannelName(entity.getChannelName())
			.setBundleId(entity.getBundleId())
			.setBundleName(entity.getBundleName())
			.setBundleType(entity.getBundleType())
			.setScreenId(entity.getScreenId())
			.setRoleScreenId(entity.getScreenId())
			.setMemberScreenId(entity.getMemberScreenId())
			.setMemberScreenName(entity.getMemberScreenName());
		
		return this;
	}	
	
	public DeviceGroupAgendaVideoDstVO set(DeviceGroupConfigVideoDstPO entity) throws Exception {
		this.setId(entity.getMemberScreenId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime())
			.setType(entity.getType())
			.setRoleId(entity.getRoleId())
			.setRoleName(entity.getRoleName())
			.setRoleChannelType(entity.getRoleChannelType())
			.setMemberId(entity.getMemberId())
			.setMemberChannelId(entity.getMemberChannelId())
			.setLayerId(entity.getLayerId())
			.setChannelId(entity.getChannelId())
			.setChannelName(entity.getChannelName())
			.setBundleId(entity.getBundleId())
			.setBundleName(entity.getBundleName())
			.setBundleType(entity.getBundleType())
			.setScreenId(entity.getScreenId())
			.setRoleScreenId(entity.getScreenId())
			.setMemberScreenId(entity.getMemberScreenId())
			.setMemberScreenName(entity.getMemberScreenName());
		
		return this;
	}
}
