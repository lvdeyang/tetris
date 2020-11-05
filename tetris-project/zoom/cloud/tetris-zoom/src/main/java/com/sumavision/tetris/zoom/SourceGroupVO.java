package com.sumavision.tetris.zoom;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class SourceGroupVO extends AbstractBaseVO<SourceGroupVO, SourceGroupPO>{

	private String name;
	
	private String type;
	
	private String userId;
	
	public String getName() {
		return name;
	}

	public SourceGroupVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getType() {
		return type;
	}

	public SourceGroupVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public SourceGroupVO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	@Override
	public SourceGroupVO set(SourceGroupPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setType(entity.getType().toString())
			.setUserId(entity.getUserId());
		return this;
	}
	
}
