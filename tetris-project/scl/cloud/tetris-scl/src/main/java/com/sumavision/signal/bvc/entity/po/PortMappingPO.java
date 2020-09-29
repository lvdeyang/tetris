package com.sumavision.signal.bvc.entity.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.signal.bvc.entity.enumeration.DstType;
import com.sumavision.signal.bvc.entity.enumeration.SrcType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 业务bundle与接入任务之间的映射
 */
@Entity
@Table(name = "BVC_PORT_MAPPING")
public class PortMappingPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;

	private SrcType srcType;
	
	private String srcChannelId;
	
	private String srcBundleId;
	
	private String srcBundleName;
	
	private Long srcRepeaterId;
	
	private Long srcAccessId;
	
	private String srcAddress;
	
	private Long srcPort;
	
	private DstType dstType;
	
	private String dstChannelId;
	
	private String dstBundleId;
	
	private String dstBundleName;
	
	private Long dstRepeaterId;
	
	private Long dstAccessId;
	
	private String dstAddress;
	
	private Long dstPort;

	@Enumerated(EnumType.STRING)
	@Column(name = "SRC_TYPE")
	public SrcType getSrcType() {
		return srcType;
	}

	public void setSrcType(SrcType srcType) {
		this.srcType = srcType;
	}

	@Column(name = "SRC_CHANNEL_ID")
	public String getSrcChannelId() {
		return srcChannelId;
	}

	public void setSrcChannelId(String srcChannelId) {
		this.srcChannelId = srcChannelId;
	}

	@Column(name = "SRC_BUNDLE_ID")
	public String getSrcBundleId() {
		return srcBundleId;
	}

	public void setSrcBundleId(String srcBundleId) {
		this.srcBundleId = srcBundleId;
	}

	@Column(name = "SRC_REPEATER_ID")
	public Long getSrcRepeaterId() {
		return srcRepeaterId;
	}

	public void setSrcRepeaterId(Long srcRepeaterId) {
		this.srcRepeaterId = srcRepeaterId;
	}

	@Column(name = "SRC_ACCESS_ID")
	public Long getSrcAccessId() {
		return srcAccessId;
	}

	public void setSrcAccessId(Long srcAccessId) {
		this.srcAccessId = srcAccessId;
	}

	@Column(name = "SRC_ADDRESS")
	public String getSrcAddress() {
		return srcAddress;
	}

	public void setSrcAddress(String srcAddress) {
		this.srcAddress = srcAddress;
	}

	@Column(name = "SRC_PORT")
	public Long getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(Long srcPort) {
		this.srcPort = srcPort;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "DST_TYPE")
	public DstType getDstType() {
		return dstType;
	}

	public void setDstType(DstType dstType) {
		this.dstType = dstType;
	}

	@Column(name = "DST_CHANNEL_ID")
	public String getDstChannelId() {
		return dstChannelId;
	}

	public void setDstChannelId(String dstChannelId) {
		this.dstChannelId = dstChannelId;
	}

	@Column(name = "DST_BUNDLE_ID")
	public String getDstBundleId() {
		return dstBundleId;
	}

	public void setDstBundleId(String dstBundleId) {
		this.dstBundleId = dstBundleId;
	}

	@Column(name = "DST_REPEATER_ID")
	public Long getDstRepeaterId() {
		return dstRepeaterId;
	}

	public void setDstRepeaterId(Long dstRepeaterId) {
		this.dstRepeaterId = dstRepeaterId;
	}

	@Column(name = "DST_ACCESS_ID")
	public Long getDstAccessId() {
		return dstAccessId;
	}

	public void setDstAccessId(Long dstAccessId) {
		this.dstAccessId = dstAccessId;
	}

	@Column(name = "DST_ADDRESS")
	public String getDstAddress() {
		return dstAddress;
	}

	public void setDstAddress(String dstAddress) {
		this.dstAddress = dstAddress;
	}

	@Column(name = "DST_PORT")
	public Long getDstPort() {
		return dstPort;
	}

	public void setDstPort(Long dstPort) {
		this.dstPort = dstPort;
	}

	@Column(name = "SRC_BUNDLE_NAME")
	public String getSrcBundleName() {
		return srcBundleName;
	}

	public void setSrcBundleName(String srcBundleName) {
		this.srcBundleName = srcBundleName;
	}

	@Column(name = "DST_BUNDLE_NAME")
	public String getDstBundleName() {
		return dstBundleName;
	}

	public void setDstBundleName(String dstBundleName) {
		this.dstBundleName = dstBundleName;
	}
	
}
