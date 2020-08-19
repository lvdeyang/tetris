package com.sumavision.bvc.device.group.po;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 录制得到的直播频道
 * @author zsy
 * @date 2018年12月23日 下午4:52:53 
 */
@Entity
@Table(name="BVC_RECORD_LIVE_CHANNEL")
public class RecordLiveChannelPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** bo和portal的直播标识(autoVideoId) */
	private String cid;
	
	/** bo的标识 */
	private String videoLiveID;
	
	/** 名字 **/
	private String name;
	
	/** 直播播放地址 **/
	private String playUrl;
	
	/** 时移播放地址 **/
	private String offsetPlayUrl;
	
	/** 关联录制（删除直播时使用） **/
	private String recordUuid;
	
//	/** 关联会议 */
//	private String groupUuid;

	/** 关联授权 */
	private DeviceGroupAuthorizationPO authorization;

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getVideoLiveID() {
		return videoLiveID;
	}

	public void setVideoLiveID(String videoLiveID) {
		this.videoLiveID = videoLiveID;
	}

	@ManyToOne
	@JoinColumn(name = "AUTHORIZATION_ID")
	public DeviceGroupAuthorizationPO getAuthorization() {
		return authorization;
	}

	public void setAuthorization(DeviceGroupAuthorizationPO authorization) {
		this.authorization = authorization;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	public String getOffsetPlayUrl() {
		return offsetPlayUrl;
	}

	public void setOffsetPlayUrl(String offsetPlayUrl) {
		this.offsetPlayUrl = offsetPlayUrl;
	}

	public String getRecordUuid() {
		return recordUuid;
	}

	public void setRecordUuid(String recordUuid) {
		this.recordUuid = recordUuid;
	}	
	
}
