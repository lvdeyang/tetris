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
	
	private String sourceTypeName;
	
	/** 源名称 */
	private String sourceName;   
	
	/** 源 sourceType为URL的时候存URL，为5G背包的时候存bundleID */
	private String source;
	
	/** 导播任务id */
	private Long guideId;
	
	/** 源是否被切换到 */
	private Boolean current;
	
	/** 音量 */
	private Long volume;
	
	/** 是否设置预监输出 */
	private Boolean isPreviewOut;
	
	/** 输出地址 */
	private String previewOut;
	
	/** 源协议 */
	private String sourceProtocol;
	
	private String sourceProtocolName;

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

	public String getSourceTypeName() {
		return sourceTypeName;
	}

	public SourceVO setSourceTypeName(String sourceTypeName) {
		this.sourceTypeName = sourceTypeName;
		return this;
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

	public SourceVO setGuideId(Long guideId) {
		this.guideId = guideId;
		return this;
	}

	public Boolean getCurrent() {
		return current;
	}

	public void setCurrent(Boolean current) {
		this.current = current;
	}
	
	public Long getVolume() {
		return volume;
	}

	public SourceVO setVolume(Long volume) {
		this.volume = volume;
		return this;
	}
	
	public Boolean getIsPreviewOut() {
		return isPreviewOut;
	}

	public SourceVO setIsPreviewOut(Boolean isPreviewOut) {
		this.isPreviewOut = isPreviewOut;
		return this;
	}

	
	public String getPreviewOut() {
		return previewOut;
	}

	public SourceVO setPreviewOut(String previewOut) {
		this.previewOut = previewOut;
		return this;
	}

	public String getSourceProtocol() {
		return sourceProtocol;
	}

	public SourceVO setSourceProtocol(String sourceProtocol) {
		this.sourceProtocol = sourceProtocol;
		return this;
	}

	public String getSourceProtocolName() {
		return sourceProtocolName;
	}

	public SourceVO setSourceProtocolName(String sourceProtocolName) {
		this.sourceProtocolName = sourceProtocolName;
		return this;
	}

	@Override
	public SourceVO set(SourcePO entity) throws Exception {
		this.setId(entity.getId());
		this.setSourceType(entity.getSourceType()!= null ? entity.getSourceType().toString(): null);
		this.setSourceTypeName(entity.getSourceType()!= null ? entity.getSourceType().getName(): null);
		this.setSourceName(entity.getSourceName());
		this.setSource(entity.getSource());
		this.setIndex(entity.getSourceNumber());
		this.setCurrent(entity.getCurrent());
		this.setVolume(entity.getVolume());
		this.setIsPreviewOut(entity.getIsPreviewOut());
		this.setPreviewOut(entity.getPreviewOut());
		this.setSourceProtocol(entity.getSourceProtocol()!= null ? entity.getSourceProtocol().toString(): null);
		this.setSourceProtocolName(entity.getSourceProtocol()!= null ? entity.getSourceProtocol().getName(): null);
		return this;
	}

}
