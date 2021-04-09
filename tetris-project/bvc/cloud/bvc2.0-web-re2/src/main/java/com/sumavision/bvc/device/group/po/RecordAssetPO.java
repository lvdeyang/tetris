package com.sumavision.bvc.device.group.po;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 录制得到的点播
 * @author zsy
 * @date 2018年12月23日 下午4:52:53 
 */
@Entity
@Table(name="BVC_RECORD_ASSET")
public class RecordAssetPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** bo和portal的点播标识(autoVideoId) */
	private String pid;
	
	/** bo的标识 */
	private String videoID;	
	
	/** 名字 **/
	private String name;
	
	/** 点播播放地址 **/
	private String assetPlayUrl;
	
	/** 下载地址 */
	private String downloadAddress;
	
	/** 下载任务id */
	private String tsTaskId;
	
	/** 任务失败 */
	private String taskError;
	
//	/** 关联会议 */
//	private String groupUuid;

	/** 关联授权 */
	private DeviceGroupAuthorizationPO authorization;
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getVideoID() {
		return videoID;
	}

	public void setVideoID(String videoID) {
		this.videoID = videoID;
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

	public String getAssetPlayUrl() {
		return assetPlayUrl;
	}

	public void setAssetPlayUrl(String assetPlayUrl) {
		this.assetPlayUrl = assetPlayUrl;
	}

	public String getDownloadAddress() {
		return downloadAddress;
	}

	public void setDownloadAddress(String downloadAddress) {
		this.downloadAddress = downloadAddress;
	}

	public String getTsTaskId() {
		return tsTaskId;
	}

	public void setTsTaskId(String tsTaskId) {
		this.tsTaskId = tsTaskId;
	}

	public String getTaskError() {
		return taskError;
	}

	public void setTaskError(String taskError) {
		this.taskError = taskError;
	}
}
