package com.sumavision.tetris.mims.app.organization;

import java.util.List;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mims.app.user.UserVO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class OrganizationVO extends AbstractBaseVO<OrganizationVO, OrganizationPO>{

	private String name;
	
	private List<UserVO> users;
	
	public String getName() {
		return name;
	}

	public OrganizationVO setName(String name) {
		this.name = name;
		return this;
	}

	public List<UserVO> getUsers() {
		return users;
	}

	public OrganizationVO setUsers(List<UserVO> users) {
		this.users = users;
		return this;
	}

	@Override
	public OrganizationVO set(OrganizationPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName());
		return this;
	}

}
