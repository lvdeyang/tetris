package com.suma.venus.resource.base.bo;

import java.util.List;

/**
 * 
 * @author lxw
 *
 */
public class LockBundleParam {

	private Long userId;
	
	private String bundleId;
	
	private List<ChannelBody> channels;
	
	private List<ScreenBody> screens;
	
	private String passByStr;
	
	//是否计数开关
	private boolean operateCountSwitch = false;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public List<ChannelBody> getChannels() {
		return channels;
	}

	public void setChannels(List<ChannelBody> channels) {
		this.channels = channels;
	}

	public List<ScreenBody> getScreens() {
		return screens;
	}

	public void setScreens(List<ScreenBody> screens) {
		this.screens = screens;
	}

	public String getPassByStr() {
		return passByStr;
	}

	public void setPassByStr(String passByStr) {
		this.passByStr = passByStr;
	}

	public boolean isOperateCountSwitch() {
		return operateCountSwitch;
	}

	public void setOperateCountSwitch(boolean operateCountSwitch) {
		this.operateCountSwitch = operateCountSwitch;
	}
	
}
