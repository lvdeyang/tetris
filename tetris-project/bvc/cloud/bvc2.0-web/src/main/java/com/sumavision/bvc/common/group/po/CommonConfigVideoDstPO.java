package com.sumavision.bvc.common.group.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.ForwardDstType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 设备组配置视频转发目的
 * @author lvdeyang
 * @date 2018年8月4日 上午11:30:58 
 */
@Entity
@Table(name="BVC_COMMON_CONFIG_VIDEO_DST")
public class CommonConfigVideoDstPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 转发目的类型 */
	private ForwardDstType type;
	
	/** 当type为ROLE时存角色id */
	private Long roleId;

	/** 当type为ROLE时存角色名称 */
	private String roleName;
	
	/** 当type为ROLE时通道类型 */
	private ChannelType roleChannelType;
	
	/** 当type为ROLE时屏幕id*/
	private String roleScreenId;
	
	/** 来自于资源层：当type为CHANNEL时存设备组成员id */
	private Long memberId;
	
	/** 来自于资源层：当type为CHANNEL时存设备组成员通道id */
	private Long memberChannelId;
	
	/** 当type为CHANNEL时存设备组成员通道别名 */
	private String memberChannelName;
	
	/** 来自于资源层：当type为CHANNEL时存接入层id */
	private String layerId;
	
	/** 来自于资源层：当type为CHANNEL时存通道id */
	private String channelId;
	
	/** 来自于资源层：当type为CHANNEL时存通道名称 */
	private String channelName;
	
	/** 来自于资源层：当type为CHANNEL时存设备id */
	private String bundleId;
	
	/** 来自于资源层：当type为CHANNEL时存设备名称 */
	private String bundleName;
	
	/** 来自于资源层：当type为CHANNEL时存设备类型 */
	private String bundleType;
	
	/** 来自于资源层：当type为CHANNEL时存设备资源类型 */
	private String venusBundleType;
	
	/** 来自于资源层：当type为SCREEN时存屏幕id */
	private String screenId;
	
	/** 来自于资源层：当type为SCREEN时存设备组成员屏幕id */
	private Long memberScreenId;
	
	/** 当type为SCREEN时存设备组成员屏幕别名 */
	private String memberScreenName;
	
	/** 关联设备组配置视频 */
	private CommonConfigVideoPO video;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public ForwardDstType getType() {
		return type;
	}

	public void setType(ForwardDstType type) {
		this.type = type;
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

	@Column(name = "ROLE_SCREEN_ID")
	public String getRoleScreenId() {
		return roleScreenId;
	}

	public void setRoleScreenId(String roleScreenId) {
		this.roleScreenId = roleScreenId;
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

	@Column(name = "MEMBER_SCREEN_ID")
	public Long getMemberScreenId() {
		return memberScreenId;
	}

	public void setMemberScreenId(Long memberScreenId) {
		this.memberScreenId = memberScreenId;
	}

	@Column(name = "MEMBER_SCREEN_NAME")
	public String getMemberScreenName() {
		return memberScreenName;
	}

	public void setMemberScreenName(String memberScreenName) {
		this.memberScreenName = memberScreenName;
	}

	@Column(name = "SCREEN_ID")
	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	@ManyToOne
	@JoinColumn(name = "VIDEO_ID")
	public CommonConfigVideoPO getVideo() {
		return video;
	}

	public void setVideo(CommonConfigVideoPO video) {
		this.video = video;
	}
	
}
