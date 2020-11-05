package com.sumavision.bvc.command.group.user.layout.player;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.bvc.command.group.user.decoder.CommandGroupDecoderScreenPO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 指挥涉及到的设备<br/>
 * @Description: 例如播放器关联的上屏设备，指挥转发的源<br/>
 * @author zsy 
 * @date 2019年9月23日 下午1:06:00
 */
@Entity
@Table(name="BVC_COMMAND_GROUP_USER_PLAYER_CAST_DEVICE")
public class CommandGroupUserPlayerCastDevicePO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	/** 用户id */
	private Long userId;

	/** 用户名称 */
	private String userName;
	
	/** 所属文件夹id*/
	private Long folderId;
	
	/***********
	 * 源设备信息（目前废弃不用） *
	 **********/
	
	/** 源设备id */
	private String srcBundleId;
	
	/** 源设备视频通道id */
	private String srcVideoChannelId;
	
	/** 源设备音频通道id */
	private String srcAudioChannelId;
	
	/** 源设备层节点id, 对应资源层nodeUid*/
	private String srcLayerId;

	/** 源设备名称 */
	private String srcBundleName;
	
	/** 设备类型 jv210、jv230、tvos、ipc、mobile */
	private String srcBundleType;
	
	/** 源设备的资源类型 */
	private String srcVenusBundleType;
	
	/***********
	 * 目的设备信息 *
	 **********/
	
	/** 目的设备id */
	private String dstBundleId;
	
	/** 目的设备视频通道id */
	private String dstVideoChannelId;
	
	/** 目的设备音频通道id */
	private String dstAudioChannelId;
	
	/** 目的设备层节点id, 对应资资层nodeUid*/
	private String dstLayerId;

	/** 目的设备名称 */
	private String dstBundleName;
	
	/** 设备类型 jv210、jv230、tvos、ipc、mobile */
	private String dstBundleType;
	
	/** 目的设备的资源类型 */
	private String dstVenusBundleType;
	
	/** 关联播放器 */
	@Deprecated
	private CommandGroupUserPlayerPO player;
	
	/** 关联播放器 */
	private PageTaskPO pageTask;
	
	/** 关联上屏方案中的分屏 */
	private CommandGroupDecoderScreenPO screen;

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "USER_NAME")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "FOLDER_ID")
	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	@Column(name = "SRC_BUNDLE_ID")
	public String getSrcBundleId() {
		return srcBundleId;
	}

	public void setSrcBundleId(String srcBundleId) {
		this.srcBundleId = srcBundleId;
	}

	@Column(name = "SRC_VIDEO_CHANNEL_ID")
	public String getSrcVideoChannelId() {
		return srcVideoChannelId;
	}

	public void setSrcVideoChannelId(String srcVideoChannelId) {
		this.srcVideoChannelId = srcVideoChannelId;
	}

	@Column(name = "SRC_AUDIO_CHANNEL_ID")
	public String getSrcAudioChannelId() {
		return srcAudioChannelId;
	}

	public void setSrcAudioChannelId(String srcAudioChannelId) {
		this.srcAudioChannelId = srcAudioChannelId;
	}

	@Column(name = "SRC_LAYER_ID")
	public String getSrcLayerId() {
		return srcLayerId;
	}

	public void setSrcLayerId(String srcLayerId) {
		this.srcLayerId = srcLayerId;
	}

	@Column(name = "SRC_BUNDLE_NAME")
	public String getSrcBundleName() {
		return srcBundleName;
	}

	public void setSrcBundleName(String srcBundleName) {
		this.srcBundleName = srcBundleName;
	}

	@Column(name = "SRC_BUNDLE_TYPE")
	public String getSrcBundleType() {
		return srcBundleType;
	}

	public void setSrcBundleType(String srcBundleType) {
		this.srcBundleType = srcBundleType;
	}

	@Column(name = "SRC_VENUS_BUNDLE_TYPE")
	public String getSrcVenusBundleType() {
		return srcVenusBundleType;
	}

	public void setSrcVenusBundleType(String srcVenusBundleType) {
		this.srcVenusBundleType = srcVenusBundleType;
	}

	@Column(name = "DST_BUNDLE_ID")
	public String getDstBundleId() {
		return dstBundleId;
	}

	public void setDstBundleId(String dstBundleId) {
		this.dstBundleId = dstBundleId;
	}

	@Column(name = "DST_VIDEO_CHANNEL_ID")
	public String getDstVideoChannelId() {
		return dstVideoChannelId;
	}

	public void setDstVideoChannelId(String dstVideoChannelId) {
		this.dstVideoChannelId = dstVideoChannelId;
	}

	@Column(name = "DST_AUDIO_CHANNEL_ID")
	public String getDstAudioChannelId() {
		return dstAudioChannelId;
	}

	public void setDstAudioChannelId(String dstAudioChannelId) {
		this.dstAudioChannelId = dstAudioChannelId;
	}

	@Column(name = "DST_LAYER_ID")
	public String getDstLayerId() {
		return dstLayerId;
	}

	public void setDstLayerId(String dstLayerId) {
		this.dstLayerId = dstLayerId;
	}

	@Column(name = "DST_BUNDLE_NAME")
	public String getDstBundleName() {
		return dstBundleName;
	}

	public void setDstBundleName(String dstBundleName) {
		this.dstBundleName = dstBundleName;
	}

	@Column(name = "DST_BUNDLE_TYPE")
	public String getDstBundleType() {
		return dstBundleType;
	}

	public void setDstBundleType(String dstBundleType) {
		this.dstBundleType = dstBundleType;
	}

	@Column(name = "DST_VENUS_BUNDLE_TYPE")
	public String getDstVenusBundleType() {
		return dstVenusBundleType;
	}

	public void setDstVenusBundleType(String dstVenusBundleType) {
		this.dstVenusBundleType = dstVenusBundleType;
	}

	@Deprecated
	@ManyToOne
	@JoinColumn(name = "PLAYER_ID")
	public CommandGroupUserPlayerPO getPlayer() {
		return player;
	}

	@Deprecated
	public void setPlayer(CommandGroupUserPlayerPO player) {
		this.player = player;
	}

	@ManyToOne
	@JoinColumn(name = "PAGE_TASK_ID")
	public PageTaskPO getPageTask() {
		return pageTask;
	}

	public void setPageTask(PageTaskPO pageTask) {
		this.pageTask = pageTask;
	}

	@ManyToOne
	@JoinColumn(name = "SCREEN_ID")
	public CommandGroupDecoderScreenPO getScreen() {
		return screen;
	}

	public void setScreen(CommandGroupDecoderScreenPO screen) {
		this.screen = screen;
	}
	
}
