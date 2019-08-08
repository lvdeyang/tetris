package com.sumavision.tetris.cs.channel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Table
@Entity(name = "TETRIS_CS_CHANNEL")
public class ChannelPO extends AbstractBasePO {
	private static final long serialVersionUID = 1L;

	/** 频道名称 */
	private String name;
	
	/** 频道生效时间 */
	private String date;
	
	/** 频道备注 */
	private String remark;
	
	/** 播发方式 */
	private String broadWay;
	
	/** 视频流媒资id */
	private Long mediaId;
	
	/** 视频流媒资url地址 */
	private String previewUrlIp;
	
	/** 视频流媒资url端口 */
	private String previewUrlPort;
	
	/** 能力播发id */
	private Integer broadId;
	
	/** 上次播发url地址 */
	private String broadUrlIp;
	
	/** 上次播发url端口 */
	private String broadUrlPort;
	
	/** 播发状态 */
	private String broadcastStatus;
	
	/** 用户组织信息 */
	private String groupId;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DATE")
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Column(name = "BROAD_WAY")
	public String getBroadWay() {
		return broadWay;
	}

	public void setBroadWay(String broadWay) {
		this.broadWay = broadWay;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "MEDIA_ID")
	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	@Column(name = "PREVIEW_URL_IP")
	public String getPreviewUrlIp() {
		return previewUrlIp;
	}

	public void setPreviewUrlIp(String previewUrlIp) {
		this.previewUrlIp = previewUrlIp;
	}

	@Column(name = "PREVIEW_URL_PORT")
	public String getPreviewUrlPort() {
		return previewUrlPort;
	}

	public void setPreviewUrlPort(String previewUrlPort) {
		this.previewUrlPort = previewUrlPort;
	}

	@Column(name = "BROAD_ID")
	public Integer getBroadId() {
		return broadId;
	}

	public void setBroadId(Integer broadId) {
		this.broadId = broadId;
	}

	@Column(name = "BROAD_URL_IP")
	public String getBroadUrlIp() {
		return broadUrlIp;
	}

	public void setBroadUrlIp(String broadUrlIp) {
		this.broadUrlIp = broadUrlIp;
	}

	@Column(name = "BROAD_URL_PORT")
	public String getBroadUrlPort() {
		return broadUrlPort;
	}

	public void setBroadUrlPort(String broadUrlPort) {
		this.broadUrlPort = broadUrlPort;
	}

	@Column(name = "BROADCAST_STATUS")
	public String getBroadcastStatus() {
		return broadcastStatus;
	}

	public void setBroadcastStatus(String broadcastStatus) {
		this.broadcastStatus = broadcastStatus;
	}

	@Column(name = "GROUP_ID")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}
