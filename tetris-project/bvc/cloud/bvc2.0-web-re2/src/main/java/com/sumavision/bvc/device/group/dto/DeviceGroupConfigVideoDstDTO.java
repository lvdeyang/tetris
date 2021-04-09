package com.sumavision.bvc.device.group.dto;

import java.util.Date;

import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.ForwardDstType;

public class DeviceGroupConfigVideoDstDTO {

	private Long id;
	
	private String uuid;
	
	private Date updateTime;
	
	private ForwardDstType type;
	
	private Long roleId;

	private String roleName;
	
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
	
	private Long memberScreenId;
	
	private String memberScreenName;
	
	private Long videoId;
	
	public DeviceGroupConfigVideoDstDTO(){}

	public DeviceGroupConfigVideoDstDTO(
			Long id, 
			String uuid, 
			Date updateTime, 
			ForwardDstType type, 
			Long roleId,
			String roleName, 
			ChannelType roleChannelType,
			Long memberId, 
			Long memberChannelId, 
			String layerId,
			String channelId, 
			String channelName, 
			String bundleId,
			String bundleName,
			String bundleType,
			String screenId,
			Long memberScreenId,
			String memberScreenName,
			Long videoId) {

		this.id = id;
		this.uuid = uuid;
		this.updateTime = updateTime;
		this.type = type;
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleChannelType = roleChannelType;
		this.memberId = memberId;
		this.memberChannelId = memberChannelId;
		this.layerId = layerId;
		this.channelId = channelId;
		this.channelName = channelName;
		this.bundleId = bundleId;
		this.bundleName = bundleName;
		this.bundleType = bundleType;
		this.screenId = screenId;
		this.memberScreenId = memberScreenId;
		this.memberScreenName = memberScreenName;
		this.videoId = videoId;
	}

	public Long getId() {
		return id;
	}

	public DeviceGroupConfigVideoDstDTO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public DeviceGroupConfigVideoDstDTO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public DeviceGroupConfigVideoDstDTO setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public ForwardDstType getType() {
		return type;
	}

	public DeviceGroupConfigVideoDstDTO setType(ForwardDstType type) {
		this.type = type;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public DeviceGroupConfigVideoDstDTO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public DeviceGroupConfigVideoDstDTO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public ChannelType getRoleChannelType() {
		return roleChannelType;
	}

	public DeviceGroupConfigVideoDstDTO setRoleChannelType(ChannelType roleChannelType) {
		this.roleChannelType = roleChannelType;
		return this;
	}

	public String getScreenId() {
		return screenId;
	}

	public DeviceGroupConfigVideoDstDTO setScreenId(String screenId) {
		this.screenId = screenId;
		return this;
	}

	public Long getMemberScreenId() {
		return memberScreenId;
	}

	public DeviceGroupConfigVideoDstDTO setMemberScreenId(Long memberScreenId) {
		this.memberScreenId = memberScreenId;
		return this;
	}

	public String getMemberScreenName() {
		return memberScreenName;
	}

	public DeviceGroupConfigVideoDstDTO setMemberScreenName(String memberScreenName) {
		this.memberScreenName = memberScreenName;
		return this;
	}

	public Long getMemberId() {
		return memberId;
	}

	public DeviceGroupConfigVideoDstDTO setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	public Long getMemberChannelId() {
		return memberChannelId;
	}

	public DeviceGroupConfigVideoDstDTO setMemberChannelId(Long memberChannelId) {
		this.memberChannelId = memberChannelId;
		return this;
	}
	
	public String getLayerId() {
		return layerId;
	}

	public DeviceGroupConfigVideoDstDTO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public DeviceGroupConfigVideoDstDTO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public DeviceGroupConfigVideoDstDTO setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public DeviceGroupConfigVideoDstDTO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public DeviceGroupConfigVideoDstDTO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public Long getVideoId() {
		return videoId;
	}

	public DeviceGroupConfigVideoDstDTO setVideoId(Long videoId) {
		this.videoId = videoId;
		return this;
	}

	public String getBundleType() {
		return bundleType;
	}

	public DeviceGroupConfigVideoDstDTO setBundleType(String bundleType) {
		this.bundleType = bundleType;
		return this;
	}		
}
