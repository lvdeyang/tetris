package com.suma.venus.resource.base.bo;


import java.util.List;

import com.alibaba.fastjson.JSONObject;


/**
 * bundle_info参数
 * @author lxw
 *
 */
public class BundleInfo{

	private String bundle_id;
	
	private String bundle_type;
	
	private Integer operate_index;
	
	/**能力通道配置信息*/
	private BundleConfig bundle_config;
	
	private String bundle_param;
	
	/**通道模板信息*/
	private List<JSONObject> channels;
	
	private List<JSONObject> screens;
	
	private String pass_by_str;
	
	/**bundle附加属性**/
	private String bundle_extra_info;
	
	/**用户附加属性**/
	private String user_extra_info;
	
	public String getBundle_id() {
		return bundle_id;
	}

	public void setBundle_id(String bundle_id) {
		this.bundle_id = bundle_id;
	}

	public String getBundle_type() {
		return bundle_type;
	}

	public void setBundle_type(String bundle_type) {
		this.bundle_type = bundle_type;
	}

	public BundleConfig getBundle_config() {
		return bundle_config;
	}

	public void setBundle_config(BundleConfig bundle_config) {
		this.bundle_config = bundle_config;
	}

	public String getBundle_param() {
		return bundle_param;
	}

	public void setBundle_param(String bundle_param) {
		this.bundle_param = bundle_param;
	}

	public List<JSONObject> getChannels() {
		return channels;
	}

	public void setChannels(List<JSONObject> channels) {
		this.channels = channels;
	}

	public Integer getOperate_index() {
		return operate_index;
	}

	public void setOperate_index(Integer operate_index) {
		this.operate_index = operate_index;
	}

	public List<JSONObject> getScreens() {
		return screens;
	}

	public void setScreens(List<JSONObject> screens) {
		this.screens = screens;
	}

	public String getPass_by_str() {
		return pass_by_str;
	}

	public void setPass_by_str(String pass_by_str) {
		this.pass_by_str = pass_by_str;
	}

	public String getBundle_extra_info() {
		return bundle_extra_info;
	}

	public void setBundle_extra_info(String bundle_extra_info) {
		this.bundle_extra_info = bundle_extra_info;
	}

	public String getUser_extra_info() {
		return user_extra_info;
	}

	public void setUser_extra_info(String user_extra_info) {
		this.user_extra_info = user_extra_info;
	}
	
}
