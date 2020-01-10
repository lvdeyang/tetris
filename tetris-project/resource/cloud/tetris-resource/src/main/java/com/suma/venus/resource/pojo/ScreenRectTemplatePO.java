package com.suma.venus.resource.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 屏及区域信息模板PO
 * @author lxw
 *
 */
@Entity
public class ScreenRectTemplatePO extends CommonPO<ScreenRectTemplatePO>{

	private String deviceModel;

	private String screenId;
	
	private String rectId;
	
	/**关联的channel_id*/
	private String channel;
	
	/**以json字符串的格式存 x、y、width、height、cut等参数*/
	private String param;
	
	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	public String getRectId() {
		return rectId;
	}

	public void setRectId(String rectId) {
		this.rectId = rectId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Column(name="param",length=1024)
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
	
}
