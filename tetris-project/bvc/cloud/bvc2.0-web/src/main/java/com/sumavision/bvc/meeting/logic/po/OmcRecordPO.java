package com.sumavision.bvc.meeting.logic.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.bvc.meeting.logic.po.CommonPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name="BVC_LOGIC_OMC_RECORD")
public class OmcRecordPO extends AbstractBasePO {
//	private String uuid;
//	private String layerId;
//	private String bundleId;
//	private String channelId;
	private String cdnChannelId;//CDN返回的channelId
	private String cdnRecvIp;//CDN收流IP，应该与CDN服务器相同
	private String cdnRecvPort;//CDN收流端口
	private String cdnRecvPort2;//CDN接收rtp流的音频收流端口
	private String cdnRecvFormat;//流类型，ts / rtp-ps
	private String boVideoLiveID;//BO的videoLiveID
	private String playUrl;//由channelId等信息拼接得到
	private String videoName;//视频名，一般是会议名
	private String videoType;//1会议，2监控
	private String stopType;//暂不使用，1表示会议视频正常结束，2表示会议视频暂时结束。
	
	private String locationID;//区域
	private String categoryLiveID;//直播分类
	private String categoryID;//点播分类
	
	private String boAutoLiveId;

	@Column(name="cdnChannelId")
	public String getCdnChannelId() {
		return cdnChannelId;
	}

	public void setCdnChannelId(String cdnChannelId) {
		this.cdnChannelId = cdnChannelId;
	}

//	@Column(name="layerId")
//	public String getLayerId() {
//		return layerId;
//	}
//
//	public void setLayerId(String layerId) {
//		this.layerId = layerId;
//	}
//
//	@Column(name="bundleId")
//	public String getBundleId() {
//		return bundleId;
//	}
//
//	public void setBundleId(String bundleId) {
//		this.bundleId = bundleId;
//	}
//
//	@Column(name="channelId")
//	public String getChannelId() {
//		return channelId;
//	}
//
//	public void setChannelId(String channelId) {
//		this.channelId = channelId;
//	}

	@Column(name="cdnRecvPort")
	public String getCdnRecvPort() {
		return cdnRecvPort;
	}

	public void setCdnRecvPort(String cdnRecvPort) {
		this.cdnRecvPort = cdnRecvPort;
	}

	@Column(name="boVideoLiveID")
	public String getBoVideoLiveID() {
		return boVideoLiveID;
	}

	public void setBoVideoLiveID(String boVideoLiveID) {
		this.boVideoLiveID = boVideoLiveID;
	}

	@Column(name="cdnRecvIp")
	public String getCdnRecvIp() {
		return cdnRecvIp;
	}

	public void setCdnRecvIp(String cdnRecvIp) {
		this.cdnRecvIp = cdnRecvIp;
	}

	@Column(name="cdnRecvFormat")
	public String getCdnRecvFormat() {
		return cdnRecvFormat;
	}

	public void setCdnRecvFormat(String cdnRecvFormat) {
		this.cdnRecvFormat = cdnRecvFormat;
	}

	@Column(name="playUrl")
	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	@Column(name="videoName")
	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	@Column(name="videoType")
	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	@Column(name="stopType")
	public String getStopType() {
		return stopType;
	}

	public void setStopType(String stopType) {
		this.stopType = stopType;
	}

	public String getCdnRecvPort2() {
		return cdnRecvPort2;
	}

	public void setCdnRecvPort2(String cdnRecvPort2) {
		this.cdnRecvPort2 = cdnRecvPort2;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

	public String getCategoryLiveID() {
		return categoryLiveID;
	}

	public void setCategoryLiveID(String categoryLiveID) {
		this.categoryLiveID = categoryLiveID;
	}

	public String getLocationID() {
		return locationID;
	}

	public void setLocationID(String locationID) {
		this.locationID = locationID;
	}

	public String getBoAutoLiveId() {
		return boAutoLiveId;
	}

	public void setBoAutoLiveId(String boAutoLiveId) {
		this.boAutoLiveId = boAutoLiveId;
	}
	
}
