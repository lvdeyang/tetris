package com.sumavision.tetris.subordinate.role;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class SubordinateRoleVO extends AbstractBaseVO<SubordinateRoleVO, SubordinateRolePO> {
	private String name;
	private Long companyId;
	private String userId;

	@Override
	public SubordinateRoleVO set(SubordinateRolePO entity) throws Exception {
		this.setId(entity.getId()).setUuid(entity.getUuid())
				.setUpdateTime(entity.getUpdateTime() == null ? ""
						: DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
				.setName(entity.getName()).setCompanyId(entity.getCompanyId()).setUserId(entity.getUserId());
		return this;
	}

	public String getName() {
		return name;
	}

	public SubordinateRoleVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public SubordinateRoleVO setCompanyId(Long companyId) {
		this.companyId = companyId;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public SubordinateRoleVO setUserId(String userId) {
		this.userId = userId;
		return this;
	}
}
