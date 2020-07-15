package com.sumavision.bvc.command.group.record;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.bo.SourceBO;
import com.sumavision.tetris.bvc.business.group.GroupMemberPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 录制的一个片段
 * @author zsy
 * @date 2019年11月20日 下午1:06:00
 */
@Entity
@Table(name="BVC_COMMAND_GROUP_RECORD_FRAGMENT")
public class CommandGroupRecordFragmentPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 录制状态 */
	private boolean run = true;
		
//	/** 转发的业务类型 */
//	ForwardBusinessType forwardBusinessType;
//	
//	/** 转发目的类型：角色/用户/设备 */
//	private ForwardDstType forwardDstType;
	
	/** 片段信息，源的用户名或转发源名称 */
	private String info;
	
	/** 目的为角色时，角色id */
	private int locationIndex;
	
	/** 业务类型，与播放器保持一致（目前无用） */
	private PlayerBusinessType playerBusinessType = PlayerBusinessType.NONE;
	
	/** 开始时间 */
	private Date startTime;
	
	/** 停止时间 */
	private Date endTime;
	
	/** 文件预览地址 */
	private String previewUrl;
	
	/** 源，设备组成员id。-1表示不是成员【录制成员时作为唯一标识】 */
	private Long srcMemberId = -1L;

	/** 关联录制 */
	CommandGroupRecordPO record;
	
	/** 文件存储位置 */
	private String storeLayerId;

	/************
	 ***视频源****
	 ************/
	
	/** 当视频源是设备时存设备id【录制转发点播时，作为唯一标识】 */
	private String videoBundleId;
	
	/** 当视频源是设备时存设备名称 */
	private String videoBundleName;
	
	/** 当视频源是设备时存设备类型 */
	private String videoBundleType;
	
	/** 当视频源是设备时存设备接入层id */
	private String videoLayerId;
	
	/** 当视频源是设备时存通道id */
	private String videoChannelId;
	
	/** 当视频源是设备时存通道类型 */
	private String videoBaseType;
	
	/** 当视频源是设备时存通道名称 */
	private String videoChannelName;
	
	/************
	 ***音频源****
	 ************/
	
	/** 当音频源是设备时存设备id */
	private String audioBundleId;
	
	/** 当音频源是设备时存设备名称 */
	private String audioBundleName;
	
	/** 当音频源是设备时存设备类型 */
	private String audioBundleType;
	
	/** 当音频源是设备时存设备接入层 */
	private String audioLayerId;
	
	/** 当音频源是设备时存通道id */
	private String audioChannelId;
	
	/** 当音频源是设备时存通道类型 */
	private String audioBaseType;
	
	/** 当音频源是设备时存通道名称 */
	private String audioChannelName;
	
	//后续：一对多关联（locationIndex, startTime endTime）PO
	
	@Column(name = "RUN")
	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	@Column(name = "INFO")
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	@Column(name = "LOCATION_INDEX")
	public int getLocationIndex() {
		return locationIndex;
	}

	public void setLocationIndex(int locationIndex) {
		this.locationIndex = locationIndex;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "PLAYER_BUSINESS_TYPE")
	public PlayerBusinessType getPlayerBusinessType() {
		return playerBusinessType;
	}

	public void setPlayerBusinessType(PlayerBusinessType playerBusinessType) {
		this.playerBusinessType = playerBusinessType;
	}

	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "PREVIEW_URL")
	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	@ManyToOne
	@JoinColumn(name = "RECORD_ID")
	public CommandGroupRecordPO getRecord() {
		return record;
	}

	public void setRecord(CommandGroupRecordPO record) {
		this.record = record;
	}

	@Column(name = "STORE_LAYER_ID")
	public String getStoreLayerId() {
		return storeLayerId;
	}

	public void setStoreLayerId(String storeLayerId) {
		this.storeLayerId = storeLayerId;
	}

	@Column(name = "VIDEO_BUNDLE_ID")
	public String getVideoBundleId() {
		return videoBundleId;
	}

	public void setVideoBundleId(String videoBundleId) {
		this.videoBundleId = videoBundleId;
	}

	@Column(name = "VIDEO_BUNDLE_NAME")
	public String getVideoBundleName() {
		return videoBundleName;
	}

	public void setVideoBundleName(String videoBundleName) {
		this.videoBundleName = videoBundleName;
	}

	@Column(name = "VIDEO_BUNDLE_TYPE")
	public String getVideoBundleType() {
		return videoBundleType;
	}

	public void setVideoBundleType(String videoBundleType) {
		this.videoBundleType = videoBundleType;
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

	@Column(name = "VIDEO_BASE_TYPE")
	public String getVideoBaseType() {
		return videoBaseType;
	}

	public void setVideoBaseType(String videoBaseType) {
		this.videoBaseType = videoBaseType;
	}

	@Column(name = "VIDEO_CHANNEL_NAME")
	public String getVideoChannelName() {
		return videoChannelName;
	}

	public void setVideoChannelName(String videoChannelName) {
		this.videoChannelName = videoChannelName;
	}

	@Column(name = "AUDIO_BUNDLE_ID")
	public String getAudioBundleId() {
		return audioBundleId;
	}

	public void setAudioBundleId(String audioBundleId) {
		this.audioBundleId = audioBundleId;
	}

	@Column(name = "AUDIO_BUNDLE_NAME")
	public String getAudioBundleName() {
		return audioBundleName;
	}

	public void setAudioBundleName(String audioBundleName) {
		this.audioBundleName = audioBundleName;
	}

	@Column(name = "AUDIO_BUNDLE_TYPE")
	public String getAudioBundleType() {
		return audioBundleType;
	}

	public void setAudioBundleType(String audioBundleType) {
		this.audioBundleType = audioBundleType;
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

	@Column(name = "AUDIO_BASE_TYPE")
	public String getAudioBaseType() {
		return audioBaseType;
	}

	public void setAudioBaseType(String audioBaseType) {
		this.audioBaseType = audioBaseType;
	}

	@Column(name = "AUDIO_CHANNEL_NAME")
	public String getAudioChannelName() {
		return audioChannelName;
	}

	public void setAudioChannelName(String audioChannelName) {
		this.audioChannelName = audioChannelName;
	}

	@Column(name = "SRC_MEMBER_ID")
	public Long getSrcMemberId() {
		return srcMemberId;
	}

	public void setSrcMemberId(Long srcMemberId) {
		this.srcMemberId = srcMemberId;
	}
	
	/** 无法设置info信息，需要额外设置 */
	public CommandGroupRecordFragmentPO setByMemberSource(GroupMemberPO member, SourceBO source, Date date) throws Exception{
		ChannelSchemeDTO videoSource = source.getVideoSource();
		BundlePO videoBundle = source.getVideoBundle();
		ChannelSchemeDTO audioSource = source.getAudioSource();
		if(audioSource == null) audioSource = new ChannelSchemeDTO();
		BundlePO audioBundle = source.getAudioBundle();
		if(audioBundle == null) audioBundle = new BundlePO();
		this.edit(
				0,
				null,
				date,
				null,//Date endTime,
				null,//String info,
				member.getId(),
				videoSource.getBundleId(),
				videoBundle.getBundleName(),
				videoBundle.getBundleType(),
				videoBundle.getAccessNodeUid(),
				videoSource.getChannelId(),
				videoSource.getBaseType(),
				audioBundle.getBundleId(),
				audioBundle.getBundleName(),
				audioBundle.getBundleType(),
				audioBundle.getAccessNodeUid(),
				audioSource.getChannelId(),
				audioSource.getBaseType()
				);
		return this;
	}
	
	/** 无法设置info信息，需要额外设置 */
	public CommandGroupRecordFragmentPO setByForward(CommandGroupForwardPO forward, CommandGroupUserPlayerPO player, Date date) throws Exception{
		this.edit(
				player.getLocationIndex(),
				player.getPlayerBusinessType(),
				date,
				null,//Date endTime,
				null,//String info,
				forward.getSrcMemberId(),
				forward.getVideoBundleId(),
				forward.getVideoBundleName(),
				forward.getVideoBundleType(),
				forward.getVideoLayerId(),
				forward.getVideoChannelId(),
				forward.getVideoBaseType(),
				forward.getAudioBundleId(),
				forward.getAudioBundleName(),
				forward.getAudioBundleType(),
				forward.getAudioLayerId(),
				forward.getAudioChannelId(),
				forward.getAudioBaseType()
				);
		return this;
	}
	
	public CommandGroupRecordFragmentPO setByDemand(CommandGroupForwardDemandPO demand, CommandGroupUserPlayerPO player, Date date) throws Exception{
		this.edit(
				player.getLocationIndex(),
				player.getPlayerBusinessType(),
				date,
				null,//Date endTime,
				demand.getVideoBundleName(),//String info,
				null,//Long srcMemberId,
				demand.getVideoBundleId(),
				demand.getVideoBundleName(),
				demand.getVideoBundleType(),
				demand.getVideoLayerId(),
				demand.getVideoChannelId(),
				demand.getVideoBaseType(),
				demand.getAudioBundleId(),
				demand.getAudioBundleName(),
				demand.getAudioBundleType(),
				demand.getAudioLayerId(),
				demand.getAudioChannelId(),
				demand.getAudioBaseType()
				);
		return this;
	}
	
	public CommandGroupRecordFragmentPO() {}
	
	public CommandGroupRecordFragmentPO edit(
			int locationIndex,
			PlayerBusinessType playerBusinessType,
			Date startTime,
			Date endTime,
			String info,
			Long srcMemberId,
			String videoBundleId,
			String videoBundleName,
			String videoBundleType,
			String videoLayerId,
			String videoChannelId,
			String videoBaseType,
			String audioBundleId,
			String audioBundleName,
			String audioBundleType,
			String audioLayerId,
			String audioChannelId,
			String audioBaseType) throws Exception{

		this.info = info;
		this.srcMemberId = srcMemberId;
		this.locationIndex = locationIndex;
		this.playerBusinessType = playerBusinessType;
		this.startTime = startTime;
		this.endTime = endTime;		
		this.setUpdateTime(startTime);
		
		if(videoBundleId != null){
			this.videoBundleId = videoBundleId;
			this.videoBundleName = videoBundleName;
			this.videoBundleType = videoBundleType;
			this.videoLayerId = videoLayerId;
			this.videoChannelId = videoChannelId;
			this.videoBaseType = videoBaseType;
			this.videoChannelName = ChannelType.transChannelName(videoChannelId);
		}
		
		if(audioBundleId != null){
			this.audioBundleId = audioBundleId;
			this.audioBundleName = audioBundleName;
			this.audioBundleType = audioBundleType;
			this.audioLayerId = audioLayerId;
			this.audioChannelId = audioChannelId;
			this.audioBaseType = audioBaseType;
			this.audioChannelName = ChannelType.transChannelName(audioChannelId);
		}
		
		return this;
	}
	
}
