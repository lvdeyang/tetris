package com.sumavision.bvc.device.group.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.ForwardSrcType;
import com.sumavision.bvc.device.group.enumeration.PollingSourceVisible;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name="BVC_DEVICE_GROUP_CONFIG_VIDEO_SRC")
public class DeviceGroupConfigVideoSrcPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;

	/** 转发源类型 */
	private ForwardSrcType type;
	
	/** 当type为VIRTUAL时虚拟源的uuid */
	private String virtualUuid;
	
	/** 当type为VIRTUAL时虚拟源的名称 */
	private String virtualName;
	
	/** 当type为ROLE时存角色id */
	private Long roleId;
	
	/** 当type为ROLE时存角色名称 */
	private String roleName;
	
	/** 当type为ROLE时通道类型 */
	private ChannelType roleChannelType;
	
	/** 设备组成员id */
	private Long memberId;
	
	/** 设备组成员通道id */
	private Long memberChannelId;
	
	/** 设备组成员通道别名 */
	private String memberChannelName;
	
	/** 是否隐藏该源 */
	private PollingSourceVisible visible = PollingSourceVisible.VISIBLE;
	
	/** 来自于资源层：接入层id */
	private String layerId;
	
	/** 来自于资源层：通道id */
	private String channelId;

	/** 来自于资源层：通道名称 */
	private String channelName;
	
	/** 来自于资源层：设备id */
	private String bundleId;
	
	/** 来自于资源层：设备名称 */
	private String bundleName;
	
	/** 关联分屏布局 */
	private DeviceGroupConfigVideoPositionPO position;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public ForwardSrcType getType() {
		return type;
	}

	public void setType(ForwardSrcType type) {
		this.type = type;
	}

	@Column(name = "VIRTUAL_UUID")
	public String getVirtualUuid() {
		return virtualUuid;
	}

	public void setVirtualUuid(String virtualUuid) {
		this.virtualUuid = virtualUuid;
	}

	@Column(name = "VIRTUAL_NAME")
	public String getVirtualName() {
		return virtualName;
	}

	public void setVirtualName(String virtualName) {
		this.virtualName = virtualName;
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

	@Enumerated(value = EnumType.STRING)
	@Column(name = "ROLE_CHANNEL_TYPE")
	public ChannelType getRoleChannelType() {
		return roleChannelType;
	}

	public void setRoleChannelType(ChannelType roleChannelType) {
		this.roleChannelType = roleChannelType;
	}

	@Column(name = "MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	@Column(name = "MEMBER_CHANNEL_ID")
	public Long getMemberChannelId() {
		return memberChannelId;
	}

	public void setMemberChannelId(Long memberChannelId) {
		this.memberChannelId = memberChannelId;
	}
	
	@Column(name = "MEMBER_CHANNEL_NAME")
	public String getMemberChannelName() {
		return memberChannelName;
	}

	public void setMemberChannelName(String memberChannelName) {
		this.memberChannelName = memberChannelName;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "VISIBLE")
	public PollingSourceVisible getVisible() {
		return visible;
	}

	public void setVisible(PollingSourceVisible visible) {
		this.visible = visible;
	}
	
	@Column(name = "LAYER_ID")
	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	@Column(name = "CHANNEL_ID")
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Column(name = "CHANNEL_NAME")
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

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

	@ManyToOne
	@JoinColumn(name = "POSITION_ID")
	public DeviceGroupConfigVideoPositionPO getPosition() {
		return position;
	}

	public void setPosition(DeviceGroupConfigVideoPositionPO position) {
		this.position = position;
	}
	
}
