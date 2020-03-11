package com.sumavision.bvc.command.emergent.broadcast;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 喊话扬声器<br/>
 * @Description: <br/>
 * @author zsy 
 * @date 2020年3月10日 下午1:06:00
 */
@Entity
@Table(name="BVC_COMMAND_BROADCAST_SPEAKER")
public class CommandBroadcastSpeakerPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	/** 可能无用 */
	private String bundleName;
	
	/** 23位标识符 */
	private String bundleIdentification;
	
	private String deviceModel;
	
	/** 可能无用 */
	private String bundleId;
	
	/** 关联设备组 */
	private CommandBroadcastSpeakPO speak;

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

	@ManyToOne
	@JoinColumn(name = "SPEAK_ID")
	public CommandBroadcastSpeakPO getSpeak() {
		return speak;
	}

	public void setSpeak(CommandBroadcastSpeakPO speak) {
		this.speak = speak;
	}
	
	public CommandBroadcastSpeakerPO set(BundlePO bundle){
		this.bundleId = bundle.getBundleId();
		this.bundleIdentification = "";//TODO
		this.bundleName = bundle.getBundleName();
		this.deviceModel = bundle.getDeviceModel();
		return this;
	}
	
}
