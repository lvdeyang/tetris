package com.sumavision.tetris.bvc.model.layout;

import java.util.Comparator;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class LayoutPositionVO extends AbstractBaseVO<LayoutPositionVO, LayoutPositionPO>{

	private Integer serialNum;
	
	private String type;
	
	private String typeName;

	private String x;
	
	private String y;
	
	private String width;
	
	private String height;
	
	private String zIndex; 
	
	public Integer getSerialNum() {
		return serialNum;
	}

	public LayoutPositionVO setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
		return this;
	}

	public String getType() {
		return type;
	}

	public LayoutPositionVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getTypeName() {
		return typeName;
	}

	public LayoutPositionVO setTypeName(String typeName) {
		this.typeName = typeName;
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

	public String getzIndex() {
		return zIndex;
	}

	public LayoutPositionVO setzIndex(String zIndex) {
		this.zIndex = zIndex;
		return this;
	}

	@Override
	public LayoutPositionVO set(LayoutPositionPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setSerialNum(entity.getSerialNum())
			.setType(entity.getType().toString())
			.setTypeName(entity.getType().getName())
			.setX(entity.getX())
			.setY(entity.getY())
			.setWidth(entity.getWidth())
			.setHeight(entity.getHeight())
			.setzIndex(entity.getzIndex());
		return this;
	}
	
	/**
	 * 按照序号排序<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 下午5:25:18
	 */
	public static class LayoutPositionComparator implements Comparator<LayoutPositionVO>{
		
		@Override
		public int compare(LayoutPositionVO o1, LayoutPositionVO o2) {
			return o1.getSerialNum().intValue() - o2.getSerialNum().intValue();
		}
		
	}

}
