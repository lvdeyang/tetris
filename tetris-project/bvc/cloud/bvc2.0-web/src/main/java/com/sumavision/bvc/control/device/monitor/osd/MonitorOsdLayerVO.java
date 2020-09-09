package com.sumavision.bvc.control.device.monitor.osd;

import java.util.Comparator;

import com.sumavision.bvc.device.monitor.osd.MonitorOsdLayerPO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdLayerType;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MonitorOsdLayerVO extends AbstractBaseVO<MonitorOsdLayerVO, MonitorOsdLayerPO> {

	private Integer x;
	
	private Integer y;
	
	private Integer layerIndex;
	
	private Long subtitleId;
	
	private String subtitleName;
	
	private String subtitleUsername;
	
	private String type;
	
	private String font;
	
	private String height;
	
	private String color;
	
	public Integer getX() {
		return x;
	}

	public MonitorOsdLayerVO setX(Integer x) {
		this.x = x;
		return this;
	}

	public Integer getY() {
		return y;
	}

	public MonitorOsdLayerVO setY(Integer y) {
		this.y = y;
		return this;
	}

	public Integer getLayerIndex() {
		return layerIndex;
	}

	public MonitorOsdLayerVO setLayerIndex(Integer layerIndex) {
		this.layerIndex = layerIndex;
		return this;
	}

	public Long getSubtitleId() {
		return subtitleId;
	}

	public MonitorOsdLayerVO setSubtitleId(Long subtitleId) {
		this.subtitleId = subtitleId;
		return this;
	}

	public String getSubtitleName() {
		return subtitleName;
	}

	public MonitorOsdLayerVO setSubtitleName(String subtitleName) {
		this.subtitleName = subtitleName;
		return this;
	}

	public String getSubtitleUsername() {
		return subtitleUsername;
	}

	public MonitorOsdLayerVO setSubtitleUsername(String subtitleUsername) {
		this.subtitleUsername = subtitleUsername;
		return this;
	}

	public String getType() {
		return type;
	}

	public MonitorOsdLayerVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getFont() {
		return font;
	}

	public MonitorOsdLayerVO setFont(String font) {
		this.font = font;
		return this;
	}

	public String getHeight() {
		return height;
	}

	public MonitorOsdLayerVO setHeight(String height) {
		this.height = height;
		return this;
	}

	public String getColor() {
		return color;
	}

	public MonitorOsdLayerVO setColor(String color) {
		this.color = color;
		return this;
	}

	@Override
	public MonitorOsdLayerVO set(MonitorOsdLayerPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setX(entity.getX())
			.setY(entity.getY())
			.setLayerIndex(entity.getLayerIndex())
			.setSubtitleId(entity.getContentId())
			.setSubtitleName(entity.getType().equals(MonitorOsdLayerType.SUBTITLE)?entity.getContentName():entity.getType().getName())
			.setSubtitleUsername(entity.getType().equals(MonitorOsdLayerType.SUBTITLE)?entity.getContentUsername():"-")
			.setType(entity.getType().toString())
			.setFont(entity.getFont()==null?"-":entity.getFont().getName())
			.setHeight(entity.getHeight()==null?"-":entity.getHeight().toString())
			.setColor(entity.getColor()==null?"-":entity.getColor());
		return this;
	}
	
	public static class MonitorOsdLayerComparator implements Comparator<MonitorOsdLayerVO>{

		@Override
		public int compare(MonitorOsdLayerVO o1, MonitorOsdLayerVO o2) {
			return o1.getLayerIndex().intValue() - o2.getLayerIndex().intValue();
		}
		
	}
	
}
