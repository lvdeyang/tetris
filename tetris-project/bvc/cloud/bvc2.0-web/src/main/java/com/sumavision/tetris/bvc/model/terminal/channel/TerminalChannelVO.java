package com.sumavision.tetris.bvc.model.terminal.channel;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TerminalChannelVO extends AbstractBaseVO<TerminalChannelVO, TerminalChannelPO>{

	/** 起一个别名 */
	private String name;
	
	/** 通道类型 */
	private String type;
	
	/** 隶属终端类型 */
	private Long terminalId;
	
	/** 与真实设备类型中的通道绑定 */
	private Long terminalBundleId;
	
	/** 终端设备名称 */
	private String terminalBundleName;
	
	/** 与真实设备类型中的通道绑定 */
	private String realChannelId;
	
	public String getName() {
		return name;
	}

	public TerminalChannelVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getType() {
		return type;
	}

	public TerminalChannelVO setType(String type) {
		this.type = type;
		return this;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public TerminalChannelVO setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	public Long getTerminalBundleId() {
		return terminalBundleId;
	}

	public TerminalChannelVO setTerminalBundleId(Long terminalBundleId) {
		this.terminalBundleId = terminalBundleId;
		return this;
	}

	public String getTerminalBundleName() {
		return terminalBundleName;
	}

	public TerminalChannelVO setTerminalBundleName(String terminalBundleName) {
		this.terminalBundleName = terminalBundleName;
		return this;
	}

	public String getRealChannelId() {
		return realChannelId;
	}

	public TerminalChannelVO setRealChannelId(String realChannelId) {
		this.realChannelId = realChannelId;
		return this;
	}

	@Override
	public TerminalChannelVO set(TerminalChannelPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setType(entity.getType().toString())
			.setTerminalId(entity.getTerminalId())
			.setTerminalBundleId(entity.getTerminalBundleId())
			.setRealChannelId(entity.getRealChannelId());
		return this;
	}

}
