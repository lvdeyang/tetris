package com.sumavision.bvc.control.device.group.vo;

import com.sumavision.bvc.device.group.po.CombineVideoSrcPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class CombineVideoSrcVo extends AbstractBaseVO<CombineVideoSrcVo, CombineVideoSrcPO>{

	private Long id;
	
	private String uuid;
	
	/** 通道别名 */
	private String name;
	
	private String combineVideoUuid;
	
	private int serialNum;
	
	private String websiteDraw;
	
	/** 设备组成员id */
	private Long memberId;
	
	/** 设备成员组通道id */
	private Long memberChannelId;
	
	/** 来自于资源管理的接入层id */
	private String layerId;
	
	/** 来自于资源管理的通道id */
	private String channelId;
	
	/** 来自于资源管理的通道名称 */
	private String channelName;
	
	/** 来自于资源管理的设备id */
	private String bundleId;
	
	/** 来自于资源管理的设备名称 */
	private String bundleName;

	public Long getId() {
		return id;
	}

	public CombineVideoSrcVo setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public CombineVideoSrcVo setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getName() {
		return name;
	}

	public CombineVideoSrcVo setName(String name) {
		this.name = name;
		return this;
	}

	public String getCombineVideoUuid() {
		return combineVideoUuid;
	}

	public CombineVideoSrcVo setCombineVideoUuid(String combineVideoUuid) {
		this.combineVideoUuid = combineVideoUuid;
		return this;
	}

	public int getSerialNum() {
		return serialNum;
	}

	public CombineVideoSrcVo setSerialNum(int serialNum) {
		this.serialNum = serialNum;
		return this;
	}

	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public CombineVideoSrcVo setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
		return this;
	}

	public Long getMemberId() {
		return memberId;
	}

	public CombineVideoSrcVo setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	public Long getMemberChannelId() {
		return memberChannelId;
	}

	public CombineVideoSrcVo setMemberChannelId(Long memberChannelId) {
		this.memberChannelId = memberChannelId;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public CombineVideoSrcVo setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public CombineVideoSrcVo setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public CombineVideoSrcVo setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public CombineVideoSrcVo setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public CombineVideoSrcVo setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	@Override
	public CombineVideoSrcVo set(CombineVideoSrcPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setName(entity.getName())
			.setCombineVideoUuid(entity.getPosition().getCombineVideo().getUuid())
			.setSerialNum(entity.getPosition().getSerialnum())
			.setWebsiteDraw(entity.getPosition().getCombineVideo().getWebsiteDraw())
			.setBundleId(entity.getBundleId())
			.setBundleName(entity.getBundleName())
			.setChannelId(entity.getChannelId())
			.setChannelName(entity.getChannelName())
			.setMemberId(entity.getMemberId())
			.setMemberChannelId(entity.getMemberChannelId())
			.setLayerId(entity.getLayerId());

		return this;
	}
	
	
	
}
