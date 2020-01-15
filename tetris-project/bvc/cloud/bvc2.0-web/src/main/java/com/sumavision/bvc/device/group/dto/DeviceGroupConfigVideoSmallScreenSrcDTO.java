package com.sumavision.bvc.device.group.dto;

import java.util.Date;

import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.ForwardSrcType;

public class DeviceGroupConfigVideoSmallScreenSrcDTO {

	private Long id;
	
	private String uuid;
	
	private Date updateTime;
	
	private ForwardSrcType type;
	
	private Long roleId;
	
	private String roleName;
	
	private ChannelType roleChannelType;
	
	private Long memberId;
	
	private Long memberChannelId;
	
	private String memberChannelName;
	
	private String layerId;
	
	private String bundleId;
	
	private String bundleName;
	
	private String channelId;
	
	private String channelName;
	
	private String virtualUuid;
	
	private String virtualName;
	
	private Long videoId;

	public DeviceGroupConfigVideoSmallScreenSrcDTO(){}
	
	public DeviceGroupConfigVideoSmallScreenSrcDTO(
			Long id, 
			String uuid, 
			Date updateTime, 
			ForwardSrcType type,
			Long roleId,
			String roleName,
			ChannelType roleChannelType,
			Long memberId, 
			Long memberChannelId, 
			String memberChannelName,
			String layerId,
			String channelId,
			String channelName,
			String bundleId,
			String bundleName,
			String virtualUuid,
			String virtualName,
			Long videoId){
		this.id = id;
		this.uuid = uuid;
		this.updateTime = updateTime;
		this.type = type;
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleChannelType = roleChannelType;
		this.memberId = memberId;
		this.memberChannelId = memberChannelId;
		this.memberChannelName = memberChannelName;
		this.layerId = layerId;
		this.channelId = channelId;
		this.channelName = channelName;
		this.bundleId = bundleId;
		this.bundleName = bundleName;
		this.virtualUuid = virtualUuid;
		this.virtualName = virtualName;
		this.videoId = videoId;
	}
	
	public Long getId() {
		return id;
	}

	public DeviceGroupConfigVideoSmallScreenSrcDTO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public DeviceGroupConfigVideoSmallScreenSrcDTO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public DeviceGroupConfigVideoSmallScreenSrcDTO setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}
	
	public ForwardSrcType getType() {
		return type;
	}

	public DeviceGroupConfigVideoSmallScreenSrcDTO setType(ForwardSrcType type) {
		this.type = type;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public DeviceGroupConfigVideoSmallScreenSrcDTO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public DeviceGroupConfigVideoSmallScreenSrcDTO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public ChannelType getRoleChannelType() {
		return roleChannelType;
	}

	public DeviceGroupConfigVideoSmallScreenSrcDTO setRoleChannelType(ChannelType roleChannelType) {
		this.roleChannelType = roleChannelType;
		return this;
	}

	public Long getMemberId() {
		return memberId;
	}

	public DeviceGroupConfigVideoSmallScreenSrcDTO setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	public Long getMemberChannelId() {
		return memberChannelId;
	}

	public DeviceGroupConfigVideoSmallScreenSrcDTO setMemberChannelId(Long memberChannelId) {
		this.memberChannelId = memberChannelId;
		return this;
	}
	
	public String getMemberChannelName() {
		return memberChannelName;
	}

	public DeviceGroupConfigVideoSmallScreenSrcDTO setMemberChannelName(String memberChannelName) {
		this.memberChannelName = memberChannelName;
		return this;
	}
	
	public String getLayerId() {
		return layerId;
	}

	public DeviceGroupConfigVideoSmallScreenSrcDTO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public DeviceGroupConfigVideoSmallScreenSrcDTO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public DeviceGroupConfigVideoSmallScreenSrcDTO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public DeviceGroupConfigVideoSmallScreenSrcDTO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public DeviceGroupConfigVideoSmallScreenSrcDTO setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public String getVirtualUuid() {
		return virtualUuid;
	}

	public DeviceGroupConfigVideoSmallScreenSrcDTO setVirtualUuid(String virtualUuid) {
		this.virtualUuid = virtualUuid;
		return this;
	}

	public String getVirtualName() {
		return virtualName;
	}

	public DeviceGroupConfigVideoSmallScreenSrcDTO setVirtualName(String virtualName) {
		this.virtualName = virtualName;
		return this;
	}

	public Long getVideoId() {
		return videoId;
	}

	public DeviceGroupConfigVideoSmallScreenSrcDTO setVideoId(Long videoId) {
		this.videoId = videoId;
		return this;
	}
	
}
