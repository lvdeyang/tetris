package com.sumavision.bvc.command.group.basic;

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

import com.sumavision.bvc.command.group.enumeration.MemberStatus;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.device.group.enumeration.MemberType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 指挥成员<br/>
 * @Description: <br/>
 * @author zsy 
 * @date 2019年9月20日 下午1:06:00
 */
@Entity
@Table(name="BVC_COMMAND_GROUP_MEMBER")
public class CommandGroupMemberPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	/** 用户id */
	private Long userId;

	/** 用户名称 */
	private String userName;

	/** 用户号码 */
	private String userNum;
	
	/** 成员类型 */
	private MemberType memberType = MemberType.USER;
	
	/** 所属文件夹id*/
	private Long folderId;
	
	/** 消息id */
	private Long messageId;
	
	/** 协同指挥消息id */
	private Long messageCoId;
	
	/***********
	 * 源设备信息（编码器） *
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
	
	/** 设备类型，对应BundlePO的deviceModel，如 jv210、jv230、tvos、ipc、mobile */
	private String srcBundleType;
	
	/** 源设备的资源类型，对应BundlePO的bundleType，如 VenusTerminal */
	private String srcVenusBundleType;
	
	/***********
	 * 目的设备信息（解码器，主要是播放器） *
	 **********/
	
	private List<CommandGroupUserPlayerPO> players;
	
//	/** 目的设备id */
//	private String dstBundleId;
//	
//	/** 目的设备视频通道id */
//	private String dstVideoChannelId;
//	
//	/** 目的设备音频通道id */
//	private String dstAudioChannelId;
//	
//	/** 目的设备层节点id, 对应资资层nodeUid*/
//	private String dstLayerId;
//
//	/** 目的设备名称 */
//	private String dstBundleName;
//	
//	/** 设备类型 jv210、jv230、tvos、ipc、mobile */
//	private String dstBundleType;
//	
//	/** 目的设备的资源类型 */
//	private String dstVenusBundleType;
	
	/** 所属业务角色id（这里是指挥关联的业务角色，并不是系统级的业务角色） */
	private Long roleId;
	
	/** 所属业务角色名称（这里是指挥关联的业务角色，并不是系统级的业务角色） */
	private String roleName;
	
//	/** new 成员类型：普通，临时源，指挥 */
//	private MemberType memberType;
	
	/** 设备状态 */
	private MemberStatus memberStatus = MemberStatus.DISCONNECT;
	
	/** 协同状态 */
	private MemberStatus cooperateStatus = MemberStatus.DISCONNECT;
	
	/** 对上静默 */
	private boolean silenceToHigher = false;
	
	/** 对下静默 */
	private boolean silenceToLower = false;
	
	/** 该成员是否是创建者 */
	private boolean isAdministrator = false;
	
	/** 关联设备组 */
	private CommandGroupPO group;

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

	@Column(name = "USER_NUM")
	public String getUserNum() {
		return userNum;
	}

	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "MEMBER_TYPE")
	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	@Column(name = "FOLDER_ID")
	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}
	
	@Column(name = "MESSAGE_ID")
	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	@Column(name = "MESSAGE_CO_ID")
	public Long getMessageCoId() {
		return messageCoId;
	}

	public void setMessageCoId(Long messageCoId) {
		this.messageCoId = messageCoId;
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

	public void setSrcBundleName(String srcUserName) {
		this.srcBundleName = srcUserName;
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

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	public List<CommandGroupUserPlayerPO> getPlayers() {
		return players;
	}

	public void setPlayers(List<CommandGroupUserPlayerPO> players) {
		this.players = players;
	}

//	@Column(name = "DST_BUNDLE_ID")
//	public String getDstBundleId() {
//		return dstBundleId;
//	}
//
//	public void setDstBundleId(String dstBundleId) {
//		this.dstBundleId = dstBundleId;
//	}
//
//	@Column(name = "DST_VIDEO_CHANNEL_ID")
//	public String getDstVideoChannelId() {
//		return dstVideoChannelId;
//	}
//
//	public void setDstVideoChannelId(String dstVideoChannelId) {
//		this.dstVideoChannelId = dstVideoChannelId;
//	}
//
//	@Column(name = "DST_AUDIO_CHANNEL_ID")
//	public String getDstAudioChannelId() {
//		return dstAudioChannelId;
//	}
//
//	public void setDstAudioChannelId(String dstAudioChannelId) {
//		this.dstAudioChannelId = dstAudioChannelId;
//	}
//
//	@Column(name = "DST_LAYER_ID")
//	public String getDstLayerId() {
//		return dstLayerId;
//	}
//
//	public void setDstLayerId(String dstLayerId) {
//		this.dstLayerId = dstLayerId;
//	}
//
//	@Column(name = "DST_BUNDLE_NAME")
//	public String getDstBundleName() {
//		return dstBundleName;
//	}
//
//	public void setDstBundleName(String dstUserName) {
//		this.dstBundleName = dstUserName;
//	}
//
//	@Column(name = "DST_BUNDLE_TYPE")
//	public String getDstBundleType() {
//		return dstBundleType;
//	}
//
//	public void setDstBundleType(String dstBundleType) {
//		this.dstBundleType = dstBundleType;
//	}
//
//	@Column(name = "DST_VENUS_BUNDLE_TYPE")
//	public String getDstVenusBundleType() {
//		return dstVenusBundleType;
//	}
//
//	public void setDstVenusBundleType(String dstVenusBundleType) {
//		this.dstVenusBundleType = dstVenusBundleType;
//	}

	@Column(name = "ROLE_ID")
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "ROLE_NAME")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "MEMBER_STATUS")
	public MemberStatus getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(MemberStatus memberStatus) {
		this.memberStatus = memberStatus;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "COOPERATE_STATUS")
	public MemberStatus getCooperateStatus() {
		return cooperateStatus;
	}

	public void setCooperateStatus(MemberStatus cooperateStatus) {
		this.cooperateStatus = cooperateStatus;
	}

	@Column(name = "SILENCE_TO_HIGHER")
	public boolean isSilenceToHigher() {
		return silenceToHigher;
	}

	public void setSilenceToHigher(boolean silenceToHigher) {
		this.silenceToHigher = silenceToHigher;
	}

	@Column(name = "SILENCE_TO_LOWER")
	public boolean isSilenceToLower() {
		return silenceToLower;
	}

	public void setSilenceToLower(boolean silenceToLower) {
		this.silenceToLower = silenceToLower;
	}

	@Column(name = "IS_ADMINISTRATOR")
	public boolean isAdministrator() {
		return isAdministrator;
	}

	public void setAdministrator(boolean isAdministrator) {
		this.isAdministrator = isAdministrator;
	}

	@ManyToOne
	@JoinColumn(name = "GROUP_ID")
	public CommandGroupPO getGroup() {
		return group;
	}

	public void setGroup(CommandGroupPO group) {
		this.group = group;
	}
	
}
