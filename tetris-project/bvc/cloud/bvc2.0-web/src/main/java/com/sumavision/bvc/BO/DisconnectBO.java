package com.sumavision.bvc.BO;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @ClassName:  DisconnectBO   
 * @Description:呼叫挂断操作BO  
 * @author: 
 * @date:   2018年7月13日 下午4:25:33   
 *     
 * @Copyright: 2018 Sumavision. All rights reserved. 
 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class DisconnectBO {

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
	 * 编解码参数
	 */
	private JSONObject codec_param;
	
	/**
	 * 字幕
	 */
	private JSONObject osds;

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

	public JSONObject getCodec_param() {
		return codec_param;
	}

	public void setCodec_param(JSONObject codec_param) {
		this.codec_param = codec_param;
	}

	public JSONObject getOsds() {
		return osds;
	}

	public void setOsds(JSONObject osds) {
		this.osds = osds;
	}

	@Override
	public String toString() {
		return "DisconnectBO [taskId=" + taskId + ", lock_type=" + lock_type + ", layerId=" + layerId + ", bundleId="
				+ bundleId + ", channelId=" + channelId + "]";
	}

}
