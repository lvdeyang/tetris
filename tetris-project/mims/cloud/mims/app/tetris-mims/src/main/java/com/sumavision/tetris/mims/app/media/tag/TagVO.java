package com.sumavision.tetris.mims.app.media.tag;

import java.util.List;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TagVO extends AbstractBaseVO<TagVO, TagPO>{
	private String name;
	
	private Long ParentId;
	
	private String remark;
	
	private Boolean disabled = false;
	
	private List<TagVO> subColumns;

	public String getName() {
		return name;
	}

	public TagVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getParentId() {
		return ParentId;
	}

	public TagVO setParentId(Long parentId) {
		ParentId = parentId;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public TagVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public TagVO setDisabled(Boolean disabled) {
		this.disabled = disabled;
		return this;
	}

	public List<TagVO> getSubColumns() {
		return subColumns;
	}

	public TagVO setSubColumns(List<TagVO> subColumns) {
		this.subColumns = subColumns;
		return this;
	}

	@Override
	public TagVO set(TagPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setName(entity.getName())
		.setParentId(entity.getParentId())
		.setRemark(entity.getRemark());
		return this;
	}
}
