package com.sumavision.bvc.command.emergent.broadcast;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 告警中关联的设备<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月22日 下午2:10:54
 */
@Entity
@Table(name="BVC_COMMAND_BROADCAST_ALARM_BUNDLE")
public class CommandBroadcastAlarmBundlePO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	/** 可能无用 */
	private String bundleName;
	
	/** 23位标识符 */
	private String bundleIdentification;
	
	private String deviceModel;
	
	/** 可能无用 */
	private String bundleId;
	
	/** 流地址 */
	private String streamUrl;
	
	/** 关联告警 */
	private CommandBroadcastAlarmPO alarm;

	@Column(name = "BUNDLE_NAME")
	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	@Column(name = "BUNDLE_IDENTIFICATION")
	public String getBundleIdentification() {
		return bundleIdentification;
	}

	public void setBundleIdentification(String bundleIdentification) {
		this.bundleIdentification = bundleIdentification;
	}

	@Column(name = "DEVICE_MODEL")
	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	@Column(name = "BUNDLE_ID")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}
	
	public String getStreamUrl() {
		return streamUrl;
	}

	public void setStreamUrl(String streamUrl) {
		this.streamUrl = streamUrl;
	}

	@ManyToOne
	@JoinColumn(name = "ALARM_ID")
	public CommandBroadcastAlarmPO getAlarm() {
		return alarm;
	}

	public void setAlarm(CommandBroadcastAlarmPO alarm) {
		this.alarm = alarm;
	}

	public CommandBroadcastAlarmBundlePO set(BundlePO bundle){
		this.bundleId = bundle.getBundleId();
		this.bundleIdentification = "";//TODO
		this.bundleName = bundle.getBundleName();
		this.deviceModel = bundle.getDeviceModel();
		this.streamUrl = bundle.getStreamUrl();
		return this;
	}
	
}
