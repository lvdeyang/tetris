package com.sumavision.tetris.bvc.business.group.demand;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.bvc.command.group.enumeration.ForwardDemandBusinessType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 指挥/会议中的媒体转发<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月8日 上午11:21:02
 */
@Entity
@Table(name="TETRIS_BVC_GROUP_DEMAND")
public class GroupDemandPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 转发的业务类型 */
	private ForwardDemandBusinessType demandType;
	
	/** 关联议程id */
	private Long agendaId;
	
	/** 目的成员名 */
	private String dstName;
	
	/** 目的，设备组成员id */
	private Long dstMemberId;
	
	/** 目的，设备组成员id。预留-1表示不是用户 */
	private Long dstUserId = -1L;
	
	/** 目的号码 */
	private String dstCode;

	/** 关联指挥/会议 */
	private String businessId;
	
	/** 临时建立的源成员id */
	private Long srcMemberId;
	
	/** 源的用户名/设备名/文件名 */
	private String srcName;
	
	/** 源号码 */
	private String srcCode;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "DEMAND_TYPE")
	public ForwardDemandBusinessType getDemandType() {
		return demandType;
	}

	public void setDemandType(ForwardDemandBusinessType demandType) {
		this.demandType = demandType;
	}

	public Long getAgendaId() {
		return agendaId;
	}

	public void setAgendaId(Long agendaId) {
		this.agendaId = agendaId;
	}

	public String getDstName() {
		return dstName;
	}

	public void setDstName(String dstName) {
		this.dstName = dstName;
	}

	@Column(name = "DST_MEMBER_ID")
	public Long getDstMemberId() {
		return dstMemberId;
	}

	public void setDstMemberId(Long dstMemberId) {
		this.dstMemberId = dstMemberId;
	}

	public Long getDstUserId() {
		return dstUserId;
	}

	public void setDstUserId(Long dstUserId) {
		this.dstUserId = dstUserId;
	}

	public String getDstCode() {
		return dstCode;
	}

	public void setDstCode(String dstCode) {
		this.dstCode = dstCode;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public Long getSrcMemberId() {
		return srcMemberId;
	}

	public void setSrcMemberId(Long srcMemberId) {
		this.srcMemberId = srcMemberId;
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

	public GroupDemandPO() {}
	
	//建立设备转发使用
	/*public GroupDemandPO(
			OriginType srcOriginType,
			OriginType dstOriginType,
			ForwardDemandBusinessType demandType,
			ForwardDemandStatus executeStatus,
			ForwardDstType forwardDstType,
			Long dstMemberId,
			String dstUserName,//Long srcMemberId,
			String dstCode,
			String srcCode,
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
		
		this.srcOriginType = srcOriginType;
		this.dstOriginType = dstOriginType;
		this.demandType = demandType;
		this.forwardDstType = forwardDstType;
		this.dstMemberId = dstMemberId;
		this.dstCode = dstCode;
		this.srcCode = srcCode;
//		this.srcMemberId = srcMemberId;
		
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

		this.dstUserName = dstUserName;
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
	}
	
	//建立文件转发使用
	public GroupDemandPO(
			ForwardDemandBusinessType demandType,
			ForwardDemandStatus executeStatus,
			ForwardDstType forwardDstType,
			Long dstMemberId,
			String dstUserName,
			String dstCode,
			String resourceId,
			String resourceName,
			String playUrl,
			Long userId,
			Long avTplId,
			Long gearId,
			DstDeviceType dstDeviceType,
			LiveType type,
			Long osdId,
			String osdUsername) throws Exception{
		
		this.demandType = demandType;
		this.forwardDstType = forwardDstType;
		this.dstMemberId = dstMemberId;
		this.dstUserName = dstUserName;
		this.dstCode = dstCode;
//		this.srcMemberId = srcMemberId;
		
		this.setUpdateTime(new Date());
		this.resourceId = resourceId;
		this.resourceName = resourceName;
		this.playUrl = playUrl;			
		
		this.userId = userId;
		this.avTplId = avTplId;
		this.gearId = gearId;
		this.dstDeviceType = dstDeviceType;
		this.type = type;
		this.osdId = osdId;
		this.osdUsername = osdUsername;
	}

	public GroupDemandPO setDstPlayer(CommandGroupUserPlayerPO player) throws Exception{
		
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
	}
	
	public GroupDemandPO clearDst() {
		
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
	
}
