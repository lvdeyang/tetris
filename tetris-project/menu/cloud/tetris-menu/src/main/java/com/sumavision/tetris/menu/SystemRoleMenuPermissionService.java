package com.sumavision.tetris.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.menu.SystemRoleMenuPermissionVO.SystemRoleMenuPermissionComparator;
import com.sumavision.tetris.menu.exception.MenuNotExistException;
import com.sumavision.tetris.menu.exception.SystemRoleMenuPermissionNotFoundException;
import com.sumavision.tetris.system.role.SystemRoleQuery;
import com.sumavision.tetris.system.role.SystemRoleVO;
import com.sumavision.tetris.system.role.exception.SystemRoleNotExistException;

/**
 * 菜单权限操作（主增删改）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月18日 下午3:05:36
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemRoleMenuPermissionService {

	@Autowired
	private MenuDAO menuDao;
	
	@Autowired
	private SystemRoleMenuPermissionDAO systemRoleMenuPermissionDao;
	
	@Autowired
	private SystemRoleMenuPermissionComparator systemRoleMenuPermissionComparator;
	
	@Autowired
	private SystemRoleQuery systemRoleQuery;
	
	/**
	 * 菜单绑定系统角色权限<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月18日 下午3:00:44
	 * @param Long menuId
	 * @param Collection<SystemRoleVO> roles 系统角色列表
	 * @return List<SystemRoleMenuPermissionVO> 添加的权限
	 */
	public List<SystemRoleMenuPermissionVO> bind(Long menuId, Collection<SystemRoleVO> roles) throws Exception{
		
		MenuPO menu = menuDao.findOne(menuId);
		if(menu == null){
			throw new MenuNotExistException(menuId);
		}
		
		List<SystemRoleMenuPermissionPO> existPermissions = systemRoleMenuPermissionDao.findByMenuIdIn(new ArrayListWrapper<Long>().add(menuId).getList());
		
		List<SystemRoleVO> complementRoles = new ArrayList<SystemRoleVO>();
		
		for(SystemRoleVO role:roles){
			boolean exist = false;
			if(existPermissions!=null && existPermissions.size()>0){
				for(SystemRoleMenuPermissionPO existPermission:existPermissions){
					if(existPermission.getRoleId().equals(role.getId())){
						exist = true;
						break;
					}
				}
			}
			if(!exist) complementRoles.add(role);
		}
		
		
		
		List<SystemRoleMenuPermissionPO> permissions = new ArrayList<SystemRoleMenuPermissionPO>();
		for(SystemRoleVO complementRole:complementRoles){
			SystemRoleMenuPermissionPO permission = new SystemRoleMenuPermissionPO();
			permission.setMenuId(menuId);
			permission.setRoleId(complementRole.getId());
			permission.setAutoGeneration(false);
			permission.setUpdateTime(new Date());
			permissions.add(permission);
		}
		systemRoleMenuPermissionDao.save(permissions);
		
		List<SystemRoleMenuPermissionVO> view_permissions = new ArrayList<SystemRoleMenuPermissionVO>();
		for(SystemRoleMenuPermissionPO permission:permissions){
			for(SystemRoleVO role:complementRoles){
				if(permission.getRoleId().equals(role.getId())){
					view_permissions.add(new SystemRoleMenuPermissionVO().set(permission, role));
					break;
				}
			}
		}
		
		//时间降序排列
		Collections.sort(view_permissions, systemRoleMenuPermissionComparator);
		
		return view_permissions;
	}
	
	/**
	 * 设置首页<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月12日 下午2:16:39
	 * @param Long id 权限id
	 * @return SystemRoleMenuPermissionVO 权限信息
	 */
	public SystemRoleMenuPermissionVO setHomePage(Long id) throws Exception{
		SystemRoleMenuPermissionPO permission = systemRoleMenuPermissionDao.findOne(id);
		if(permission == null){
			throw new SystemRoleMenuPermissionNotFoundException(id);
		}
		List<SystemRoleVO> roles = systemRoleQuery.listByIds(new ArrayListWrapper<String>().add(permission.getRoleId()).getList());
		if(roles==null || roles.size()<=0){
			throw new SystemRoleNotExistException(Long.valueOf(permission.getRoleId()));
		}
		Long systemRoleId = Long.valueOf(permission.getRoleId());
		List<SystemRoleMenuPermissionPO> permissions = systemRoleMenuPermissionDao.findByRoleIdAndIdNotIn(systemRoleId.toString(), new ArrayListWrapper<Long>().add(id).getList());
		if(permissions!=null && permissions.size()>0){
			List<SystemRoleMenuPermissionPO> needSave = new ArrayList<SystemRoleMenuPermissionPO>();
			for(SystemRoleMenuPermissionPO p:permissions){
				if(SystemRoleMenuPermissionFlag.HOME_PAGE.equals(p.getFlag())){
					p.setFlag(null);
					needSave.add(p);
				}
			}
			systemRoleMenuPermissionDao.save(needSave);
		}
		permission.setFlag(SystemRoleMenuPermissionFlag.HOME_PAGE);
		systemRoleMenuPermissionDao.save(permission);
		return new SystemRoleMenuPermissionVO().set(permission, roles.get(0));
	}
	
}
