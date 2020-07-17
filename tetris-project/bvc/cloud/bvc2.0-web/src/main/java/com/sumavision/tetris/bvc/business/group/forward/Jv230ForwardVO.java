package com.sumavision.tetris.bvc.business.group.forward;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class Jv230ForwardVO extends AbstractBaseVO<Jv230ForwardVO, Jv230ForwardPO>{

	private String layerId;
	
	private String bundleId;
	
	private String channelId;
	
	private int serialNum;
	
	private int x;
	
	private int y;
	
	private int w;
	
	private int h;
	
	private String businessName;
	
	private String sourceType;
	
	private String sourceTypeName;
	
	private String sourceLayerId;
	
	private String sourceBundleId;
	
	private String sourceChannelId;
	
	/** 用户id */
	private String userId;
	
	private Long terminalId;
	
	private String businessType;
	
	private String businessTypeName;
	
	public String getLayerId() {
		return layerId;
	}

	public Jv230ForwardVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public Jv230ForwardVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public Jv230ForwardVO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public int getSerialNum() {
		return serialNum;
	}

	public Jv230ForwardVO setSerialNum(int serialNum) {
		this.serialNum = serialNum;
		return this;
	}

	public int getX() {
		return x;
	}

	public Jv230ForwardVO setX(int x) {
		this.x = x;
		return this;
	}

	public int getY() {
		return y;
	}

	public Jv230ForwardVO setY(int y) {
		this.y = y;
		return this;
	}

	public int getW() {
		return w;
	}

	public Jv230ForwardVO setW(int w) {
		this.w = w;
		return this;
	}

	public int getH() {
		return h;
	}

	public Jv230ForwardVO setH(int h) {
		this.h = h;
		return this;
	}

	public String getBusinessName() {
		return businessName;
	}

	public Jv230ForwardVO setBusinessName(String businessName) {
		this.businessName = businessName;
		return this;
	}

	public String getSourceType() {
		return sourceType;
	}

	public Jv230ForwardVO setSourceType(String sourceType) {
		this.sourceType = sourceType;
		return this;
	}

	public String getSourceTypeName() {
		return sourceTypeName;
	}

	public Jv230ForwardVO setSourceTypeName(String sourceTypeName) {
		this.sourceTypeName = sourceTypeName;
		return this;
	}

	public String getSourceLayerId() {
		return sourceLayerId;
	}

	public Jv230ForwardVO setSourceLayerId(String sourceLayerId) {
		this.sourceLayerId = sourceLayerId;
		return this;
	}

	public String getSourceBundleId() {
		return sourceBundleId;
	}

	public Jv230ForwardVO setSourceBundleId(String sourceBundleId) {
		this.sourceBundleId = sourceBundleId;
		return this;
	}

	public String getSourceChannelId() {
		return sourceChannelId;
	}

	public Jv230ForwardVO setSourceChannelId(String sourceChannelId) {
		this.sourceChannelId = sourceChannelId;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public Jv230ForwardVO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public Jv230ForwardVO setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	public String getBusinessType() {
		return businessType;
	}

	public Jv230ForwardVO setBusinessType(String businessType) {
		this.businessType = businessType;
		return this;
	}

	public String getBusinessTypeName() {
		return businessTypeName;
	}

	public Jv230ForwardVO setBusinessTypeName(String businessTypeName) {
		this.businessTypeName = businessTypeName;
		return this;
	}

	@Override
	public Jv230ForwardVO set(Jv230ForwardPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setLayerId(entity.getLayerId())
			.setBundleId(entity.getBundleId())
			.setChannelId(entity.getChannelId())
			.setX(entity.getX())
			.setY(entity.getY())
			.setW(entity.getW())
			.setH(entity.getH())
			.setBusinessName(entity.getBusinessName())
			.setSourceType(entity.getSourceType().toString())
			.setSourceTypeName(entity.getSourceType().getName())
			.setSourceLayerId(entity.getSourceLayerId())
			.setSourceBundleId(entity.getSourceBundleId())
			.setSourceChannelId(entity.getSourceChannelId())
			.setUserId(entity.getUserId())
			.setTerminalId(entity.getTerminalId())
			.setBusinessType(entity.getBusinessType().toString())
			.setBusinessTypeName(entity.getBusinessType().getName());
		return this;
	}
	
}
