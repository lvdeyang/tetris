/**
 * 
 */
package com.sumavision.tetris.guide.control;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 类型概述<br/>
 * <p>
 * 详细描述
 * </p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月2日 下午7:35:14
 */
@Entity
@Table(name = "TETRIS_SOURCE_PO")
public class SourcePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 源编号 */
	private Long sourceNumber;
	
	/** 源类型 */
	private SourceType sourceType;
	
	/** 源名称 */
	private String sourceName;   
	
	/** 源 sourceType为URL的时候存URL，为5G背包的时候存bundleID */
	private String source;
	
	/** 导播任务id */
	private Long guideId;
	
	/** 源是否被切换到 */
	private Boolean current;
	
	/**预监是否切换到*/
	private Boolean pvmCurrent;
	
	/** 音量 */
	private Long volume;
	
	/** 是否设置预监输出 */
	private Boolean isPreviewOut;
	
	/** 输出地址 */
	private String previewOut;
	
	/** 源协议 */
	private SourceProtocol sourceProtocol;
	
	@Column(name = "SOURCE_NUMBER")
	public Long getSourceNumber() {
		return sourceNumber;
	}

	public void setSourceNumber(Long sourceNumber) {
		this.sourceNumber = sourceNumber;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "SOURCE_TYPE")
	public SourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	@Column(name = "SOURCE_NAME")
	public String getSourceName() {
		return sourceName;
	}


	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	@Column(name = "SOURCE")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	@Column(name = "GUIDE_ID")
	public Long getGuideId() {
		return guideId;
	}

	public void setGuideId(Long guideId) {
		this.guideId = guideId;
	}

	@Column(name = "CURRENT")
	public Boolean getCurrent() {
		return current;
	}

	public void setCurrent(Boolean current) {
		this.current = current;
	}
	
	
	@Column(name = "PGM_CURRENT")
	public Boolean getPvmCurrent() {
		return pvmCurrent;
	}

	public void setPvmCurrent(Boolean pvmCurrent) {
		this.pvmCurrent = pvmCurrent;
	}

	@Column(name = "VOLUME")
	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}
	
	@Column(name = "IS_PREVIEWOUT")
	public Boolean getIsPreviewOut() {
		return isPreviewOut;
	}

	public void setIsPreviewOut(Boolean isPreviewOut) {
		this.isPreviewOut = isPreviewOut;
	}

	
	@Column(name = "PREVIEWOUT")
	public String getPreviewOut() {
		return previewOut;
	}

	public void setPreviewOut(String previewOut) {
		this.previewOut = previewOut;
	}

	@Column(name = "SOURCE_PROTOCOL")
	@Enumerated(value = EnumType.STRING)
	public SourceProtocol getSourceProtocol() {
		return sourceProtocol;
	}

	public void setSourceProtocol(SourceProtocol sourceProtocol) {
		this.sourceProtocol = sourceProtocol;
	}

}
