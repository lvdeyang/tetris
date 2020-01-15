package com.sumavision.bvc.device.group.bo;

import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;

public class ChannelBO {

	private String id = "";
	
	private String channelMemberId = "";
	
	//所属buddleId
	private String bundleId = "";
	
	//所属bundleName
	private String bundleName = "";
	
	//所属memberId(业务层获得)
	private Long memberId = 0l;
	
	//通道编号
	private String channelId = "";
	
	//通道状态
	private String channelStatus = "";
	
	//通道别名
	private String name = "";
	
	//通道名称
	private String channelName = "";
	
	//通道类型
	private String channelType = "";

	public String getId() {
		return id;
	}

	public ChannelBO setId(String id) {
		this.id = id;
		return this;
	}
	
	public String getBundleName() {
		return bundleName;
	}

	public ChannelBO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getChannelMemberId() {
		return channelMemberId;
	}

	public ChannelBO setChannelMemberId(String channelMemberId) {
		this.channelMemberId = channelMemberId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public ChannelBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public ChannelBO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getChannelStatus() {
		return channelStatus;
	}

	public ChannelBO setChannelStatus(String channelStatus) {
		this.channelStatus = channelStatus;
		return this;
	}

	public String getName() {
		return name;
	}

	public ChannelBO setName(String name) {
		this.name = name;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public ChannelBO setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public String getChannelType() {
		return channelType;
	}

	public ChannelBO setChannelType(String channelType) {
		this.channelType = channelType;
		return this;
	}	
	
	public Long getMemberId() {
		return memberId;
	}

	public ChannelBO setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	public ChannelBO set(DeviceGroupMemberChannelPO channel){
		this.setId(getChannelId())
			.setBundleId(channel.getBundleId())
			.setBundleName(channel.getBundleName())
			.setChannelId(channel.getChannelId())
			.setChannelName(channel.getChannelName())
			.setChannelType(channel.getChannelType())
			.setChannelMemberId(channel.getId().toString())
			.setName(channel.getName());					
		
		return this;		
	}

}
