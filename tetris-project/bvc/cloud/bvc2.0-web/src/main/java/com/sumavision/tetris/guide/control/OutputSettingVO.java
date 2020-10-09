/**
 * 
 */
package com.sumavision.tetris.guide.control;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月4日 上午9:42:37
 */
public class OutputSettingVO extends AbstractBaseVO<OutputSettingVO, OutputSettingPO>{
	
	private Long id;
	
	/** 输出编号 */
	private String name;
	
	/** 输出协议 */
	private String outputProtocol;
	
	private String outputProtocolName;
	
	/** 输出地址 */
	private String outputAddress;
	
	/** 码率控制方式 */
	private String rateCtrl;
	
	private String rateCtrlName;
	
	/** 系统码率 */
	private Long bitrate;
	
	/** 输出组id */
	private Long groupId;
	
	public Long getId() {
		return id;
	}

	public OutputSettingVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public OutputSettingVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getOutputProtocol() {
		return outputProtocol;
	}

	public OutputSettingVO setOutputProtocol(String outputProtocol) {
		this.outputProtocol = outputProtocol;
		return this;
	}

	public String getOutputAddress() {
		return outputAddress;
	}

	public OutputSettingVO setOutputAddress(String outputAddress) {
		this.outputAddress = outputAddress;
		return this;
	}
	
	public String getRateCtrl() {
		return rateCtrl;
	}

	public OutputSettingVO setRateCtrl(String rateCtrl) {
		this.rateCtrl = rateCtrl;
		return this;
	}

	public Long getBitrate() {
		return bitrate;
	}

	public OutputSettingVO setBitrate(Long bitrate) {
		this.bitrate = bitrate;
		return this;
	}
	
	public String getOutputProtocolName() {
		return outputProtocolName;
	}

	public OutputSettingVO setOutputProtocolName(String outputProtocolName) {
		this.outputProtocolName = outputProtocolName;
		return this;
	}
	
	public Long getGroupId() {
		return groupId;
	}

	public OutputSettingVO setGroupId(Long groupId) {
		this.groupId = groupId;
		return this;
	}
	
	public String getRateCtrlName() {
		return rateCtrlName;
	}

	public OutputSettingVO setRateCtrlName(String rateCtrlName) {
		this.rateCtrlName = rateCtrlName;
		return this;
	}

	@Override
	public OutputSettingVO set(OutputSettingPO entity) throws Exception {
		this.setId(entity.getId());
		this.setName(entity.getName());
		this.setOutputProtocol(entity.getOutputProtocol()!= null ? entity.getOutputProtocol().toString(): null);
		this.setOutputProtocolName(entity.getOutputProtocol()!= null ? entity.getOutputProtocol().getName(): null);
		this.setOutputAddress(entity.getOutputAddress());
		this.setRateCtrl(entity.getRateCtrl()!= null ? entity.getRateCtrl().toString(): null);
		this.setRateCtrlName(entity.getRateCtrl()!= null ? entity.getRateCtrl().getName(): null);
		this.setBitrate(entity.getBitrate());
		this.setGroupId(entity.getGroupId());
		return this;
	}
	
	
}
