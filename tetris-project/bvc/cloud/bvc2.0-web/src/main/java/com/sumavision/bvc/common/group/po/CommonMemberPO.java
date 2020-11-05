package com.sumavision.bvc.common.group.po;

import java.util.Set;

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

import com.sumavision.bvc.device.group.enumeration.MemberStatus;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name="BVC_COMMON_MEMBER")
public class CommonMemberPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 设备id */
	private String bundleId;

	/** 设备名称 */
	private String bundleName;
	
	/** 设备类型 jv210、jv230、tvos、ipc、mobile */
	private String bundleType;
	
	/** 资源设备类型 */
	private String venusBundleType;
	
	/** 执行层id, 对应资源层nodeUid*/
	private String layerId;
	
	/** 所属文件夹id*/
	private Long folderId;
	
	/** 设备分类这里面还没有定 */
	
	/** 所属业务角色id（这里是会议关联的业务角色，并不是系统级的业务角色） */
	private Long roleId;
	
	/** 所属业务角色名称（这里是会议关联的业务角色，并不是系统级的业务角色） */
	private String roleName;
	
	/** 是否开启音频 */
	private boolean openAudio;
	
	/***********
	 * 主要机顶盒用 *
	 **********/
	
	/** 设备状态 */
	private MemberStatus memberStatus = MemberStatus.DISCONNECT;
	
	/** 设备是否是管理员 */
	private boolean isAdministrator = false;
	
	/** 设备userId */
	private Long userId;
	
	/** 关联设备组 */
	private CommonGroupPO group;
	
	/** 关联设备组成员通道 */
	private Set<CommonMemberChannelPO> channels;
	
	/** 关联设备组成员屏幕 */
	private Set<CommonMemberScreenPO> screens;

	@Column(name = "BUNDLE_ID")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	@Column(name = "BUNDLE_NAME")
	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}
	
	@Column(name = "BUNDLE_TYPE")
	public String getBundleType() {
		return bundleType;
	}

	public void setBundleType(String bundleType) {
		this.bundleType = bundleType;
	}

	@Column(name = "VENUS_BUNDLE_TYPE")
	public String getVenusBundleType() {
		return venusBundleType;
	}

	public void setVenusBundleType(String venusBundleType) {
		this.venusBundleType = venusBundleType;
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
	
	@Column(name = "OPEN_AUDIO")
	public boolean isOpenAudio() {
		return openAudio;
	}

	public void setOpenAudio(boolean openAudio) {
		this.openAudio = openAudio;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "MEMBER_STATUS")
	public MemberStatus getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(MemberStatus memberStatus) {
		this.memberStatus = memberStatus;
	}

	@Column(name = "ADMINISTRATOR")
	public boolean isAdministrator() {
		return isAdministrator;
	}

	public void setAdministrator(boolean isAdministrator) {
		this.isAdministrator = isAdministrator;
	}

	@Column(name = "userId")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@ManyToOne
	@JoinColumn(name = "GROUP_ID")
	public CommonGroupPO getGroup() {
		return group;
	}

	public void setGroup(CommonGroupPO group) {
		this.group = group;
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CommonMemberChannelPO> getChannels() {
		return channels;
	}

	public void setChannels(Set<CommonMemberChannelPO> channels) {
		this.channels = channels;
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CommonMemberScreenPO> getScreens() {
		return screens;
	}

	public void setScreens(Set<CommonMemberScreenPO> screens) {
		this.screens = screens;
	}

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}
}
