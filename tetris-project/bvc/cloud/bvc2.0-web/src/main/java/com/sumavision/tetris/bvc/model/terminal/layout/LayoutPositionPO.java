package com.sumavision.tetris.bvc.model.terminal.layout;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 描述屏幕位置以及大小<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:20:58
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_TERMINAL_LAYOUT_POSITION")
public class LayoutPositionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 屏幕primaryKey */
	private String screenPrimaryKey;
	
	/** 横坐标 */
	private String x;
	
	/** 纵坐标 */
	private String y;
	
	/** 宽度 */
	private String width;
	
	/** 高度 */
	private String height;
	
	/** 涂层顺序 */
	private Integer zIndex;
	
	/** 隶属布局id */
	private Long layoutId;

	@Column(name = "SCREEN_PRIMARY_KEY")
	public String getScreenPrimaryKey() {
		return screenPrimaryKey;
	}

	public void setScreenPrimaryKey(String screenPrimaryKey) {
		this.screenPrimaryKey = screenPrimaryKey;
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

	@Column(name = "Z_INDEX")
	public Integer getZIndex() {
		return zIndex;
	}

	public void setZIndex(Integer zIndex) {
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
