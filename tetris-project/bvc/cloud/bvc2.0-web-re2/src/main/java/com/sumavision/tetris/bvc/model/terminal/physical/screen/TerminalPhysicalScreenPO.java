package com.sumavision.tetris.bvc.model.terminal.physical.screen;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 终端物理屏幕<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月15日 下午3:00:41
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_TERMINAL_PHYSICAL_SCREEN")
public class TerminalPhysicalScreenPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 物理屏名称 */
	private String name;
	
	/** 终端id */
	private Long terminalId;
	
	/** 音频输出id */
	private Long terminalAudioOutputId;
	
	/** 布局列号 */
	private Integer col;
	
	/** 布局行号 */
	private Integer row;
	
	/** 左偏移 */
	private String x;
	
	/** 上偏移 */
	private String y;
	
	/** 宽 */
	private String width;
	
	/** 高 */
	private String height;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	@Column(name = "TERMINAL_AUDIO_OUTPUT_ID")
	public Long getTerminalAudioOutputId() {
		return terminalAudioOutputId;
	}

	public void setTerminalAudioOutputId(Long terminalAudioOutputId) {
		this.terminalAudioOutputId = terminalAudioOutputId;
	}

	@Column(name = "COL")
	public Integer getCol() {
		return col;
	}

	public void setCol(Integer col) {
		this.col = col;
	}

	@Column(name = "ROW")
	public Integer getRow() {
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
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
	
}
