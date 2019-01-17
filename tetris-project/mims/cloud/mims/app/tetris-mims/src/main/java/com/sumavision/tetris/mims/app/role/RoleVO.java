package com.sumavision.tetris.mims.app.role;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class RoleVO extends AbstractBaseVO<RoleVO, RolePO>{

	private String name;
	
	private boolean removeable;
	
	public String getName() {
		return name;
	}

	public RoleVO setName(String name) {
		this.name = name;
		return this;
	}

	public boolean isRemoveable() {
		return removeable;
	}

	public RoleVO setRemoveable(boolean removeable) {
		this.removeable = removeable;
		return this;
	}

	@Override
	public RoleVO set(RolePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setRemoveable(entity.getRemoveable());
		return this;
	}
	
}
