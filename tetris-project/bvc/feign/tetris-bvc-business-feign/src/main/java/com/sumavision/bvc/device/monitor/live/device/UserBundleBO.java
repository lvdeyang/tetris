package com.sumavision.bvc.device.monitor.live.device;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>那个用户下对应的那个设备</p>
 * <b>作者:</b>lixin<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年11月12日 下午3:29:47
 */
public class UserBundleBO {

	/** 用户的id*/
	private Long userId;
	
	/** 设备的bundleid*/
	private List<String> bundleIds = new ArrayList<String>();

	public Long getUserId() {
		return userId;
	}

	public UserBundleBO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public List<String> getBundleIds() {
		return bundleIds;
	}

	public UserBundleBO setBundleIds(List<String> bundleIds) {
		this.bundleIds = bundleIds;
		return this;
	}
	
	
}
