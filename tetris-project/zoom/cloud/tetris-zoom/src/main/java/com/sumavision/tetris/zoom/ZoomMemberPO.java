package com.sumavision.tetris.zoom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 会议成员<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月2日 上午9:27:20
 */
@Entity
@Table(name = "TETRIS_ZOOM_MEMBER")
public class ZoomMemberPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

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
	
	/** 记录成员终端类型 */
	private ZoomMemberType type;

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name = "USERNO")
	public String getUserno() {
		return userno;
	}

	public void setUserno(String userno) {
		this.userno = userno;
	}

	@Column(name = "USER_NICKNAME")
	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	@Column(name = "RE_NAME")
	public String getRename() {
		return rename;
	}

	public void setRename(String rename) {
		this.rename = rename;
	}

	@Column(name = "TOURIST")
	public Boolean getTourist() {
		return tourist;
	}

	public void setTourist(Boolean tourist) {
		this.tourist = tourist;
	}

	@Column(name = "ZOOM_ID")
	public Long getZoomId() {
		return zoomId;
	}

	public void setZoomId(Long zoomId) {
		this.zoomId = zoomId;
	}

	@Column(name = "CHAIRMAN")
	public Boolean getChairman() {
		return chairman;
	}

	public void setChairman(Boolean chairman) {
		this.chairman = chairman;
	}

	@Column(name = "SPOKESMANE")
	public Boolean getSpokesman() {
		return spokesman;
	}

	public void setSpokesman(Boolean spokesman) {
		this.spokesman = spokesman;
	}

	@Column(name = "JO_IN")
	public Boolean getJoin() {
		return join;
	}

	public void setJoin(Boolean join) {
		this.join = join;
	}

	@Column(name = "MY_AUDIO")
	public Boolean getMyAudio() {
		return myAudio;
	}

	public void setMyAudio(Boolean myAudio) {
		this.myAudio = myAudio;
	}

	@Column(name = "MY_VIDEO")
	public Boolean getMyVideo() {
		return myVideo;
	}

	public void setMyVideo(Boolean myVideo) {
		this.myVideo = myVideo;
	}

	@Column(name = "THEIR_AUDIO")
	public Boolean getTheirAudio() {
		return theirAudio;
	}

	public void setTheirAudio(Boolean theirAudio) {
		this.theirAudio = theirAudio;
	}

	@Column(name = "THEIR_VIDEO")
	public Boolean getTheirVideo() {
		return theirVideo;
	}

	public void setTheirVideo(Boolean theirVideo) {
		this.theirVideo = theirVideo;
	}

	@Column(name = "SHARE_SCREEN")
	public Boolean getShareScreen() {
		return shareScreen;
	}

	public void setShareScreen(Boolean shareScreen) {
		this.shareScreen = shareScreen;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public ZoomMemberType getType() {
		return type;
	}

	public void setType(ZoomMemberType type) {
		this.type = type;
	}
	
}
