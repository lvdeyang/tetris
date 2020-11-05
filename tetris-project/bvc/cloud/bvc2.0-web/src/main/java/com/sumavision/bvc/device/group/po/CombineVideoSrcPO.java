package com.sumavision.bvc.device.group.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.device.group.enumeration.CombineVideoSrcType;
import com.sumavision.bvc.device.group.enumeration.ForwardSrcType;
import com.sumavision.bvc.device.group.enumeration.PollingSourceVisible;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.bvc.business.bo.SourceBO;
import com.sumavision.tetris.orm.po.AbstractBasePO;
/**
 * @ClassName: 合屏视频源
 * @author zy
 * @date 2018年7月31日 下午2:35:15 
 */
@Entity
@Table(name="BVC_DEVICE_GROUP_COMBINE_VIDEO_SRC")
public class CombineVideoSrcPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 合屏源类型 */
	private CombineVideoSrcType type;
	
	/** 通道别名 */
	private String name;
	
	/** 虚拟源uuid */
	private String virtualUuid;

	/** 设备组成员id */
	private Long memberId;
	
	/** 设备成员组通道id */
	private Long memberChannelId;
	
	/** 是否隐藏该源 */
	private PollingSourceVisible visible = PollingSourceVisible.VISIBLE;
		
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
	
	/** 关联合屏的分屏 */
	private  CombineVideoPositionPO position;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public CombineVideoSrcType getType() {
		return type;
	}

	public void setType(CombineVideoSrcType type) {
		this.type = type;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "VIRTUAL_UUID")
	public String getVirtualUuid() {
		return virtualUuid;
	}

	public void setVirtualUuid(String virtualUuid) {
		this.virtualUuid = virtualUuid;
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
	@JoinColumn(name = "COMBINE_VIDEO_POSITION_ID")
	public CombineVideoPositionPO getPosition() {
		return position;
	}

	public void setPosition(CombineVideoPositionPO position) {
		this.position = position;
	}
	
	/**
	 * @Title: 从配置中复制数据<br/> 
	 * @param src 配置数据
	 * @return CombineVideoSrcPO 合屏数据
	 * @throws
	 */
	public CombineVideoSrcPO set(DeviceGroupConfigVideoSrcPO src){
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
	}
	public CombineVideoSrcPO set(com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoSrcPO src){
		if(com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoSrcType.ROLE_CHANNEL.equals(src.getCombineVideoSrcType())){
			//角色通道配置在外面二次遍历进行设置数据
			this.setType(CombineVideoSrcType.CHANNEL);
			this.setName(src.getCombineVideoSrcType().toString());//以name标记源类型
			this.setBundleId(src.getSrcId());
//			this.setBundleName(src.getRoleName());
//			this.setChannelId(src.getRoleChannelType().toString());
		}else if(com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoSrcType.VIRTUAL.equals(src.getCombineVideoSrcType())){
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
	}
	
	/**
	 * 从通道数据中复制数据<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月15日 下午1:31:23
	 * @param channel 通道数据
	 * @return
	 */
	public CombineVideoSrcPO set(DeviceGroupMemberChannelPO channel){
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
	public CombineVideoSrcPO set(SourceBO sourceBO){
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
	}
	
}
