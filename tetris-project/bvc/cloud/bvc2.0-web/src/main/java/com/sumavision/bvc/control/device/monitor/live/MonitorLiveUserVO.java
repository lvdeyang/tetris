package com.sumavision.bvc.control.device.monitor.live;

import com.sumavision.bvc.device.monitor.live.user.MonitorLiveUserPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MonitorLiveUserVO extends AbstractBaseVO<MonitorLiveUserVO, MonitorLiveUserPO>{

	/** 被点播用户名 */
	private String srcUsername;
	
	/** 源视频名称 */
	private String srcVideoName;
	
	/** 源音频名称 */
	private String srcAudioName;
	
	/** 操作业务用户名 */
	private String dstUsername;
	
	/** 目的视频名称 */
	private String dstVideoName;
	
	/** 目的音频名称 */
	private String dstAudioName;
	
	/** 目标设备类型 */
	private String dstDeviceType;
	
	/** 点播任务类型 */
	private String type;
	
	public String getSrcUsername() {
		return srcUsername;
	}

	public MonitorLiveUserVO setSrcUsername(String srcUsername) {
		this.srcUsername = srcUsername;
		return this;
	}

	public String getSrcVideoName() {
		return srcVideoName;
	}

	public MonitorLiveUserVO setSrcVideoName(String srcVideoName) {
		this.srcVideoName = srcVideoName;
		return this;
	}

	public String getSrcAudioName() {
		return srcAudioName;
	}

	public MonitorLiveUserVO setSrcAudioName(String srcAudioName) {
		this.srcAudioName = srcAudioName;
		return this;
	}

	public String getDstUsername() {
		return dstUsername;
	}

	public MonitorLiveUserVO setDstUsername(String dstUsername) {
		this.dstUsername = dstUsername;
		return this;
	}

	public String getDstVideoName() {
		return dstVideoName;
	}

	public MonitorLiveUserVO setDstVideoName(String dstVideoName) {
		this.dstVideoName = dstVideoName;
		return this;
	}

	public String getDstAudioName() {
		return dstAudioName;
	}

	public MonitorLiveUserVO setDstAudioName(String dstAudioName) {
		this.dstAudioName = dstAudioName;
		return this;
	}

	public String getDstDeviceType() {
		return dstDeviceType;
	}

	public MonitorLiveUserVO setDstDeviceType(String dstDeviceType) {
		this.dstDeviceType = dstDeviceType;
		return this;
	}

	public String getType() {
		return type;
	}

	public MonitorLiveUserVO setType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public MonitorLiveUserVO set(MonitorLiveUserPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setSrcUsername(entity.getSrcUsername())
			.setSrcVideoName(new StringBufferWrapper().append(entity.getBundleName()==null?"":entity.getBundleName()).append("-").append(entity.getVideoChannelName()==null?"":entity.getVideoChannelName()).toString())
			.setSrcAudioName(new StringBufferWrapper().append(entity.getBundleName()==null?"":entity.getBundleName()).append("-").append(entity.getAudioChannelName()==null?"":entity.getAudioChannelName()).toString())
			.setDstUsername(entity.getUsername())
			.setDstVideoName(new StringBufferWrapper().append(entity.getDstVideoBundleName()==null?"":entity.getDstVideoBundleName()).append("-").append(entity.getDstVideoChannelName()==null?"":entity.getDstVideoChannelName()).toString())
			.setDstAudioName(new StringBufferWrapper().append(entity.getDstAudioBundleName()==null?"":entity.getDstAudioBundleName()).append("-").append(entity.getDstAudioChannelName()==null?"":entity.getDstAudioChannelName()).toString())
			.setDstDeviceType(entity.getDstDeviceType()==null?"":entity.getDstDeviceType().getName())
			.setType(entity.getType()==null?"":entity.getType().getName());
		return this;
	}

}
