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
	
	/** 输出编号 */
	private Long index;
	
	/** 输出协议 */
	private String outputProtocol;
	
	/** 输出地址 */
	private String outputAddress;
	
	/** 导播任务编号 */
	private Long guidePO;

	public Long getIndex() {
		return index;
	}

	public OutputSettingVO setIndex(Long index) {
		this.index = index;
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

	public Long getGuidePO() {
		return guidePO;
	}

	public OutputSettingVO setGuidePO(Long guidePO) {
		this.guidePO = guidePO;
		return this;
	}

	@Override
	public OutputSettingVO set(OutputSettingPO entity) throws Exception {
		this.setIndex(entity.getIndex());
		this.setOutputProtocol(entity.getOutputProtocol());
		this.setOutputAddress(entity.getOutputAddress());
		this.setGuidePO(entity.getGuidePO());
		return this;
	}
	
	
}
