package com.sumavision.signal.bvc.resource.dto;

import com.suma.venus.resource.constant.VenusParamConstant.ParamScope;
import com.suma.venus.resource.pojo.ChannelSchemePO.LockStatus;

public class ChannelSchemeDTO {

	/****************************
	 * 以下字段来自于ChannelSchemePO
	 ****************************/
	
	private Long id;
	
	private String bundleId;
	
	private String channelName;
	
	private String channelId;
	
	private LockStatus channelStatus;
	
	private Integer operateIndex;
	
	/****************************
	 * 以下字段来自于ChannelTemplatePO
	 ****************************/
	
	private Long channelTemplateId;
	
	private String deviceModel;
	
	private String bundleType;
	
	private String channelNameFromTemplate;
	
	private Integer maxChannelCnt;
	
	private String baseType;
	
	private String externType;
	
	private ParamScope paramScope;
	
	public ChannelSchemeDTO(
			Long id,
			String bundleId,
			String channelName,
			String channelId,
			LockStatus channelStatus,
			Integer operateIndex,
			Long channelTemplateId,
			String deviceModel,
			String bundleType,
			String channelNameFromTemplate,
			Integer maxChannelCnt,
			String baseType,
			String externType,
			ParamScope paramScope
			){
		this.id = id;
		this.bundleId = bundleId;
		this.channelName = channelName;
		this.channelId = channelId;
		this.channelStatus = channelStatus;
		this.operateIndex = operateIndex;
		this.channelTemplateId = channelTemplateId;
		this.deviceModel = deviceModel;
		this.bundleType = bundleType;
		this.channelNameFromTemplate = channelNameFromTemplate;
		this.maxChannelCnt = maxChannelCnt;
		this.baseType = baseType;
		this.externType = externType;
		this.paramScope = paramScope;
	}

	public Long getId() {
		return id;
	}

	public ChannelSchemeDTO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public ChannelSchemeDTO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public ChannelSchemeDTO setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public ChannelSchemeDTO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public LockStatus getChannelStatus() {
		return channelStatus;
	}

	public ChannelSchemeDTO setChannelStatus(LockStatus channelStatus) {
		this.channelStatus = channelStatus;
		return this;
	}

	public Integer getOperateIndex() {
		return operateIndex;
	}

	public ChannelSchemeDTO setOperateIndex(Integer operateIndex) {
		this.operateIndex = operateIndex;
		return this;
	}

	public Long getChannelTemplateId() {
		return channelTemplateId;
	}

	public ChannelSchemeDTO setChannelTemplateId(Long channelTemplateId) {
		this.channelTemplateId = channelTemplateId;
		return this;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public ChannelSchemeDTO setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
		return this;
	}

	public String getBundleType() {
		return bundleType;
	}

	public ChannelSchemeDTO setBundleType(String bundleType) {
		this.bundleType = bundleType;
		return this;
	}

	public String getChannelNameFromTemplate() {
		return channelNameFromTemplate;
	}

	public ChannelSchemeDTO setChannelNameFromTemplate(String channelNameFromTemplate) {
		this.channelNameFromTemplate = channelNameFromTemplate;
		return this;
	}

	public Integer getMaxChannelCnt() {
		return maxChannelCnt;
	}

	public ChannelSchemeDTO setMaxChannelCnt(Integer maxChannelCnt) {
		this.maxChannelCnt = maxChannelCnt;
		return this;
	}

	public String getBaseType() {
		return baseType;
	}

	public ChannelSchemeDTO setBaseType(String baseType) {
		this.baseType = baseType;
		return this;
	}

	public String getExternType() {
		return externType;
	}

	public ChannelSchemeDTO setExternType(String externType) {
		this.externType = externType;
		return this;
	}

	public ParamScope getParamScope() {
		return paramScope;
	}

	public ChannelSchemeDTO setParamScope(ParamScope paramScope) {
		this.paramScope = paramScope;
		return this;
	}
	
}
