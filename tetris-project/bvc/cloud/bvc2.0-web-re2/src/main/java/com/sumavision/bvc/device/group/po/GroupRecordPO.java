package com.sumavision.bvc.device.group.po;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sumavision.bvc.device.group.enumeration.RecordType;
//import com.netflix.infix.lang.infix.antlr.EventFilterParser.boolean_expr_return;
//import com.netflix.infix.lang.infix.antlr.EventFilterParser.null_predicate_return;
import com.sumavision.bvc.device.group.enumeration.SourceType;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 录制的一个片段
 * @author zsy
 * @date 2019年11月20日 下午1:06:00
 */
@Entity
@Table(name="BVC_GROUP_RECORD")
public class GroupRecordPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 录制状态 */
	private boolean run = true;
	
	/** 片段信息，源的用户名或转发源名称（会议名：xx视角） */
	private String info;
	
	/** 开始时间 */
	private Date startTime;
	
	/** 停止时间 */
	private Date endTime;
	
	/** 录制类型 */
	private RecordType type;
	
	/** 文件预览地址 */
	private String previewUrl;
	
//	/** 源，设备组成员id。-1表示不是成员【录制成员时作为唯一标识】 */
//	private Long srcMemberId = -1L;

	/** 关联录制信息 */
	private GroupRecordInfoPO recordInfo;
	
	/** 文件存储位置 */
	private String storeLayerId;
	
	/** 观看角色的id */
	private Long roleId;

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

	public GroupRecordPO setRun(boolean run) {
		this.run = run;
		return this;
	}

	@Column(name = "INFO")
	public String getInfo() {
		return info;
	}

	public GroupRecordPO setInfo(String info) {
		this.info = info;
		return this;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}

	public GroupRecordPO setStartTime(Date startTime) {
		this.startTime = startTime;
		return this;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}

	public GroupRecordPO setEndTime(Date endTime) {
		this.endTime = endTime;
		return this;
	}

	@Column(name = "PREVIEW_URL")
	public String getPreviewUrl() {
		return previewUrl;
	}

	public GroupRecordPO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	@ManyToOne
	public GroupRecordInfoPO getRecordInfo() {
		return recordInfo;
	}

	public GroupRecordPO setRecordInfo(GroupRecordInfoPO recordInfo) {
		this.recordInfo = recordInfo;
		return this;
	}

	@Column(name = "STORE_LAYERID")
	public String getStoreLayerId() {
		return storeLayerId;
	}
	
	public GroupRecordPO setStoreLayerId(String storeLayerId) {
		this.storeLayerId = storeLayerId;
		return this;
	}

	@Column(name = "ROLEID")
	public Long getRoleId() {
		return roleId;
	}

	public GroupRecordPO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "VIDEO_TYPE")
	public SourceType getVideoType() {
		return videoType;
	}

	public GroupRecordPO setVideoType(SourceType videoType) {
		this.videoType = videoType;
		return this;
	}

	@Column(name = "VIDEO_MEMBERID")
	public Long getVideoMemberId() {
		return videoMemberId;
	}

	public GroupRecordPO setVideoMemberId(Long videoMemberId) {
		this.videoMemberId = videoMemberId;
		return this;
	}
	
	@Column(name = "VIDEO_MEMBER_CHANNELID")
	public Long getVideoMemberChannelId() {
		return videoMemberChannelId;
	}

	public GroupRecordPO setVideoMemberChannelId(Long videoMemberChannelId) {
		this.videoMemberChannelId = videoMemberChannelId;
		return this;
	}

	@Column(name = "VIDEO_BUNDLEID")
	public String getVideoBundleId() {
		return videoBundleId;
	}

	public GroupRecordPO setVideoBundleId(String videoBundleId) {
		this.videoBundleId = videoBundleId;
		return this;
	}

	@Column(name = "VIDEO_LAYERID")
	public String getVideoLayerId() {
		return videoLayerId;
	}

	public GroupRecordPO setVideoLayerId(String videoLayerId) {
		this.videoLayerId = videoLayerId;
		return this;
	}

	@Column(name = "VIDEO_CHANNELID")
	public String getVideoChannelId() {
		return videoChannelId;
	}

	public GroupRecordPO setVideoChannelId(String videoChannelId) {
		this.videoChannelId = videoChannelId;
		return this;
	}

	@Column(name = "COMBINE_VIDEO_UUID")
	public String getCombineVideoUuid() {
		return combineVideoUuid;
	}

	public GroupRecordPO setCombineVideoUuid(String combineVideoUuid) {
		this.combineVideoUuid = combineVideoUuid;
		return this;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "AUDIO_TYPE")
	public SourceType getAudioType() {
		return audioType;
	}

	public GroupRecordPO setAudioType(SourceType audioType) {
		this.audioType = audioType;
		return this;
	}

	@Column(name = "AUDIO_MEMBERID")
	public Long getAudioMemberId() {
		return audioMemberId;
	}

	public GroupRecordPO setAudioMemberId(Long audioMemberId) {
		this.audioMemberId = audioMemberId;
		return this;
	}

	@Column(name = "AUDIO_MEMBER_CHANNELID")
	public Long getAudioMemberChannelId() {
		return audioMemberChannelId;
	}

	public GroupRecordPO setAudioMemberChannelId(Long audioMemberChannelId) {
		this.audioMemberChannelId = audioMemberChannelId;
		return this;
	}

	@Column(name = "AUDIO_BUNDLEID")
	public String getAudioBundleId() {
		return audioBundleId;
	}

	public GroupRecordPO setAudioBundleId(String audioBundleId) {
		this.audioBundleId = audioBundleId;
		return this;
	}

	@Column(name = "AUDIO_LAYERID")
	public String getAudioLayerId() {
		return audioLayerId;
	}

	public GroupRecordPO setAudioLayerId(String audioLayerId) {
		this.audioLayerId = audioLayerId;
		return this;
	}

	@Column(name = "AUDIO_CHANNELID")
	public String getAudioChannelId() {
		return audioChannelId;
	}

	public GroupRecordPO setAudioChannelId(String audioChannelId) {
		this.audioChannelId = audioChannelId;
		return this;
	}

	@Column(name = "COMBINE_AUDIO_UUID")
	public String getCombineAudioUuid() {
		return combineAudioUuid;
	}

	public GroupRecordPO setCombineAudioUuid(String combineAudioUuid) {
		this.combineAudioUuid = combineAudioUuid;
		return this;
	}
	
	@Column(name = "TYPE")
	public RecordType getType() {
		return type;
	}

	public GroupRecordPO setType(RecordType type) {
		this.type = type;
		return this;
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
	
	public void generateInformation(PageTaskPO pageTask){
		
		this.setStartTime(new Date())
			.setVideoType(SourceType.CHANNEL)
			.setVideoMemberId(pageTask.getSrcVideoMemberId())
			.setVideoMemberChannelId(pageTask.getSrcVideoMemberTerminalChannelId())
			.setVideoBundleId(pageTask.getSrcVideoBundleId())
			.setVideoLayerId(pageTask.getSrcVideoLayerId())
			.setVideoChannelId(pageTask.getSrcVideoChannelId())
			
			.setAudioType(SourceType.CHANNEL)
			.setAudioMemberId(pageTask.getSrcAudioMemberId())
//			.setAudioMemberChannelId()
			.setAudioBundleId(pageTask.getSrcAudioBundleId())
			.setAudioLayerId(pageTask.getSrcAudioLayerId())
			.setAudioChannelId(pageTask.getSrcAudioChannelId());
	}
}
