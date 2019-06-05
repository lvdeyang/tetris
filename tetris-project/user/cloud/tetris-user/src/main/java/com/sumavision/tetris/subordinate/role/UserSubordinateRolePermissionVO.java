package com.sumavision.tetris.subordinate.role;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;
import com.sumavision.tetris.system.role.UserSystemRolePermissionPO;
import com.sumavision.tetris.system.role.UserSystemRolePermissionVO;
import com.sumavision.tetris.user.UserPO;

public class UserSubordinateRolePermissionVO extends AbstractBaseVO<UserSubordinateRolePermissionVO, UserSubordinateRolePermissionPO> {

	private Long roleId;
	private Long userId;
	
	private String nickname;

	@Override
	public UserSubordinateRolePermissionVO set(UserSubordinateRolePermissionPO entity) throws Exception {
		this.setId(entity.getId()).setUuid(entity.getUuid())
				.setUpdateTime(entity.getUpdateTime() == null ? ""
						: DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
				.setRoleId(entity.getRoleId()).setUserId(entity.getUserId());
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public UserSubordinateRolePermissionVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public UserSubordinateRolePermissionVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}
	
	public String getNickname() {
		return nickname;
	}

	public UserSubordinateRolePermissionVO setNickname(String nickname) {
		this.nickname = nickname;
		return this;
	}

	public UserSubordinateRolePermissionVO set(UserSubordinateRolePermissionPO permission, UserPO user){
		this.setId(permission.getId())
			.setUuid(permission.getUuid())
			.setUpdateTime(permission.getUpdateTime()==null?"":DateUtil.format(permission.getUpdateTime(), DateUtil.dateTimePattern))
			.setRoleId(permission.getRoleId())
			.setUserId(user.getId())
			.setNickname(user.getNickname());
		return this;
	}
}
