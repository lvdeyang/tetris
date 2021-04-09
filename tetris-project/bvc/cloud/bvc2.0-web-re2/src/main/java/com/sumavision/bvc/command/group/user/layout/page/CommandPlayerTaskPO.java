package com.sumavision.bvc.command.group.user.layout.page;

import java.util.Comparator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 播放器任务列表<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年5月27日 下午2:59:23
 */
@Entity
@Table(name="BVC_COMMAND_PLAYER_TASK")
public class CommandPlayerTaskPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	/** 位置索引，用来对应播放器，表示在布局中的第几个位置，通常取值0-15 */
	private Integer locationIndex;
	
	/** 整个列表中的排序索引 */
	private int taskIndex;
	
	private PlayerBusinessType playerBusinessType = PlayerBusinessType.NONE;

	/** 业务id，
	 * BASIC_COMMAND等指挥业务为“指挥id-观看对象userId”，
	 * COMMAND_FORWARD_xxx等指挥转发时为“指挥id-转发PO的id”，
	 * USER_CALL和USER_VOICE时为UserLiveCallPO的id
	 * PLAY_FILE时为resourceFileId，可能出现重复
	 * PLAY_USER和PLAY_DEVICE等点播任务时为CommandVodPO的id
	 */
	private String businessId;

	/** 业务名称，用于显示在播放器顶端，对应播放器信息中的businessInfo */
	private String businessName;

	/** 文件、录像的播放地址 */
	private String playUrl;
	
	/** osd id */
	private Long osdId;
	
	/** osd 名称 */
	private String osdName;
	
	/***********
	 * 源信息 *
	 **********/
	
	/** 源视频设备层节点id */
	private String srcVideoLayerId;
	
	/** 源视频设备id */
	private String srcVideoBundleId;
	
	/** 源设备视频通道id */
	private String srcVideoChannelId;
	
	/** 源音频设备层节点id */
	private String srcAudioLayerId;
	
	/** 源音频设备id */
	private String srcAudioBundleId;
	
	/** 源设备音频通道id */
	private String srcAudioChannelId;
	
	/** 关联用户信息 */
	private CommandGroupUserInfoPO userInfo;

	@Column(name = "LOCATION_INDEX")
	public Integer getLocationIndex() {
		return locationIndex;
	}

	public void setLocationIndex(Integer locationIndex) {
		this.locationIndex = locationIndex;
	}
	
	@Column(name = "TASK_INDEX")
	public int getTaskIndex() {
		return taskIndex;
	}

	public void setTaskIndex(int taskIndex) {
		this.taskIndex = taskIndex;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "PLAYER_BUSINESS_TYPE")
	public PlayerBusinessType getPlayerBusinessType() {
		return playerBusinessType;
	}

	public void setPlayerBusinessType(PlayerBusinessType playerBusinessType) {
		this.playerBusinessType = playerBusinessType;
	}

	@Column(name = "BUSINESS_ID")
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	@Column(name = "BUSINESS_NAME")
	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	@Column(name = "PLAY_URL")
	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}
	
	@ManyToOne
	@JoinColumn(name = "USER_INFO_ID")
	public CommandGroupUserInfoPO getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(CommandGroupUserInfoPO userInfo) {
		this.userInfo = userInfo;
	}
	
	@Column(name = "OSD_ID")
	public Long getOsdId() {
		return osdId;
	}

	public void setOsdId(Long osdId) {
		this.osdId = osdId;
	}

	@Column(name = "OSD_NAME")
	public String getOsdName() {
		return osdName;
	}

	public void setOsdName(String osdName) {
		this.osdName = osdName;
	}
	
	@Column(name = "SRC_VIDEO_LAYER_ID")
	public String getSrcVideoLayerId() {
		return srcVideoLayerId;
	}

	public void setSrcVideoLayerId(String srcVideoLayerId) {
		this.srcVideoLayerId = srcVideoLayerId;
	}

	@Column(name = "SRC_VIDEO_BUNDLE_ID")
	public String getSrcVideoBundleId() {
		return srcVideoBundleId;
	}

	public void setSrcVideoBundleId(String srcVideoBundleId) {
		this.srcVideoBundleId = srcVideoBundleId;
	}

	@Column(name = "SRC_VIDEO_CHANNEL_ID")
	public String getSrcVideoChannelId() {
		return srcVideoChannelId;
	}

	public void setSrcVideoChannelId(String srcVideoChannelId) {
		this.srcVideoChannelId = srcVideoChannelId;
	}

	@Column(name = "SRC_AUDIO_LAYER_ID")
	public String getSrcAudioLayerId() {
		return srcAudioLayerId;
	}

	public void setSrcAudioLayerId(String srcAudioLayerId) {
		this.srcAudioLayerId = srcAudioLayerId;
	}

	@Column(name = "SRC_AUDIO_BUNDLE_ID")
	public String getSrcAudioBundleId() {
		return srcAudioBundleId;
	}

	public void setSrcAudioBundleId(String srcAudioBundleId) {
		this.srcAudioBundleId = srcAudioBundleId;
	}

	@Column(name = "SRC_AUDIO_CHANNEL_ID")
	public String getSrcAudioChannelId() {
		return srcAudioChannelId;
	}

	public void setSrcAudioChannelId(String srcAudioChannelId) {
		this.srcAudioChannelId = srcAudioChannelId;
	}
	
	/**
	 * 将任务置空<br/>
	 * <p>仅设置值，还需要额外进行save操作</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午3:34:05
	 * @return
	 */
	public CommandPlayerTaskPO setFree(){
		this.setPlayerBusinessType(PlayerBusinessType.NONE);
		this.setLocationIndex(null);
		this.setBusinessId(null);
		this.setBusinessName(null);
		this.setPlayUrl(null);
		this.setOsdId(null);
		this.setOsdName(null);
		this.setSrcAudioBundleId(null);
		this.setSrcAudioChannelId(null);
		this.setSrcAudioLayerId(null);
		this.setSrcVideoBundleId(null);
		this.setSrcVideoChannelId(null);
		this.setSrcVideoLayerId(null);
		return this;
	}
	
	/** 该任务是否是播放文件 */
	public boolean playingFile(){
		PlayerBusinessType businessType = this.playerBusinessType;
		if(PlayerBusinessType.PLAY_FILE.equals(businessType)
				|| PlayerBusinessType.PLAY_RECORD.equals(businessType)
				|| PlayerBusinessType.PLAY_COMMAND_RECORD.equals(businessType)
				|| PlayerBusinessType.COMMAND_FORWARD_FILE.equals(businessType)){
			return true;
		}
		return false;
	}
	
	/** 任务与任务是否相同 */
	public boolean equalsTask(CommandPlayerTaskPO task){
		if(this.businessId == null) return false;
		if(this.businessId.equals(task.getBusinessId())
				&& this.playerBusinessType.equals(task.getPlayerBusinessType())){
			return true;
		}
		return false;
	}
	
	/** 任务与播放器进行的任务是否相同 */
	public boolean equalsPlayer(CommandGroupUserPlayerPO player){
		if(this.businessId == null) return false;
		if(this.businessId.equals(player.getBusinessId())
				&& this.playerBusinessType.equals(player.getPlayerBusinessType())){
			return true;
		}
		return false;
	}
	
	public CommandPlayerTaskPO set(CommandPlayerTaskBO task){
		this.playerBusinessType = task.getPlayerBusinessType();
		this.businessId = task.getBusinessId();
		this.businessName = task.getBusinessName();
		this.osdId = task.getOsdId();
		this.osdName = task.getOsdName();
		this.playUrl = task.getPlayUrl();
		this.srcAudioBundleId = task.getSrcAudioBundleId();
		this.srcAudioChannelId = task.getSrcAudioChannelId();
		this.srcAudioLayerId = task.getSrcAudioLayerId();
		this.srcVideoBundleId = task.getSrcVideoBundleId();
		this.srcVideoChannelId = task.getSrcVideoChannelId();
		this.srcVideoLayerId = task.getSrcVideoLayerId();
		return this;
	}
	
	//这个可能没用，再考虑
	@Deprecated
	public CommandPlayerTaskPO set(CommandGroupUserPlayerPO player){
		this.playerBusinessType = player.getPlayerBusinessType();
		this.locationIndex = player.getLocationIndex();
		this.businessId = player.getBusinessId();
		this.businessName = player.getBusinessName();
		this.osdId = player.getOsdId();
		this.osdName = player.getOsdName();
		this.playUrl = player.getPlayUrl();
		this.srcAudioBundleId = player.getSrcAudioBundleId();
		this.srcAudioChannelId = player.getSrcAudioChannelId();
		this.srcAudioLayerId = player.getSrcAudioLayerId();
		this.srcVideoBundleId = player.getSrcVideoBundleId();
		this.srcVideoChannelId = player.getSrcVideoChannelId();
		this.srcVideoLayerId = player.getSrcVideoLayerId();
		return this;
	}

	/**
	 * 索引排序，从小到大<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月27日 下午6:47:33
	 */
	public static final class TaskComparatorFromIndex implements Comparator<CommandPlayerTaskPO>{
		@Override
		public int compare(CommandPlayerTaskPO o1, CommandPlayerTaskPO o2) {
			
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
