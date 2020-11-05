/*package com.sumavision.tetris.mims.app.group;

import java.util.List;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;
import com.sumavision.tetris.user.UserVO;

*//**
 * 用户组/角色<br/>
 * <p>
 * 	1.映射资源层来的用户组-用uuid存groupId<br/>
 *  2.映射本系统内的角色---正常存<br/>
 * </p>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月19日 下午5:12:12
 *//*
public class GroupVO extends AbstractBaseVO<GroupVO, OrganizationPO>{
	
	*//** 群组名称 *//*
	private String name;
	
	*//** 是否可删除 *//*
	private boolean removeable;
	
	*//** 群组内用户 *//*
	private List<UserVO> users;
	
	public String getName() {
		return name;
	}

	public GroupVO setName(String name) {
		this.name = name;
		return this;
	}
	
	public boolean isRemoveable() {
		return removeable;
	}

	public GroupVO setRemoveable(boolean removeable) {
		this.removeable = removeable;
		return this;
	}

	public List<UserVO> getUsers() {
		return users;
	}

	public GroupVO setUsers(List<UserVO> users) {
		this.users = users;
		return this;
	}

	@Override
	public GroupVO set(OrganizationPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName());
		return this;
	}
	
}
*/