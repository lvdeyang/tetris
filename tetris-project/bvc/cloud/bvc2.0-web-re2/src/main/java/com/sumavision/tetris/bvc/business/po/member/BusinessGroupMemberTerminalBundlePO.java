package com.sumavision.tetris.bvc.business.po.member;

import java.util.List;
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

import com.sumavision.tetris.bvc.model.terminal.TerminalBundleType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 
 * 类型概述<br/>
 * <p>对应 TerminalBundlePO；参考了 DeviceGroupMemberPO</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月27日 上午11:44:54
 */
@Entity
@Table(name="BVC_BUSINESS_GROUP_MEMBER_TERMINAL_BUNDLE")
public class BusinessGroupMemberTerminalBundlePO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 设备id */
	private String bundleId;

	/** 设备名称 */
	private String bundleName;

	/** 号码，通常11位 */
	private String username;
	
	/** 设备类型 资源层的deviceModel jv210、jv230、tvos、ipc、mobile */
	private String deviceModel;
	
	/** 资源设备类型 */
	private String bundleType;
	
	/** 执行层id, 对应资源层nodeUid*/
	private String layerId;
	
	/** 所属文件夹id*/
	private Long folderId;	
	
	/** 关联成员 */
	private BusinessGroupMemberPO member;
	
	/** 是否开启组播编码 */
	private Boolean multicastEncode;
	
	/** 组播编码地址 */
	private String multicastEncodeAddr;
	
	/** 组播源ip */
	private String multicastSourceIp;
	
	/** 所拥有的终端设备通道 */
	private List<BusinessGroupMemberTerminalBundleChannelPO> memberTerminalBundleChannels;
	
	/** TerminalBundlePO 信息 */
	
	/** id */
	private String terminalBundleId;
	
	/** 名称 */
	private String terminalBundleName;
	
	/** 终端设备编解码类型 */
	private TerminalBundleType terminalBundleType;
	
	/** 隶属终端id */
	private Long terminalId;

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
	
	@Column(name = "USERNAME")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	@ManyToOne
	@JoinColumn(name = "MEMBER_ID")
	public BusinessGroupMemberPO getMember() {
		return member;
	}

	public void setMember(BusinessGroupMemberPO member) {
		this.member = member;
	}

	@OneToMany(mappedBy = "memberTerminalBundle", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BusinessGroupMemberTerminalBundleChannelPO> getMemberTerminalBundleChannels() {
		return memberTerminalBundleChannels;
	}

	public void setMemberTerminalBundleChannels(List<BusinessGroupMemberTerminalBundleChannelPO> memberTerminalBundleChannels) {
		this.memberTerminalBundleChannels = memberTerminalBundleChannels;
	}

	@Column(name = "TERMINAL_BUNDLE_ID")
	public String getTerminalBundleId() {
		return terminalBundleId;
	}

	public void setTerminalBundleId(String terminalBundleId) {
		this.terminalBundleId = terminalBundleId;
	}

	@Column(name = "TERMINAL_BUNDLE_NAME")
	public String getTerminalBundleName() {
		return terminalBundleName;
	}

	public void setTerminalBundleName(String terminalBundleName) {
		this.terminalBundleName = terminalBundleName;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TERMINAL_BUNDLE_TYPE")
	public TerminalBundleType getTerminalBundleType() {
		return terminalBundleType;
	}

	public void setTerminalBundleType(TerminalBundleType terminalBundleType) {
		this.terminalBundleType = terminalBundleType;
	}

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	@Column(name = "MULTICAST_ENCODE")
	public Boolean getMulticastEncode() {
		return multicastEncode;
	}

	public void setMulticastEncode(Boolean multicastEncode) {
		this.multicastEncode = multicastEncode;
	}

	@Column(name = "MULTICAST_ENCODE_ADDR")
	public String getMulticastEncodeAddr() {
		return multicastEncodeAddr;
	}

	public void setMulticastEncodeAddr(String multicastEncodeAddr) {
		this.multicastEncodeAddr = multicastEncodeAddr;
	}

	@Column(name = "MULTICAST_SOURCE_IP")
	public String getMulticastSourceIp() {
		return multicastSourceIp;
	}

	public void setMulticastSourceIp(String multicastSourceIp) {
		this.multicastSourceIp = multicastSourceIp;
	}
}
