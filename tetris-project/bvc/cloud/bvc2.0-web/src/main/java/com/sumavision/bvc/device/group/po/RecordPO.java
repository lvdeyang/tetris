package com.sumavision.bvc.device.group.po;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sumavision.bvc.device.group.enumeration.GroupType;
import com.sumavision.bvc.device.group.enumeration.RecordType;
import com.sumavision.bvc.device.group.enumeration.SourceType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 设备组中的录制 
 * @author lvdeyang
 * @date 2018年8月8日 下午4:52:53 
 */
@Entity
@Table(name="BVC_DEVICE_GROUP_RECORD")
public class RecordPO  extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 录制状态 */
	private boolean run;
	
	/** 记录group类型 */
	private GroupType groupType;
	
	/** 视频名称 */
	private String videoName;
	
	/** 视频描述 */
	private String description;
	
	/** 录制类型 */
	private RecordType type;
	
	/** 如果是录制方案这里存录制方案id@@通道类型， 如果录制的是视频这个存视频id，如果录制的是设备，这个地方不存，用videoMemberId或者audioMemberId判断 */
	private String recordId;
	
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
	
	/** 录制中的发布 */
	private Set<PublishStreamPO> publishStreams;
	
	/** 关联设备组 */
	private DeviceGroupPO group;

	@Column(name = "RUN")
	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "GROUP_TYPE")
	public GroupType getGroupType() {
		return groupType;
	}

	public void setGroupType(GroupType groupType) {
		this.groupType = groupType;
	}

	@Column(name = "VIDEO_NAME")
	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public RecordType getType() {
		return type;
	}

	public void setType(RecordType type) {
		this.type = type;
	}

	@Column(name = "RECORD_ID")
	public String getRecordId() {
		return recordId;
	}
	
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	@Column(name = "VIDEO_TYPE")
	public SourceType getVideoType() {
		return videoType;
	}

	public void setVideoType(SourceType videoType) {
		this.videoType = videoType;
	}

	@Column(name = "VIDEO_MEMBER_ID")
	public Long getVideoMemberId() {
		return videoMemberId;
	}

	public void setVideoMemberId(Long videoMemberId) {
		this.videoMemberId = videoMemberId;
	}

	@Column(name = "VIDEO_MEMBER_CHANNEL_ID")
	public Long getVideoMemberChannelId() {
		return videoMemberChannelId;
	}

	public void setVideoMemberChannelId(Long videoMemberChannelId) {
		this.videoMemberChannelId = videoMemberChannelId;
	}

	@Column(name = "VIDEO_BUNDLE_ID")
	public String getVideoBundleId() {
		return videoBundleId;
	}

	public void setVideoBundleId(String videoBundleId) {
		this.videoBundleId = videoBundleId;
	}

	@Column(name = "VIDEO_LAYER_ID")
	public String getVideoLayerId() {
		return videoLayerId;
	}

	public void setVideoLayerId(String videoLayerId) {
		this.videoLayerId = videoLayerId;
	}

	@Column(name = "VIDEO_CHANNEL_ID")
	public String getVideoChannelId() {
		return videoChannelId;
	}

	public void setVideoChannelId(String videoChannelId) {
		this.videoChannelId = videoChannelId;
	}

	@Column(name = "COMBINE_VIDEO_UUID")
	public String getCombineVideoUuid() {
		return combineVideoUuid;
	}

	public void setCombineVideoUuid(String combineVideoUuid) {
		this.combineVideoUuid = combineVideoUuid;
	}

	@Column(name = "AUDIO_TYPE")
	public SourceType getAudioType() {
		return audioType;
	}

	public void setAudioType(SourceType audioType) {
		this.audioType = audioType;
	}

	@Column(name = "AUDIO_MEMBER_ID")
	public Long getAudioMemberId() {
		return audioMemberId;
	}

	public void setAudioMemberId(Long audioMemberId) {
		this.audioMemberId = audioMemberId;
	}

	@Column(name = "AUDIO_MEMBER_CHANNEL_ID")
	public Long getAudioMemberChannelId() {
		return audioMemberChannelId;
	}

	public void setAudioMemberChannelId(Long audioMemberChannelId) {
		this.audioMemberChannelId = audioMemberChannelId;
	}

	@Column(name = "AUDIO_BUNDLE_ID")
	public String getAudioBundleId() {
		return audioBundleId;
	}

	public void setAudioBundleId(String audioBundleId) {
		this.audioBundleId = audioBundleId;
	}

	@Column(name = "AUDIO_LAYER_ID")
	public String getAudioLayerId() {
		return audioLayerId;
	}

	public void setAudioLayerId(String audioLayerId) {
		this.audioLayerId = audioLayerId;
	}

	@Column(name = "AUDIO_CHANNEL_ID")
	public String getAudioChannelId() {
		return audioChannelId;
	}

	public void setAudioChannelId(String audioChannelId) {
		this.audioChannelId = audioChannelId;
	}

	@Column(name = "COMBINE_AUDIO_UUID")
	public String getCombineAudioUuid() {
		return combineAudioUuid;
	}

	public void setCombineAudioUuid(String combineAudioUuid) {
		this.combineAudioUuid = combineAudioUuid;
	}

	@ManyToOne
	@JoinColumn(name = "GROUP_ID")
	public DeviceGroupPO getGroup() {
		return group;
	}

	public void setGroup(DeviceGroupPO group) {
		this.group = group;
	}

	@OneToMany(mappedBy = "record", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<PublishStreamPO> getPublishStreams() {
		return publishStreams;
	}

	public void setPublishStreams(Set<PublishStreamPO> publishStreams) {
		this.publishStreams = publishStreams;
	}
	
}
