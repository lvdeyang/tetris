package com.suma.venus.resource.vo;

import org.springframework.beans.BeanUtils;

import com.suma.venus.resource.pojo.ChannelSchemePO;

public class ChannelSchemeVO {

	/*所属bundle的ID*/
	private String bundleId;
	
	/*配置的通道模板的ID*/
	private Long channelTemplateID;
	
	/*配置的通道标号*/
	private String channelId;
	
	private String channelStatus;
	
	private String channelName;
	
	public static ChannelSchemeVO fromPO(ChannelSchemePO po){
		ChannelSchemeVO vo= new ChannelSchemeVO();
		BeanUtils.copyProperties(po, vo, "channelStatus");
		vo.setChannelStatus(po.getChannelStatus().toString());
		return vo;
	}

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public Long getChannelTemplateID() {
		return channelTemplateID;
	}

	public void setChannelTemplateID(Long channelTemplateID) {
		this.channelTemplateID = channelTemplateID;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelStatus() {
		return channelStatus;
	}

	public void setChannelStatus(String channelStatus) {
		this.channelStatus = channelStatus;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
}
