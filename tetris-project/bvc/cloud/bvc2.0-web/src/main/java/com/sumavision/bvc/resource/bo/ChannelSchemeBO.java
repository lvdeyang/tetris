package com.sumavision.bvc.resource.bo;

import com.suma.venus.resource.constant.VenusParamConstant.ParamScope;
import com.suma.venus.resource.pojo.ChannelSchemePO.LockStatus;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;

/**
 * 用来扩展通道源的参数<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月19日 下午1:27:40
 */
public class ChannelSchemeBO extends ChannelSchemeDTO {

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
	
	public ChannelSchemeBO(){}
	
	public ChannelSchemeBO(
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

	public ChannelSchemeBO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public ChannelSchemeBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public ChannelSchemeBO setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public ChannelSchemeBO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public LockStatus getChannelStatus() {
		return channelStatus;
	}

	public ChannelSchemeBO setChannelStatus(LockStatus channelStatus) {
		this.channelStatus = channelStatus;
		return this;
	}

	public Integer getOperateIndex() {
		return operateIndex;
	}

	public ChannelSchemeBO setOperateIndex(Integer operateIndex) {
		this.operateIndex = operateIndex;
		return this;
	}

	public Long getChannelTemplateId() {
		return channelTemplateId;
	}

	public ChannelSchemeBO setChannelTemplateId(Long channelTemplateId) {
		this.channelTemplateId = channelTemplateId;
		return this;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public ChannelSchemeBO setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
		return this;
	}

	public String getBundleType() {
		return bundleType;
	}

	public ChannelSchemeBO setBundleType(String bundleType) {
		this.bundleType = bundleType;
		return this;
	}

	public String getChannelNameFromTemplate() {
		return channelNameFromTemplate;
	}

	public ChannelSchemeBO setChannelNameFromTemplate(String channelNameFromTemplate) {
		this.channelNameFromTemplate = channelNameFromTemplate;
		return this;
	}

	public Integer getMaxChannelCnt() {
		return maxChannelCnt;
	}

	public ChannelSchemeBO setMaxChannelCnt(Integer maxChannelCnt) {
		this.maxChannelCnt = maxChannelCnt;
		return this;
	}

	public String getBaseType() {
		return baseType;
	}

	public ChannelSchemeBO setBaseType(String baseType) {
		this.baseType = baseType;
		return this;
	}

	public String getExternType() {
		return externType;
	}

	public ChannelSchemeBO setExternType(String externType) {
		this.externType = externType;
		return this;
	}

	public ParamScope getParamScope() {
		return paramScope;
	}

	public ChannelSchemeBO setParamScope(ParamScope paramScope) {
		this.paramScope = paramScope;
		return this;
	}
	
}
