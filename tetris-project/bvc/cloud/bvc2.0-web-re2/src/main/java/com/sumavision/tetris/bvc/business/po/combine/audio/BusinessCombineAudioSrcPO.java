package com.sumavision.tetris.bvc.business.po.combine.audio;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.bvc.command.group.enumeration.SrcType;
import com.sumavision.bvc.device.group.enumeration.CombineVideoSrcType;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalBundleChannelPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.bvc.model.agenda.SourceType;
import com.sumavision.tetris.bvc.util.MultiRateUtil;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name="BVC_BUSINESS_GROUP_COMBINE_AUDIO_SRC")
public class BusinessCombineAudioSrcPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 该源是否能找到通道源，在角色没有成员、成员没有通道等多种情况下，为false，此时该src作为占位，不实际生成合屏协议 */
	private boolean hasSource = true; 

	/** 成员id */
	private Long memberId; 
		
	/** 来自于资源管理的接入层id */
	private String layerId; 
	
	/** 来自于资源管理的通道id */
	private String channelId; 
	
	/** 来自于资源管理的通道名称 */
	private String channelName; 
	
	/** 来自于资源管理的设备id */
	private String bundleId; 
	
	/** 来自于资源管理的设备名称 */
	private String bundleName; 
	
	/** 关联混音/或单音频 */
	private BusinessCombineAudioPO combineAudio;
	
	/** 成员的终端通道BusinessGroupMemberTerminalChannelPO.getId */
	private Long memberTerminalChannelId;
	
	/** 成员的终端设备通道BusinessGroupMemberTerminalBundleChannelPO.getId */
	private Long memberTerminalBundleChannelId;
	
	/**--------- CustomAudioPO 信息，用来确定唯一性 ------------*/
	
	/** 音频源id：角色音频编码通道、会场音频编码通道 */
	private Long sourceId; 
	
	/** 音频源类型 */
	private SourceType sourceType; 

	@Column(name = "HAS_SOURCE")
	public boolean isHasSource() {
		return hasSource;
	}

	public void setHasSource(boolean hasSource) {
		this.hasSource = hasSource;
	}

	@Column(name = "MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getMemberTerminalChannelId() {
		return memberTerminalChannelId;
	}

	public void setMemberTerminalChannelId(Long memberTerminalChannelId) {
		this.memberTerminalChannelId = memberTerminalChannelId;
	}

	public Long getMemberTerminalBundleChannelId() {
		return memberTerminalBundleChannelId;
	}

	public void setMemberTerminalBundleChannelId(Long memberTerminalBundleChannelId) {
		this.memberTerminalBundleChannelId = memberTerminalBundleChannelId;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	@Enumerated(value = EnumType.STRING)
	public SourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
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
	@JoinColumn(name = "COMBINE_AUDIO_ID")
	public BusinessCombineAudioPO getCombineAudio() {
		return combineAudio;
	}

	public void setCombineAudio(BusinessCombineAudioPO combineAudio) {
		this.combineAudio = combineAudio;
	}

	public BusinessCombineAudioSrcPO(){
		this.setUpdateTime(new Date());
	}
	
	/**
	 * @Title: 从配置中复制数据<br/> 
	 * @param src 配置数据
	 * @return CombineVideoSrcPO 合屏数据
	 * @throws
	 */
	/*public BusinessCombineVideoSrcPO set(DeviceGroupConfigVideoSrcPO src){
		if(ForwardSrcType.ROLE.equals(src.getType())){
			//角色配置在外面二次遍历进行设置数据
			this.setType(CombineVideoSrcType.CHANNEL);
			this.setName(src.getType().toString());
			this.setBundleId(src.getRoleId().toString());
			this.setBundleName(src.getRoleName());
			this.setChannelId(src.getRoleChannelType().toString());
		}else if(ForwardSrcType.VIRTUAL.equals(src.getType())){
			this.setType(CombineVideoSrcType.VIRTUAL);
			this.setVirtualUuid(src.getVirtualUuid());
		}else{
			this.setType(CombineVideoSrcType.CHANNEL);
			this.setName(src.getMemberChannelName());
			this.setMemberId(src.getMemberId());
			this.setMemberChannelId(src.getMemberChannelId());
			this.setLayerId(src.getLayerId());
			this.setBundleId(src.getBundleId());
			this.setBundleName(src.getBundleName());
			this.setChannelId(src.getChannelId());
			this.setChannelName(src.getChannelName());
			this.setVisible(src.getVisible());
		}
		return this;
	}*/
	/*public CombineVideoSrcPO set(com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoSrcPO src){
		if(com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoSrcType.ROLE_CHANNEL.equals(src.getCombineVideoSrcType())){
			//角色通道配置在外面二次遍历进行设置数据
			this.setType(CombineVideoSrcType.CHANNEL);
			this.setName(src.getCombineVideoSrcType().toString());//以name标记源类型
			this.setBundleId(src.getSrcId());
//			this.setBundleName(src.getRoleName());
//			this.setChannelId(src.getRoleChannelType().toString());
		}else if(com.sumavision.tetris.bvc.model.agsenda.combine.CombineVideoSrcType.VIRTUAL.equals(src.getCombineVideoSrcType())){
			//TODO:虚拟源
			this.setType(CombineVideoSrcType.VIRTUAL);
			this.setVirtualUuid(src.getUuid());
		}else{
			//TODO:设备通道
			this.setType(CombineVideoSrcType.CHANNEL);
//			this.setName(src.getMemberChannelName());
//			this.setMemberId(src.getMemberId());
//			this.setMemberChannelId(src.getMemberChannelId());
//			this.setLayerId(src.getLayerId());
//			this.setBundleId(src.getBundleId());
//			this.setBundleName(src.getBundleName());
//			this.setChannelId(src.getChannelId());
//			this.setChannelName(src.getChannelName());
//			this.setVisible(src.getVisible());
		}
		return this;
	}*/
	
	/**
	 * 从通道数据中复制数据<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月15日 下午1:31:23
	 * @param channel 通道数据
	 * @return
	 */
	/*public BusinessCombineVideoSrcPO set(DeviceGroupMemberChannelPO channel){
		this.setType(CombineVideoSrcType.CHANNEL);
		this.setName(channel.getName());
		this.setMemberId(channel.getMember().getId());
		this.setMemberChannelId(channel.getId());
		this.setLayerId(channel.getMember().getLayerId());
		this.setBundleId(channel.getBundleId());
		this.setBundleName(channel.getBundleName());
		this.setChannelId(channel.getChannelId());
		this.setChannelName(channel.getChannelName());
		return this;
	}*/
	public BusinessCombineAudioSrcPO set(BusinessGroupMemberTerminalChannelPO terminalChannel){
		
		if(terminalChannel == null){
			this.setHasSource(false);
			return this;
		}
		
		this.setMemberTerminalChannelId(terminalChannel.getId());
		
		List<BusinessGroupMemberTerminalBundleChannelPO> bundleChannels = terminalChannel.getMemberTerminalBundleChannels();
		MultiRateUtil multiRateUtil = SpringContext.getBean(MultiRateUtil.class);
		BusinessGroupMemberTerminalBundleChannelPO srcChannel = multiRateUtil.queryDefultEncodeChannel(bundleChannels);
		if(srcChannel == null){
			this.setHasSource(false);
			return this;
		}
		
		this.setHasSource(true);
		this.setMemberId(terminalChannel.getMember().getId());
		this.setMemberTerminalBundleChannelId(srcChannel.getId());
		this.setLayerId(srcChannel.getMemberTerminalBundle().getLayerId());
		this.setBundleId(srcChannel.getBundleId());
		this.setBundleName(srcChannel.getBundleName());
		this.setChannelId(srcChannel.getChannelId());
		this.setChannelName(srcChannel.getChannelName());
		return this;
	}
	
	/**
	 * 从SourceBO复制数据<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月9日 下午8:46:10
	 * @param sourceBO
	 * @return
	 */
	/*public BusinessCombineVideoSrcPO set(SourceBO sourceBO){
		BundlePO videoBundle = sourceBO.getVideoBundle();
		ChannelSchemeDTO channel = sourceBO.getVideoSourceChannel();
		this.setType(CombineVideoSrcType.CHANNEL);
		this.setName(channel.getChannelName());
		this.setMemberId(sourceBO.getSrcVideoMemberId());
		this.setMemberChannelId(channel.getId());
		this.setLayerId(sourceBO.getVideoBundle().getAccessNodeUid());
		this.setBundleId(channel.getBundleId());
		this.setBundleName(videoBundle.getBundleName());
		this.setChannelId(channel.getChannelId());
		this.setChannelName(channel.getChannelName());
		return this;
	}*/
	
//	/** 该源是否能找到通道源，在角色没有成员、成员没有通道等多种情况下，为false，此时该src作为占位，不实际生成合屏协议 */
//	private boolean hasSource = true; 
//	/** 成员id */
//	private Long memberId; 
//	/** 来自于资源管理的接入层id */
//	private String layerId; 
//	/** 来自于资源管理的通道id */
//	private String channelId; 
//	/** 来自于资源管理的通道名称 */
//	private String channelName; 
//	/** 来自于资源管理的设备id */
//	private String bundleId; 
//	/** 来自于资源管理的设备名称 */
//	private String bundleName; 
//	/** 关联混音/或单音频 */
//	private BusinessCombineAudioPO combineAudio;
//	/** 成员的终端通道BusinessGroupMemberTerminalChannelPO.getId */
//	private Long memberTerminalChannelId;
//	/** 成员的终端设备通道BusinessGroupMemberTerminalBundleChannelPO.getId */
//	private Long memberTerminalBundleChannelId;
//	/** 音频源id：角色音频编码通道、会场音频编码通道 */
//	private Long sourceId; 
//	/** 音频源类型 */
//	private SourceType sourceType; 

	/**
	 * 复制音频源<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月15日 下午4:06:58
	 * @param src
	 * @return
	 */
	public BusinessCombineAudioSrcPO set(BusinessCombineAudioSrcPO src){
		this.setHasSource(src.isHasSource());
		this.setMemberId(src.getMemberId());
		this.setLayerId(src.getLayerId());
		this.setChannelId(src.getChannelId());
		this.setChannelName(src.getChannelName());
		this.setBundleId(src.getBundleId());
		this.setBundleName(src.getBundleName());
		this.setMemberTerminalChannelId(src.getMemberTerminalChannelId());
		this.setMemberTerminalBundleChannelId(src.getMemberTerminalBundleChannelId());
		this.setSourceId(src.getSourceId());
		this.setSourceType(src.getSourceType());
		return this;
	}
	
}
