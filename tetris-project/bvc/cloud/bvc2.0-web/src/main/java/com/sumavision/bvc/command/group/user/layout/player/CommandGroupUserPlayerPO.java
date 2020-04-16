package com.sumavision.bvc.command.group.user.layout.player;

import java.util.Comparator;
import java.util.List;

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

import com.suma.venus.resource.base.bo.PlayerBundleBO;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 用户界面上的某个播放器<br/>
 * @Description: locationIndex限定的某一固定位置的播放器。不是某一特定的播放器bundle<br/>
 * @author zsy
 * @ps 参考了 WebSipPlayerVO
 * @date 2019年9月20日 下午1:06:00
 */
@Entity
@Table(name="BVC_COMMAND_GROUP_USER_PLAYER")
public class CommandGroupUserPlayerPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	/** 位置索引，表示在布局中的第几个位置，取值0-15 */
	private int locationIndex;
	
	private PlayerBusinessType playerBusinessType = PlayerBusinessType.NONE;

	/** 业务id，
	 * BASIC_COMMAND时为指挥id，
	 * DEMAND_FORWARD时为点播MonitorLiveDevicePO的id，
	 * USER_CALL和USER_VOICE时为UserLiveCallPO的id
	 * PLAY_FILE时为resourceFileId
	 * PLAY_USER和PLAY_DEVICE时为CommandVodPO的id
	 */
	private String businessId;
	
	/** 关联指挥成员 */
	private CommandGroupMemberPO member;
	
	//转发不需要记录，通过播放器的recover机制重新拉流

	/** 业务名称，用于显示在播放器顶端，对应播放器信息中的businessInfo */
	private String businessName;

	/** 文件、录像的播放地址 */
	private String playUrl;
	
	/** 号码 */
	private String code;
	
	/** 用户名 */
	private String username;
	
	/** 密码 */
	private String password;
	
//	/** 客户端ip */
//	private String ip;
	
	/** 播放器端口：从配置文件获取 */
	private String port;
	
	/** 设备id */
	private String bundleId;
	
	/** 设备类型，对应BundlePO的bundleType，如VenusTerminal */
	private String bundleType;
	
	/** 设备名称 */
	private String bundleName;
	
	/** 接入层id */
	private String layerId;
	
	/** 接入层注册ip */
	private String registerLayerIp;
	
	/** 接入层注册端口 */
	private String registerLayerPort;
	
	/** 视频通道id */
	private String videoChannelId;
	
	/** 视频通道类型 */
	private String videoBaseType;
	
	/** 音频通道id */
	private String audioChannelId;

	/** 音频通道类型 */
	private String audioBaseType;
	
	/** 绑定的上屏设备 */
	private List<CommandGroupUserPlayerCastDevicePO> castDevices;
	
	/** 关联用户信息 */
	private CommandGroupUserInfoPO userInfo;

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

	@Column(name = "BUSINESS_ID")
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
	@ManyToOne
	@JoinColumn(name = "MEMBER_ID")
	public CommandGroupMemberPO getMember() {
		return member;
	}

	public void setMember(CommandGroupMemberPO member) {
		this.member = member;
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

	@Column(name = "BUNDLE_ID")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}
	
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "USERNAME")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "PASSWORD")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "PORT")
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Column(name = "BUNDLE_TYPE")
	public String getBundleType() {
		return bundleType;
	}

	public void setBundleType(String bundleType) {
		this.bundleType = bundleType;
	}

	@Column(name = "BUNDLE_NAME")
	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	@Column(name = "REGISTER_LAYER_IP")
	public String getRegisterLayerIp() {
		return registerLayerIp;
	}

	public void setRegisterLayerIp(String registerLayerIp) {
		this.registerLayerIp = registerLayerIp;
	}

	@Column(name = "REGISTER_LAYER_PORT")
	public String getRegisterLayerPort() {
		return registerLayerPort;
	}

	public void setRegisterLayerPort(String registerLayerPort) {
		this.registerLayerPort = registerLayerPort;
	}

	@Column(name = "VIDEO_BASE_TYPE")
	public String getVideoBaseType() {
		return videoBaseType;
	}

	public void setVideoBaseType(String videoBaseType) {
		this.videoBaseType = videoBaseType;
	}

	@Column(name = "AUDIO_BASE_TYPE")
	public String getAudioBaseType() {
		return audioBaseType;
	}

	public void setAudioBaseType(String audioBaseType) {
		this.audioBaseType = audioBaseType;
	}

	@Column(name = "VIDEO_CHANNEL_ID")
	public String getVideoChannelId() {
		return videoChannelId;
	}

	public void setVideoChannelId(String videoChannelId) {
		this.videoChannelId = videoChannelId;
	}

	@Column(name = "AUDIO_CHANNEL_ID")
	public String getAudioChannelId() {
		return audioChannelId;
	}

	public void setAudioChannelId(String audioChannelId) {
		this.audioChannelId = audioChannelId;
	}

	@Column(name = "LAYER_ID")
	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	@OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CommandGroupUserPlayerCastDevicePO> getCastDevices() {
		return castDevices;
	}

	public void setCastDevices(List<CommandGroupUserPlayerCastDevicePO> castDevices) {
		this.castDevices = castDevices;
	}
	
	@ManyToOne
	@JoinColumn(name = "USER_INFO_ID")
	public CommandGroupUserInfoPO getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(CommandGroupUserInfoPO userInfo) {
		this.userInfo = userInfo;
	}
	
	public CommandGroupUserPlayerPO set(PlayerBundleBO entity){
		this.setCode(entity.getUsername());
		this.setUsername(entity.getUsername());
		this.setPassword(entity.getPassword());
		this.setBundleId(entity.getBundleId());
		this.setBundleName(entity.getBundleName());
		this.setBundleType(entity.getBundleType());
		this.setLayerId(entity.getAccessLayerId());
		this.setRegisterLayerIp(entity.getAccessLayerIp()==null?"0.0.0.0":entity.getAccessLayerIp());
		this.setRegisterLayerPort(entity.getAccessLayerPort()==null?"0":entity.getAccessLayerPort().toString());
	
		List<String> channelIds = entity.getChannelIds();
		for(String channelId:channelIds){
			if(channelId.startsWith("VenusVideoOut")){
				this.setVideoChannelId(channelId);
				this.setVideoBaseType("VenusVideoOut");
			}else if(channelId.startsWith("VenusAudioOut")){
				this.setAudioChannelId(channelId);
				this.setAudioBaseType("VenusAudioOut");
			}
		}
		return this;
	}
	
	/**
	 * 释放播放器<br/>
	 * <p>仅设置值，还需要额外从member解除对该player的关联，并save操作</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午3:34:05
	 * @return
	 */
	public CommandGroupUserPlayerPO setFree(){
		this.setPlayerBusinessType(PlayerBusinessType.NONE);
		this.setBusinessId(null);
		this.setBusinessName(null);
		this.setPlayUrl(null);
		this.setMember(null);
		return this;
	}
	
	/** 该播放器是否正在播放文件 */
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

	/**
	 * 播放器索引排序，从小到大<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月16日 下午6:47:33
	 */
	public static final class PlayerComparatorFromIndex implements Comparator<CommandGroupUserPlayerPO>{
		@Override
		public int compare(CommandGroupUserPlayerPO o1, CommandGroupUserPlayerPO o2) {
			
			long id1 = o1.getLocationIndex();
			long id2 = o2.getLocationIndex();
			
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
