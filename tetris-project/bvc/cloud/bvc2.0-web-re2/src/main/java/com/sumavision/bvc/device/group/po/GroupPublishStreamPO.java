package com.sumavision.bvc.device.group.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sumavision.bvc.device.group.enumeration.RecordType;
import com.sumavision.bvc.device.group.enumeration.SourceType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 设备组中的录制 
 * @author lvdeyang
 * @date 2018年8月8日 下午4:52:53 
 */
@Entity
@Table(name="BVC_GROUP_RECORD_PUBLISH")
public class GroupPublishStreamPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 流类型 */
	private String format = "rtmp";
	
	/** 发布地址 */
	private String url;
	
	/** 发布地址转换 */
	private String transferUrl;
	
	/** cdn推流地址 */
	private String udp;
	
	/** 媒资id */
	private Long mimsId;
	
	/** 会议id */
	private Long groupId;

	@Column(name = "FORMAT")
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Column(name = "URL")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "UDP")
	public String getUdp() {
		return udp;
	}

	public void setUdp(String udp) {
		this.udp = udp;
	}

	@Column(name = "MIMS_ID")
	public Long getMimsId() {
		return mimsId;
	}

	public void setMimsId(Long mimsId) {
		this.mimsId = mimsId;
	}

	@Column(name = "TRANSFER_URL")
	public String getTransferUrl() {
		return transferUrl;
	}

	public void setTransferUrl(String transferUrl) {
		this.transferUrl = transferUrl;
	}
	
	@Column(name = "GROUPID")
	public Long getGroupId() {
		return groupId;
	}

	public GroupPublishStreamPO setGroupId(Long groupId) {
		this.groupId = groupId;
		return this;
	}

	//--------------GroupPublishStreamPO移植来的参数
	/** 直播状态 */
	private boolean run = true;
	
	/** 片段信息，源的用户名或转发源名称（会议名：xx视角） */
	private String info;
	
	/** 开始时间 */
	private Date startTime;
	
	/** 停止时间 */
	private Date endTime;

	/************
	 ***视频源****
	 ************/
	
	/** 录制真实视频内容类型：合屏【|通道】 */
	private SourceType videoType;
	
	/** 当录制的是通道时，存设备组成员id */
	private Long videoMemberId;
	
	/** 当录制的是通道时，存设备组成员通道id */
	private Long videoMemberChannelId;
	
	/** 当录制的是通道时，存设来自于资源层的设备id */
	private String videoBundleId;
	
	/** 当录制的是通道时，存设来自于资源层的设备layerId */
	private String videoLayerId;
	
	/** 当录制的是通道时，存设来自于资源层的通道id */
	private String videoChannelId;
	
	/** 当录制的是合屏时，存合屏的uuid */
	private String combineVideoUuid;
	
	/************
	 ***音频源****
	 ************/
	
	/** 录制真实音频内容类型：混音【|通道】 */
	private SourceType audioType;
	
	/** 当录制的是通道时，存设备组成员id */
	private Long audioMemberId;
	
	/** 当录制的是通道时，存设备组成员通道id */
	private Long audioMemberChannelId;
	
	/** 当录制的是通道时，存设来自于资源层的设备id */
	private String audioBundleId;
	
	/** 当录制的是通道时，存设来自于资源层的设备layerId */
	private String audioLayerId;
	
	/** 当录制的是通道时，存设来自于资源层的通道id */
	private String audioChannelId;
	
	/** 当录制的是混音时，存混音的uuid */
	private String combineAudioUuid;
	
	@Column(name = "RUN")
	public boolean isRun() {
		return run;
	}

	public GroupPublishStreamPO setRun(boolean run) {
		this.run = run;
		return this;
	}

	@Column(name = "INFO")
	public String getInfo() {
		return info;
	}

	public GroupPublishStreamPO setInfo(String info) {
		this.info = info;
		return this;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}

	public GroupPublishStreamPO setStartTime(Date startTime) {
		this.startTime = startTime;
		return this;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}

	public GroupPublishStreamPO setEndTime(Date endTime) {
		this.endTime = endTime;
		return this;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "VIDEO_TYPE")
	public SourceType getVideoType() {
		return videoType;
	}

	public GroupPublishStreamPO setVideoType(SourceType videoType) {
		this.videoType = videoType;
		return this;
	}

	@Column(name = "VIDEO_MEMBERID")
	public Long getVideoMemberId() {
		return videoMemberId;
	}

	public GroupPublishStreamPO setVideoMemberId(Long videoMemberId) {
		this.videoMemberId = videoMemberId;
		return this;
	}
	
	@Column(name = "VIDEO_MEMBER_CHANNELID")
	public Long getVideoMemberChannelId() {
		return videoMemberChannelId;
	}

	public GroupPublishStreamPO setVideoMemberChannelId(Long videoMemberChannelId) {
		this.videoMemberChannelId = videoMemberChannelId;
		return this;
	}

	@Column(name = "VIDEO_BUNDLEID")
	public String getVideoBundleId() {
		return videoBundleId;
	}

	public GroupPublishStreamPO setVideoBundleId(String videoBundleId) {
		this.videoBundleId = videoBundleId;
		return this;
	}

	@Column(name = "VIDEO_LAYERID")
	public String getVideoLayerId() {
		return videoLayerId;
	}

	public GroupPublishStreamPO setVideoLayerId(String videoLayerId) {
		this.videoLayerId = videoLayerId;
		return this;
	}

	@Column(name = "VIDEO_CHANNELID")
	public String getVideoChannelId() {
		return videoChannelId;
	}

	public GroupPublishStreamPO setVideoChannelId(String videoChannelId) {
		this.videoChannelId = videoChannelId;
		return this;
	}

	@Column(name = "COMBINE_VIDEO_UUID")
	public String getCombineVideoUuid() {
		return combineVideoUuid;
	}

	public GroupPublishStreamPO setCombineVideoUuid(String combineVideoUuid) {
		this.combineVideoUuid = combineVideoUuid;
		return this;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "AUDIO_TYPE")
	public SourceType getAudioType() {
		return audioType;
	}

	public GroupPublishStreamPO setAudioType(SourceType audioType) {
		this.audioType = audioType;
		return this;
	}

	@Column(name = "AUDIO_MEMBERID")
	public Long getAudioMemberId() {
		return audioMemberId;
	}

	public GroupPublishStreamPO setAudioMemberId(Long audioMemberId) {
		this.audioMemberId = audioMemberId;
		return this;
	}

	@Column(name = "AUDIO_MEMBER_CHANNELID")
	public Long getAudioMemberChannelId() {
		return audioMemberChannelId;
	}

	public GroupPublishStreamPO setAudioMemberChannelId(Long audioMemberChannelId) {
		this.audioMemberChannelId = audioMemberChannelId;
		return this;
	}

	@Column(name = "AUDIO_BUNDLEID")
	public String getAudioBundleId() {
		return audioBundleId;
	}

	public GroupPublishStreamPO setAudioBundleId(String audioBundleId) {
		this.audioBundleId = audioBundleId;
		return this;
	}

	@Column(name = "AUDIO_LAYERID")
	public String getAudioLayerId() {
		return audioLayerId;
	}

	public GroupPublishStreamPO setAudioLayerId(String audioLayerId) {
		this.audioLayerId = audioLayerId;
		return this;
	}

	@Column(name = "AUDIO_CHANNELID")
	public String getAudioChannelId() {
		return audioChannelId;
	}

	public GroupPublishStreamPO setAudioChannelId(String audioChannelId) {
		this.audioChannelId = audioChannelId;
		return this;
	}

	@Column(name = "COMBINE_AUDIO_UUID")
	public String getCombineAudioUuid() {
		return combineAudioUuid;
	}

	public GroupPublishStreamPO setCombineAudioUuid(String combineAudioUuid) {
		this.combineAudioUuid = combineAudioUuid;
		return this;
	}

	/**
	 * 根据GroupRecordPO构建PublishStream<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月26日 下午4:57:01
	 * @param record
	 */
	public void setInformationFormGroupRecord(GroupRecordPO record) {
		
		this.setRun(record.isRun())
			.setInfo(record.getInfo())
			.setStartTime(record.getStartTime())
			.setEndTime(record.getEndTime())
			.setVideoType(record.getVideoType())
			.setVideoMemberId(record.getVideoMemberId())
			.setVideoMemberChannelId(record.getVideoMemberChannelId())
			.setVideoBundleId(record.getVideoBundleId())
			.setVideoLayerId(record.getVideoLayerId())
			.setVideoChannelId(record.getVideoChannelId())
			.setCombineVideoUuid(record.getCombineVideoUuid())
			.setAudioType(record.getAudioType())
			.setAudioMemberId(record.getAudioMemberId())
			.setAudioMemberChannelId(record.getAudioMemberChannelId())
			.setAudioBundleId(record.getAudioBundleId())
			.setAudioLayerId(record.getAudioLayerId())
			.setAudioChannelId(record.getAudioChannelId())
			.setCombineAudioUuid(record.getCombineAudioUuid());
	}
	
	/**
	 * 如果有声音视频数据返回true并清理数据<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月28日 上午10:09:21
	 * @return
	 */
	public boolean emptyAndClear(){
		if(!hasNoVideoAndAudio()){
			clearVideoInfo();
			clearAudioInfo();
			return hasNoVideoAndAudio();
		}else {
			return hasNoVideoAndAudio();
		}
	}
	
	/**
	 * 校验--没有声音、视频源返回true<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月28日 上午10:12:56
	 * @return
	 */
	private boolean hasNoVideoAndAudio(){
		if(this.getAudioBundleId() != null || this.getCombineAudioUuid() != null || this.getVideoBundleId() != null || this.getCombineVideoUuid() != null){
			return false;
		}
		return true;
	}
	
	/**
	 * 清理视频相关的数据<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月28日 上午10:12:59
	 */
	public void clearVideoInfo() {
		
		this.setAudioType(null)
			.setAudioMemberId(null)
			.setAudioMemberChannelId(null)
			.setAudioBundleId("")
			.setAudioLayerId("")
			.setAudioChannelId("")
			.setCombineAudioUuid("");
	}
	
	/**
	 * 清理声音相关的数据<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月28日 上午10:39:44
	 */
	public void clearAudioInfo() {
		
		this.setAudioType(null)
			.setAudioMemberId(null)
			.setAudioMemberChannelId(null)
			.setAudioBundleId("")
			.setAudioLayerId("")
			.setAudioChannelId("")
			.setCombineAudioUuid("");
	}
}
