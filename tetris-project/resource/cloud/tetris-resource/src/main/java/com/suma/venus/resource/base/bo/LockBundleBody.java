package com.suma.venus.resource.base.bo;

import java.util.List;

/**
 * 
 * lockBundle的PO子元素 批量lockBundle时使用
 * 
 * @author chenmo
 *
 */
public class LockBundleBody {

	private String bundleId;

	private List<ChannelBody> channels;

	private List<ScreenBody> screens;

	private String passByStr;

	// 是否计数开关
	private boolean operateCountSwitch = false;

	public LockBundleParam transToLockBundleParam() {

		LockBundleParam lockBundleParam = new LockBundleParam();
		lockBundleParam.setBundleId(this.bundleId);
		lockBundleParam.setChannels(this.channels);
		lockBundleParam.setScreens(this.screens);
		lockBundleParam.setPassByStr(this.passByStr);
		lockBundleParam.setOperateCountSwitch(this.operateCountSwitch);

		return lockBundleParam;
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
