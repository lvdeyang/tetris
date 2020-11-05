package com.sumavision.bvc.BO;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @ClassName:  ConnectBundleBO   
 * @Description:TODO  
 * @author: 
 * @date:   2018年11月12日 上午10:06:04   
 *     
 * @Copyright: 2018 Sumavision. All rights reserved. 
 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class ConnectBundleBO {
	
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
	 * bundle类型
	 */
	private String bundle_type;
	
	/**
	 * device类型，jv210/player 等
	 */
	private String device_model;
	
	/**
	 * 业务类型 meeting/vod
	 */
	private String businessType = "meeting";
	
	/**
	 * 操作类型 start/stop/other等。逻辑层对start和stop进行计数
	 */
	private String operateType = "other";
	
	
	private List<Bundle_ChannelBO> channels;
	/**
	 * 屏幕信息
	 */
	private JSONArray screens;
	
	/**
	 * 透传字符串,最大长度1000000000；
	 */
	private JSONObject pass_by_str;
	
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

	
	public JSONArray getScreens() {
		return screens;
	}

	public void setScreens(JSONArray screens) {
		this.screens = screens;
	}

	public String getBundle_type() {
		return bundle_type;
	}

	public void setBundle_type(String bundle_type) {
		this.bundle_type = bundle_type;
	}

	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public JSONObject getPass_by_str() {
		return pass_by_str;
	}

	public void setPass_by_str(JSONObject pass_by_str) {
		this.pass_by_str = pass_by_str;
	}

	public List<Bundle_ChannelBO> getChannels() {
		return channels;
	}

	public void setChannels(List<Bundle_ChannelBO> channels) {
		this.channels = channels;
	}

	@Override
	public String toString() {
		return "ConnectBundleBO [taskId=" + taskId + ", lock_type=" + lock_type + ", layerId=" + layerId + ", bundleId="
				+ bundleId + ", bundle_type=" + bundle_type + ", channels=" + channels + ", screens=" + screens
				+ ", pass_by_str=" + pass_by_str + "]";
	}

}
