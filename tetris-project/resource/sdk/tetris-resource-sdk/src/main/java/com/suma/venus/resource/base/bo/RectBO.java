package com.suma.venus.resource.base.bo;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 屏上区域BO
 * @author lxw
 *
 */
public class RectBO {

	private String rect_id;
	
	/**关联的channel_id*/
	private List<String> channel;
	
	/**x、y、width、height、cut等参数结构*/
	private JSONObject param;
	
	public String getRect_id() {
		return rect_id;
	}

	public void setRect_id(String rect_id) {
		this.rect_id = rect_id;
	}

	public List<String> getChannel() {
		return channel;
	}

	public void setChannel(List<String> channel) {
		this.channel = channel;
	}

	public JSONObject getParam() {
		return param;
	}

	public void setParam(JSONObject param) {
		this.param = param;
	}

}
