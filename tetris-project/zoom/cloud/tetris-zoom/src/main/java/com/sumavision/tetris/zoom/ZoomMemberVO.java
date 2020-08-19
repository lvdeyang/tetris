package com.sumavision.tetris.zoom;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ZoomMemberVO extends AbstractBaseVO<ZoomMemberVO, ZoomMemberPO>{

	/** 用户id */
	private String userId;
	
	/** 用户号码 */
	private String userno;
	
	/** 用户昵称 */
	private String userNickname;
	
	/** 重命名 */
	private String rename;
	
	/** 是否是游客 */
	private Boolean tourist;
	
	/** 会议id */
	private Long zoomId;
	
	/** 是否是主席 */
	private Boolean chairman;
	
	/** 是否是发言人 */
	private Boolean spokesman;
	
	/** 是否加入会议 */
	private Boolean join;
	
	/** 是否开启自己音频 */
	private Boolean myAudio;
	
	/** 是否开启自己视频 */
	private Boolean myVideo;
	
	/** 是否接收音频 */
	private Boolean theirAudio;
	
	/** 是否接收视频 */
	private Boolean theirVideo;
	
	/** 是否共享屏幕 */
	private Boolean shareScreen;
	
	/** 设备id */
	private String bundleId;
	
	/** 接入层id */
	private String layerId;
	
	/** 视频通道id */
	private String videoChannelId;
	
	/** 音频通道id */
	private String audioChannelId;
	
	/** 屏幕视频通道id */
	private String screenVideoChannelId;
	
	/** 屏幕音频通道id */
	private String screenAudioChannelId;
	
	/** 会议成员终端类型 */
	private String type;
	
	public String getUserId() {
		return userId;
	}

	public ZoomMemberVO setUserId(String userId) {
		this.userId = userId;
		return this;
	}
	
	public String getUserno() {
		return userno;
	}

	public ZoomMemberVO setUserno(String userno) {
		this.userno = userno;
		return this;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public ZoomMemberVO setUserNickname(String userNickname) {
		this.userNickname = userNickname;
		return this;
	}

	public String getRename() {
		return rename;
	}

	public ZoomMemberVO setRename(String rename) {
		this.rename = rename;
		return this;
	}

	public Boolean getTourist() {
		return tourist;
	}

	public ZoomMemberVO setTourist(Boolean tourist) {
		this.tourist = tourist;
		return this;
	}

	public Long getZoomId() {
		return zoomId;
	}

	public ZoomMemberVO setZoomId(Long zoomId) {
		this.zoomId = zoomId;
		return this;
	}

	public Boolean getChairman() {
		return chairman;
	}

	public ZoomMemberVO setChairman(Boolean chairman) {
		this.chairman = chairman;
		return this;
	}

	public Boolean getSpokesman() {
		return spokesman;
	}

	public ZoomMemberVO setSpokesman(Boolean spokesman) {
		this.spokesman = spokesman;
		return this;
	}

	public Boolean getJoin() {
		return join;
	}

	public ZoomMemberVO setJoin(Boolean join) {
		this.join = join;
		return this;
	}

	public Boolean getMyAudio() {
		return myAudio;
	}

	public ZoomMemberVO setMyAudio(Boolean myAudio) {
		this.myAudio = myAudio;
		return this;
	}

	public Boolean getMyVideo() {
		return myVideo;
	}

	public ZoomMemberVO setMyVideo(Boolean myVideo) {
		this.myVideo = myVideo;
		return this;
	}

	public Boolean getTheirAudio() {
		return theirAudio;
	}

	public ZoomMemberVO setTheirAudio(Boolean theirAudio) {
		this.theirAudio = theirAudio;
		return this;
	}

	public Boolean getTheirVideo() {
		return theirVideo;
	}

	public ZoomMemberVO setTheirVideo(Boolean theirVideo) {
		this.theirVideo = theirVideo;
		return this;
	}

	public Boolean getShareScreen() {
		return shareScreen;
	}

	public ZoomMemberVO setShareScreen(Boolean shareScreen) {
		this.shareScreen = shareScreen;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public ZoomMemberVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public ZoomMemberVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getVideoChannelId() {
		return videoChannelId;
	}

	public ZoomMemberVO setVideoChannelId(String videoChannelId) {
		this.videoChannelId = videoChannelId;
		return this;
	}

	public String getAudioChannelId() {
		return audioChannelId;
	}

	public ZoomMemberVO setAudioChannelId(String audioChannelId) {
		this.audioChannelId = audioChannelId;
		return this;
	}

	public String getScreenVideoChannelId() {
		return screenVideoChannelId;
	}

	public ZoomMemberVO setScreenVideoChannelId(String screenVideoChannelId) {
		this.screenVideoChannelId = screenVideoChannelId;
		return this;
	}

	public String getScreenAudioChannelId() {
		return screenAudioChannelId;
	}

	public ZoomMemberVO setScreenAudioChannelId(String screenAudioChannelId) {
		this.screenAudioChannelId = screenAudioChannelId;
		return this;
	}

	public String getType() {
		return type;
	}

	public ZoomMemberVO setType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public ZoomMemberVO set(ZoomMemberPO entity) throws Exception {
		this.setId(entity.getId())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setUserId(entity.getUserId())
			.setUserno(entity.getUserno())
			.setUserNickname(entity.getUserNickname())
			.setRename(entity.getRename())
			.setTourist(entity.getTourist())
			.setZoomId(entity.getZoomId())
			.setChairman(entity.getChairman())
			.setSpokesman(entity.getSpokesman())
			.setJoin(entity.getJoin())
			.setMyAudio(entity.getMyAudio())
			.setMyVideo(entity.getMyVideo())
			.setTheirAudio(entity.getTheirAudio())
			.setTheirVideo(entity.getTheirVideo())
			.setShareScreen(entity.getShareScreen())
			.setType(entity.getType().toString());
		return this;
	}

}
