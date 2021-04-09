package com.sumavision.tetris.bvc.model.terminal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 终端定义<br/>
 * <p>
 * 	终端代表设备类型与设备数量的组合<br/>
 * </p>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:06:27
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_TERMINAL")
public class TerminalPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 名称 */
	private String name;
	
	/** 终端类型 */
	private TerminalType type;
	
	/** 是否启动物理屏幕布局 */
	private Boolean physicalScreenLayout;
	
	/** 列数 */
	private Integer columns;
	
	/** 行数 */
	private Integer rows;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TYPE")
	@Enumerated(value = EnumType.STRING)
	public TerminalType getType() {
		return type;
	}

	public void setType(TerminalType type) {
		this.type = type;
	}

	@Column(name = "PHYSICAL_SCREEN_LAYOUT")
	public Boolean getPhysicalScreenLayout() {
		return physicalScreenLayout;
	}

	public void setPhysicalScreenLayout(Boolean physicalScreenLayout) {
		this.physicalScreenLayout = physicalScreenLayout;
	}

	@Column(name = "COLUMNS")
	public Integer getColumns() {
		return columns;
	}

	public void setColumns(Integer columns) {
		this.columns = columns;
	}

	@Column(name = "ROWS")
	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}
	
}
