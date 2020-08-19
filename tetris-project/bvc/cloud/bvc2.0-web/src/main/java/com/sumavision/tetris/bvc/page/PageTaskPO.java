package com.sumavision.tetris.bvc.page;

import java.util.Comparator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.ExecuteStatus;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.group.TransmissionMode;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionPO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaSourceType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 解码播放任务，组成列表进行分页<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年5月27日 下午2:59:23
 */
@Entity
@Table(name="TETRIS_BVC_PAGE_TASK")
public class PageTaskPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	/** 排序索引 */
	private int taskIndex;
	
	/** 实际界面上的位置索引，qt界面播放器使用 */
	private Integer locationIndex;
	
	/** 业务类型 */
	private BusinessInfoType businessType = BusinessInfoType.NONE;
	
	/** 视频执行状态 */
	private ExecuteStatus videoStatus = ExecuteStatus.DONE;
	
	/** 音频执行状态 */
	private ExecuteStatus audioStatus = ExecuteStatus.DONE;
	
	/** 关联业务 */
	/** 业务id，
	 * BASIC_COMMAND等指挥业务为“指挥id-观看对象userId”，
	 * COMMAND_FORWARD_xxx等指挥转发时为“指挥id-转发PO的id”，
	 * USER_CALL和USER_VOICE时为UserLiveCallPO的id
	 * PLAY_USER/PLAY_DEVICE/PLAY_FILE等点播任务时为VodPO的id
	 */
	private String businessId;
	
	private String forwardUuid;

	/** 业务名称，用于文字显示，对应播放器信息中的businessInfo */
	private String businessName;
	
	/************
	 ***视频源****
	 ************/
	
	/** 转发类型，枚举类型：合屏【|通道】 【|混音】【|通道】*/
	private AgendaSourceType videoSourceType;
	
	/** 单播/组播 */
	private TransmissionMode videoTransmissionMode = TransmissionMode.UNICAST;

	/** 组播地址 */
	private String videoMultiAddr;
	
	/** 转发类型为合屏【|混音】：存合屏【|混音】uuid */
	private String combineVideoUuid;
	
	private String playUrl;
	
	/** 用户userId/设备bundleId/文件id/合屏uuid */
	private String srcVideoId;
	
	private OriginType srcVideoOrigin = OriginType.INNER;
	
	/** 用户名/设备名/文件名 */
	private String srcVideoName;
	
	/** 用户号码/设备号码 */
	private String srcVideoCode;
	
	/** 源设备名称 */
	private String srcVideoBundleName;
	
	/** 源设备类型 */
	private String srcVideoBundleType;
	
	/** 源设备id */
	private String srcVideoBundleId;
	
	/** 源设备接入层id */
	private String srcVideoLayerId;
	
	/** 源通道id */
	private String srcVideoChannelId;
	
	/** 源存通道类型 */
	private String srcVideoBaseType;
	
	/** 源通道名称 */
	private String srcVideoChannelName;
	
	/************
	 ***音频源****
	 ************/
	
	/** 转发类型，枚举类型：合屏【|通道】 【|混音】【|通道】*/
	private AgendaSourceType audioSourceType;
	
	/** 音频单播/组播 */
	private TransmissionMode audioTransmissionMode = TransmissionMode.UNICAST;
	
	/** 音频组播地址 */
	private String audioMultiAddr;
	
	/** 转发类型为合屏【|混音】：存合屏【|混音】uuid */
	private String combineAudioUuid;
	
	/** 用户userId/设备bundleId/文件id/合屏uuid */
	private String srcAudioId;
	
	private OriginType srcAudioOrigin = OriginType.INNER;
	
	/** 用户名/设备名/文件名 */
	private String srcAudioName;
	
	/** 用户号码/设备号码 */
	private String srcAudioCode;
	
	/** 源设备名称 */
	private String srcAudioBundleName;
	
	/** 源设备类型 */
	private String srcAudioBundleType;
	
	/** 源设备id */
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
	
	/***********
	 * 目的信息，当前分页不显示该任务时则都为null *
	 **********/
	
	/** 是否正在分页中展示 */
	private boolean showing = false;
	
	//可能还要加一个“目的类型”
	
	/** 目的成员id */
	private Long dstMemberId;
	
	/** TerminalBundleUserPermissionPO的id */
	private String dstId;
	
	/** 目标设备号码 */
	private String dstCode;
	
	/** 目标设备id */
	private String dstBundleId;
	
	/** 目标设备名称 */
	private String dstBundleName;
	
	/** 目标设备类型 */
	private String dstBundleType;
	
	/** 目标设备接入层id */
	private String dstLayerId;
	
	/** 目标视频通道 */
	private String dstVideoChannelId;
	
	/** 目标视频通道类型 */
	private String dstVideoBaseType;
	
	/** 目标视频通道名称 */
	private String dstVideoChannelName;
	
	/** 目标音频通道 */
	private String dstAudioChannelId;
	
	/** 目标音频通道类型 */
	private String dstAudioBaseType;
	
	/** 目标音频通道名称 */
	private String dstAudioChannelName;
	
	/** 关联分页信息 */
	private PageInfoPO pageInfo;
	
	@Column(name = "TASK_INDEX")
	public int getTaskIndex() {
		return taskIndex;
	}

	public void setTaskIndex(int taskIndex) {
		this.taskIndex = taskIndex;
	}

	public Integer getLocationIndex() {
		return locationIndex;
	}

	public void setLocationIndex(Integer locationIndex) {
		this.locationIndex = locationIndex;
	}

	@Column(name = "BUSINESS_TYPE")
	@Enumerated(value = EnumType.STRING)
	public BusinessInfoType getBusinessInfoType() {
		return businessType;
	}

	public void setBusinessInfoType(BusinessInfoType businessType) {
		this.businessType = businessType;
	}

	@Column(name = "BUSINESS_NAME")
	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	
	@Column(name = "VIDEO_STATUS")
	@Enumerated(value = EnumType.STRING)
	public ExecuteStatus getVideoStatus() {
		return videoStatus;
	}

	public void setVideoStatus(ExecuteStatus videoStatus) {
		this.videoStatus = videoStatus;
	}

	@Column(name = "AUDIO_STATUS")
	@Enumerated(value = EnumType.STRING)
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

	public String getForwardUuid() {
		return forwardUuid;
	}

	public void setForwardUuid(String forwardUuid) {
		this.forwardUuid = forwardUuid;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "VIDEO_SOURCE_TYPE")
	public AgendaSourceType getVideoSourceType() {
		return videoSourceType;
	}

	public void setVideoSourceType(AgendaSourceType videoSourceType) {
		this.videoSourceType = videoSourceType;
	}
	
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

	public String getCombineVideoUuid() {
		return combineVideoUuid;
	}

	public void setCombineVideoUuid(String combineVideoUuid) {
		this.combineVideoUuid = combineVideoUuid;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	public String getSrcVideoName() {
		return srcVideoName;
	}

	public void setSrcVideoName(String srcVideoName) {
		this.srcVideoName = srcVideoName;
	}

	public String getSrcVideoCode() {
		return srcVideoCode;
	}

	public void setSrcVideoCode(String srcVideoCode) {
		this.srcVideoCode = srcVideoCode;
	}

	@Column(name = "SRC_VIDEO_ORIGIN")
	@Enumerated(value = EnumType.STRING)
	public OriginType getSrcVideoOrigin() {
		return srcVideoOrigin;
	}

	public void setSrcVideoOrigin(OriginType srcVideoOrigin) {
		this.srcVideoOrigin = srcVideoOrigin;
	}

	public String getSrcVideoId() {
		return srcVideoId;
	}

	public void setSrcVideoId(String srcVideoId) {
		this.srcVideoId = srcVideoId;
	}

	public String getSrcVideoBundleName() {
		return srcVideoBundleName;
	}

	public void setSrcVideoBundleName(String srcVideoBundleName) {
		this.srcVideoBundleName = srcVideoBundleName;
	}

	public String getSrcVideoBundleType() {
		return srcVideoBundleType;
	}

	public void setSrcVideoBundleType(String srcVideoBundleType) {
		this.srcVideoBundleType = srcVideoBundleType;
	}

	public String getSrcVideoBundleId() {
		return srcVideoBundleId;
	}

	public void setSrcVideoBundleId(String srcVideoBundleId) {
		this.srcVideoBundleId = srcVideoBundleId;
	}

	public String getSrcVideoLayerId() {
		return srcVideoLayerId;
	}

	public void setSrcVideoLayerId(String srcVideoLayerId) {
		this.srcVideoLayerId = srcVideoLayerId;
	}

	public String getSrcVideoChannelId() {
		return srcVideoChannelId;
	}

	public void setSrcVideoChannelId(String srcVideoChannelId) {
		this.srcVideoChannelId = srcVideoChannelId;
	}

	public String getSrcVideoBaseType() {
		return srcVideoBaseType;
	}

	public void setSrcVideoBaseType(String srcVideoBaseType) {
		this.srcVideoBaseType = srcVideoBaseType;
	}

	public String getSrcVideoChannelName() {
		return srcVideoChannelName;
	}

	public void setSrcVideoChannelName(String srcVideoChannelName) {
		this.srcVideoChannelName = srcVideoChannelName;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "AUDIO_SOURCE_TYPE")
	public AgendaSourceType getAudioSourceType() {
		return audioSourceType;
	}

	public void setAudioSourceType(AgendaSourceType audioSourceType) {
		this.audioSourceType = audioSourceType;
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
	
	public String getCombineAudioUuid() {
		return combineAudioUuid;
	}

	public void setCombineAudioUuid(String combineAudioUuid) {
		this.combineAudioUuid = combineAudioUuid;
	}

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

	@Column(name = "SRC_AUDIO_ORIGIN")
	@Enumerated(value = EnumType.STRING)
	public OriginType getSrcAudioOrigin() {
		return srcAudioOrigin;
	}

	public void setSrcAudioOrigin(OriginType srcAudioOrigin) {
		this.srcAudioOrigin = srcAudioOrigin;
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

	public boolean isShowing() {
		return showing;
	}

	public void setShowing(boolean showing) {
		this.showing = showing;
	}

	public Long getDstMemberId() {
		return dstMemberId;
	}

	public void setDstMemberId(Long dstMemberId) {
		this.dstMemberId = dstMemberId;
	}

	public String getDstId() {
		return dstId;
	}

	public void setDstId(String dstId) {
		this.dstId = dstId;
	}

	public String getDstCode() {
		return dstCode;
	}

	public void setDstCode(String dstCode) {
		this.dstCode = dstCode;
	}

	public String getDstBundleId() {
		return dstBundleId;
	}

	public void setDstBundleId(String dstBundleId) {
		this.dstBundleId = dstBundleId;
	}

	public String getDstBundleName() {
		return dstBundleName;
	}

	public void setDstBundleName(String dstBundleName) {
		this.dstBundleName = dstBundleName;
	}

	public String getDstBundleType() {
		return dstBundleType;
	}

	public void setDstBundleType(String dstBundleType) {
		this.dstBundleType = dstBundleType;
	}

	public String getDstLayerId() {
		return dstLayerId;
	}

	public void setDstLayerId(String dstLayerId) {
		this.dstLayerId = dstLayerId;
	}

	public String getDstVideoChannelId() {
		return dstVideoChannelId;
	}

	public void setDstVideoChannelId(String dstVideoChannelId) {
		this.dstVideoChannelId = dstVideoChannelId;
	}

	public String getDstVideoBaseType() {
		return dstVideoBaseType;
	}

	public void setDstVideoBaseType(String dstVideoBaseType) {
		this.dstVideoBaseType = dstVideoBaseType;
	}

	public String getDstVideoChannelName() {
		return dstVideoChannelName;
	}

	public void setDstVideoChannelName(String dstVideoChannelName) {
		this.dstVideoChannelName = dstVideoChannelName;
	}

	public String getDstAudioChannelId() {
		return dstAudioChannelId;
	}

	public void setDstAudioChannelId(String dstAudioChannelId) {
		this.dstAudioChannelId = dstAudioChannelId;
	}

	public String getDstAudioBaseType() {
		return dstAudioBaseType;
	}

	public void setDstAudioBaseType(String dstAudioBaseType) {
		this.dstAudioBaseType = dstAudioBaseType;
	}

	public String getDstAudioChannelName() {
		return dstAudioChannelName;
	}

	public void setDstAudioChannelName(String dstAudioChannelName) {
		this.dstAudioChannelName = dstAudioChannelName;
	}

	@ManyToOne
	@JoinColumn(name = "PAGE_INFO_ID")
	public PageInfoPO getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfoPO pageInfo) {
		this.pageInfo = pageInfo;
	}
	
	/**
	 * 将任务的目的置空<br/>
	 * <p>删除任务时应直接删除PO，不应使用此方法</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午3:34:05
	 * @return
	 */
	public PageTaskPO setFree(){
//		this.setBusinessType(BusinessType.NONE);
		this.setLocationIndex(null);
//		this.setBusinessId(null);
		this.setShowing(false);
		return this;
	}
	
	/** 该任务是否是播放文件 */
	/*public boolean playingFile(){
//		BusinessType businessType = this.businessType;
		if(BusinessType.PLAY_FILE.equals(businessType)
				|| BusinessType.PLAY_RECORD.equals(businessType)
				|| BusinessType.PLAY_COMMAND_RECORD.equals(businessType)
				|| BusinessType.COMMAND_FORWARD_FILE.equals(businessType)){
			return true;
		}
		return false;
	}*/
	
	/** 任务与任务是否相同 TODO:完善判断条件，判空等 */
	public boolean equalsTask(PageTaskPO task){
		if(this.getId().equals(task.getId())) return true;
		if(this.businessId.equals(task.getBusinessId())
				&& this.srcVideoId.equals(task.getSrcVideoId())){
			if(this.srcAudioId==null && task.getSrcAudioId()==null) return true;
			if(this.srcAudioId!=null && this.srcAudioId.equals(task.getSrcAudioId())) return true;
			return false;
		}
		return false;
	}
	
	/** 任务与播放器进行的任务是否相同 */
	/*public boolean equalsPlayer(CommandGroupUserPlayerPO player){
		if(this.businessId == null) return false;
		if(this.businessId.equals(player.getBusinessId())
				&& this.businessType.equals(player.getPlayerBusinessType())){
			return true;
		}
		return false;
	}*/
	
	public PageTaskPO setDstByUser(TerminalBundleUserPermissionPO player, BundlePO bundlePO){
//		this.businessType = player.getPlayerBusinessType();//TODO
//		this.locationIndex = player.getLocationIndex();
//		this.businessId = player.getBusinessId();
//		this.businessName = player.getBusinessName();
		this.dstId = player.getId().toString();
		this.dstAudioBaseType = "VenusAudioOut";
		this.dstAudioChannelId = ChannelType.AUDIODECODE1.getChannelId();
		this.dstAudioChannelName = ChannelType.AUDIODECODE1.getName();
		this.dstBundleId = player.getBundleId();
		this.dstBundleName = bundlePO.getBundleName();
		this.dstBundleType = bundlePO.getBundleType();
		this.dstLayerId = bundlePO.getAccessNodeUid();
		this.dstVideoBaseType = "VenusVideoOut";
		this.dstVideoChannelId = ChannelType.VIDEODECODE1.getChannelId();
		this.dstVideoChannelName = ChannelType.VIDEOENCODE1.getName();
		return this;
	}
	
	public PageTaskPO setDstByHall(TerminalBundleConferenceHallPermissionPO decoder, BundlePO bundlePO){
//		this.businessType = player.getPlayerBusinessType();//TODO
//		this.locationIndex = player.getLocationIndex();
//		this.businessId = player.getBusinessId();
//		this.businessName = player.getBusinessName();
		this.dstId = decoder.getId().toString();
		this.dstAudioBaseType = "VenusAudioOut";
		this.dstAudioChannelId = ChannelType.AUDIODECODE1.getChannelId();
		this.dstAudioChannelName = ChannelType.AUDIODECODE1.getName();
		this.dstBundleId = decoder.getBundleId();
		this.dstBundleName = bundlePO.getBundleName();
		this.dstBundleType = bundlePO.getBundleType();
		this.dstLayerId = bundlePO.getAccessNodeUid();
		this.dstVideoBaseType = "VenusVideoOut";
		this.dstVideoChannelId = ChannelType.VIDEODECODE1.getChannelId();
		this.dstVideoChannelName = ChannelType.VIDEOENCODE1.getName();
		return this;
	}
	
	public PageTaskPO clearDst(){
//		this.businessType = player.getPlayerBusinessType();//TODO
//		this.locationIndex = player.getLocationIndex();
//		this.businessId = player.getBusinessId();
//		this.businessName = player.getBusinessName();
		this.dstId = null;
		this.locationIndex = null;
		this.dstAudioBaseType = null;
		this.dstAudioChannelId = null;
		this.dstAudioChannelName = null;
		this.dstBundleId = null;
		this.dstBundleName = null;
		this.dstBundleType = null;
		this.dstLayerId = null;
		this.dstVideoBaseType = null;
		this.dstVideoChannelId = null;
		this.dstVideoChannelName = null;
		return this;
	}
	
	public PageTaskPO set(PageTaskBO task){//TODO
		this.businessType = task.getBusinessInfoType();
		this.businessId = task.getBusinessId();
		this.forwardUuid = task.getVideoForwardUuid();
		this.businessName = task.getBusinessName();
		
		return this;
	}
	
	//这个可能没用，再考虑
	@Deprecated
	public PageTaskPO set(CommandGroupUserPlayerPO player){
//		this.businessType = player.getPlayerBusinessType();//TODO
//		this.locationIndex = player.getLocationIndex();
//		this.businessId = player.getBusinessId();
		this.businessName = player.getBusinessName();
		return this;
	}

	/**
	 * 索引排序，从小到大<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月27日 下午6:47:33
	 */
	public static final class TaskComparatorFromIndex implements Comparator<PageTaskPO>{
		@Override
		public int compare(PageTaskPO o1, PageTaskPO o2) {
			
			long id1 = o1.getTaskIndex();
			long id2 = o2.getTaskIndex();
			
			if(id1 > id2){
				return 1;
			}
			if(id1 == id2){
				return 0;
			}
			return -1;
		}
	}
	
}
