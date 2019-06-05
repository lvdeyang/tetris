package com.sumavision.tetris.subordinate.role;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.sql.visitor.functions.If;
import com.sumavision.tetris.system.role.SystemRolePO;
import com.sumavision.tetris.system.role.UserSystemRolePermissionPO;
import com.sumavision.tetris.system.role.UserSystemRolePermissionVO;
import com.sumavision.tetris.system.role.exception.SystemRoleNotExistException;
import com.sumavision.tetris.user.UserDAO;
import com.sumavision.tetris.user.UserPO;
import com.sumavision.tetris.user.UserVO;

/**
 * 隶属角色对应用户操作（主增删改）<br/>
 * <b>作者:</b>lzp<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月30日 上午9:58:25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserSubordinateRolePermissionService {
	@Autowired
	private UserSubordinateRolePermissionDAO userSubordinateRolePermissionDAO;
	
	@Autowired
	private SubordinateRoleDAO subordinateRoleDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	/**
	 * 绑定隶属角色的成员列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月29日 上午11:44:44
	 * 
	 * @return List<UserVO> 角色列表
	 */
	public List<UserVO> bindUserFromRole(List<Long> userIds, Long roleId) throws Exception{
		SubordinateRolePO role = subordinateRoleDAO.findOne(roleId);
		if(role == null) return null;
		
		List<UserPO> users = userDAO.findAll(userIds);
		if(users==null || users.size()<=0) return null;
		
		List<UserSubordinateRolePermissionPO> existPermissions = userSubordinateRolePermissionDAO.findByRoleIdAndUserIdIn(roleId, userIds);
		
		List<UserSubordinateRolePermissionPO> permissions = new ArrayList<UserSubordinateRolePermissionPO>();
		for(UserPO user:users){
			boolean finded = false;
			if(existPermissions!=null && existPermissions.size()>0){
				for(UserSubordinateRolePermissionPO existPermission:existPermissions){
					if(user.getId().equals(existPermission.getUserId())){
						finded = true;
						break;
					}
				}
			}
			if(!finded){
				UserSubordinateRolePermissionPO permissionUser = userSubordinateRolePermissionDAO.findByUserId(user.getId());
				if (permissionUser != null) {
					permissionUser.setRoleId(roleId);
				}else {
					permissionUser = new UserSubordinateRolePermissionPO();
					permissionUser.setRoleId(role.getId());
					permissionUser.setUserId(user.getId());
					permissionUser.setUpdateTime(new Date());
				}
				permissions.add(permissionUser);
			}
		}
		
		userSubordinateRolePermissionDAO.save(permissions);
		
//		List<UserSubordinateRolePermissionVO> view_permissions = new ArrayList<UserSubordinateRolePermissionVO>();
//		for(UserSubordinateRolePermissionPO permission:permissions){
//			for(UserPO user:users){
//				if(permission.getUserId().equals(user.getId())){
//					view_permissions.add(new UserSubordinateRolePermissionVO().set(permission, user));
//					break;
//				}
//			}
//		}
//		
//		return view_permissions;
		return UserVO.getConverter(UserVO.class).convert(users, UserVO.class);
	}
	
	/**
	 * 移除隶属角色的成员<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月5日 上午11:44:44
	 * 
	 * @return UserVO 角色
	 */
	public UserVO removeUserFromRole(Long userId){
		UserPO userPO = userDAO.findOne(userId);

		userSubordinateRolePermissionDAO.removeByUserId(userId);

		return userPO != null ? new UserVO().set(userPO) : null;
	}
}
