package com.sumavision.bvc.device.group.bo;

import com.suma.venus.resource.pojo.ScreenRectTemplatePO;

public class ScreenRectBO {

	private String screenId;
	
	private String rectId;
	
	private String bundleType;
	
	private String channel;
	
	private String param;

	public String getScreenId() {
		return screenId;
	}

	public ScreenRectBO setScreenId(String screenId) {
		this.screenId = screenId;
		return this;
	}

	public String getRectId() {
		return rectId;
	}

	public ScreenRectBO setRectId(String rectId) {
		this.rectId = rectId;
		return this;
	}

	public String getBundleType() {
		return bundleType;
	}

	public ScreenRectBO setBundleType(String bundleType) {
		this.bundleType = bundleType;
		return this;
	}

	public String getChannel() {
		return channel;
	}

	public ScreenRectBO setChannel(String channel) {
		this.channel = channel;
		return this;
	}

	public String getParam() {
		return param;
	}

	public ScreenRectBO setParam(String param) {
		this.param = param;
		return this;
	}
	
	/**
	 * @Title: 资源持久化对象复制数据
	 * @param rect 资源持久化对象
	 * @return ScreenRectBO
	 */
	public ScreenRectBO set(ScreenRectTemplatePO rect){
		this.setScreenId(rect.getScreenId())
			.setBundleType(rect.getDeviceModel())
			.setRectId(rect.getRectId())
			.setChannel(rect.getChannel())
			.setParam(rect.getParam());
			
		return this;
	}
	
}
