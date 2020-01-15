package com.suma.venus.resource.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.suma.venus.resource.constant.VenusParamConstant.ParamScope;

/**
 * 通道模板表
 * @author lxw
 *
 */
@Entity
public class ChannelTemplatePO extends CommonPO<ChannelTemplatePO>{

	/**对应模板规范里的bundle_model，取值如jv210、cdn*/
	private String deviceModel;
	
	/**对应模板规范里的bundle_type，如VenusTerminal（终端）、VenusMixer（混合器）、
	 * VenusTransporter(转发器)、VenusOutConnection(外部输出)、VenusVideoMatrix(大屏)*/
	private String bundleType;
	
	/**对应模板里定义的channel_name，如JV210_TerminalAudioIn、JV210_TerminalAudioOut*/
	private String channelName;

	/**通道最大可设置的数量*/
	private Integer maxChannelCnt;
	
	/**对应模板里channel_param结构里的base_type，如VenusAudioIn、VenusAudioOut*/
	private String baseType;
	
	/**对应模板里channel_param结构里的extern_type，暂未使用*/
	private String externType;
	
	/**模板参数作用域类型，资源层内部使用*/
	private ParamScope paramScope;
	
	/**默认通道ID*/
	private String defaultChannelIds;

	@Column(name="device_model")
	public String getDeviceModel() {
		return deviceModel;
	}

	@Column(name="channel_name")
	public String getChannelName() {
		return channelName;
	}

	@Column(name="max_channel_cnt")
	public Integer getMaxChannelCnt() {
		return maxChannelCnt;
	}

	@Column(name="base_type")
	public String getBaseType() {
		return baseType;
	}
	
	@Column(name="extern_type")
	public String getExternType() {
		return externType;
	}
	
	@Column(name="bundle_type")
	public String getBundleType() {
		return bundleType;
	}

	public void setBundleType(String bundleType) {
		this.bundleType = bundleType;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public void setMaxChannelCnt(Integer maxChannelCnt) {
		this.maxChannelCnt = maxChannelCnt;
	}

	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}

	public void setExternType(String externType) {
		this.externType = externType;
	}

	public ParamScope getParamScope() {
		return paramScope;
	}

	public void setParamScope(ParamScope paramScope) {
		this.paramScope = paramScope;
	}
	
	public String getDefaultChannelIds() {
		return defaultChannelIds;
	}

	public void setDefaultChannelIds(String defaultChannelIds) {
		this.defaultChannelIds = defaultChannelIds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((baseType == null) ? 0 : baseType.hashCode());
		result = prime * result + ((bundleType == null) ? 0 : bundleType.hashCode());
		result = prime * result + ((channelName == null) ? 0 : channelName.hashCode());
		result = prime * result + ((defaultChannelIds == null) ? 0 : defaultChannelIds.hashCode());
		result = prime * result + ((deviceModel == null) ? 0 : deviceModel.hashCode());
		result = prime * result + ((externType == null) ? 0 : externType.hashCode());
		result = prime * result + ((maxChannelCnt == null) ? 0 : maxChannelCnt.hashCode());
		result = prime * result + ((paramScope == null) ? 0 : paramScope.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChannelTemplatePO other = (ChannelTemplatePO) obj;
		if (baseType == null) {
			if (other.baseType != null)
				return false;
		} else if (!baseType.equals(other.baseType))
			return false;
		if (bundleType == null) {
			if (other.bundleType != null)
				return false;
		} else if (!bundleType.equals(other.bundleType))
			return false;
		if (channelName == null) {
			if (other.channelName != null)
				return false;
		} else if (!channelName.equals(other.channelName))
			return false;
		if (defaultChannelIds == null) {
			if (other.defaultChannelIds != null)
				return false;
		} else if (!defaultChannelIds.equals(other.defaultChannelIds))
			return false;
		if (deviceModel == null) {
			if (other.deviceModel != null)
				return false;
		} else if (!deviceModel.equals(other.deviceModel))
			return false;
		if (externType == null) {
			if (other.externType != null)
				return false;
		} else if (!externType.equals(other.externType))
			return false;
		if (maxChannelCnt == null) {
			if (other.maxChannelCnt != null)
				return false;
		} else if (!maxChannelCnt.equals(other.maxChannelCnt))
			return false;
		if (paramScope != other.paramScope)
			return false;
		return true;
	}

}
