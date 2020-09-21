package com.sumavision.tetris.bvc.business.forward;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.bvc.device.group.enumeration.ForwardDstType;
import com.sumavision.bvc.device.group.enumeration.ForwardSourceType;
import com.sumavision.bvc.device.monitor.live.DstDeviceType;
import com.sumavision.bvc.system.po.TplContentPO;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.ExecuteStatus;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.group.TransmissionMode;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardType;
import com.sumavision.tetris.bvc.model.agenda.AgendaSourceType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 各业务的转发源<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月8日 上午11:19:48
 */
@Entity
@Table(name = "TETRIS_BVC_COMMON_FORWARD")
public class CommonForwardPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 音/视频 */
	private AgendaForwardType type;
	
	/** 视频执行状态 */
	private ExecuteStatus videoStatus = ExecuteStatus.DONE;
	
	/** 音频执行状态 */
	private ExecuteStatus audioStatus = ExecuteStatus.DONE;
	
	/** 业务类型 */
	private BusinessInfoType businessInfoType;

	/** 关联业务 */
	/** 业务id，
	 * BASIC_COMMAND等指挥业务为“指挥id-观看对象userId”，
	 * COMMAND_FORWARD_xxx等指挥转发时为“指挥id-转发PO的id”，
	 * USER_CALL和USER_VOICE时为UserLiveCallPO的id
	 * PLAY_USER/PLAY_DEVICE/PLAY_FILE等点播任务时为VodPO的id
	 */
	private String businessId;

	/************
	 ***视频源****
	 ************/
	
	/** 转发类型，枚举类型：合屏【|通道】 【|混音】【|通道】*/
	private AgendaSourceType videoSourceType;
	
	/** 单播/组播 */
	private TransmissionMode videoTransmissionMode = TransmissionMode.UNICAST;

	/** 组播地址 */
	private String videoMultiAddr;

	/** 源组播地址 */
	private String videoMultiSrcAddr;
	
