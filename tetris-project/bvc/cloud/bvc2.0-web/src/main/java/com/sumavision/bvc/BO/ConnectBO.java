package com.sumavision.bvc.BO;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.BO.ForwardSetBO.SrcBO;

/**
 * 
 * @ClassName:  ConnectBO   
 * @Description:呼叫操作BO  
 * @author: 
 * @date:   2018年7月13日 下午4:16:23   
 *     
 * @Copyright: 2018 Sumavision. All rights reserved. 
 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class ConnectBO {
	
	/**
	 * 任务Id
	 */
	private String taskId;
	
	/**
	 * 锁类型 write/read 
	 */
	private String lock_type;
	
	/**
	 * 接入层Id
	 */
	private String layerId;
	/**
	 * 设备ID
	 */
	private String bundleId;
	
	/**
	 * 设备能力通道ID
	 */
	private String channelId;
	
	/**
	 * channelParam的基本能力参数类型
	 */
	private String base_type;
	
	/**
	 * 通道的源参数
	 */
	private SrcBO source_param;
	
	/**
	 * 编解码参数
	 */
	private JSONObject codec_param;
	
	/**
	 * 字幕
	 */
	private JSONObject osds;

	/**
	 * 屏幕信息
	 */
	private JSONArray screens;
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getLock_type() {
		return lock_type;
	}

	public void setLock_type(String lock_type) {
		this.lock_type = lock_type;
	}

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}			

	public String getBase_type() {
		return base_type;
	}

	public void setBase_type(String base_type) {
		this.base_type = base_type;
	}

	public SrcBO getSource_param() {
		return source_param;
	}

	public void setSource_param(SrcBO source_param) {
		this.source_param = source_param;
	}

	public JSONObject getCodec_param() {
		return codec_param;
	}

	public void setCodec_param(JSONObject codec_param) {
		this.codec_param = codec_param;
	}	
	
	public JSONArray getScreens() {
		return screens;
	}

	public void setScreens(JSONArray screens) {
		this.screens = screens;
	}

	public JSONObject getOsds() {
		return osds;
	}

	public void setOsds(JSONObject osds) {
		this.osds = osds;
	}

	@Override
	public String toString() {
		return "ConnectBO [taskId=" + taskId + ", lock_type=" + lock_type + ", layerId=" + layerId + ", bundleId="
				+ bundleId + ", channelId=" + channelId + ", source_param=" + source_param + ", codec_param="
				+ codec_param + "]";
	}

	//后面还要修改
	public ChannelSchemeBO transToChannelBo(){
		ChannelSchemeBO channelSchemeBO = new ChannelSchemeBO();
		channelSchemeBO.setBundleId(bundleId);
		channelSchemeBO.setChannelId((channelId));
		channelSchemeBO.setChannelParam(codec_param);
		return channelSchemeBO;
	}

}
