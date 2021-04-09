package com.sumavision.tetris.bvc.model.terminal.physical.screen;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TerminalPhysicalScreenVO extends AbstractBaseVO<TerminalPhysicalScreenVO, TerminalPhysicalScreenPO>{

	/** 物理屏名称 */
	private String name;
	
	/** 终端id */
	private Long terminalId;
	
	/** 布局列号 */
	private Integer column;
	
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
	
	public String getName() {
		return name;
	}

	public TerminalPhysicalScreenVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public TerminalPhysicalScreenVO setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	public Integer getColumn() {
		return column;
	}

	public TerminalPhysicalScreenVO setColumn(Integer column) {
		this.column = column;
		return this;
	}

	public Integer getRow() {
		return row;
	}

	public TerminalPhysicalScreenVO setRow(Integer row) {
		this.row = row;
		return this;
	}

	public String getX() {
		return x;
	}

	public TerminalPhysicalScreenVO setX(String x) {
		this.x = x;
		return this;
	}

	public String getY() {
		return y;
	}

	public TerminalPhysicalScreenVO setY(String y) {
		this.y = y;
		return this;
	}

	public String getWidth() {
		return width;
	}

	public TerminalPhysicalScreenVO setWidth(String width) {
		this.width = width;
		return this;
	}

	public String getHeight() {
		return height;
	}

	public TerminalPhysicalScreenVO setHeight(String height) {
		this.height = height;
		return this;
	}

	@Override
	public TerminalPhysicalScreenVO set(TerminalPhysicalScreenPO entity) throws Exception {
		this.setId(entity.getId())
			.setName(entity.getName())
			.setTerminalId(entity.getTerminalId())
			.setColumn(entity.getCol())
			.setRow(entity.getRow())
			.setX(entity.getX())
			.setY(entity.getY())
			.setWidth(entity.getWidth())
			.setHeight(entity.getHeight());
		return this;
	}
	
	
}
