package com.sumavision.signal.bvc.entity.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.signal.bvc.entity.enumeration.InternetAccessType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 终端绑定转发器数据<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月22日 上午10:48:06
 */
@Entity
@Table(name = "BVC_TERMINAL_BIND_REPEATER")
public class TerminalBindRepeaterPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;

	/** 终端名称 */
	private String bundleName;
	
	/** 终端bundleId */
	private String bundleId;
	
	/** 终端ip */
	private String bundleIp;
	
	/** 终端venus类型 */
	private String bundleType;
	
	/** 终端设备类型 */
	private String deviceModel;
	
	/** 终端接入id */
	private String layerId;
	
	/** 终端绑定转发器id */
	private Long repeaterId;
	
	/** 终端绑定转发器网口id */
	private Long accessId;
	
	/** 终端绑定转发器网口地址 */
	private String accessAddress;
	
	/** 终端绑定转发器网口类型 */
	private InternetAccessType accessType;

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

	@Column(name ="BUNDLE_IP")
	public String getBundleIp() {
		return bundleIp;
	}

	public void setBundleIp(String bundleIp) {
		this.bundleIp = bundleIp;
	}

	@Column(name = "BUNDLE_TYPE")
	public String getBundleType() {
		return bundleType;
	}

	public void setBundleType(String bundleType) {
		this.bundleType = bundleType;
	}

	@Column(name = "DEVICE_MODEL")
	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	@Column(name = "LAYER_ID")
	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	@Column(name = "REPEATER_ID")
	public Long getRepeaterId() {
		return repeaterId;
	}

	public void setRepeaterId(Long repeaterId) {
		this.repeaterId = repeaterId;
	}

	@Column(name = "ACCESS_ID")
	public Long getAccessId() {
		return accessId;
	}

	public void setAccessId(Long accessId) {
		this.accessId = accessId;
	}

	@Column(name = "ACCESS_ADDRESS")
	public String getAccessAddress() {
		return accessAddress;
	}

	public void setAccessAddress(String accessAddress) {
		this.accessAddress = accessAddress;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "ACCESS_TYPE")
	public InternetAccessType getAccessType() {
		return accessType;
	}

	public void setAccessType(InternetAccessType accessType) {
		this.accessType = accessType;
	}
	
}
