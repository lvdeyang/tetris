package com.sumavision.bvc.device.group.bo;

/**
 * 云台控制透传内容<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月20日 下午7:04:16
 */
public class PtzctrlPassByContent implements BasePassByContent{

	private String xml;

	public String getXml() {
		return xml;
	}

	public PtzctrlPassByContent setXml(String xml) {
		this.xml = xml;
		return this;
	}
	
}
