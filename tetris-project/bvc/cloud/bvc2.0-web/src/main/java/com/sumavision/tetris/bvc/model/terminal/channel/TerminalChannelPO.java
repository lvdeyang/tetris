package com.sumavision.tetris.bvc.model.terminal.channel;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 为终端创建的通道，与终端定义的真实设备类型的通道一一对应<br/>
 * <p>要问建这个有什么好处：<br/>
 * 	1.可以为终端的通道起一个别名<br/>
 *  2.前台显示终端的时候不需要再把设备显示出来，直接显示通道<br/>
 * </p>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月15日 下午2:29:39
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_TERMINAL_CHANNEL")
public class TerminalChannelPO extends AbstractBasePO{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;

	/** 起一个别名 */
	private String name;
	
	/** 通道类型 */
	private TerminalChannelType type;
	
	/** 隶属终端类型 */
	private Long terminalId;
	
	/** 与真实设备类型中的通道绑定 */
	private Long terminalBundleId;
	
	/** 与真实设备类型中的通道绑定 */
	private String realChannelId;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public TerminalChannelType getType() {
		return type;
	}

	public void setType(TerminalChannelType type) {
		this.type = type;
	}

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	@Column(name = "TERMINAL_BUNDLE_ID")
	public Long getTerminalBundleId() {
		return terminalBundleId;
	}

	public void setTerminalBundleId(Long terminalBundleId) {
		this.terminalBundleId = terminalBundleId;
	}

	@Column(name = "REAL_CHANNEL_ID")
	public String getRealChannelId() {
		return realChannelId;
	}

	public void setRealChannelId(String realChannelId) {
		this.realChannelId = realChannelId;
	}
	
}
