package com.sumavision.bvc.meeting.logic.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.bvc.meeting.logic.po.CommonPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name="BVC_LOGIC_OUT_CONN_MEDIA_MUX")
public class OutConnMediaMuxPO extends AbstractBasePO {
//	private String uuid;
	private String layerId;
	private String bundleId;
	private String channelId;
	private String url;//给CDN发流的地址
	private String format;//ts rtp-ps rtmp 
	private String recordUuid;//在发布rtmp时使用，记录录制的uuid

	@Column(name="url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

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

	@Column(name="format")
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Column(name="recordUuid")
	public String getRecordUuid() {
		return recordUuid;
	}

	public void setRecordUuid(String recordUuid) {
		this.recordUuid = recordUuid;
	}
	
}
