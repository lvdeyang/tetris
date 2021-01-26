package com.sumavision.tetris.business.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.alarm.bo.OprlogParamBO.EOprlogType;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.system.role.SystemRoleDAO;
import com.sumavision.tetris.system.role.SystemRolePO;
import com.sumavision.tetris.system.role.UserSystemRolePermissionDAO;
import com.sumavision.tetris.system.role.UserSystemRolePermissionPO;
import com.sumavision.tetris.system.role.UserSystemRolePermissionVO;
import com.sumavision.tetris.system.role.exception.SystemRoleNotExistException;
import com.sumavision.tetris.user.OperationLogService;
import com.sumavision.tetris.user.UserDAO;
import com.sumavision.tetris.user.UserPO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.user.exception.UserNotExistException;

/**
 * 用户业务角色权限操作（主增删改）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月21日 下午2:32:04
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserBusinessRolePermissionService {

	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private SystemRoleDAO systemRoleDao;
	
	@Autowired
	private UserSystemRolePermissionDAO userSystemRolePermissionDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private OperationLogService operationLogService;
	
	/**
	 * 用户绑定系统角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月21日 下午2:07:50
	 * @param Long userId 用户id
	 * @param Collection<Long> roleIds 系统角色id列表
	 * @return List<UserSystemRolePermissionVO> 新建的权限列表
	 */
	public List<UserSystemRolePermissionVO> bindBusinessRole(Long userId, Collection<Long> roleIds) throws Exception{
		
		UserPO user = userDao.findById(userId);
		if(user == null){
			throw new UserNotExistException(userId);
		}
		
		List<SystemRolePO> roles = systemRoleDao.findAllById(roleIds);
		if(roles==null || roles.size()<=0) return null;
		
		List<UserSystemRolePermissionPO> existPermissions = userSystemRolePermissionDao.findByUserIdAndRoleIdIn(userId, roleIds);
		
		List<UserSystemRolePermissionPO> permissions = new ArrayList<UserSystemRolePermissionPO>();
		for(SystemRolePO role:roles){
			boolean finded = false;
			if(existPermissions!=null && existPermissions.size()>0){
				for(UserSystemRolePermissionPO existPermission:existPermissions){
					if(role.getId().equals(existPermission.getRoleId())){
						finded = true;
						break;
					}
				}
			}
			if(!finded){
				UserSystemRolePermissionPO permission = new UserSystemRolePermissionPO();
				permission.setRoleId(role.getId());
				permission.setUserId(user.getId());
				permission.setAutoGeneration(false);
				permission.setUpdateTime(new Date());
				permission.setRoleType(role.getType());
				permissions.add(permission);
			}
		}
		
		userSystemRolePermissionDao.saveAll(permissions);
		
		List<UserSystemRolePermissionVO> view_permissions = new ArrayList<UserSystemRolePermissionVO>();
		for(UserSystemRolePermissionPO permission:permissions){
			for(SystemRolePO role:roles){
				if(permission.getRoleId().equals(role.getId())){
					view_permissions.add(new UserSystemRolePermissionVO().set(permission, role));
					break;
				}
			}
		}
		
		return view_permissions;
	}
	
	/**
	 * 业务角色授权用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月23日 下午8:06:45
	 * @param Long roleId 系统角色id
	 * @param Collectin<Long> userIds 用户id列表
	 * @return List<UserSystemRolePermissionVO> 添加的权限列表
	 */
	public List<UserSystemRolePermissionVO> bindUser(Long roleId, Collection<Long> userIds) throws Exception{
		
		SystemRolePO role = systemRoleDao.findById(roleId);
		if(role == null){
			throw new SystemRoleNotExistException(roleId);
		}
		
		List<UserPO> users = userDao.findAllById(userIds);
		if(users==null || users.size()<=0) return null;
		
		List<UserSystemRolePermissionPO> existPermissions = userSystemRolePermissionDao.findByRoleIdAndUserIdIn(roleId, userIds);
		
		StringBufferWrapper userName = new StringBufferWrapper();
		
		List<UserSystemRolePermissionPO> permissions = new ArrayList<UserSystemRolePermissionPO>();
		for(UserPO user:users){
			boolean finded = false;
			if(existPermissions!=null && existPermissions.size()>0){
				for(UserSystemRolePermissionPO existPermission:existPermissions){
					if(user.getId().equals(existPermission.getUserId())){
						finded = true;
						break;
					}
				}
			}
			if(!finded){
				UserSystemRolePermissionPO permission = new UserSystemRolePermissionPO();
				permission.setRoleId(role.getId());
				permission.setUserId(user.getId());
				permission.setAutoGeneration(false);
				permission.setUpdateTime(new Date());
				permission.setRoleType(role.getType());
				permissions.add(permission);
			}
			userName.append(user.getUsername()).append(",");
		}
		
		userSystemRolePermissionDao.saveAll(permissions);
		
		List<UserSystemRolePermissionVO> view_permissions = new ArrayList<UserSystemRolePermissionVO>();
		for(UserSystemRolePermissionPO permission:permissions){
			for(UserPO user:users){
				if(permission.getUserId().equals(user.getId())){
					view_permissions.add(new UserSystemRolePermissionVO().set(permission, user));
					break;
				}
			}
		}
		
		//用户绑定角色日志
		UserVO userVO = userQuery.current();
		operationLogService.send(userVO.getUsername(), "用户绑定角色", "用户  " + userVO.getUsername() + " 添加用户  " + userName.toString() + " 到 " + role.getName() + " 角色", EOprlogType.USER_ROLE_CHANGE);
		
		return view_permissions;
	}
	
	/**
	 * 解绑用户系统角色权限<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月21日 下午5:29:54
	 * @param Long id 权限id
	 */
	public void unbind(Long id) throws Exception{
		UserSystemRolePermissionPO permission = userSystemRolePermissionDao.findById(id);
		
		if(permission != null){
			userSystemRolePermissionDao.delete(permission);
			
			UserPO userPO = userDao.findById(permission.getUserId());
			SystemRolePO role = systemRoleDao.findById(permission.getRoleId());
			
			//用户解除角色日志
			UserVO userVO = userQuery.current();
			operationLogService.send(userVO.getUsername(), "用户解除角色", "用户  " + userVO.getUsername() + " 解除用户  " + userPO.getUsername() + " 的 " + role.getName() + " 角色", EOprlogType.USER_ROLE_CHANGE);
		}
	}
	
}
