package com.sumavision.bvc.control.device.group.vo;

import com.sumavision.bvc.device.group.po.DeviceGroupConfigAudioPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupAgendaAudioVO extends AbstractBaseVO<DeviceGroupAgendaAudioVO, DeviceGroupConfigAudioPO>{
	
	/** 设备组成员id */
	private Long memberId;
	
	/** 设备组成员通道id */
	private Long memberChannelId;
	
	/** 来自于资源层：设备接入层id */
	private String layerId;
	
	/** 来自于资源层：通道id */
	private String channelId;
	
	/** 来自于资源层：通道名称 */
	private String channelName;
	
	/** 来自于资源层：设备id */
	private String bundleId;
	
	/** 来自于资源层：设备名称 */
	private String bundleName;

	public Long getMemberId() {
		return memberId;
	}

	public DeviceGroupAgendaAudioVO setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	public Long getMemberChannelId() {
		return memberChannelId;
	}

	public DeviceGroupAgendaAudioVO setMemberChannelId(Long memberChannelId) {
		this.memberChannelId = memberChannelId;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public DeviceGroupAgendaAudioVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public DeviceGroupAgendaAudioVO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public DeviceGroupAgendaAudioVO setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public DeviceGroupAgendaAudioVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public DeviceGroupAgendaAudioVO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	@Override
	public DeviceGroupAgendaAudioVO set(DeviceGroupConfigAudioPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime())
			.setMemberId(entity.getMemberId())
			.setMemberChannelId(entity.getMemberChannelId())
			.setLayerId(entity.getLayerId())
			.setChannelId(entity.getChannelId())
			.setChannelName(entity.getChannelName())
			.setBundleId(entity.getBundleId())
			.setBundleName(entity.getBundleName());

		return this;
	}
	
}
