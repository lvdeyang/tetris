package com.suma.venus.resource.pojo;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.suma.venus.resource.pojo.ChannelSchemePO.LockStatus;

/**
 * bundle上的屏配置信息及状态表
 * @author lxw
 *
 */
@Entity
public class ScreenSchemePO extends CommonPO<ScreenSchemePO>{
	
	/*所属bundle的ID*/
	private String bundleId;
	
	/***************通过devicemodel和screenId关联到ScreenRectTemplate*********************/
	private String deviceModel;
	
	/**屏ID*/
	private String screenId;
	/*******************************************************************************/
	
	private LockStatus status = LockStatus.IDLE;
	
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}
	
	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	@Enumerated(EnumType.STRING)
	public LockStatus getStatus() {
		return status;
	}

	public void setStatus(LockStatus status) {
		this.status = status;
	}
	
}
