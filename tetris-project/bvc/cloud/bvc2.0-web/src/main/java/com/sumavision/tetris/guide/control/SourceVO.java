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
	private SourceType sourceType;
	
	/** 源名称 */
	private String sourceName;   
	
	/** 源 sourceType为URL的时候存URL，为5G背包的时候存bundleID */
	private String source;
	
	/** 导播任务id */
	private Long guideId;

	public Long getIndex() {
		return index;
	}

	public SourceVO setIndex(Long index) {
		this.index = index;
		return this;
	}

	public SourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceName() {
		return sourceName;
	}

	public SourceVO setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}

	public String getSource() {
		return source;
	}

	public SourceVO setSource(String source) {
		this.source = source;
		return this;
	}

	public Long getGuideId() {
		return guideId;
	}

	public void setGuideId(Long guideId) {
		this.guideId = guideId;
	}

	@Override
	public SourceVO set(SourcePO entity) throws Exception {
		this.setId(entity.getId());
		this.setSourceType(entity.getSourceType());
		this.setSourceName(entity.getSourceName());
		this.setSource(entity.getSource());
		this.setId(entity.getId());
		return this;
	}

}
