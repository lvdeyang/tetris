package com.sumavision.bvc.control.device.group.vo;

import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoSrcPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupVirtualVideoSrcVO extends AbstractBaseVO<DeviceGroupVirtualVideoSrcVO, DeviceGroupConfigVideoSrcPO>{

	private int serialnum;
	
	private Long memberId;
	
	private Long memberChannelId;
	
	private String memberChannelName;
	
	private String layerId;
	
	private String channelId;
	
	private String channelName;
	
	private String bundleId;
	
	private String bundleName;
	
	public int getSerialnum() {
		return serialnum;
	}
	
	public DeviceGroupVirtualVideoSrcVO setSerialnum(int serialnum) {
		this.serialnum = serialnum;
		return this;
	}
	
	public Long getMemberId() {
		return memberId;
	}
	
	public DeviceGroupVirtualVideoSrcVO setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}
	
	public Long getMemberChannelId() {
		return memberChannelId;
	}
	
	public DeviceGroupVirtualVideoSrcVO setMemberChannelId(Long memberChannelId) {
		this.memberChannelId = memberChannelId;
		return this;
	}
	
	public String getMemberChannelName() {
		return memberChannelName;
	}

	public DeviceGroupVirtualVideoSrcVO setMemberChannelName(String memberChannelName) {
		this.memberChannelName = memberChannelName;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}
	
	public DeviceGroupVirtualVideoSrcVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}
	
	public String getChannelId() {
		return channelId;
	}
	
	public DeviceGroupVirtualVideoSrcVO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}
	
	public String getChannelName() {
		return channelName;
	}
	
	public DeviceGroupVirtualVideoSrcVO setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}
	
	public String getBundleId() {
		return bundleId;
	}
	
	public DeviceGroupVirtualVideoSrcVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}
	
	public String getBundleName() {
		return bundleName;
	}
	
	public DeviceGroupVirtualVideoSrcVO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}
	
	@Override
	public DeviceGroupVirtualVideoSrcVO set(DeviceGroupConfigVideoSrcPO entity) throws Exception {
		this.setBundleId(entity.getBundleId())
			.setBundleName(entity.getBundleName())
			.setChannelId(entity.getChannelId())
			.setChannelName(entity.getChannelName())
			.setMemberChannelName(entity.getMemberChannelName())
			.setLayerId(entity.getLayerId())
			.setMemberChannelId(entity.getMemberChannelId())
			.setMemberId(entity.getMemberId())
			.setSerialnum(entity.getPosition().getSerialnum());
		
		return this;
	}

}
