package com.sumavision.tetris.mims.app.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoleService {

	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private RoleUserPermissionDAO roleUserPermissionDao;
	
	/**
	 * 删除角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月8日 下午7:20:29
	 * @param RolePO role 待删除角色
	 * @return RolePO 删除的角色
	 */
	public RolePO delete(RolePO role) throws Exception{
		
		//删除权限
		List<RoleUserPermissionPO> permissions = roleUserPermissionDao.findByRoleId(role.getId());
		roleUserPermissionDao.deleteInBatch(permissions);
		
		roleDao.delete(role);
		
		return role;
	}
	
	/**
	 * 用户绑定角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月10日 下午3:00:29
	 * @param RolePO role 角色
	 * @param Collection<String> userIds 用户id列表
	 */
	public void userBinding(RolePO role, Collection<String> userIds) throws Exception{
		List<RoleUserPermissionPO> permissions = new ArrayList<RoleUserPermissionPO>();
		for(String userId:userIds){
			RoleUserPermissionPO permission = new RoleUserPermissionPO();
			permission.setRoleId(role.getId());
			permission.setUserId(userId);
			permission.setUpdateTime(new Date());
			permissions.add(permission);
		}
		roleUserPermissionDao.save(permissions);
	}
	
}
