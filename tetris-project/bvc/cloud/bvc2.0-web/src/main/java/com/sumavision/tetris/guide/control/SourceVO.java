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
 * <b>日期：</b>2020年9月3日 下午6:46:53
 */
public class SourceVO extends AbstractBaseVO<SourceVO, SourcePO>{
	
	/** 源编号 */
	private Long index;
	
	/** 源类型 */
	private String sourceType;
	
	/** 源名称 */
	private String sourceName;   
	
	/** 源地址 */
	private String sourceAddress;
	
	/** 导播任务编号 */
	private Long guidePO;

	public Long getIndex() {
		return index;
	}

	public SourceVO setIndex(Long index) {
		this.index = index;
		return this;
	}

	public String getSourceType() {
		return sourceType;
	}

	public SourceVO setSourceType(String sourceType) {
		this.sourceType = sourceType;
		return this;
	}

	public String getSourceName() {
		return sourceName;
	}

	public SourceVO setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}

	public String getSourceAddress() {
		return sourceAddress;
	}

	public SourceVO setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
		return this;
	}

	public Long getGuidePO() {
		return guidePO;
	}

	public SourceVO setGuidePO(Long guidePO) {
		this.guidePO = guidePO;
		return this;
	}

	@Override
	public SourceVO set(SourcePO entity) throws Exception {
		this.setId(entity.getId());
		this.setSourceType(entity.getSourceType());
		this.setSourceName(entity.getSourceName());
		this.setSourceAddress(entity.getSourceAddress());
		this.setGuidePO(entity.getGuidePO());
		return this;
	}

}
