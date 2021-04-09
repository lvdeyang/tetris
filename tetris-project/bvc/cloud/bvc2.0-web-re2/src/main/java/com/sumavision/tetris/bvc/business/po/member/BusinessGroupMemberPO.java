package com.sumavision.tetris.bvc.business.po.member;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;

import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.group.GroupMemberOnlineStatus;
import com.sumavision.tetris.bvc.business.group.GroupMemberStatus;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "BVC_BUSINESS_GROUP_MEMBER")
public class BusinessGroupMemberPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 成员名称 */
	private String name;
	
	/** 成员类型：用户/会场/设备
	 * 设备类型的成员，表示成员的身份是设备，能找到唯一对应的设备（级联时很必要），但依然要封装成会场，执行议程也按照会场来处理 */
	private GroupMemberType groupMemberType;
	
	/** 用户/设备号码 */
	private String code;
	
	/** 当成员是会场的时候，标识该会场是否是设备包装来的，true该会场是设备包装来的*/
	private Boolean fromDevice;
	
	/** 成员为用户时存userId, 设备、会场都存会场id */
	private String originId;
	
	/** 仅当groupMemberType==MEMBER_DEVICE时有效 */
	private String bundleId;
	
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
	
	/** 成员状态 */
	private GroupMemberOnlineStatus onlineStatus = GroupMemberOnlineStatus.ONLINE;
	
	/** 是否是主席 */
	private Boolean isAdministrator = false;
	
//	/** 隶属业务id */
//	private Long groupId;
	
	/** 非组业务时使用，如指挥的媒体转发 ,是GroupPO的id*/
	private String businessId;
	
	/** 所属业务角色RolePO的id */
	private Long roleId;
	
	/** 所属业务角色RolePO的名称 */
	private String roleName;
	
	/** 成员的等级*/
	private Integer level;
	
	/** 拥有的终端设备 */
	private List<BusinessGroupMemberTerminalBundlePO> terminalBundles;
	
	/** 拥有的终端通道 */
	private List<BusinessGroupMemberTerminalChannelPO> channels;
	
	/** 拥有的物理屏幕 */
	private List<BusinessGroupMemberTerminalScreenPO> screens;
	
	/** 拥有的音频输出 */
	private List<BusinessGroupMemberTerminalAudioOutputPO> audioOutputs;
		
	/**--------- TerminalPO 信息 ------------*/
	
	/** id */
	private Long terminalId;
	
	/** 名称 */
	private String terminalname;

	/** 终端类型 */
	private TerminalType terminalType;
	
	/** 是否启动物理屏幕布局 */
	private Boolean physicalScreenLayout;
	
	//关联group
	private GroupPO group;
	
	@Column(name = "TERMINAL_NAME")
	public String getTerminalname() {
		return terminalname;
	}

	public void setTerminalname(String terminalname) {
		this.terminalname = terminalname;
	}

	@Column(name = "TERMINAL_TYPE")
	@Enumerated(value = EnumType.STRING)
	public TerminalType getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(TerminalType terminalType) {
		this.terminalType = terminalType;
	}

	public Boolean getPhysicalScreenLayout() {
		return physicalScreenLayout;
	}

	public void setPhysicalScreenLayout(Boolean physicalScreenLayout) {
		this.physicalScreenLayout = physicalScreenLayout;
	}

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

	@Column(name = "BUNDLE_ID")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
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

	@Column(name = "ONLINE_STATUS")
	public GroupMemberOnlineStatus getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(GroupMemberOnlineStatus onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	@Column(name = "IS_ADMINISTRATOR")
	public Boolean getIsAdministrator() {
		return isAdministrator;
	}

	public void setIsAdministrator(Boolean isAdministrator) {
		this.isAdministrator = isAdministrator;
	}

//	@Column(name = "GROUP_ID")
//	public Long getGroupId() {
//		return groupId;
//	}
//
//	public void setGroupId(Long groupId) {
//		this.groupId = groupId;
//	}

	@Column(name = "BUSINESS_ID")
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

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

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BusinessGroupMemberTerminalBundlePO> getTerminalBundles() {
		return terminalBundles;
	}

	public void setTerminalBundles(List<BusinessGroupMemberTerminalBundlePO> terminalBundles) {
		this.terminalBundles = terminalBundles;
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BusinessGroupMemberTerminalChannelPO> getChannels() {
		return channels;
	}

	public void setChannels(List<BusinessGroupMemberTerminalChannelPO> channels) {
		this.channels = channels;
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BusinessGroupMemberTerminalScreenPO> getScreens() {
		return screens;
	}

	public void setScreens(List<BusinessGroupMemberTerminalScreenPO> screens) {
		this.screens = screens;
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BusinessGroupMemberTerminalAudioOutputPO> getAudioOutputs() {
		return audioOutputs;
	}

	public void setAudioOutputs(List<BusinessGroupMemberTerminalAudioOutputPO> audioOutputs) {
		this.audioOutputs = audioOutputs;
	}

	@ManyToOne
	@JoinColumn(name = "BUSINESS_GROUP_ID")
	public GroupPO getGroup() {
		return group;
	}

	public void setGroup(GroupPO group) {
		this.group = group;
	}
	
	@Column(name = "LEVEL")
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
	@Column(name = "FROM_DEVICE")
	public Boolean getFromDevice() {
		return fromDevice;
	}

	public void setFromDevice(Boolean fromDevice) {
		this.fromDevice = fromDevice;
	}

	public void overWriteSetTerminalBundle(List<BusinessGroupMemberTerminalBundlePO> terminalBundleList){
		if(this.getTerminalBundles() == null){
			this.setTerminalBundles(terminalBundleList);
		}else{
			this.getTerminalBundles().addAll(terminalBundleList);
		}
	}
	
	public void overWriteSetTerminalChannel(List<BusinessGroupMemberTerminalChannelPO> terminalChannelList){
		if(this.getChannels() == null){
			this.setChannels(terminalChannelList);
		}else{
			this.getChannels().addAll(terminalChannelList);
		}
	}
	
	public void overWriteSetScreen(List<BusinessGroupMemberTerminalScreenPO> screenList){
		if(this.getScreens() == null){
			this.setScreens(screenList);
		}else{
			this.getScreens().addAll(screenList);
		}
	}
	
	public void overWriteSetAudioOutputs(List<BusinessGroupMemberTerminalAudioOutputPO> audioOutputList){
		if(this.getAudioOutputs() ==null){
			this.setAudioOutputs(audioOutputList);
		}else{
			this.getAudioOutputs().addAll(audioOutputList);
		}
	}
	
	public List<Long> allChannelIds(){
		List<Long> channelIds = getChannels().stream().map(BusinessGroupMemberTerminalChannelPO::getId).collect(Collectors.toList());
		return channelIds;
	}
}
