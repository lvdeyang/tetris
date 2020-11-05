package com.sumavision.tetris.bvc.business.terminal.user;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TerminalBundleUserPermissionVO extends AbstractBaseVO<TerminalBundleUserPermissionVO, TerminalBundleUserPermissionPO>{

	/** 终端id */
	private Long terminalId;
	
	/** 终端设备id */
	private Long terminalBundleId;
	
	/** 设备类型 */
	private String bundleType;
	
	/** bundleId */
	private String bundleId;
	
	/** bundleName */
	private String bundleName;
	
	/** 用户id */
	private String userId;
	
	public Long getTerminalId() {
		return terminalId;
	}

	public TerminalBundleUserPermissionVO setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	public Long getTerminalBundleId() {
		return terminalBundleId;
	}

	public TerminalBundleUserPermissionVO setTerminalBundleId(Long terminalBundleId) {
		this.terminalBundleId = terminalBundleId;
		return this;
	}

	public String getBundleType() {
		return bundleType;
	}

	public TerminalBundleUserPermissionVO setBundleType(String bundleType) {
		this.bundleType = bundleType;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public TerminalBundleUserPermissionVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public TerminalBundleUserPermissionVO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public TerminalBundleUserPermissionVO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	@Override
	public TerminalBundleUserPermissionVO set(TerminalBundleUserPermissionPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setTerminalId(entity.getTerminalId())
			.setTerminalBundleId(entity.getTerminalBundleId())
			.setBundleType(entity.getBundleType())
			.setBundleId(entity.getBundleId())
			.setBundleName(entity.getBundleName())
			.setUserId(entity.getUserId());
		return this;
	}

}
