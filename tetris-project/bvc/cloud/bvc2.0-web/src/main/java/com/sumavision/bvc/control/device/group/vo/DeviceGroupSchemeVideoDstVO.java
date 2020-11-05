package com.sumavision.bvc.control.device.group.vo;

import com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoDstDTO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.ForwardDstType;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoDstPO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupSchemeVideoDstVO extends AbstractBaseVO<DeviceGroupSchemeVideoDstVO, DeviceGroupConfigVideoDstDTO>{

	private ForwardDstType type;
	
	private Long roleId;

	private String roleName;
	
	private BusinessRoleSpecial special;
	
	private BusinessRoleType roleType;
	
	private Long memberId;
	
	private Long memberChannelId;
	
	private ChannelType roleChannelType;
	
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

	public DeviceGroupSchemeVideoDstVO setType(ForwardDstType type) {
		this.type = type;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public DeviceGroupSchemeVideoDstVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public DeviceGroupSchemeVideoDstVO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public BusinessRoleSpecial getSpecial() {
		return special;
	}

	public DeviceGroupSchemeVideoDstVO setSpecial(BusinessRoleSpecial special) {
		this.special = special;
		return this;
	}

	public BusinessRoleType getRoleType() {
		return roleType;
	}

	public DeviceGroupSchemeVideoDstVO setRoleType(BusinessRoleType roleType) {
		this.roleType = roleType;
		return this;
	}

	public Long getMemberId() {
		return memberId;
	}

	public DeviceGroupSchemeVideoDstVO setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	public Long getMemberChannelId() {
		return memberChannelId;
	}

	public DeviceGroupSchemeVideoDstVO setMemberChannelId(Long memberChannelId) {
		this.memberChannelId = memberChannelId;
		return this;
	}

	public ChannelType getRoleChannelType() {
		return roleChannelType;
	}

	public DeviceGroupSchemeVideoDstVO setRoleChannelType(ChannelType roleChannelType) {
		this.roleChannelType = roleChannelType;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public DeviceGroupSchemeVideoDstVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public DeviceGroupSchemeVideoDstVO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public DeviceGroupSchemeVideoDstVO setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public DeviceGroupSchemeVideoDstVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public DeviceGroupSchemeVideoDstVO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getBundleType() {
		return bundleType;
	}

	public DeviceGroupSchemeVideoDstVO setBundleType(String bundleType) {
		this.bundleType = bundleType;
		return this;
	}

	public String getScreenId() {
		return screenId;
	}

	public DeviceGroupSchemeVideoDstVO setScreenId(String screenId) {
		this.screenId = screenId;
		return this;
	}

	public String getRoleScreenId() {
		return roleScreenId;
	}

	public DeviceGroupSchemeVideoDstVO setRoleScreenId(String roleScreenId) {
		this.roleScreenId = roleScreenId;
		return this;
	}

	public Long getMemberScreenId() {
		return memberScreenId;
	}

	public DeviceGroupSchemeVideoDstVO setMemberScreenId(Long memberScreenId) {
		this.memberScreenId = memberScreenId;
		return this;
	}

	public String getMemberScreenName() {
		return memberScreenName;
	}

	public DeviceGroupSchemeVideoDstVO setMemberScreenName(String memberScreenName) {
		this.memberScreenName = memberScreenName;
		return this;
	}

	@Override
	public DeviceGroupSchemeVideoDstVO set(DeviceGroupConfigVideoDstDTO entity) throws Exception {
		this.setId(entity.getMemberScreenId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime())
			.setType(entity.getType())
			.setRoleId(entity.getRoleId())
			.setRoleName(entity.getRoleName())
			.setMemberId(entity.getMemberId())
			.setMemberChannelId(entity.getMemberChannelId())
			.setRoleChannelType(entity.getRoleChannelType())
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
	
	public DeviceGroupSchemeVideoDstVO set(DeviceGroupConfigVideoDstPO entity) throws Exception {
		this.setId(entity.getMemberScreenId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime())
			.setType(entity.getType())
			.setRoleId(entity.getRoleId())
			.setRoleName(entity.getRoleName())
			.setMemberId(entity.getMemberId())
			.setMemberChannelId(entity.getMemberChannelId())
			.setRoleChannelType(entity.getRoleChannelType())
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
