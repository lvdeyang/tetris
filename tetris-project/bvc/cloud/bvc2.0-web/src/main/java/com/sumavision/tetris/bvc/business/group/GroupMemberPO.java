package com.sumavision.tetris.bvc.business.group;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 业务组成员<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:38:30
 */
@Entity
@Table(name = "TETRIS_BVC_BUSINESS_GROUP_MEMBER")
public class GroupMemberPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 成员名称 */
	private String name;
	
	/** 成员类型：用户/设备 */
	private GroupMemberType groupMemberType;
	
	/** 用户/设备号码 */
	private String code;
	
	/** 成员为用户时存userId, 设备时存bundleId，会场存会场id */
	private String originId;
	
	/** 终端类型 */
	private Long terminalId;
	
	/** 用户来源 */
	private OriginType originType = OriginType.INNER;
	
	/** 成员-用户或设备所在的文件夹 */
	private Long folderId;
	
	/** 对上静默 */
	private Boolean silenceToHigher = false;
	
	/** 对下静默 */
	private Boolean silenceToLower = false;
	
	/** 是否开启自己音频 */
	private Boolean myAudio = true;
	
	/** 是否开启自己视频 */
	private Boolean myVideo = true;
	
	/** 成员状态 */
	private GroupMemberStatus groupMemberStatus = GroupMemberStatus.DISCONNECT;
	
	/** 是否是主席 */
	private Boolean isAdministrator = false;
	
	/** 隶属业务id */
	private Long groupId;
	
	/** 非组业务时使用，如指挥的媒体转发 */
	private String businessId;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "GROUP_MEMBER_TYPE")
	@Enumerated(value = EnumType.STRING)
	public GroupMemberType getGroupMemberType() {
		return groupMemberType;
	}

	public void setGroupMemberType(GroupMemberType groupMemberType) {
		this.groupMemberType = groupMemberType;
	}

	@Column(name = "ORIGIN_ID")
	public String getOriginId() {
		return originId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setOriginId(String originId) {
		this.originId = originId;
	}

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	@Column(name = "ORIGIN_TYPE")
	@Enumerated(value = EnumType.STRING)
	public OriginType getOriginType() {
		return originType;
	}

	public void setOriginType(OriginType originType) {
		this.originType = originType;
	}

	@Column(name = "FOLDER_ID")
	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	@Column(name = "SILENCE_TO_HIGHER")
	public Boolean getSilenceToHigher() {
		return silenceToHigher;
	}

	public void setSilenceToHigher(Boolean silenceToHigher) {
		this.silenceToHigher = silenceToHigher;
	}

	@Column(name = "SILENCE_TO_LOWER")
	public Boolean getSilenceToLower() {
		return silenceToLower;
	}

	public void setSilenceToLower(Boolean silenceToLower) {
		this.silenceToLower = silenceToLower;
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

	@Column(name = "GROUP_MEMBER_STATUS")
	@Enumerated(value = EnumType.STRING)
	public GroupMemberStatus getGroupMemberStatus() {
		return groupMemberStatus;
	}

	public void setGroupMemberStatus(GroupMemberStatus groupMemberStatus) {
		this.groupMemberStatus = groupMemberStatus;
	}

	@Column(name = "IS_ADMINISTRATOR")
	public Boolean getIsAdministrator() {
		return isAdministrator;
	}

	public void setIsAdministrator(Boolean isAdministrator) {
		this.isAdministrator = isAdministrator;
	}

	@Column(name = "GROUP_ID")
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Column(name = "BUSINESS_ID")
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
}
