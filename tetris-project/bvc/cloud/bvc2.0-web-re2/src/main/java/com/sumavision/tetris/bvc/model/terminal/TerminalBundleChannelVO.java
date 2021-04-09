package com.sumavision.tetris.bvc.model.terminal;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TerminalBundleChannelVO extends AbstractBaseVO<TerminalBundleChannelVO, TerminalBundleChannelPO>{

	private String channelId;

	private String type;
	
	private String typeName;
	
	private Long terminalBundleId;
	
	private String terminalBundleName;
	
	/** bundle名称 */
	private String name;
	
	/** 资源层bundle类型 */
	private String bundleType;
	
	/**编解码类型*/
	private String terminalBundleType;
	
	private String terminalBundleTypeName;
	
	private Long terminalId;
	
	public String getChannelId() {
		return channelId;
	}

	public TerminalBundleChannelVO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getType() {
		return type;
	}

	public TerminalBundleChannelVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getTypeName() {
		return typeName;
	}

	public TerminalBundleChannelVO setTypeName(String typeName) {
		this.typeName = typeName;
		return this;
	}

	public Long getTerminalBundleId() {
		return terminalBundleId;
	}

	public TerminalBundleChannelVO setTerminalBundleId(Long terminalBundleId) {
		this.terminalBundleId = terminalBundleId;
		return this;
	}

	public String getTerminalBundleName() {
		return terminalBundleName;
	}

	public TerminalBundleChannelVO setTerminalBundleName(String terminalBundleName) {
		this.terminalBundleName = terminalBundleName;
		return this;
	}

	public String getName() {
		return name;
	}

	public TerminalBundleChannelVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getBundleType() {
		return bundleType;
	}

	public TerminalBundleChannelVO setBundleType(String bundleType) {
		this.bundleType = bundleType;
		return this;
	}

	public String getTerminalBundleType() {
		return terminalBundleType;
	}

	public TerminalBundleChannelVO setTerminalBundleType(String terminalBundleType) {
		this.terminalBundleType = terminalBundleType;
		return this;
	}

	public String getTerminalBundleTypeName() {
		return terminalBundleTypeName;
	}

	public TerminalBundleChannelVO setTerminalBundleTypeName(String terminalBundleTypeName) {
		this.terminalBundleTypeName = terminalBundleTypeName;
		return this;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public TerminalBundleChannelVO setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	@Override
	public TerminalBundleChannelVO set(TerminalBundleChannelPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setChannelId(entity.getChannelId())
			.setType(entity.getType().toString())
			.setTypeName(entity.getType().getName())
			.setTerminalBundleId(entity.getTerminalBundleId());
		return this;
	}
	
	public TerminalBundleChannelVO set(TerminalBundleChannelPO entity, TerminalBundlePO terminalBundle) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setChannelId(entity.getChannelId())
			.setType(entity.getType().toString())
			.setTypeName(entity.getType().getName())
			.setTerminalBundleId(entity.getTerminalBundleId())
			.setTerminalBundleName(terminalBundle.getName())
			.setBundleType(terminalBundle.getBundleType());
		return this;
	}
	
	public TerminalBundleChannelVO set(TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO entity) throws Exception{
		this.setId(entity.getId())
			.setChannelId(entity.getChannelId())
			.setType(entity.getType().toString())
			.setTypeName(entity.getType().getName())
			.setTerminalBundleId(entity.getTerminalBundleId())
			.setTerminalBundleName(entity.getBundleName())
			.setBundleType(entity.getDeviceMode())
			.setTerminalBundleType(entity.getBundleType().toString())
			.setTerminalBundleTypeName(entity.getBundleType().getName());
		return this;
	}

}
