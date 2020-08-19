package com.sumavision.tetris.cs.area;

import java.util.List;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class AreaVO extends AbstractBaseVO<AreaVO, AreaPO> {

	private String name;
	private String areaId;
	private Long channelId;
	private Long parentId;
	private Boolean disabled = true;
	private List<AreaVO> subColumns;
	
	@Override
	public AreaVO set(AreaPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime() == null ? "": DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setName(entity.getName())
		.setAreaId(entity.getAreaId())
		.setChannelId(entity.getChannelId())
		.setParentId(entity.getParentId());
		
		return this;
	}

	public String getName() {
		return name;
	}

	public AreaVO setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getAreaId() {
		return areaId;
	}

	public AreaVO setAreaId(String areaId) {
		this.areaId = areaId;
		return this;
	}

	public Long getChannelId() {
		return channelId;
	}

	public AreaVO setChannelId(Long channelId) {
		this.channelId = channelId;
		return this;
	}

	public Long getParentId() {
		return parentId;
	}

	public AreaVO setParentId(Long parentId) {
		this.parentId = parentId;
		return this;
	}

	public List<AreaVO> getSubColumns() {
		return subColumns;
	}

	public AreaVO setSubColumns(List<AreaVO> subColumns) {
		this.subColumns = subColumns;
		return this;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public AreaVO setDisabled(Boolean disabled) {
		this.disabled = disabled;
		return this;
	}
}
