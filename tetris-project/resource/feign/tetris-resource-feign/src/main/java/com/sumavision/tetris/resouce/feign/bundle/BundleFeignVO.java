package com.sumavision.tetris.resouce.feign.bundle;

import com.alibaba.fastjson.JSONObject;

public class BundleFeignVO {

	private String bundle_id;

	private String bundle_name;

	private String bundle_Type;

	private String deviceModel;

	private String deviceVersion;

	private String deviceIp;

	private Integer devicePort;

	private String username;

	private String password;

	private String access_node_uid;

	private String bundle_status;

	private JSONObject extra_info;

	public String getBundle_id() {
		return bundle_id;
	}

	public void setBundle_id(String bundle_id) {
		this.bundle_id = bundle_id;
	}

	public String getBundle_name() {
		return bundle_name;
	}

	public void setBundle_name(String bundle_name) {
		this.bundle_name = bundle_name;
	}

	public String getBundle_Type() {
		return bundle_Type;
	}

	public void setBundle_Type(String bundle_Type) {
		this.bundle_Type = bundle_Type;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
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

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public Integer getDevicePort() {
		return devicePort;
	}

	public void setDevicePort(Integer devicePort) {
		this.devicePort = devicePort;
	}

	public String getAccess_node_uid() {
		return access_node_uid;
	}

	public void setAccess_node_uid(String access_node_uid) {
		this.access_node_uid = access_node_uid;
	}

	public String getBundle_status() {
		return bundle_status;
	}

	public void setBundle_status(String bundle_status) {
		this.bundle_status = bundle_status;
	}

	public JSONObject getExtra_info() {
		return extra_info;
	}

	public void setExtra_info(JSONObject extra_info) {
		this.extra_info = extra_info;
	}

}
