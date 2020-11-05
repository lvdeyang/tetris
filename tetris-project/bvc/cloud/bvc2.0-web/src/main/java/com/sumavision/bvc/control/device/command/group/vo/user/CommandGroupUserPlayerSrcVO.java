package com.sumavision.bvc.control.device.command.group.vo.user;

import com.sumavision.tetris.bvc.page.PageTaskPO;

/**
 * 播放器的源信息<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月30日 上午11:45:12
 */
public class CommandGroupUserPlayerSrcVO {
	
	/***********
	 * 源信息 *
	 **********/
	
	/** 源视频设备id */
	private String srcVideoBundleId;
	
	/** 源设备视频通道id */
	private String srcVideoChannelId;
	
	/** 源设备层节点id, 对应资资层nodeUid*/
	private String srcVideoLayerId;
	
	/** 源号码 */
	private String srcVideoCode;
	
	/** 源音频设备id */
	private String srcAudioBundleId;

	/** 源设备音频通道id */
	private String srcAudioChannelId;
	
	/** 源设备层节点id, 对应资资层nodeUid*/
	private String srcAudioLayerId;
	
	/** 源号码 */
	private String srcAudioCode;
	
	public String getSrcVideoBundleId() {
		return srcVideoBundleId;
	}

	public void setSrcVideoBundleId(String srcVideoBundleId) {
		this.srcVideoBundleId = srcVideoBundleId;
	}

	public String getSrcVideoChannelId() {
		return srcVideoChannelId;
	}

	public void setSrcVideoChannelId(String srcVideoChannelId) {
		this.srcVideoChannelId = srcVideoChannelId;
	}

	public String getSrcVideoLayerId() {
		return srcVideoLayerId;
	}

	public void setSrcVideoLayerId(String srcVideoLayerId) {
		this.srcVideoLayerId = srcVideoLayerId;
	}

	public String getSrcVideoCode() {
		return srcVideoCode;
	}

	public void setSrcVideoCode(String srcVideoCode) {
		this.srcVideoCode = srcVideoCode;
	}

	public String getSrcAudioBundleId() {
		return srcAudioBundleId;
	}

	public void setSrcAudioBundleId(String srcAudioBundleId) {
		this.srcAudioBundleId = srcAudioBundleId;
	}

	public String getSrcAudioChannelId() {
		return srcAudioChannelId;
	}

	public void setSrcAudioChannelId(String srcAudioChannelId) {
		this.srcAudioChannelId = srcAudioChannelId;
	}

	public String getSrcAudioLayerId() {
		return srcAudioLayerId;
	}

	public void setSrcAudioLayerId(String srcAudioLayerId) {
		this.srcAudioLayerId = srcAudioLayerId;
	}

	public String getSrcAudioCode() {
		return srcAudioCode;
	}

	public void setSrcAudioCode(String srcAudioCode) {
		this.srcAudioCode = srcAudioCode;
	}
	
	public CommandGroupUserPlayerSrcVO set(PageTaskPO pageTask){
		this.srcVideoBundleId = pageTask.getSrcVideoBundleId();
		this.srcVideoChannelId = pageTask.getSrcVideoChannelId();
		this.srcVideoLayerId = pageTask.getSrcVideoLayerId();
		this.srcVideoCode = pageTask.getSrcVideoCode();
		this.srcAudioBundleId = pageTask.getSrcAudioBundleId();
		this.srcAudioChannelId = pageTask.getSrcAudioChannelId();
		this.srcAudioLayerId = pageTask.getSrcAudioLayerId();
		this.srcAudioCode = pageTask.getSrcAudioCode();
		return this;
	}

}
