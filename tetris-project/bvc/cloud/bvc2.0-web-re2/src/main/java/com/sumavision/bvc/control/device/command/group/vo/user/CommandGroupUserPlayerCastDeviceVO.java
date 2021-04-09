package com.sumavision.bvc.control.device.command.group.vo.user;

import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;

/**
 * @ClassName: 播放器关联的上屏设备<br/>
 * @Description: <br/>
 * @author zsy 
 * @date 2019年10月8日
 */
public class CommandGroupUserPlayerCastDeviceVO {
	
	/***********
	 * 目的设备信息 *
	 **********/
	
	/** 目的设备id */
	private String dstBundleId;
	
	/** 目的设备视频通道id */
	private String dstVideoChannelId;

	/** 目的设备音频通道id */
	private String dstAudioChannelId;
	
	/** 目的设备层节点id, 对应资资层nodeUid*/
	private String dstLayerId;

	/** 目的设备名称 */
	private String dstBundleName;
	
	/** 设备类型 jv210、jv230、tvos、ipc、mobile */
	private String dstBundleType;
	
	/** 目的设备的资源类型 */
	private String dstVenusBundleType;
	
	public String getDstBundleId() {
		return dstBundleId;
	}

	public void setDstBundleId(String dstBundleId) {
		this.dstBundleId = dstBundleId;
	}

	public String getDstVideoChannelId() {
		return dstVideoChannelId;
	}

	public void setDstVideoChannelId(String dstVideoChannelId) {
		this.dstVideoChannelId = dstVideoChannelId;
	}

	public String getDstAudioChannelId() {
		return dstAudioChannelId;
	}

	public void setDstAudioChannelId(String dstAudioChannelId) {
		this.dstAudioChannelId = dstAudioChannelId;
	}

	public String getDstLayerId() {
		return dstLayerId;
	}

	public void setDstLayerId(String dstLayerId) {
		this.dstLayerId = dstLayerId;
	}

	public String getDstBundleName() {
		return dstBundleName;
	}

	public void setDstBundleName(String dstBundleName) {
		this.dstBundleName = dstBundleName;
	}

	public String getDstBundleType() {
		return dstBundleType;
	}

	public void setDstBundleType(String dstBundleType) {
		this.dstBundleType = dstBundleType;
	}

	public String getDstVenusBundleType() {
		return dstVenusBundleType;
	}

	public void setDstVenusBundleType(String dstVenusBundleType) {
		this.dstVenusBundleType = dstVenusBundleType;
	}
	
	public CommandGroupUserPlayerCastDeviceVO set(CommandGroupUserPlayerCastDevicePO device){
		this.dstBundleId = device.getDstBundleId();
		this.dstVideoChannelId = device.getDstVideoChannelId();
		this.dstAudioChannelId = device.getDstAudioChannelId();
		this.dstLayerId = device.getDstLayerId();
		this.dstBundleName = device.getDstBundleName();
		this.dstBundleType = device.getDstBundleType();
		this.dstVenusBundleType = device.getDstVenusBundleType();
		return this;
	}

}
