package com.suma.venus.resource.vo;
/**
 * 通道模板VO
 * @author lxw
 *
 */
public class ChannelTemplateVO {
	
	private Long id;
	
	private String channelName;
	
	private String deviceModel;
	
	private String bundleType;
	
	private String channelVersion;
	
	public ChannelTemplateVO(){}
	
	public ChannelTemplateVO(String deviceModel) {
		super();
		this.deviceModel = deviceModel;
	}

	public ChannelTemplateVO(String deviceModel, String bundleType) {
		super();
		this.deviceModel = deviceModel;
		this.bundleType = bundleType;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getChannelVersion() {
		return channelVersion;
	}

	public void setChannelVersion(String channelVersion) {
		this.channelVersion = channelVersion;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getBundleType() {
		return bundleType;
	}

	public void setBundleType(String bundleType) {
		this.bundleType = bundleType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bundleType == null) ? 0 : bundleType.hashCode());
		result = prime * result + ((deviceModel == null) ? 0 : deviceModel.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChannelTemplateVO other = (ChannelTemplateVO) obj;
		if (bundleType == null) {
			if (other.bundleType != null)
				return false;
		} else if (!bundleType.equals(other.bundleType))
			return false;
		if (deviceModel == null) {
			if (other.deviceModel != null)
				return false;
		} else if (!deviceModel.equals(other.deviceModel))
			return false;
		return true;
	}

}
