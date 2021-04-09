package com.sumavision.tetris.bvc.business.po.member;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelType;
import com.sumavision.tetris.bvc.model.terminal.channel.ChannelParamsType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 
 * 类型概述<br/>
 * <p>对应 TerminalBundleChannelPO</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月27日 上午11:45:42
 */
@Entity
@Table(name="BVC_BUSINESS_GROUP_MEMBER_TERMINAL_BUNDLE_CHANNEL")
public class BusinessGroupMemberTerminalBundleChannelPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 通道别名 */
	private String name;
	
	/** 业务层的通道类型，这个类型与资源层的通道类型一一对应 */
	private ChannelType type;
	
	/** 来自于资源层通道id */
	private String channelId;
	
	/** 来自于资源层通道名称 */
	private String channelName;
	
	/** 来自于资源层通道类型 */
	private String baseType;
	
	/** 来自于资源层设备id */
	private String bundleId;
	
	/** 来自于资源层设备名称 */
	private String bundleName;
	
	/** 来自于资源层设备类型 */
	private String deviceModel;
	
	/** 来自资源层venus设备类型 */
	private String bundleType;
	
	/** 关联BusinessGroupMemberTerminalBundlePO */
	private BusinessGroupMemberTerminalBundlePO memberTerminalBundle;
	
	/** 所属设备终端通道，不同码率的设备通道对应为1个终端通道 */
	private BusinessGroupMemberTerminalChannelPO memberTerminalChannel;
	
	/**--------- TerminalBundleChannelPO 信息 ---------*/
	
	/** id */
	private Long terminalBundleChannelId;
	
	/** 通道音/视频编/解码类型 */
	private TerminalBundleChannelType terminalBundleChannelType;
	
	/** 自适应通道参数：超高清、高清、标清。来自 TerminalChannelBundleChannelPermissionPO */
	private ChannelParamsType channelParamsType;
	
	/** 隶属终端设备id */
	private Long terminalBundleId;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public ChannelType getType() {
		return type;
	}

	public void setType(ChannelType type) {
		this.type = type;
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

	@Column(name = "BASE_TYPE")
	public String getBaseType() {
		return baseType;
	}

	public void setBaseType(String baseType) {
		this.baseType = baseType;
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
	
	@Column(name = "DEVICE_MODEL")
	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	@Column(name = "BUNDLE_TYPE")
	public String getBundleType() {
		return bundleType;
	}

	public void setBundleType(String bundleType) {
		this.bundleType = bundleType;
	}

	@ManyToOne
	@JoinColumn(name = "MEMBER_TERMINAL_BUNDLE_ID")
	public BusinessGroupMemberTerminalBundlePO getMemberTerminalBundle() {
		return memberTerminalBundle;
	}

	public void setMemberTerminalBundle(BusinessGroupMemberTerminalBundlePO memberTerminalBundle) {
		this.memberTerminalBundle = memberTerminalBundle;
	}
	
	@ManyToOne
	@JoinColumn(name = "MEMBER_TERMINAL_CHANNEL_ID")
	public BusinessGroupMemberTerminalChannelPO getMemberTerminalChannel() {
		return memberTerminalChannel;
	}

	public void setMemberTerminalChannel(BusinessGroupMemberTerminalChannelPO memberTerminalChannel) {
		this.memberTerminalChannel = memberTerminalChannel;
	}

	@Column(name = "TERMINAL_BUNDLE_CHANNEL_ID")
	public Long getTerminalBundleChannelId() {
		return terminalBundleChannelId;
	}

	public void setTerminalBundleChannelId(Long terminalBundleChannelId) {
		this.terminalBundleChannelId = terminalBundleChannelId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TERMINAL_BUNDLE_CHANNEL_TYPE")
	public TerminalBundleChannelType getTerminalBundleChannelType() {
		return terminalBundleChannelType;
	}

	public void setTerminalBundleChannelType(TerminalBundleChannelType terminalBundleChannelType) {
		this.terminalBundleChannelType = terminalBundleChannelType;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "CHANNEL_PARAMS_TYPE")
	public ChannelParamsType getChannelParamsType() {
		return channelParamsType;
	}

	public void setChannelParamsType(ChannelParamsType channelParamsType) {
		this.channelParamsType = channelParamsType;
	}

	@Column(name = "TERMINAL_BUNDLE_ID")
	public Long getTerminalBundleId() {
		return terminalBundleId;
	}

	public void setTerminalBundleId(Long terminalBundleId) {
		this.terminalBundleId = terminalBundleId;
	}

	/**
	 * @ClassName: 通道排序器，id为 类型-编号， 按照编号从小到大排列<br/> 
	 * @author lvdeyang
	 * @date 2018年11月12日 上午8:36:10 
	 */
	public static final class ChannelComparatorFromPO implements Comparator<BusinessGroupMemberTerminalBundleChannelPO>{
		@Override
		public int compare(BusinessGroupMemberTerminalBundleChannelPO o1, BusinessGroupMemberTerminalBundleChannelPO o2) {
			
			long id1 = Long.parseLong(o1.getChannelId().split("_")[1]);
			long id2 = Long.parseLong(o2.getChannelId().split("_")[1]);
			
			if(id1 > id2){
				return 1;
			}
			if(id1 == id2){
				return 0;
			}
			return -1;
		}
	}
	
	/**
	 * @ClassName: 通道排序器，id为 类型-编号， 按照编号从小到大排列<br/> 
	 * @author lvdeyang
	 * @date 2018年8月27日 上午8:36:10 
	 */
	public static final class ChannelComparatorFromJSON implements Comparator<JSONObject>{
		@Override
		public int compare(JSONObject o1, JSONObject o2) {
			
			long id1 = Long.parseLong(o1.getString("id").split("_")[1]);
			long id2 = Long.parseLong(o2.getString("id").split("_")[1]);
			
			if(id1 > id2){
				return 1;
			}
			if(id1 == id2){
				return 0;
			}
			return -1;
		}
	}
	
	/**
	 * @ClassName: 通道排序器，id为 类型-编号， 按照编号从小到大排列<br/> 
	 * @author lvdeyang
	 * @date 2018年10月23日 上午8:36:10 
	 */
	public static final class ChannelComparatorFromDTO implements Comparator<ChannelSchemeDTO>{
		@Override
		public int compare(ChannelSchemeDTO o1, ChannelSchemeDTO o2) {
			
			long id1 = Long.parseLong(o1.getChannelId().split("_")[1]);
			long id2 = Long.parseLong(o2.getChannelId().split("_")[1]);
			
			if(id1 > id2){
				return 1;
			}
			if(id1 == id2){
				return 0;
			}
			return -1;
		}
	}
	
}
