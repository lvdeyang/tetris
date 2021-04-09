package com.sumavision.bvc.control.device.group.vo;

import com.sumavision.bvc.device.group.po.ChannelForwardPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ChannelForwardVO extends AbstractBaseVO<ChannelForwardVO, ChannelForwardPO>{

	/*****************
	 * 以下描述源
	 *****************/
	
	/** 转发类型：合屏【|通道】 【|混音】【|通道】*/
	private String forwardSourceType;
	
	/** 转发类型为合屏【|混音】：存合屏【|混音】uuid */
	private String combineUuid;
	
	/** 转发类型为通道：源的设备组成员id */
	private Long sourceMemberId;
	
	/** 转发类型为通道：源的设备组成员通道id */
	private Long sourceMemberChannelId;
	
	/** 转发类型为通道，来自于资源层 */
	private String sourceLayerId;
	
	/** 转发类型为通道，来自于资源层：源的设备id */
	private String sourceBundleId;
	
	/** 转发类型为通道，来自于资源层：源的设备名称 */
	private String sourceBundleName;
	
	/** 转发类型为通道，来自于资源层：源的通道id */
	private String sourceChannelId;
	
	/** 转发类型为通道，来自于资源层：源的通道名称 */
	private String sourceChannelName;
	
	/** 转发类型为通道，源的通道别名 */
	private String sourceName;
	
	/*****************
	 * 以下描述目的
	 *****************/
	
	/** 设备组成员id */
	private Long memberId;
	
	/** 设备组成员通道id */
	private Long memberChannelId;
	
	/** 来自于资源层 */
	private String layerId;
	
	/** 真实的设备id（来自资源层） */
	private String bundleId;
	
	/** 真实的设备名称 （来自资源层）*/
	private String bundleName;
	
	/** 真实的通道id（来自于资源层） */
	private String channelId;
	
	/** 真实的通道名称（来自于资源层） */
	private String channelName;
	
	/** 真实的通道别名 */
	private String name;

	public String getForwardSourceType() {
		return forwardSourceType;
	}

	public ChannelForwardVO setForwardSourceType(String forwardSourceType) {
		this.forwardSourceType = forwardSourceType;
		return this;
	}

	public String getCombineUuid() {
		return combineUuid;
	}

	public ChannelForwardVO setCombineUuid(String combineUuid) {
		this.combineUuid = combineUuid;
		return this;
	}

	public Long getSourceMemberId() {
		return sourceMemberId;
	}

	public ChannelForwardVO setSourceMemberId(Long sourceMemberId) {
		this.sourceMemberId = sourceMemberId;
		return this;
	}

	public Long getSourceMemberChannelId() {
		return sourceMemberChannelId;
	}

	public ChannelForwardVO setSourceMemberChannelId(Long sourceMemberChannelId) {
		this.sourceMemberChannelId = sourceMemberChannelId;
		return this;
	}

	public String getSourceLayerId() {
		return sourceLayerId;
	}

	public ChannelForwardVO setSourceLayerId(String sourceLayerId) {
		this.sourceLayerId = sourceLayerId;
		return this;
	}

	public String getSourceBundleId() {
		return sourceBundleId;
	}

	public ChannelForwardVO setSourceBundleId(String sourceBundleId) {
		this.sourceBundleId = sourceBundleId;
		return this;
	}

	public String getSourceBundleName() {
		return sourceBundleName;
	}

	public ChannelForwardVO setSourceBundleName(String sourceBundleName) {
		this.sourceBundleName = sourceBundleName;
		return this;
	}

	public String getSourceChannelId() {
		return sourceChannelId;
	}

	public ChannelForwardVO setSourceChannelId(String sourceChannelId) {
		this.sourceChannelId = sourceChannelId;
		return this;
	}

	public String getSourceChannelName() {
		return sourceChannelName;
	}

	public ChannelForwardVO setSourceChannelName(String sourceChannelName) {
		this.sourceChannelName = sourceChannelName;
		return this;
	}

	public String getSourceName() {
		return sourceName;
	}

	public ChannelForwardVO setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}

	public Long getMemberId() {
		return memberId;
	}

	public ChannelForwardVO setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	public Long getMemberChannelId() {
		return memberChannelId;
	}

	public ChannelForwardVO setMemberChannelId(Long memberChannelId) {
		this.memberChannelId = memberChannelId;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public ChannelForwardVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public ChannelForwardVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public ChannelForwardVO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public ChannelForwardVO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public ChannelForwardVO setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public String getName() {
		return name;
	}

	public ChannelForwardVO setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public ChannelForwardVO set(ChannelForwardPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setForwardSourceType(entity.getForwardSourceType().getName())
			.setCombineUuid(entity.getCombineUuid())
			.setSourceMemberId(entity.getSourceMemberId())
			.setSourceMemberChannelId(entity.getSourceMemberChannelId())
			.setSourceLayerId(entity.getSourceLayerId())
			.setSourceBundleId(entity.getSourceBundleId())
			.setSourceBundleName(entity.getSourceBundleName())
			.setSourceChannelId(entity.getSourceChannelId())
			.setSourceChannelName(entity.getSourceChannelName())
			.setSourceName(entity.getSourceName())
			.setMemberId(entity.getMemberId())
			.setMemberChannelId(entity.getMemberChannelId())
			.setLayerId(entity.getLayerId())
			.setBundleId(entity.getBundleId())
			.setBundleName(entity.getBundleName())
			.setChannelId(entity.getChannelId())
			.setChannelName(entity.getChannelName())
			.setName(entity.getName());
			
		return this;
	}	
	
}
