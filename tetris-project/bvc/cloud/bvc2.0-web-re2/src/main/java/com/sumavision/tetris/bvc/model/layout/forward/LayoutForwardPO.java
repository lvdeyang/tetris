package com.sumavision.tetris.bvc.model.layout.forward;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 画面/虚拟源转发配置<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月23日 下午4:04:09
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_LAYOUT_FORWARD")
public class LayoutForwardPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 虚拟源id */
	private Long layoutId;
	
	/** 转发源，如果源是合屏模板，存合屏模板id，如果源是虚拟源分屏，存虚拟源分屏“序号” */
	private Long sourceId;
	
	/** 源类型 */
	private LayoutForwardSourceType sourceType;
	
	/** 终端id */
	private Long terminalId;
	
	/** 终端物理屏id */
	private Long terminalPhysicalScreenId;
	
	/** 终端解码通道, 如果取值-1，则在当前屏幕关联的解码中随机选择 */
	private Long terminalDecodeChannelId;
	
	/** 是否启用物理屏幕中各解码之间的布局 */
	private Boolean enablePosition;
	
	/** 以下布局为同一物理屏幕上的解码通道之间的布局 */
	
	/** 左偏移 */
	private String x;
	
	/** 上偏移 */
	private String y;
	
	/** 宽度 */
	private String width;
	
	/** 高度 */
	private String height;
	
	/** 涂层 */
	private String zIndex;

	@Column(name = "LAYOUT_ID")
	public Long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
	}

	@Column(name = "SOURCE_ID")
	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "SOURCE_TYPE")
	public LayoutForwardSourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(LayoutForwardSourceType sourceType) {
		this.sourceType = sourceType;
	}

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	@Column(name = "TERMINAL_PHYSICAL_SCREEN_ID")
	public Long getTerminalPhysicalScreenId() {
		return terminalPhysicalScreenId;
	}

	public void setTerminalPhysicalScreenId(Long terminalPhysicalScreenId) {
		this.terminalPhysicalScreenId = terminalPhysicalScreenId;
	}

	@Column(name = "TERMINAL_DECODE_CHANNEL_ID")
	public Long getTerminalDecodeChannelId() {
		return terminalDecodeChannelId;
	}

	public void setTerminalDecodeChannelId(Long terminalDecodeChannelId) {
		this.terminalDecodeChannelId = terminalDecodeChannelId;
	}

	@Column(name = "ENABLE_POSITION")
	public Boolean getEnablePosition() {
		return enablePosition;
	}

	public void setEnablePosition(Boolean enablePosition) {
		this.enablePosition = enablePosition;
	}

	@Column(name = "X")
	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	@Column(name = "Y")
	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	@Column(name = "WIDTH")
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	@Column(name = "HEIGHT")
	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@Column(name = "ZINDEX")
	public String getzIndex() {
		return zIndex;
	}

	public void setzIndex(String zIndex) {
		this.zIndex = zIndex;
	}
	
}
