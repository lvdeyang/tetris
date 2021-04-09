package com.sumavision.bvc.device.group.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 混音源通道 
 * @author zy
 * @date 2018年7月31日 下午2:25:41 
 */
@Entity
@Table(name="BVC_DEVICE_GROUP_COMBINE_AUDIO_SRC")
public class CombineAudioSrcPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 设备组成员id */
	private Long memberId;
	
	/** 设备组成员通道id */
	private Long memberChannelId;
	
	/** 来自资源层的接入层id */
	private String layerId;
	
	/** 来自资源层的通道id */
	private String channelId;
	
	/** 来自资源层的通道名称 */
	private String channelName;
	
	/** 来自资源层的设备id */
	private String bundleId;
	
	/** 来自资源层的设备名称 */
	private String bundleName;
	
	/** 关联混音 */
	private  CombineAudioPO combineAudio;

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

	@Column(name = "CHANNEL_NAME")
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
	@JoinColumn(name = "COMBINE_AUDIO_ID")
	public CombineAudioPO getCombineAudio() {
		return combineAudio;
	}

	public void setCombineAudio(CombineAudioPO combineAudio) {
		this.combineAudio = combineAudio;
	}
	
	/**
	 * @Title: 数据转换方法<br/> 
	 * @Description: 根据设备组成员以及通道生成混音源<br/>
	 * @param entity
	 * @return CombineAudioSrcPO 
	 */
	public CombineAudioSrcPO set(DeviceGroupMemberChannelPO entity){
		this.setMemberId(entity.getMember().getId());
		this.setMemberChannelId(entity.getId());
		this.setLayerId(entity.getMember().getLayerId());
		this.setBundleId(entity.getBundleId());
		this.setBundleName(entity.getBundleName());
		this.setChannelId(entity.getChannelId());
		this.setChannelName(entity.getChannelName());
		return this;
	}
	
}
