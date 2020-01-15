package com.sumavision.bvc.control.device.group.vo;

import com.sumavision.bvc.device.group.po.CombineAudioSrcPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class CombineAudioSrcVO extends AbstractBaseVO<CombineAudioSrcVO, CombineAudioSrcPO> {

	private Long id;
	
	private String uuid;
	
	private String combineAudioUuid;
	
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
	
	public Long getId() {
		return id;
	}

	public CombineAudioSrcVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public CombineAudioSrcVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getCombineAudioUuid() {
		return combineAudioUuid;
	}

	public CombineAudioSrcVO setCombineAudioUuid(String combineAudioUuid) {
		this.combineAudioUuid = combineAudioUuid;
		return this;
	}

	public Long getMemberId() {
		return memberId;
	}

	public CombineAudioSrcVO setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	public Long getMemberChannelId() {
		return memberChannelId;
	}

	public CombineAudioSrcVO setMemberChannelId(Long memberChannelId) {
		this.memberChannelId = memberChannelId;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public CombineAudioSrcVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public CombineAudioSrcVO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public CombineAudioSrcVO setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public CombineAudioSrcVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public CombineAudioSrcVO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	@Override
	public CombineAudioSrcVO set(CombineAudioSrcPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setCombineAudioUuid(entity.getCombineAudio().getUuid())
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
