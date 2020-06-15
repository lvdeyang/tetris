package com.sumavision.tetris.bvc.business.terminal.user.layout;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 用户自定义屏幕布局<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:35:49
 */
@Entity
@Table(name = "TETRIS_BVC_BUSINESS_TERMINAL_USER_LAYOUT_POSITION_USER_SETTINGS")
public class LayoutPositionUserSettingsPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 布局id */
	private Long layoutPositionId;
	
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
	
	/** 用户id */
	private String userId;

	@Column(name = "LAYOUT_POSITION_ID")
	public Long getLayoutPositionId() {
		return layoutPositionId;
	}

	public void setLayoutPositionId(Long layoutPositionId) {
		this.layoutPositionId = layoutPositionId;
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
	public Integer getzIndex() {
		return zIndex;
	}

	public void setzIndex(Integer zIndex) {
		this.zIndex = zIndex;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
