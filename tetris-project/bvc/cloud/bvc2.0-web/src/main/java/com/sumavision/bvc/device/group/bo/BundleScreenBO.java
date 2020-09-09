package com.sumavision.bvc.device.group.bo;

import java.util.ArrayList;
import java.util.List;

import com.suma.venus.resource.pojo.ScreenSchemePO;

public class BundleScreenBO {

	private String bundleId;
	
	private String screenId;
	
	private String bundleType;
	
	private List<ScreenRectBO> rects;

	public String getBundleId() {
		return bundleId;
	}

	public BundleScreenBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getScreenId() {
		return screenId;
	}

	public BundleScreenBO setScreenId(String screenId) {
		this.screenId = screenId;
		return this;
	}

	public String getBundleType() {
		return bundleType;
	}

	public BundleScreenBO setBundleType(String bundleType) {
		this.bundleType = bundleType;
		return this;
	}

	public List<ScreenRectBO> getRects() {
		return rects;
	}

	public BundleScreenBO setRects(List<ScreenRectBO> rects) {
		this.rects = rects;
		return this;
	}	
	
	/**
	 * @Title: 持久化对象复制
	 * @param screen
	 * @return BundleScreenBO 
	 */
	public BundleScreenBO set(ScreenSchemePO screen){
		this.setBundleId(screen.getBundleId())
			.setBundleType(screen.getDeviceModel())
			.setScreenId(screen.getScreenId())
			.setRects(new ArrayList<ScreenRectBO>());
		return this;
	}
}
