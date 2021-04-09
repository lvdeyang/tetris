package com.sumavision.tetris.bvc.business.po.member.vo;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.group.GroupMemberOnlineStatus;
import com.sumavision.tetris.bvc.business.group.GroupMemberStatus;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalAudioOutputPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalBundlePO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalScreenPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalType;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.util.BaseUtils;

public class BusinessGroupMemberVO {
	
	private Long id;
	
	private String uuid;
	
	private String updateTime;

	/** 成员名称 */
	private String name;
	
	/** 成员类型：用户/设备 */
	private String groupMemberType;
	
	/** 用户/设备号码 */
	private String code;
	
	/** 成员为用户时存userId, 设备时存bundleId，会场存会场id */
	private String originId;
	
	/** 用户来源 */
	private String originType;
	
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
	private String groupMemberStatus;
	
	/** 成员状态 */
	private String onlineStatus;
	
	/** 是否是主席 */
	private Boolean isAdministrator = false;
	
//	/** 隶属业务id */
//	private Long groupId;
	
	/** 非组业务时使用，如指挥的媒体转发 */
	private String businessId;
	
	/** 所属业务角色RolePO的id */
	private Long roleId;
	
	/** 所属业务角色RolePO的名称 */
	private String roleName;
		
	/**--------- TerminalPO 信息 ------------*/
	
	/** id */
	private Long terminalId;
	
	/** 名称 */
	private String terminalname;

	/** 终端类型 */
	private String terminalType;
	
	/** 是否启动物理屏幕布局 */
	private Boolean physicalScreenLayout;
	
	//关联group
	private Long groupId;
	
	public Long getId() {
		return id;
	}

	public BusinessGroupMemberVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public BusinessGroupMemberVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public BusinessGroupMemberVO setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public String getName() {
		return name;
	}

	public BusinessGroupMemberVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getGroupMemberType() {
		return groupMemberType;
	}

	public BusinessGroupMemberVO setGroupMemberType(String groupMemberType) {
		this.groupMemberType = groupMemberType;
		return this;
	}

	public String getCode() {
		return code;
	}

	public BusinessGroupMemberVO setCode(String code) {
		this.code = code;
		return this;
	}

	public String getOriginId() {
		return originId;
	}

	public BusinessGroupMemberVO setOriginId(String originId) {
		this.originId = originId;
		return this;
	}

	public String getOriginType() {
		return originType;
	}

	public BusinessGroupMemberVO setOriginType(String originType) {
		this.originType = originType;
		return this;
	}

	public Long getFolderId() {
		return folderId;
	}

	public BusinessGroupMemberVO setFolderId(Long folderId) {
		this.folderId = folderId;
		return this;
	}

	public Boolean getSilenceToHigher() {
		return silenceToHigher;
	}

	public BusinessGroupMemberVO setSilenceToHigher(Boolean silenceToHigher) {
		this.silenceToHigher = silenceToHigher;
		return this;
	}

	public Boolean getSilenceToLower() {
		return silenceToLower;
	}

	public BusinessGroupMemberVO setSilenceToLower(Boolean silenceToLower) {
		this.silenceToLower = silenceToLower;
		return this;
	}

	public Boolean getMyAudio() {
		return myAudio;
	}

	public BusinessGroupMemberVO setMyAudio(Boolean myAudio) {
		this.myAudio = myAudio;
		return this;
	}

	public Boolean getMyVideo() {
		return myVideo;
	}

	public BusinessGroupMemberVO setMyVideo(Boolean myVideo) {
		this.myVideo = myVideo;
		return this;
	}

	public String getGroupMemberStatus() {
		return groupMemberStatus;
	}

	public BusinessGroupMemberVO setGroupMemberStatus(String groupMemberStatus) {
		this.groupMemberStatus = groupMemberStatus;
		return this;
	}

	public String getOnlineStatus() {
		return onlineStatus;
	}

	public BusinessGroupMemberVO setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus;
		return this;
	}

	public Boolean getIsAdministrator() {
		return isAdministrator;
	}

	public BusinessGroupMemberVO setIsAdministrator(Boolean isAdministrator) {
		this.isAdministrator = isAdministrator;
		return this;
	}

	public String getBusinessId() {
		return businessId;
	}

	public BusinessGroupMemberVO setBusinessId(String businessId) {
		this.businessId = businessId;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public BusinessGroupMemberVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public BusinessGroupMemberVO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public BusinessGroupMemberVO setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	public String getTerminalname() {
		return terminalname;
	}

	public BusinessGroupMemberVO setTerminalname(String terminalname) {
		this.terminalname = terminalname;
		return this;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public BusinessGroupMemberVO setTerminalType(String terminalType) {
		this.terminalType = terminalType;
		return this;
	}

	public Boolean getPhysicalScreenLayout() {
		return physicalScreenLayout;
	}

	public BusinessGroupMemberVO setPhysicalScreenLayout(Boolean physicalScreenLayout) {
		this.physicalScreenLayout = physicalScreenLayout;
		return this;
	}

	public Long getGroupId() {
		return groupId;
	}

	public BusinessGroupMemberVO setGroupId(Long groupId) {
		this.groupId = groupId;
		return this;
	}

	public BusinessGroupMemberVO set(BusinessGroupMemberPO member){
		
		this.setId(member.getId())
			.setUuid(member.getUuid())
			.setUpdateTime(member.getUpdateTime()!=null?DateUtil.format(member.getUpdateTime()):"-")
			.setName(BaseUtils.stringIsNotBlank(member.getName())?member.getName():"-")
			.setGroupMemberType(member.getGroupMemberType()!=null?member.getGroupMemberType().toString():"-")
			.setCode(BaseUtils.stringIsNotBlank(member.getCode())? member.getCode():"-")
			.setOriginId(BaseUtils.stringIsNotBlank(member.getOriginId())?member.getOriginId():"-")
			.setOriginType(member.getOriginType()!=null?member.getOriginType().toString():"-")
			.setFolderId(member.getFolderId())
			.setSilenceToHigher(member.getSilenceToHigher())
			.setSilenceToLower(member.getSilenceToLower())
			.setMyAudio(member.getMyAudio())
			.setMyVideo(member.getMyVideo())
			.setGroupMemberStatus(member.getGroupMemberStatus()!=null?member.getGroupMemberStatus().toString():"-")
			.setOnlineStatus(member.getOnlineStatus()!=null?member.getOnlineStatus().toString():"-")
			.setIsAdministrator(member.getIsAdministrator())
			.setBusinessId(BaseUtils.stringIsNotBlank(member.getBusinessId())?member.getBusinessId():"-")
			.setRoleId(member.getRoleId())
			.setRoleName(BaseUtils.stringIsNotBlank(member.getRoleName())?member.getRoleName():"-")
			.setTerminalId(member.getTerminalId())
			.setTerminalname(BaseUtils.stringIsNotBlank(member.getTerminalname())?member.getTerminalname():"-")
			.setTerminalType(member.getTerminalType()!=null? member.getTerminalType().toString():"-")
			.setPhysicalScreenLayout(member.getPhysicalScreenLayout())
			.setGroupId(member.getGroup()!=null?member.getGroup().getId():null);
		
		return this;
	}
	
}
