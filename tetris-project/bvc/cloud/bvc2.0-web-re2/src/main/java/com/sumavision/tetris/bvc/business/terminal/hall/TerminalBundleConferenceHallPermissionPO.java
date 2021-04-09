package com.sumavision.tetris.bvc.business.terminal.hall;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_BVC_BUSINESS_TERMINAL_BUNDLE_CONFERENCE_HALL_PERMISSION")
public class TerminalBundleConferenceHallPermissionPO extends AbstractBasePO{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;

	/** 会场id */
	private Long conferenceHallId;
	
	/** 终端设备定义id */
	private Long terminalBundleId;
	
	/** 绑定真实设备id */
	private String bundleId;
	
	/** 设备类型 */
	private String bundleType;

	/** 设备名称 */
	private String bundleName;
	
	@Column(name = "CONFERENCE_HALL_ID")
	public Long getConferenceHallId() {
		return conferenceHallId;
	}

	public void setConferenceHallId(Long conferenceHallId) {
		this.conferenceHallId = conferenceHallId;
	}

	@Column(name = "TERMINAL_BUNDLE_ID")
	public Long getTerminalBundleId() {
		return terminalBundleId;
	}

	public void setTerminalBundleId(Long terminalBundleId) {
		this.terminalBundleId = terminalBundleId;
	}

	@Column(name = "BUNDLE_ID")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	@Column(name = "BUNDLE_TYPE")
	public String getBundleType() {
		return bundleType;
	}

	public void setBundleType(String bundleType) {
		this.bundleType = bundleType;
	}

	@Column(name = "BUNDLE_NAME")
	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}
	
}
