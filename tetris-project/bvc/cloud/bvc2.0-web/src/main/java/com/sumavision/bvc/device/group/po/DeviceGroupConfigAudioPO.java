package com.sumavision.bvc.device.group.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 设备组配置音频内容 
 * @author lvdeyang
 * @date 2018年8月4日 上午11:01:19 
 */
@Entity
@Table(name="BVC_DEVICE_GROUP_CONFIG_AUDIO_SRC")
public class DeviceGroupConfigAudioPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
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
	
	/** 关联设备组配置 */
	private DeviceGroupConfigPO config;

	@Column(name = "MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	@Column(name = "MEMBER_CHANNEL_ID")
	public Long getMemberChannelId() {
		return memberChannelId;
	}

	public void setMemberChannelId(Long memberChannelId) {
		this.memberChannelId = memberChannelId;
	}
	
	@Column(name = "LAYER_ID")
	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	@Column(name = "CHANNEL_ID")
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Column(name = "CHANNLE_NAME")
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	@Column(name = "BUNDLE_ID")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	@Column(name = "BUNDLE_NAME")
	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	@ManyToOne
	@JoinColumn(name = "CONFIG_ID")
	public DeviceGroupConfigPO getConfig() {
		return config;
	}

	public void setConfig(DeviceGroupConfigPO config) {
		this.config = config;
	}
	
}
