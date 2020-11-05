package com.sumavision.signal.bvc.mq.bo;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class BundleBO {
	
	private String bundle_id;
	
	private String bundle_type;
	
	private String device_model;
	
	private List<ChannelBO> channels;
	
	private List<ScreenBO> screens;
	
	private String pass_by_str;

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

	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public List<ChannelBO> getChannels() {
		return channels;
	}

	public void setChannels(List<ChannelBO> channels) {
		this.channels = channels;
	}

	public List<ScreenBO> getScreens() {
		return screens;
	}

	public void setScreens(List<ScreenBO> screens) {
		this.screens = screens;
	}

	public String getPass_by_str() {
		return pass_by_str;
	}

	public void setPass_by_str(String pass_by_str) {
		this.pass_by_str = pass_by_str;
	}
}
