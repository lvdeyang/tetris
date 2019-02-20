package com.sumavision.tetris.cms.column;

import java.util.List;

import com.sumavision.tetris.cms.template.TemplateTagVO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ColumnVO extends AbstractBaseVO<ColumnVO, ColumnPO>{

	private String name;
	private long parentId;
	private List<ColumnVO> subColumns;
	
	public String getName() {
		return name;
	}

	public ColumnVO setName(String name) {
		this.name = name;
		return this;
	}



	@Override
	public ColumnVO set(ColumnPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setParentId(entity.getParentId());
		return this;
	}

	public long getParentId() {
		return parentId;
	}

	public ColumnVO setParentId(long parentId) {
		this.parentId = parentId;
		return this;
	}

	public List<ColumnVO> getSubColumns() {
		return subColumns;
	}

	public void setSubColumns(List<ColumnVO> subColumns) {
		this.subColumns = subColumns;
	}

}
