package com.sumavision.tetris.bvc.model.layout.forward;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.sumavision.tetris.bvc.model.layout.LayoutPositionPO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class LayoutForwardTreeNodeVO {

	private Long id;
	
	private String name;
	
	private Long layoutForwardId;
	
	private Integer serialNum;
	
	private LayoutForwardTreeNodeType type;
	
	private List<LayoutForwardTreeNodeVO> children;
	
	private Boolean enablePosition;
	
	private String x;
	
	private String y;
	
	private String width;
	
	private String height;
	
	private String zIndex;
	
	private Boolean current;
	
	public Long getId() {
		return id;
	}

	public LayoutForwardTreeNodeVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public LayoutForwardTreeNodeVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getLayoutForwardId() {
		return layoutForwardId;
	}

	public LayoutForwardTreeNodeVO setLayoutForwardId(Long layoutForwardId) {
		this.layoutForwardId = layoutForwardId;
		return this;
	}
	
	public Integer getSerialNum() {
		return serialNum;
	}

	public LayoutForwardTreeNodeVO setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
		return this;
	}

	public LayoutForwardTreeNodeType getType() {
		return type;
	}

	public LayoutForwardTreeNodeVO setType(LayoutForwardTreeNodeType type) {
		this.type = type;
		return this;
	}

	public List<LayoutForwardTreeNodeVO> getChildren() {
		return children;
	}

	public LayoutForwardTreeNodeVO setChildren(List<LayoutForwardTreeNodeVO> children) {
		this.children = children;
		return this;
	}

	public Boolean getEnablePosition() {
		return enablePosition;
	}

	public LayoutForwardTreeNodeVO setEnablePosition(Boolean enablePosition) {
		this.enablePosition = enablePosition;
		return this;
	}

	public String getX() {
		return x;
	}

	public LayoutForwardTreeNodeVO setX(String x) {
		this.x = x;
		return this;
	}

	public String getY() {
		return y;
	}

	public LayoutForwardTreeNodeVO setY(String y) {
		this.y = y;
		return this;
	}

	public String getWidth() {
		return width;
	}

	public LayoutForwardTreeNodeVO setWidth(String width) {
		this.width = width;
		return this;
	}

	public String getHeight() {
		return height;
	}

	public LayoutForwardTreeNodeVO setHeight(String height) {
		this.height = height;
		return this;
	}

	public String getzIndex() {
		return zIndex;
	}

	public LayoutForwardTreeNodeVO setzIndex(String zIndex) {
		this.zIndex = zIndex;
		return this;
	}
	
	public Boolean getCurrent() {
		return current;
	}

	public LayoutForwardTreeNodeVO setCurrent(Boolean current) {
		this.current = current;
		return this;
	}

	public LayoutForwardTreeNodeVO setDecodeChannelNode(LayoutForwardPO entity){
		this.setId(entity.getTerminalDecodeChannelId())
			.setType(LayoutForwardTreeNodeType.DECODE_CHANNEL)
			.setLayoutForwardId(entity.getId())
			.setEnablePosition(entity.getEnablePosition())
			.setX(entity.getX())
			.setY(entity.getY())
			.setWidth(entity.getWidth())
			.setHeight(entity.getHeight())
			.setzIndex(entity.getzIndex())
			.setCurrent(false)
			.setChildren(new ArrayList<LayoutForwardTreeNodeVO>());
		return this;
	}
	
	public LayoutForwardTreeNodeVO setLayoutPositionNode(LayoutForwardPO entity, LayoutPositionPO positionEntity){
		this.setId(new Date().getTime())
			.setSerialNum(Integer.valueOf(entity.getSourceId().toString()))
			.setName(new StringBufferWrapper().append("布局").append(entity.getSourceId()).append("（").append(positionEntity.getType().getName()).append("）").toString())
			.setLayoutForwardId(entity.getId())
			.setType(LayoutForwardTreeNodeType.LAYOUT_POSITION)
			.setEnablePosition(true)
			.setX("0")
			.setY("0")
			.setWidth("10000")
			.setHeight("10000")
			.setzIndex("0")
			.setCurrent(false);
		return this;
	}
	
	public LayoutForwardTreeNodeVO setForwardNode(LayoutForwardPO entity){
		this.setId(new Date().getTime())
			.setName("转发")
			.setType(LayoutForwardTreeNodeType.FORWARD)
			.setLayoutForwardId(entity.getId())
			.setCurrent(false)
			.setChildren(new ArrayList<LayoutForwardTreeNodeVO>());
		return this;
	}
	
	public LayoutForwardTreeNodeVO setCombineTemplateNode(LayoutForwardPO entity, CombineTemplatePO combineTemplate){
		this.setId(combineTemplate.getId())
			.setName("合屏")
			.setType(LayoutForwardTreeNodeType.COMBINE_TEMPLATE)
			.setLayoutForwardId(entity.getId())
			.setCurrent(false)
			.setChildren(new ArrayList<LayoutForwardTreeNodeVO>());
		return this;
	}
	
	public LayoutForwardTreeNodeVO setCombineTemplatePositionNode(LayoutForwardPO entity, CombineTemplatePositionPO combineTemplatePosition, LayoutPositionPO positionEntity){
		this.setId(combineTemplatePosition.getId())
			.setSerialNum(combineTemplatePosition.getLayoutPositionSerialNum())
			.setName(new StringBufferWrapper().append("布局").append(combineTemplatePosition.getLayoutPositionSerialNum()).append("（").append(positionEntity.getType().getName()).append("）").toString())
			.setType(LayoutForwardTreeNodeType.COMBINE_TEMPLATE_POSITION)
			.setLayoutForwardId(entity.getId())
			.setEnablePosition(true)
			.setX(combineTemplatePosition.getX())
			.setY(combineTemplatePosition.getY())
			.setWidth(combineTemplatePosition.getWidth())
			.setHeight(combineTemplatePosition.getHeight())
			.setzIndex(combineTemplatePosition.getzIndex())
			.setCurrent(false);
		return this;
	}
	
	public static class CombineTemplatePositionNodeComparator implements Comparator<LayoutForwardTreeNodeVO>{

		@Override
		public int compare(LayoutForwardTreeNodeVO o1, LayoutForwardTreeNodeVO o2) {
			return o1.getSerialNum().intValue() - o2.getSerialNum().intValue();
		}
		
	}
	
}
