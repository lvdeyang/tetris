package com.sumavision.signal.bvc.entity.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.signal.bvc.entity.enumeration.OnlineStatus;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 心跳设备信息<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年8月2日 上午9:02:32
 */
@Entity
@Table(name = "BVC_HEART_BEAT_BUNDLE")
public class HeartBeatBundlePO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;

	private String bundleName;
	
	private String bundleId;
	
	private String bundleType;
	
	private String layerId;
	
	private String deviceIp;
	
	private String deviceModel;
	
	private OnlineStatus onlineStatus = OnlineStatus.OFFLINE;

	@Column(name = "BUNDLE_NAME")
	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	@Column(name = "BUNDLE_ID")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	@Column(name = "BUNDLE_TYPE")
	public String getBundleType() {
		return bundleType;
	}

	public void setBundleType(String bundleType) {
		this.bundleType = bundleType;
	}

	@Column(name = "LAYER_ID")
	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	@Column(name = "DEVICE_IP")
	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	@Column(name = "DEVICE_MODEL")
	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "ONLINE_STATUS")
	public OnlineStatus getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(OnlineStatus onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
}
