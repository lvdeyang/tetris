package com.sumavision.bvc.meeting.logic.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

//@Entity
@Table(name="BVC_LOGIC_COMBINE_VIDEO_SCREEN")
public class CombineVideoScreenPO extends AbstractBasePO {
//	private String uuid;
	private String dstUuid;
	private String layerId;
	private String bundleId;
	private String channelId;
	private String codec;
	
//	@Column(name="codec")
//	public String getCodec() {
//		return codec;
//	}
//
//	public void setCodec(String codec) {
//		this.codec = codec;
//	}

//	@Column(name="uuid")
//	public String getUuid() {
//		return uuid;
//	}
//
//	public void setUuid(String uuid) {
//		this.uuid = uuid;
//	}

	@Column(name="layerId")
	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	@Column(name="bundleId")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	@Column(name="channelId")
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Column(name="dstUuid")
	public String getDstUuid() {
		return dstUuid;
	}

	public void setDstUuid(String dstUuid) {
		this.dstUuid = dstUuid;
	}
}
