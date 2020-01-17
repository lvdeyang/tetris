package com.suma.venus.resource.vo;

import com.alibaba.fastjson.JSONObject;

public class BundleFeignVO {

	private String bundle_id;

	private String bundle_name;

	private String bundle_type;

	private String device_model;

	private String device_version;

	private String device_ip;

	private Integer device_port;

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

	public String getDevice_version() {
		return device_version;
	}

	public void setDevice_version(String device_version) {
		this.device_version = device_version;
	}

	public String getDevice_ip() {
		return device_ip;
	}

	public void setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
	}

	public Integer getDevice_port() {
		return device_port;
	}

	public void setDevice_port(Integer device_port) {
		this.device_port = device_port;
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
