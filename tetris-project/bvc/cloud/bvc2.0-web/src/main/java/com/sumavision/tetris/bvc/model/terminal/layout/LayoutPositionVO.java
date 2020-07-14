package com.sumavision.tetris.bvc.model.terminal.layout;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class LayoutPositionVO extends AbstractBaseVO<LayoutPositionVO, LayoutPositionPO>{

	/** 屏幕名称 */
	private String screenName;
	
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
	
	public String getScreenName() {
		return screenName;
	}

	public LayoutPositionVO setScreenName(String screenName) {
		this.screenName = screenName;
		return this;
	}

	public String getScreenPrimaryKey() {
		return screenPrimaryKey;
	}

	public LayoutPositionVO setScreenPrimaryKey(String screenPrimaryKey) {
		this.screenPrimaryKey = screenPrimaryKey;
		return this;
	}

	public String getX() {
		return x;
	}

	public LayoutPositionVO setX(String x) {
		this.x = x;
		return this;
	}

	public String getY() {
		return y;
	}

	public LayoutPositionVO setY(String y) {
		this.y = y;
		return this;
	}

	public String getWidth() {
		return width;
	}

	public LayoutPositionVO setWidth(String width) {
		this.width = width;
		return this;
	}

	public String getHeight() {
		return height;
	}

	public LayoutPositionVO setHeight(String height) {
		this.height = height;
		return this;
	}

	public Integer getzIndex() {
		return zIndex;
	}

	public LayoutPositionVO setzIndex(Integer zIndex) {
		this.zIndex = zIndex;
		return this;
	}

	public Long getLayoutId() {
		return layoutId;
	}

	public LayoutPositionVO setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
		return this;
	}

	@Override
	public LayoutPositionVO set(LayoutPositionPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setScreenPrimaryKey(entity.getScreenPrimaryKey())
			.setX(entity.getX())
			.setY(entity.getY())
			.setWidth(entity.getWidth())
			.setHeight(entity.getHeight())
			.setzIndex(entity.getZIndex())
			.setLayoutId(entity.getLayoutId());
		return this;
	}

}