//	private ForwardDstType forwardDstType;

	/** 用户名/设备名/文件名 */
	private String srcName;
	
	/** 用户号码/设备号码 */
	private String srcCode;
	
	private OriginType srcOrigin = OriginType.INNER;
	
	private Long srcMemberId;
	
	/** 用户userId/设备bundleId/文件id/合屏uuid */
	private String srcId;
	
	/** 源设备名称 */
	private String srcBundleName;
	
	/** 源设备类型 */
	private String srcBundleType;
	
	/** 源设备类型 */
	private String srcBundleId;
	
	/** 源设备接入层id */
	private String srcLayerId;
	
	/** 源通道id */
	private String srcChannelId;
	
	/** 源存通道类型 */
	private String srcBaseType;
	
	/** 源通道名称 */
	private String srcChannelName;

	/************
	 ***音频源****
	 ************/
	
	/** 转发类型，枚举类型：合屏【|通道】 【|混音】【|通道】*/
	private AgendaSourceType audioSourceType;
	
	/** 音频单播/组播 */
	private TransmissionMode audioTransmissionMode = TransmissionMode.UNICAST;
	
	/** 音频组播地址 */
	private String audioMultiAddr;

	/** 音频源组播地址 */
	private String audioMultiSrcAddr;
	
	/** 用户名/设备名/文件名 */
	private String srcAudioName;
	
	/** 用户号码/设备号码 */
	private String srcAudioCode;
	
	private OriginType srcAudioOrigin = OriginType.INNER;
	
	private Long srcAudioMemberId;
	
	/** 用户userId/设备bundleId/文件id/合屏uuid */
	private String srcAudioId;
	
	/** 源设备名称 */
	private String srcAudioBundleName;
	
	/** 源设备类型 */
	private String srcAudioBundleType;
	
	/** 源设备类型 */
	private String srcAudioBundleId;
	
	/** 源设备接入层id */
	private String srcAudioLayerId;
	
	/** 源通道id */
	private String srcAudioChannelId;
	
	/** 源存通道类型 */
	private String srcAudioBaseType;
	
	/** 源通道名称 */
	private String srcAudioChannelName;
	
	/** osd id */
	private Long osdId;
	
	/** osd 名称 */
	private String osdName;
	
	/************
	 ***目的，具体信息由 PageTaskPO 记录 ***
	 ************/
	
	private OriginType dstOrigin = OriginType.INNER;
	
	private Long dstMemberId;
	
	/** 分屏LayoutPosition的id */
	private Long positionId;
	
	/** 目标设备类型 */
	private DstDeviceType dstDeviceType;
	
	@Enumerated(value = EnumType.STRING)
	public TransmissionMode getVideoTransmissionMode() {
		return videoTransmissionMode;
	}

	public void setVideoTransmissionMode(TransmissionMode videoTransmissionMode) {
		this.videoTransmissionMode = videoTransmissionMode;
	}

	public String getVideoMultiAddr() {
		return videoMultiAddr;
	}

	public void setVideoMultiAddr(String videoMultiAddr) {
		this.videoMultiAddr = videoMultiAddr;
	}
	
	public String getVideoMultiSrcAddr() {
		return videoMultiSrcAddr;
	}

	public void setVideoMultiSrcAddr(String videoMultiSrcAddr) {
		this.videoMultiSrcAddr = videoMultiSrcAddr;
	}

	@Enumerated(value = EnumType.STRING)
	public TransmissionMode getAudioTransmissionMode() {
		return audioTransmissionMode;
	}

	public void setAudioTransmissionMode(TransmissionMode audioTransmissionMode) {
		this.audioTransmissionMode = audioTransmissionMode;
	}

	public String getAudioMultiAddr() {
		return audioMultiAddr;
	}

	public void setAudioMultiAddr(String audioMultiAddr) {
		this.audioMultiAddr = audioMultiAddr;
	}

	public String getAudioMultiSrcAddr() {
		return audioMultiSrcAddr;
	}

	public void setAudioMultiSrcAddr(String audioMultiSrcAddr) {
		this.audioMultiSrcAddr = audioMultiSrcAddr;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "DST_DEVICE_TYPE")
	public DstDeviceType getDstDeviceType() {
		return dstDeviceType;
	}

	public void setDstDeviceType(DstDeviceType dstDeviceType) {
		this.dstDeviceType = dstDeviceType;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public AgendaForwardType getType() {
		return type;
	}

	public void setType(AgendaForwardType type) {
		this.type = type;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "VIDEO_STATUS")
	public ExecuteStatus getVideoStatus() {
		return videoStatus;
	}

	public void setVideoStatus(ExecuteStatus videoStatus) {
		this.videoStatus = videoStatus;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "AUDIO_STATUS")
	public ExecuteStatus getAudioStatus() {
		return audioStatus;
	}

	public void setAudioStatus(ExecuteStatus audioStatus) {
		this.audioStatus = audioStatus;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public Long getDstMemberId() {
		return dstMemberId;
	}

	public void setDstMemberId(Long dstMemberId) {
		this.dstMemberId = dstMemberId;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "VIDEO_SOURCE_TYPE")
	public AgendaSourceType getVideoSourceType() {
		return videoSourceType;
	}

	public void setVideoSourceType(AgendaSourceType videoSourceType) {
		this.videoSourceType = videoSourceType;
	}

	public String getSrcName() {
		return srcName;
	}

	public void setSrcName(String srcName) {
		this.srcName = srcName;
	}

	public String getSrcCode() {
		return srcCode;
	}

	public void setSrcCode(String srcCode) {
		this.srcCode = srcCode;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "SRC_ORIGIN")
	public OriginType getSrcOrigin() {
		return srcOrigin;
	}

	public void setSrcOrigin(OriginType srcOrigin) {
		this.srcOrigin = srcOrigin;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "DST_ORIGIN")
	public OriginType getDstOrigin() {
		return dstOrigin;
	}

	public void setDstOrigin(OriginType dstOrigin) {
		this.dstOrigin = dstOrigin;
	}

	public String getSrcId() {
		return srcId;
	}

	public Long getSrcMemberId() {
		return srcMemberId;
	}

	public void setSrcMemberId(Long srcMemberId) {
		this.srcMemberId = srcMemberId;
	}

	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}

	public String getSrcBundleName() {
		return srcBundleName;
	}

	public void setSrcBundleName(String srcBundleName) {
		this.srcBundleName = srcBundleName;
	}

	public String getSrcBundleType() {
		return srcBundleType;
	}

	public void setSrcBundleType(String srcBundleType) {
		this.srcBundleType = srcBundleType;
	}

	public String getSrcBundleId() {
		return srcBundleId;
	}

	public void setSrcBundleId(String srcBundleId) {
		this.srcBundleId = srcBundleId;
	}

	public String getSrcLayerId() {
		return srcLayerId;
	}

	public void setSrcLayerId(String srcLayerId) {
		this.srcLayerId = srcLayerId;
	}

	public String getSrcChannelId() {
		return srcChannelId;
	}

	public void setSrcChannelId(String srcChannelId) {
		this.srcChannelId = srcChannelId;
	}

	public String getSrcBaseType() {
		return srcBaseType;
	}

	public void setSrcBaseType(String srcBaseType) {
		this.srcBaseType = srcBaseType;
	}

	public String getSrcChannelName() {
		return srcChannelName;
	}

	public void setSrcChannelName(String srcChannelName) {
		this.srcChannelName = srcChannelName;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "BUSINESS_INFO_TYPE")
	public BusinessInfoType getBusinessInfoType() {
		return businessInfoType;
	}

	public void setBusinessInfoType(BusinessInfoType businessInfoType) {
		this.businessInfoType = businessInfoType;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "AUDIO_SOURCE_TYPE")
	public AgendaSourceType getAudioSourceType() {
		return audioSourceType;
	}

	public void setAudioSourceType(AgendaSourceType audioSourceType) {
		this.audioSourceType = audioSourceType;
	}

//	public String getCombineAudioUuid() {
//		return combineAudioUuid;
//	}
//
//	public void setCombineAudioUuid(String combineAudioUuid) {
//		this.combineAudioUuid = combineAudioUuid;
//	}

	public String getSrcAudioName() {
		return srcAudioName;
	}

	public void setSrcAudioName(String srcAudioName) {
		this.srcAudioName = srcAudioName;
	}

	public String getSrcAudioCode() {
		return srcAudioCode;
	}

	public void setSrcAudioCode(String srcAudioCode) {
		this.srcAudioCode = srcAudioCode;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "SRC_AUDIO_ORIGIN")
	public OriginType getSrcAudioOrigin() {
		return srcAudioOrigin;
	}

	public void setSrcAudioOrigin(OriginType srcAudioOrigin) {
		this.srcAudioOrigin = srcAudioOrigin;
	}

	public Long getSrcAudioMemberId() {
		return srcAudioMemberId;
	}

	public void setSrcAudioMemberId(Long srcAudioMemberId) {
		this.srcAudioMemberId = srcAudioMemberId;
	}

	public String getSrcAudioId() {
		return srcAudioId;
	}

	public void setSrcAudioId(String srcAudioId) {
		this.srcAudioId = srcAudioId;
	}

	public String getSrcAudioBundleName() {
		return srcAudioBundleName;
	}

	public void setSrcAudioBundleName(String srcAudioBundleName) {
		this.srcAudioBundleName = srcAudioBundleName;
	}

	public String getSrcAudioBundleType() {
		return srcAudioBundleType;
	}

	public void setSrcAudioBundleType(String srcAudioBundleType) {
		this.srcAudioBundleType = srcAudioBundleType;
	}

	public String getSrcAudioBundleId() {
		return srcAudioBundleId;
	}

	public void setSrcAudioBundleId(String srcAudioBundleId) {
		this.srcAudioBundleId = srcAudioBundleId;
	}

	public String getSrcAudioLayerId() {
		return srcAudioLayerId;
	}

	public void setSrcAudioLayerId(String srcAudioLayerId) {
		this.srcAudioLayerId = srcAudioLayerId;
	}

	public String getSrcAudioChannelId() {
		return srcAudioChannelId;
	}

	public void setSrcAudioChannelId(String srcAudioChannelId) {
		this.srcAudioChannelId = srcAudioChannelId;
	}

	public String getSrcAudioBaseType() {
		return srcAudioBaseType;
	}

	public void setSrcAudioBaseType(String srcAudioBaseType) {
		this.srcAudioBaseType = srcAudioBaseType;
	}

	public String getSrcAudioChannelName() {
		return srcAudioChannelName;
	}

	public void setSrcAudioChannelName(String srcAudioChannelName) {
		this.srcAudioChannelName = srcAudioChannelName;
	}

	public Long getOsdId() {
		return osdId;
	}

	public void setOsdId(Long osdId) {
		this.osdId = osdId;
	}

	public String getOsdName() {
		return osdName;
	}

	public void setOsdName(String osdName) {
		this.osdName = osdName;
	}

	public CommonForwardPO() {}
	
	/*public CommonForwardPO(
			ForwardBusinessType forwardBusinessType,
			ExecuteStatus executeStatus,
			ForwardDstType forwardDstType,
			Long dstMemberId,
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
			String audioBaseType,
			String dstVideoBundleId,
			String dstVideoBundleName,
			String dstVideoBundleType,
			String dstVideoLayerId,
			String dstVideoChannelId,
			String dstVideoBaseType,
			String dstAudioBundleId,
			String dstAudioBundleName,
			String dstAudioBundleType,
			String dstAudioLayerId,
			String dstAudioChannelId,
			String dstAudioBaseType,
			Long userId,
			Long avTplId,
			Long gearId,
			DstDeviceType dstDeviceType,
			LiveType type,
			Long osdId,
			String osdUsername) throws Exception{
		
		this.forwardBusinessType = forwardBusinessType;
		this.executeStatus = executeStatus;
		this.forwardDstType = forwardDstType;
		this.dstMemberId = dstMemberId;
		this.srcMemberId = srcMemberId;
		
		this.setUpdateTime(new Date());
		this.videoBundleId = videoBundleId;
		this.videoBundleName = videoBundleName;
		this.videoBundleType = videoBundleType;
		this.videoLayerId = videoLayerId;
		this.videoChannelId = videoChannelId;
		this.videoBaseType = videoBaseType;
		this.videoChannelName = ChannelType.transChannelName(videoChannelId);
		
		if(audioBundleId != null){
			this.audioBundleId = audioBundleId;
			this.audioBundleName = audioBundleName;
			this.audioBundleType = audioBundleType;
			this.audioLayerId = audioLayerId;
			this.audioChannelId = audioChannelId;
			this.audioBaseType = audioBaseType;
			this.audioChannelName = ChannelType.transChannelName(audioChannelId);
		}
		
		this.dstVideoBundleId = dstVideoBundleId;
		this.dstVideoBundleName = dstVideoBundleName;
		this.dstVideoBundleType = dstVideoBundleType;
		this.dstVideoLayerId = dstVideoLayerId;
		this.dstVideoChannelId = dstVideoChannelId;
		this.dstVideoBaseType = dstVideoBaseType;
		this.dstVideoChannelName = ChannelType.transChannelName(dstVideoChannelId);
		
		if(dstAudioBundleId != null){
			this.dstAudioBundleId = dstAudioBundleId;
			this.dstAudioBundleName = dstAudioBundleName;
			this.dstAudioBundleType = dstAudioBundleType;
			this.dstAudioLayerId = dstAudioLayerId;
			this.dstAudioChannelId = dstAudioChannelId;
			this.dstAudioBaseType = dstAudioBaseType;
			this.dstAudioChannelName = ChannelType.transChannelName(dstAudioChannelId);
		}
		
		this.userId = userId;
		this.avTplId = avTplId;
		this.gearId = gearId;
		this.dstDeviceType = dstDeviceType;
		this.type = type;
		this.osdId = osdId;
		this.osdUsername = osdUsername;
	}*/
	
	/*public CommonForwardPO setDstPlayer(CommandGroupUserPlayerPO player) throws Exception{
		
		this.setUpdateTime(new Date());
		
		this.dstVideoBundleId = player.getBundleId();
		this.dstVideoBundleName = player.getBundleName();
		this.dstVideoBundleType = player.getBundleType();
		this.dstVideoLayerId = player.getLayerId();
		this.dstVideoChannelId = player.getVideoChannelId();
		this.dstVideoBaseType = "VenusVideoOut";
		this.dstVideoChannelName = ChannelType.transChannelName(player.getVideoChannelId());
		
		this.dstAudioBundleId = player.getBundleId();
		this.dstAudioBundleName = player.getBundleName();
		this.dstAudioBundleType = player.getBundleType();
		this.dstAudioLayerId = player.getLayerId();
		this.dstAudioChannelId = player.getAudioChannelId();
		this.dstAudioBaseType = "VenusAudioOut";
		this.dstAudioChannelName = ChannelType.transChannelName(player.getAudioChannelId());
		
		return this;
	}*/
	
	/*public CommonForwardPO clearDst(){
		
		this.setUpdateTime(new Date());
		
		this.dstVideoBundleId = null;
		this.dstVideoBundleName = null;
		this.dstVideoBundleType = null;
		this.dstVideoLayerId = null;
		this.dstVideoChannelId = null;
		this.dstVideoBaseType = null;
		this.dstVideoChannelName = null;
		
		this.dstAudioBundleId = null;
		this.dstAudioBundleName = null;
		this.dstAudioBundleType = null;
		this.dstAudioLayerId = null;
		this.dstAudioChannelId = null;
		this.dstAudioBaseType = null;
		this.dstAudioChannelName = null;
		
		return this;
	}*/
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((srcId == null) ? 0 : srcId.hashCode());
		result = prime * result + ((dstMemberId == null) ? 0 : dstMemberId.hashCode());
		result = prime * result + ((videoSourceType == null) ? 0 : videoSourceType.hashCode());
		result = prime * result + ((audioSourceType == null) ? 0 : audioSourceType.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		CommonForwardPO forward = (CommonForwardPO) obj;
		if(type.equals(forward.getType())
				&& srcId.equals(forward.getSrcId())
				&& (videoSourceType == null || videoSourceType.equals(forward.getVideoSourceType()))
//				&& (combineVideoUuid == null || combineVideoUuid.equals(forward.getCombineVideoUuid()))
				&& (audioSourceType == null || audioSourceType.equals(forward.getAudioSourceType()))
//				&& (combineAudioUuid == null || combineAudioUuid.equals(forward.getCombineAudioUuid()))
				&& (srcMemberId == null || srcMemberId.equals(forward.getSrcMemberId()))
				&& (dstMemberId == null || dstMemberId.equals(forward.getDstMemberId()))){
			return true;
		}
		return false;
	}
	
}
