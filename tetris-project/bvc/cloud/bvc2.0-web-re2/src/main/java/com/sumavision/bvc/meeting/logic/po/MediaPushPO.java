package com.sumavision.bvc.meeting.logic.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.bvc.meeting.logic.po.CommonPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name="BVC_LOGIC_MEDIA_PUSH")
public class MediaPushPO extends AbstractBasePO {
	private String layerId;
	private String bundleId;
	private String channelId;
	private String fileSource;

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

	@Column(name="fileSource")
	public String getFileSource() {
		return fileSource;
	}

	public void setFileSource(String fileSource) {
		this.fileSource = fileSource;
	}
	
}
