package com.suma.venus.resource.base.bo;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class BundleBody {

	private DeviceInfoParam device_info;
	
	private String bundle_name;
	
	private String bundle_id;
	
	private String layer_id;
	
	private String bundle_type;
	
	private String device_model;
	
	private String bundle_status;
	
	private List<ChannelBody> channels;
	
	private String username;
	
	private String password;
	
	private JSONObject extra_info;
	
	private Long folderId;
	
	private String access_node_uid;
	
	/**屏信息*/
//	private List<ScreenBO> screens;
	
	public String getBundle_name() {
		return bundle_name;
	}

	public void setBundle_name(String bundle_name) {
		this.bundle_name = bundle_name;
	}

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public DeviceInfoParam getDevice_info() {
		return device_info;
	}

	public void setDevice_info(DeviceInfoParam device_info) {
		this.device_info = device_info;
	}

	public String getBundle_id() {
		return bundle_id;
	}

	public void setBundle_id(String bundle_id) {
		this.bundle_id = bundle_id;
	}
	
	
	public String getLayer_id() {
		return layer_id;
	}

	public void setLayer_id(String layer_id) {
		this.layer_id = layer_id;
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

	public String getBundle_status() {
		return bundle_status;
	}

	public void setBundle_status(String bundle_status) {
		this.bundle_status = bundle_status;
	}

	public List<ChannelBody> getChannels() {
		return channels;
	}

	public void setChannels(List<ChannelBody> channels) {
		this.channels = channels;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public JSONObject getExtra_info() {
		return extra_info;
	}

	public void setExtra_info(JSONObject extra_info) {
		this.extra_info = extra_info;
	}

	public String getAccess_node_uid() {
		return access_node_uid;
	}

	public void setAccess_node_uid(String access_node_uid) {
		this.access_node_uid = access_node_uid;
	}

}
