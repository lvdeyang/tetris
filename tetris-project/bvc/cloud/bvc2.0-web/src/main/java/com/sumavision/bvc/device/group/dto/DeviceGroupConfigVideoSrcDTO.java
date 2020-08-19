package com.sumavision.bvc.device.group.dto;

import java.util.Date;

import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.ForwardSrcType;
import com.sumavision.bvc.device.group.enumeration.PictureType;
import com.sumavision.bvc.device.group.enumeration.PollingSourceVisible;
import com.sumavision.bvc.device.group.enumeration.PollingStatus;

public class DeviceGroupConfigVideoSrcDTO {

	private Long id;
	
	private String uuid;
	
	private Date updateTime;
	
	private ForwardSrcType type;
	
	private Long roleId;
	
	private String roleName;
	
	private ChannelType roleChannelType;
	
	private int serialnum;
	
	private PictureType pictureType;
	
	private String pollingTime;
	
	private PollingStatus pollingStatus;
	
	private PollingSourceVisible visible;
	
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

	public DeviceGroupConfigVideoSrcDTO(){}
	
	public DeviceGroupConfigVideoSrcDTO(
			Long id, 
			String uuid, 
			Date updateTime, 
			ForwardSrcType type,
			Long roleId,
			String roleName,
			ChannelType roleChannelType,
			int serialnum, 
			PictureType pictureType,
			String pollingTime,
			PollingStatus pollingStatus,
			PollingSourceVisible visible,
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
		this.serialnum = serialnum;
		this.pictureType = pictureType;
		this.pollingTime = pollingTime;
		this.pollingStatus = pollingStatus;
		this.visible = visible;
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

	public DeviceGroupConfigVideoSrcDTO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public DeviceGroupConfigVideoSrcDTO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public DeviceGroupConfigVideoSrcDTO setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}
	
	public ForwardSrcType getType() {
		return type;
	}

	public DeviceGroupConfigVideoSrcDTO setType(ForwardSrcType type) {
		this.type = type;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public DeviceGroupConfigVideoSrcDTO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public DeviceGroupConfigVideoSrcDTO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public ChannelType getRoleChannelType() {
		return roleChannelType;
	}

	public DeviceGroupConfigVideoSrcDTO setRoleChannelType(ChannelType roleChannelType) {
		this.roleChannelType = roleChannelType;
		return this;
	}

	public int getSerialnum() {
		return serialnum;
	}

	public DeviceGroupConfigVideoSrcDTO setSerialnum(int serialnum) {
		this.serialnum = serialnum;
		return this;
	}

	public PictureType getPictureType() {
		return pictureType;
	}

	public DeviceGroupConfigVideoSrcDTO setPictureType(PictureType pictureType) {
		this.pictureType = pictureType;
		return this;
	}

	public String getPollingTime() {
		return pollingTime;
	}

	public DeviceGroupConfigVideoSrcDTO setPollingTime(String pollingTime) {
		this.pollingTime = pollingTime;
		return this;
	}

	public PollingStatus getPollingStatus() {
		return pollingStatus;
	}

	public DeviceGroupConfigVideoSrcDTO setPollingStatus(PollingStatus pollingStatus) {
		this.pollingStatus = pollingStatus;
		return this;
	}

	public PollingSourceVisible getVisible() {
		return visible;
	}

	public void setVisible(PollingSourceVisible visible) {
		this.visible = visible;
	}

	public Long getMemberId() {
		return memberId;
	}

	public DeviceGroupConfigVideoSrcDTO setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	public Long getMemberChannelId() {
		return memberChannelId;
	}

	public DeviceGroupConfigVideoSrcDTO setMemberChannelId(Long memberChannelId) {
		this.memberChannelId = memberChannelId;
		return this;
	}
	
	public String getMemberChannelName() {
		return memberChannelName;
	}

	public DeviceGroupConfigVideoSrcDTO setMemberChannelName(String memberChannelName) {
		this.memberChannelName = memberChannelName;
		return this;
	}
	
	public String getLayerId() {
		return layerId;
	}

	public DeviceGroupConfigVideoSrcDTO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public DeviceGroupConfigVideoSrcDTO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public DeviceGroupConfigVideoSrcDTO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public DeviceGroupConfigVideoSrcDTO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public DeviceGroupConfigVideoSrcDTO setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public String getVirtualUuid() {
		return virtualUuid;
	}

	public DeviceGroupConfigVideoSrcDTO setVirtualUuid(String virtualUuid) {
		this.virtualUuid = virtualUuid;
		return this;
	}

	public String getVirtualName() {
		return virtualName;
	}

	public DeviceGroupConfigVideoSrcDTO setVirtualName(String virtualName) {
		this.virtualName = virtualName;
		return this;
	}

	public Long getVideoId() {
		return videoId;
	}

	public DeviceGroupConfigVideoSrcDTO setVideoId(Long videoId) {
		this.videoId = videoId;
		return this;
	}
	
}
