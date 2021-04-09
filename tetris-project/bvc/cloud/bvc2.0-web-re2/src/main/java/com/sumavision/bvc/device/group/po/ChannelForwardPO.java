package com.sumavision.bvc.device.group.po;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.CombineVideoSrcType;
import com.sumavision.bvc.device.group.enumeration.ForwardDstType;
import com.sumavision.bvc.device.group.enumeration.ForwardSourceType;
import com.sumavision.bvc.device.group.enumeration.PictureType;
import com.sumavision.bvc.device.group.enumeration.ScreenLayout;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 通道转发 
 * @author zy
 * @date 2018年7月31日 下午2:10:41 
 */
@Entity
@Table(name="BVC_DEVICE_GROUP_CHANNEL_FOWARD")
public class ChannelForwardPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/*****************
	 * 以下描述源
	 *****************/
	
	/** 转发类型，枚举类型：合屏【|通道】 【|混音】【|通道】*/
	private ForwardSourceType forwardSourceType;
	
	private ForwardDstType forwardDstType;
	
	/** 目的为角色时，角色id */
	private Long roleId;
	
	/** 记录这个转发来源于哪个配置 */
	private String originVideoUuid;
	
	/** 转发类型为合屏【|混音】：存合屏【|混音】uuid */
	private String combineUuid;
	
	/** 转发类型为通道：源的设备组成员id */
	private Long sourceMemberId;
	
	/** 转发类型为通道：源的设备组成员通道id */
	private Long sourceMemberChannelId;
	
	/** 转发类型为通道，来自于资源层 */
	private String sourceLayerId;
	
	/** 转发类型为通道，来自于资源层：源的设备id */
	private String sourceBundleId;
	
	/** 转发类型为通道，来自于资源层：源的设备名称 */
	private String sourceBundleName;
	
	/** 转发类型为通道，来自于资源层：源的通道id */
	private String sourceChannelId;
	
	/** 转发类型为通道，来自于资源层：源的通道名称 */
	private String sourceChannelName;
	
	/** 转发类型为通道，通道别名 */
	private String sourceName;
	
	/*****************
	 * 以下描述目的
	 *****************/
	
	/** 设备组成员id */
	private Long memberId;
	
	/** 设备组成员通道id */
	private Long memberChannelId;
	
	/** 来自于资源层 */
	private String layerId;
	
	/** 真实的设备id（来自资源层） */
	private String bundleId;
	
	/** 真实的设备名称 （来自资源层）*/
	private String bundleName;
	
	/** 真实设备类型 （来自资源层） */
	private String venusBundleType;
	
	private String screenId;
	
	private String rectId;
	
	/** 真实的通道id（来自于资源层） */
	private String channelId;
	
	/** 真实的通道名称（来自于资源层） */
	private String channelName;
	
	/** 真实的通道别名 */
	private String name;
	
	/** 业务层的通道类型 */
	private ChannelType channelType;
	
	/** 屏幕布局 */
	private ScreenLayout layout;
	
	/************
	 * 记录屏幕覆盖
	 ************/
	
	/** 屏幕覆盖发生的通道id */
	private String overlapChannelId;
	
	private String overlapRectId;
	
	/** 屏幕覆盖横偏移 */
	private String overlapX;
	
	/** 屏幕覆盖纵偏移 */
	private String overlapY;
	
	/** 屏幕覆盖宽 */
	private String overlapW;
	
	/** 屏幕覆盖高 */
	private String overlapH;
	
	/** 关联设备组 */
	private DeviceGroupPO group;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "FORWARD_SOURCE_TYPE")
	public ForwardSourceType getForwardSourceType() {
		return forwardSourceType;
	}

	public void setForwardSourceType(ForwardSourceType forwardSourceType) {
		this.forwardSourceType = forwardSourceType;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "FORWARD_DST_TYPE")
	public ForwardDstType getForwardDstType() {
		return forwardDstType;
	}

	public void setForwardDstType(ForwardDstType forwardDstType) {
		this.forwardDstType = forwardDstType;
	}
	
	@Column(name = "ROLE_ID")
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "ORIGIN_VIDEO_UUID")
	public String getOriginVideoUuid() {
		return originVideoUuid;
	}

	public void setOriginVideoUuid(String originVideoUuid) {
		this.originVideoUuid = originVideoUuid;
	}

	@Column(name = "COMBINE_UUID")
	public String getCombineUuid() {
		return combineUuid;
	}

	public void setCombineUuid(String combineUuid) {
		this.combineUuid = combineUuid;
	}

	@Column(name = "SOURCE_MEMBER_ID")
	public Long getSourceMemberId() {
		return sourceMemberId;
	}

	public void setSourceMemberId(Long sourceMemberId) {
		this.sourceMemberId = sourceMemberId;
	}

	@Column(name = "SOURCE_MEMBER_CHANNEL_ID")
	public Long getSourceMemberChannelId() {
		return sourceMemberChannelId;
	}

	public void setSourceMemberChannelId(Long sourceMemberChannelId) {
		this.sourceMemberChannelId = sourceMemberChannelId;
	}
	
	@Column(name = "LAYER_ID")
	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	@Column(name = "SOURCE_LAYER_ID")
	public String getSourceLayerId() {
		return sourceLayerId;
	}

	public void setSourceLayerId(String sourceLayerId) {
		this.sourceLayerId = sourceLayerId;
	}

	@Column(name = "SOURCE_BUNDLE_ID")
	public String getSourceBundleId() {
		return sourceBundleId;
	}

	public void setSourceBundleId(String sourceBundleId) {
		this.sourceBundleId = sourceBundleId;
	}

	@Column(name = "SOURCE_BUNDLE_NAME")
	public String getSourceBundleName() {
		return sourceBundleName;
	}

	public void setSourceBundleName(String sourceBundleName) {
		this.sourceBundleName = sourceBundleName;
	}

	@Column(name = "SOURCE_CHANNLE_ID")
	public String getSourceChannelId() {
		return sourceChannelId;
	}

	public void setSourceChannelId(String sourceChannelId) {
		this.sourceChannelId = sourceChannelId;
	}

	@Column(name = "SOURCE_CHANNLE_NAME")
	public String getSourceChannelName() {
		return sourceChannelName;
	}

	public void setSourceChannelName(String sourceChannelName) {
		this.sourceChannelName = sourceChannelName;
	}

	@Column(name = "SOURCE_NAME")
	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
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

	@Column(name = "VENUS_BUNDLE_TYPE")
	public String getVenusBundleType() {
		return venusBundleType;
	}

	public void setVenusBundleType(String venusBundleType) {
		this.venusBundleType = venusBundleType;
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

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "CHANNEL_TYPE")
	public ChannelType getChannelType() {
		return channelType;
	}

	public void setChannelType(ChannelType channelType) {
		this.channelType = channelType;
	}

	@Column(name = "LAYOUT")
	public ScreenLayout getLayout() {
		return layout;
	}

	public void setLayout(ScreenLayout layout) {
		this.layout = layout;
	}

	@Column(name = "OVERLAP_CHANNEL_ID")
	public String getOverlapChannelId() {
		return overlapChannelId;
	}

	public void setOverlapChannelId(String overlapChannelId) {
		this.overlapChannelId = overlapChannelId;
	}

	@Column(name = "OVERLAP_X")
	public String getOverlapX() {
		return overlapX;
	}

	public void setOverlapX(String overlapX) {
		this.overlapX = overlapX;
	}

	@Column(name = "OVERLAP_Y")
	public String getOverlapY() {
		return overlapY;
	}

	public void setOverlapY(String overlapY) {
		this.overlapY = overlapY;
	}

	@Column(name = "OVERLAP_W")
	public String getOverlapW() {
		return overlapW;
	}

	public void setOverlapW(String overlapW) {
		this.overlapW = overlapW;
	}

	@Column(name = "OVERLAP_H")
	public String getOverlapH() {
		return overlapH;
	}

	public void setOverlapH(String overlapH) {
		this.overlapH = overlapH;
	}

	@Column(name = "SCREEN_ID")
	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	@Column(name = "RECT_ID")
	public String getRectId() {
		return rectId;
	}

	public void setRectId(String rectId) {
		this.rectId = rectId;
	}

	@Column(name = "OVERLAP_RECT_ID")
	public String getOverlapRectId() {
		return overlapRectId;
	}

	public void setOverlapRectId(String overlapRectId) {
		this.overlapRectId = overlapRectId;
	}

	@ManyToOne
	@JoinColumn(name = "GROUP_ID")
	public DeviceGroupPO getGroup() {
		return group;
	}

	public void setGroup(DeviceGroupPO group) {
		this.group = group;
	}
	
	/**
	 * @Title: 生成视频转发 <br/>
	 * @param src 源通道
	 * @param dst 目的通道
	 * @return ChannelFowardPO 视频转发数据
	 */
	public ChannelForwardPO generateVideoForward(DeviceGroupConfigVideoSrcPO src, DeviceGroupConfigVideoDstPO dst){
		this.setForwardSourceType(ForwardSourceType.FORWARVIDEO);
		this.setForwardDstType(ForwardDstType.CHANNEL);
		this.setCombineUuid(null);
		this.setSourceMemberId(src.getMemberId());
		this.setSourceMemberChannelId(src.getMemberChannelId());
		this.setSourceLayerId(src.getLayerId());
		this.setSourceBundleId(src.getBundleId());
		this.setSourceBundleName(src.getBundleName());
		this.setSourceChannelId(src.getChannelId());
		this.setSourceChannelName(src.getChannelName());
		this.setSourceName(src.getMemberChannelName());
		this.setLayerId(dst.getLayerId());
		this.setMemberId(dst.getMemberId());
		this.setMemberChannelId(dst.getMemberChannelId());
		this.setChannelId(dst.getChannelId());
		this.setChannelName(dst.getChannelName());
		this.setName(dst.getMemberChannelName());
		this.setBundleId(dst.getBundleId());
		this.setBundleName(dst.getBundleName());
		this.setVenusBundleType(dst.getVenusBundleType());
		this.setOverlapChannelId(null);
		this.setOverlapX(null);
		this.setOverlapY(null);
		this.setOverlapW(null);
		this.setOverlapH(null);
		return this;
	}
	
	/**
	 * @Title: 生成视频转发 <br/>
	 * @param src 源通道
	 * @param dst 目的通道
	 * @return ChannelFowardPO 视频转发数据
	 */
	public ChannelForwardPO generateVideoForward(DeviceGroupConfigVideoSrcPO src, DeviceGroupMemberChannelPO dst){
		this.setForwardSourceType(ForwardSourceType.FORWARVIDEO);
		this.setForwardDstType(ForwardDstType.CHANNEL);
		if(src != null){
			this.setSourceMemberId(src.getMemberId());
			this.setSourceMemberChannelId(src.getMemberChannelId());
			this.setSourceLayerId(src.getLayerId());
			this.setSourceBundleId(src.getBundleId());
			this.setSourceBundleName(src.getBundleName());
			this.setSourceChannelId(src.getChannelId());
			this.setSourceChannelName(src.getChannelName());
			this.setSourceName(src.getMemberChannelName());
		}
		this.setCombineUuid(null);
		this.setLayerId(dst.getMember().getLayerId());
		this.setMemberId(dst.getMember().getId());
		this.setMemberChannelId(dst.getId());
		this.setChannelId(dst.getChannelId());
		this.setChannelName(dst.getChannelName());
		this.setChannelType(dst.getType());
		this.setBundleId(dst.getBundleId());
		this.setBundleName(dst.getBundleName());
		this.setVenusBundleType(dst.getVenusBundleType());
		this.setName(dst.getName());
		this.setOverlapChannelId(null);
		this.setOverlapX(null);
		this.setOverlapY(null);
		this.setOverlapW(null);
		this.setOverlapH(null);
		return this;
	}
	
	/**
	 * @Title: 生成视频转发 -- 角色 <br/>
	 * @param src 源通道
	 * @param role 目标角色
	 * @param dst 目的通道
	 * @return ChannelFowardPO 视频转发数据
	 */
	public ChannelForwardPO generateVideoForward(DeviceGroupConfigVideoSrcPO src, DeviceGroupBusinessRolePO role, DeviceGroupMemberChannelPO dst){
		this.setForwardSourceType(ForwardSourceType.FORWARVIDEO);
		this.setForwardDstType(ForwardDstType.ROLE);
		if(src != null){
			this.setSourceMemberId(src.getMemberId());
			this.setSourceMemberChannelId(src.getMemberChannelId());
			this.setSourceLayerId(src.getLayerId());
			this.setSourceBundleId(src.getBundleId());
			this.setSourceBundleName(src.getBundleName());
			this.setSourceChannelId(src.getChannelId());
			this.setSourceChannelName(src.getChannelName());
			this.setSourceName(src.getMemberChannelName());
		}
		this.setCombineUuid(null);
		this.setLayerId(role.getLayerId());
		this.setRoleId(role.getId());
		this.setChannelId(dst.getChannelId());
		this.setChannelName(dst.getChannelName());
		this.setChannelType(dst.getType());
		this.setBundleId(role.getBundleId());
		this.setBundleName(role.getName());
		this.setVenusBundleType(role.getBaseType());
		this.setName(dst.getName());
		this.setOverlapChannelId(null);
		this.setOverlapX(null);
		this.setOverlapY(null);
		this.setOverlapW(null);
		this.setOverlapH(null);
		
		return this;
	}
	
	/**
	 * @Title: 生成小屏视频转发 <br/>
	 * @param src 源通道
	 * @param dst 目的通道
	 * @return ChannelFowardPO 视频转发数据
	 */
	public ChannelForwardPO generateVideoForward(DeviceGroupConfigVideoSmallSrcPO src, DeviceGroupMemberChannelPO dst){
		this.setForwardSourceType(ForwardSourceType.FORWARVIDEO);
		this.setForwardDstType(ForwardDstType.CHANNEL);
		if(src != null){
			this.setSourceMemberId(src.getMemberId());
			this.setSourceMemberChannelId(src.getMemberChannelId());
			this.setSourceLayerId(src.getLayerId());
			this.setSourceBundleId(src.getBundleId());
			this.setSourceBundleName(src.getBundleName());
			this.setSourceChannelId(src.getChannelId());
			this.setSourceChannelName(src.getChannelName());
			this.setSourceName(src.getMemberChannelName());
		}
		this.setCombineUuid(null);
		this.setLayerId(dst.getMember().getLayerId());
		this.setMemberId(dst.getMember().getId());
		this.setMemberChannelId(dst.getId());
		this.setChannelId(dst.getChannelId());
		this.setChannelName(dst.getChannelName());
		this.setChannelType(dst.getType());
		this.setBundleId(dst.getBundleId());
		this.setBundleName(dst.getBundleName());
		this.setVenusBundleType(dst.getVenusBundleType());
		this.setName(dst.getName());
		this.setOverlapChannelId(null);
		this.setOverlapX(null);
		this.setOverlapY(null);
		this.setOverlapW(null);
		this.setOverlapH(null);
		return this;
	}
	
	/**
	 * @Title: 生成小屏视频转发--角色 <br/>
	 * @param src 源通道
	 * @param dst 目的通道
	 * @return ChannelFowardPO 视频转发数据
	 */
	public ChannelForwardPO generateVideoForward(DeviceGroupConfigVideoSmallSrcPO src, DeviceGroupBusinessRolePO role, DeviceGroupMemberChannelPO dst){
		this.setForwardSourceType(ForwardSourceType.FORWARVIDEO);
		this.setForwardDstType(ForwardDstType.ROLE);
		if(src != null){
			this.setSourceMemberId(src.getMemberId());
			this.setSourceMemberChannelId(src.getMemberChannelId());
			this.setSourceLayerId(src.getLayerId());
			this.setSourceBundleId(src.getBundleId());
			this.setSourceBundleName(src.getBundleName());
			this.setSourceChannelId(src.getChannelId());
			this.setSourceChannelName(src.getChannelName());
			this.setSourceName(src.getMemberChannelName());
		}
		this.setCombineUuid(null);
		this.setLayerId(role.getLayerId());
		this.setRoleId(role.getId());
		this.setChannelId(dst.getChannelId());
		this.setChannelName(dst.getChannelName());
		this.setChannelType(dst.getType());
		this.setBundleId(role.getBundleId());
		this.setBundleName(role.getName());
		this.setVenusBundleType(role.getBaseType());
		this.setName(dst.getName());
		this.setOverlapChannelId(null);
		this.setOverlapX(null);
		this.setOverlapY(null);
		this.setOverlapW(null);
		this.setOverlapH(null);
		return this;
	}
	
	/**
	 * 生成通道转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月18日 上午11:01:12
	 * @param src 源通道
	 * @param dst 目的通道
	 * @return ChannelForwardPO 通道转发
	 */
	public ChannelForwardPO generateVideoForward(DeviceGroupMemberChannelPO src, DeviceGroupMemberChannelPO dst){
		this.setForwardSourceType(ForwardSourceType.FORWARVIDEO);
		this.setForwardDstType(ForwardDstType.CHANNEL);
		if(src != null){
			this.setSourceMemberId(src.getMember().getId());
			this.setSourceMemberChannelId(src.getId());
			this.setSourceLayerId(src.getMember().getLayerId());
			this.setSourceBundleId(src.getBundleId());
			this.setSourceBundleName(src.getBundleName());
			this.setSourceChannelId(src.getChannelId());
			this.setSourceChannelName(src.getChannelName());
			this.setSourceName(src.getName());
		}
		this.setCombineUuid(null);
		this.setLayerId(dst.getMember().getLayerId());
		this.setMemberId(dst.getMember().getId());
		this.setMemberChannelId(dst.getId());
		this.setChannelId(dst.getChannelId());
		this.setChannelName(dst.getChannelName());
		this.setChannelType(dst.getType());
		this.setBundleId(dst.getBundleId());
		this.setBundleName(dst.getBundleName());
		this.setVenusBundleType(dst.getVenusBundleType());
		this.setName(dst.getName());
		this.setOverlapChannelId(null);
		this.setOverlapX(null);
		this.setOverlapY(null);
		this.setOverlapW(null);
		this.setOverlapH(null);
		return this;
	}
	
	/**
	 * 生成角色通道转发<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月31日 上午11:01:12
	 * @param src 源通道
	 * @param role 目的角色
	 * @param dst 目的通道
	 * @return ChannelForwardPO 通道转发
	 */
	public ChannelForwardPO generateVideoForward(DeviceGroupMemberChannelPO src, DeviceGroupBusinessRolePO role, DeviceGroupMemberChannelPO dst){
		this.setForwardSourceType(ForwardSourceType.FORWARVIDEO);
		this.setForwardDstType(ForwardDstType.ROLE);
		if(src != null){
			this.setSourceMemberId(src.getMember().getId());
			this.setSourceMemberChannelId(src.getId());
			this.setSourceLayerId(src.getMember().getLayerId());
			this.setSourceBundleId(src.getBundleId());
			this.setSourceBundleName(src.getBundleName());
			this.setSourceChannelId(src.getChannelId());
			this.setSourceChannelName(src.getChannelName());
			this.setSourceName(src.getName());
		}
		this.setCombineUuid(null);
		this.setLayerId(role.getLayerId());
		this.setRoleId(role.getId());
		this.setChannelId(dst.getChannelId());
		this.setChannelName(dst.getChannelName());
		this.setChannelType(dst.getType());
		this.setBundleId(role.getBundleId());
		this.setBundleName(role.getName());
		this.setVenusBundleType(role.getBaseType());
		this.setName(dst.getName());
		this.setOverlapChannelId(null);
		this.setOverlapX(null);
		this.setOverlapY(null);
		this.setOverlapW(null);
		this.setOverlapH(null);
		return this;
	}
	
	/**
	 * @Title: 生成合屏转发<br/> 
	 * @param combineVideoUuid 合屏uuid
	 * @param decodeChannel 目的通道
	 * @return ChannelFowardPO 合屏转发数据
	 */
	public ChannelForwardPO generateCombineVideoForward(String combineVideoUuid, DeviceGroupConfigVideoDstPO dst){
		this.setSourceMemberId(null);
		this.setSourceMemberChannelId(null);
		this.setSourceLayerId(null);
		this.setSourceBundleId(null);
		this.setSourceBundleName(null);
		this.setSourceChannelId(null);
		this.setSourceChannelName(null);
		this.setSourceName(null);
		this.setForwardSourceType(ForwardSourceType.COMBINEVIDEO);
		this.setForwardDstType(ForwardDstType.CHANNEL);
		this.setCombineUuid(combineVideoUuid);
		this.setMemberId(dst.getMemberId());
		this.setMemberChannelId(dst.getMemberChannelId());
		this.setLayerId(dst.getLayerId());
		this.setChannelId(dst.getChannelId());
		this.setChannelName(dst.getChannelName());
		this.setBundleId(dst.getBundleId());
		this.setBundleName(dst.getBundleName());
		this.setVenusBundleType(dst.getVenusBundleType());
		this.setName(dst.getMemberChannelName());
		this.setOverlapChannelId(null);
		this.setOverlapX(null);
		this.setOverlapY(null);
		this.setOverlapW(null);
		this.setOverlapH(null);
		return this;
	}
	
	/**
	 * @Title: 生成合屏转发<br/> 
	 * @param combineVideoUuid 合屏uuid
	 * @param decodeChannel 目的通道
	 * @return ChannelFowardPO 合屏转发数据
	 */
	public ChannelForwardPO generateCombineVideoForward(String combineVideoUuid, DeviceGroupMemberChannelPO dst){
		this.setSourceMemberId(null);
		this.setSourceMemberChannelId(null);
		this.setSourceLayerId(null);
		this.setSourceBundleId(null);
		this.setSourceBundleName(null);
		this.setSourceChannelId(null);
		this.setSourceChannelName(null);
		this.setSourceName(null);
		
		this.setForwardSourceType(ForwardSourceType.COMBINEVIDEO);
		this.setForwardDstType(ForwardDstType.CHANNEL);
		this.setCombineUuid(combineVideoUuid);
		this.setMemberId(dst.getMember().getId());
		this.setMemberChannelId(dst.getId());
		this.setLayerId(dst.getMember().getLayerId());
		this.setChannelId(dst.getChannelId());
		this.setChannelName(dst.getChannelName());
		this.setChannelType(dst.getType());
		this.setBundleId(dst.getBundleId());
		this.setBundleName(dst.getBundleName());
		this.setVenusBundleType(dst.getVenusBundleType());
		this.setName(dst.getName());
		this.setOverlapChannelId(null);
		this.setOverlapX(null);
		this.setOverlapY(null);
		this.setOverlapW(null);
		this.setOverlapH(null);
		return this;
	}
	
	/**
	 * @Title: 生成合屏转发--role<br/> 
	 * @param combineVideoUuid 合屏uuid
	 * @param decodeChannel 目的通道
	 * @return ChannelFowardPO 合屏转发数据
	 */
	public ChannelForwardPO generateCombineVideoForward(String combineVideoUuid, DeviceGroupMemberChannelPO dst, DeviceGroupBusinessRolePO role){
		this.setSourceMemberId(null);
		this.setSourceMemberChannelId(null);
		this.setSourceLayerId(null);
		this.setSourceBundleId(null);
		this.setSourceBundleName(null);
		this.setSourceChannelId(null);
		this.setSourceChannelName(null);
		this.setSourceName(null);
		
		this.setForwardSourceType(ForwardSourceType.COMBINEVIDEO);
		this.setForwardDstType(ForwardDstType.ROLE);
		this.setCombineUuid(combineVideoUuid);
		this.setLayerId(role.getLayerId());
		this.setRoleId(role.getId());
		this.setChannelId(dst.getChannelId());
		this.setChannelName(dst.getChannelName());
		this.setChannelType(dst.getType());
		this.setBundleId(role.getBundleId());
		this.setBundleName(role.getName());
		this.setVenusBundleType(role.getBaseType());
		this.setName(dst.getName());
		this.setOverlapChannelId(null);
		this.setOverlapX(null);
		this.setOverlapY(null);
		this.setOverlapW(null);
		this.setOverlapH(null);
		return this;
	}
	
	/**
	 * @Title: 设置合屏转发的屏幕覆盖 <br/>
	 * @param combineVideo 合屏
	 * @return ChannelForwardPO 合屏转发数据
	 */
	public ChannelForwardPO generateOverlap(CombineVideoPO combineVideo){
		String bundleId = this.getBundleId();
		CombineVideoPositionPO overlap = null;
		Set<CombineVideoPositionPO> positions = combineVideo.getPositions();
		if(positions!=null && positions.size()>0){
			for(CombineVideoPositionPO position:positions){
				if(PictureType.POLLING.equals(position.getPictureType())) continue;
				List<CombineVideoSrcPO> srcs = position.getSrcs();
				if(srcs==null || srcs.size()<=0) continue;
				CombineVideoSrcPO src = srcs.iterator().next();
				if(src.getType().equals(CombineVideoSrcType.CHANNEL) && src.getBundleId().equals(bundleId)){
					overlap = position;
					break;
				}
			}
		}
		
		if(overlap != null){
			//设置屏幕覆盖
			this.setOverlapChannelId(overlap.getSrcs().iterator().next().getChannelId());
			this.setOverlapX(overlap.getX());
			this.setOverlapY(overlap.getY());
			this.setOverlapW(overlap.getW());
			this.setOverlapH(overlap.getH());
		}
		
		return this;
	}
	
	/**
	 * @Title: 生成音频转发<br/> 
	 * @param encodeChannel 源通道
	 * @param decodeChannel 目的通道
	 * @return ChannelFowardPO 音频转发数据
	 */
	public ChannelForwardPO generateAudioForward(DeviceGroupMemberChannelPO encodeChannel, DeviceGroupMemberChannelPO decodeChannel){
		this.setForwardSourceType(ForwardSourceType.FORWARDAUDIO);
		this.setForwardDstType(ForwardDstType.CHANNEL);
		this.setSourceMemberId(encodeChannel.getMember().getId());
		this.setSourceMemberChannelId(encodeChannel.getId());
		this.setSourceLayerId(encodeChannel.getMember().getLayerId());
		this.setSourceBundleId(encodeChannel.getBundleId());
		this.setSourceBundleName(encodeChannel.getBundleName());
		this.setSourceChannelId(encodeChannel.getChannelId());
		this.setSourceChannelName(encodeChannel.getChannelName());
		this.setSourceName(encodeChannel.getName());
		this.setMemberId(decodeChannel.getMember().getId());
		this.setMemberChannelId(decodeChannel.getId());
		this.setLayerId(decodeChannel.getMember().getLayerId());
		this.setBundleId(decodeChannel.getBundleId());
		this.setBundleName(decodeChannel.getBundleName());
		this.setVenusBundleType(decodeChannel.getVenusBundleType());
		this.setChannelId(decodeChannel.getChannelId());
		this.setChannelName(decodeChannel.getChannelName());
		this.setName(decodeChannel.getName());
		this.setOverlapChannelId(null);
		this.setOverlapX(null);
		this.setOverlapY(null);
		this.setOverlapW(null);
		this.setOverlapH(null);
		return this;
	}
	
	/**
	 * @Title: 生成混音转发<br/> 
	 * @param combineAudioUuid 混音uuid
	 * @param decodeChannel 目的通道
	 * @return ChannelFowardPO 混音转发数据
	 */
	public ChannelForwardPO generateCombineAudioForward(String combineAudioUuid, DeviceGroupMemberChannelPO decodeChannel){
		this.setForwardSourceType(ForwardSourceType.COMBINEAUDIO);
		this.setForwardDstType(ForwardDstType.CHANNEL);
		this.setCombineUuid(combineAudioUuid);
		this.setMemberId(decodeChannel.getMember().getId());
		this.setMemberChannelId(decodeChannel.getId());
		this.setLayerId(decodeChannel.getMember().getLayerId());
		this.setBundleId(decodeChannel.getBundleId());
		this.setBundleName(decodeChannel.getBundleName());
		this.setVenusBundleType(decodeChannel.getVenusBundleType());
		this.setChannelId(decodeChannel.getChannelId());
		this.setChannelName(decodeChannel.getChannelName());
		this.setName(decodeChannel.getName());
		this.setOverlapChannelId(null);
		this.setOverlapX(null);
		this.setOverlapY(null);
		this.setOverlapW(null);
		this.setOverlapH(null);
		return this;
	}
	
	/**
	 * @Title: 复制转发源<br/> 
	 * @param from 复制对象
	 * @return ChannelFowardPO 
	 */
	public ChannelForwardPO copySource(ChannelForwardPO from){
		this.setForwardSourceType(from.getForwardSourceType());
		if(this.getForwardSourceType().equals(ForwardSourceType.COMBINEAUDIO) || 
				this.getForwardSourceType().equals(ForwardSourceType.COMBINEVIDEO)){
			this.setCombineUuid(from.getCombineUuid());
		}else{
			this.setSourceMemberId(from.getSourceMemberId());
			this.setSourceMemberChannelId(from.getSourceMemberChannelId());
			this.setBundleId(from.getBundleId());
			this.setBundleName(from.getBundleName());
			this.setChannelId(from.getChannelId());
			this.setChannelName(from.getChannelName());
		}
		return this;
	}
	
}
