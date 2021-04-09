package com.sumavision.tetris.bvc.model.layout;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 虚拟源布局<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月23日 下午3:40:34
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_LAYOUT_POSITION")
public class LayoutPositionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 布局序号 */
	private Integer serialNum;
	
	/** 布局内容类型：远端、本地 */
	private LayoutPositionType type;

	/** 布局左偏移 */
	private String x;
	
	/** 布局上偏移 */
	private String y;
	
	/** 布局宽度 */
	private String width;
	
	/** 布局高度 */
	private String height;
	
	/** 布局涂层 */
	private String zIndex; 
	
	/** 画面id/虚拟源id */
	private Long layoutId;

	@Column(name = "SERIAL_NUM")
	public Integer getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public LayoutPositionType getType() {
		return type;
	}

	public void setType(LayoutPositionType type) {
		this.type = type;
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

	@Column(name = "LAYOUT_ID")
	public Long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
	}
	
}
