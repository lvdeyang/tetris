package com.sumavision.tetris.cs.menu;

import java.util.List;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class CsMenuVO extends AbstractBaseVO<CsMenuVO, CsMenuPO> {
	private Long ChannelId;
	private String name;
	private Long parentId;
	private String parentPath;
	private String remark;
	private List<CsMenuVO> subColumns;

	@Override
	public CsMenuVO set(CsMenuPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime() == null ? "": DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setName(entity.getName())
		.setChannelId(entity.getChannelId())
		.setParentId(entity.getParentId())
		.setParentPath(entity.getParentPath())
		.setRemark(entity.getRemark());

		return this;
	}

	public Long getChannelId() {
		return ChannelId;
	}

	public CsMenuVO setChannelId(Long channelId) {
		this.ChannelId = channelId;
		return this;
	}

	public String getName() {
		return name;
	}

	public CsMenuVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getParentId() {
		return parentId;
	}

	public CsMenuVO setParentId(Long parentId) {
		this.parentId = parentId;
		return this;
	}

	public String getParentPath() {
		return parentPath;
	}

	public CsMenuVO setParentPath(String parentPath) {
		this.parentPath = parentPath;
		return this;
	}
	

	public String getRemark() {
		return remark;
	}

	public CsMenuVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public List<CsMenuVO> getSubColumns() {
		return subColumns;
	}

	public void setSubColumns(List<CsMenuVO> subColumns) {
		this.subColumns = subColumns;
	}
}
