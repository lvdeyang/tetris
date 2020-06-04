package com.sumavision.tetris.bvc.model.terminal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 终端定义中的设备<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:07:40
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_TERMINAL_BUNDLE")
public class TerminalBundlePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 名称 */
	private String name;
	
	/** 资源层bundle类型 */
	private String bundleType;
	
	/** 终端设备编解码类型 */
	private TerminalBundleType type;
	
	/** 隶属终端id */
	private Long terminalId;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "BUNDLE_TYPE")
	@Enumerated(value = EnumType.STRING)
	public String getBundleType() {
		return bundleType;
	}

	public void setBundleType(String bundleType) {
		this.bundleType = bundleType;
	}

	@Column(name = "TYPE")
	@Enumerated(value = EnumType.STRING)
	public TerminalBundleType getType() {
		return type;
	}

	public void setType(TerminalBundleType type) {
		this.type = type;
	}

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}
	
}
