package com.sumavision.tetris.bvc.business.terminal.hall;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TerminalBundleConferenceHallPermissionVO extends AbstractBaseVO<TerminalBundleConferenceHallPermissionVO, TerminalBundleConferenceHallPermissionPO>{
	
	private Long conferenceHallId;
	
	private Long terminalBundleId;
	
	private String bundleId;
	
	private String bundleType;

	private String bundleName;
	
	public Long getConferenceHallId() {
		return conferenceHallId;
	}

	public TerminalBundleConferenceHallPermissionVO setConferenceHallId(Long conferenceHallId) {
		this.conferenceHallId = conferenceHallId;
		return this;
	}

	public Long getTerminalBundleId() {
		return terminalBundleId;
	}

	public TerminalBundleConferenceHallPermissionVO setTerminalBundleId(Long terminalBundleId) {
		this.terminalBundleId = terminalBundleId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public TerminalBundleConferenceHallPermissionVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleType() {
		return bundleType;
	}

	public TerminalBundleConferenceHallPermissionVO setBundleType(String bundleType) {
		this.bundleType = bundleType;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public TerminalBundleConferenceHallPermissionVO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	@Override
	public TerminalBundleConferenceHallPermissionVO set(TerminalBundleConferenceHallPermissionPO entity)
			throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setConferenceHallId(entity.getConferenceHallId())
			.setTerminalBundleId(entity.getTerminalBundleId())
			.setBundleId(entity.getBundleId())
			.setBundleType(entity.getBundleType())
			.setBundleName(entity.getBundleName());
		return this;
	}

}
