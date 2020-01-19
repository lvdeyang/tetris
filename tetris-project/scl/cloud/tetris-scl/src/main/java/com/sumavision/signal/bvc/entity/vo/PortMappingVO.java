package com.sumavision.signal.bvc.entity.vo;

import com.sumavision.signal.bvc.entity.enumeration.DstType;
import com.sumavision.signal.bvc.entity.enumeration.SrcType;
import com.sumavision.signal.bvc.entity.po.PortMappingPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class PortMappingVO extends AbstractBaseVO<PortMappingVO, PortMappingPO>{

	private Long id;
	
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
	
	private String taskId;
	
	private String netIp;

	public Long getId() {
		return id;
	}

	public PortMappingVO setId(Long id) {
		this.id = id;
		return this;
	}

	public SrcType getSrcType() {
		return srcType;
	}

	public PortMappingVO setSrcType(SrcType srcType) {
		this.srcType = srcType;
		return this;
	}

	public String getSrcChannelId() {
		return srcChannelId;
	}

	public PortMappingVO setSrcChannelId(String srcChannelId) {
		this.srcChannelId = srcChannelId;
		return this;
	}

	public String getSrcBundleId() {
		return srcBundleId;
	}

	public PortMappingVO setSrcBundleId(String srcBundleId) {
		this.srcBundleId = srcBundleId;
		return this;
	}

	public String getSrcBundleName() {
		return srcBundleName;
	}

	public PortMappingVO setSrcBundleName(String srcBundleName) {
		this.srcBundleName = srcBundleName;
		return this;
	}

	public Long getSrcRepeaterId() {
		return srcRepeaterId;
	}

	public PortMappingVO setSrcRepeaterId(Long srcRepeaterId) {
		this.srcRepeaterId = srcRepeaterId;
		return this;
	}

	public Long getSrcAccessId() {
		return srcAccessId;
	}

	public PortMappingVO setSrcAccessId(Long srcAccessId) {
		this.srcAccessId = srcAccessId;
		return this;
	}

	public String getSrcAddress() {
		return srcAddress;
	}

	public PortMappingVO setSrcAddress(String srcAddress) {
		this.srcAddress = srcAddress;
		return this;
	}

	public Long getSrcPort() {
		return srcPort;
	}

	public PortMappingVO setSrcPort(Long srcPort) {
		this.srcPort = srcPort;
		return this;
	}

	public DstType getDstType() {
		return dstType;
	}

	public PortMappingVO setDstType(DstType dstType) {
		this.dstType = dstType;
		return this;
	}

	public String getDstChannelId() {
		return dstChannelId;
	}

	public PortMappingVO setDstChannelId(String dstChannelId) {
		this.dstChannelId = dstChannelId;
		return this;
	}

	public String getDstBundleId() {
		return dstBundleId;
	}

	public PortMappingVO setDstBundleId(String dstBundleId) {
		this.dstBundleId = dstBundleId;
		return this;
	}

	public String getDstBundleName() {
		return dstBundleName;
	}

	public PortMappingVO setDstBundleName(String dstBundleName) {
		this.dstBundleName = dstBundleName;
		return this;
	}

	public Long getDstRepeaterId() {
		return dstRepeaterId;
	}

	public PortMappingVO setDstRepeaterId(Long dstRepeaterId) {
		this.dstRepeaterId = dstRepeaterId;
		return this;
	}

	public Long getDstAccessId() {
		return dstAccessId;
	}

	public PortMappingVO setDstAccessId(Long dstAccessId) {
		this.dstAccessId = dstAccessId;
		return this;
	}

	public String getDstAddress() {
		return dstAddress;
	}

	public PortMappingVO setDstAddress(String dstAddress) {
		this.dstAddress = dstAddress;
		return this;
	}

	public Long getDstPort() {
		return dstPort;
	}

	public PortMappingVO setDstPort(Long dstPort) {
		this.dstPort = dstPort;
		return this;
	}

	public String getTaskId() {
		return taskId;
	}

	public PortMappingVO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getNetIp() {
		return netIp;
	}

	public PortMappingVO setNetIp(String netIp) {
		this.netIp = netIp;
		return this;
	}

	@Override
	public PortMappingVO set(PortMappingPO entity) throws Exception {
		this.setDstAccessId(entity.getDstAccessId())
			.setDstAddress(entity.getDstAddress())
			.setDstBundleId(entity.getDstBundleId())
			.setDstBundleName(entity.getDstBundleName())
			.setDstChannelId(entity.getDstChannelId())
			.setDstPort(entity.getDstPort())
			.setDstRepeaterId(entity.getDstRepeaterId())
			.setDstType(entity.getDstType())
			.setId(entity.getId())
			.setSrcAccessId(entity.getSrcAccessId())
			.setSrcAddress(entity.getSrcAddress())
			.setSrcBundleId(entity.getSrcBundleId())
			.setSrcBundleName(entity.getSrcBundleName())
			.setSrcChannelId(entity.getSrcChannelId())
			.setSrcPort(entity.getSrcPort())
			.setSrcRepeaterId(entity.getSrcRepeaterId())
			.setSrcType(entity.getSrcType());
		return this;
	}
}
