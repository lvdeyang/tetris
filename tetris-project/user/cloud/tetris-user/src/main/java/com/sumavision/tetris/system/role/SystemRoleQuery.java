package com.sumavision.tetris.system.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

/**
 * 用户系统角色查询<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月11日 下午3:21:45
 */
@Component
public class SystemRoleQuery {

	@Autowired
	private SystemRoleDAO systemRoleDao;
	
	@Autowired
	private SystemRoleGroupDAO systemRoleGroupDao;
	
	/**
	 * 查询用户的所有系统角色，包含组织机构关联角色，取并集<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月11日 下午3:21:59
	 * @param String userId 用户id
	 * @return List<SystemRoleVO> 角色列表
	 */
	public List<SystemRoleVO> queryUserRoles(String userId) throws Exception{
		return new ArrayListWrapper<SystemRoleVO>().add(new SystemRoleVO().setRoleId("1").setRoleName("菜单运维").setLevel_1(SystemRoleLevel.SYSTEM_ADMIN).setLevel_2(SystemRoleLevel.MENU))
												   .add(new SystemRoleVO().setRoleId("2").setRoleName("流程运维").setLevel_1(SystemRoleLevel.SYSTEM_ADMIN).setLevel_2(SystemRoleLevel.MENU))
												   .add(new SystemRoleVO().setRoleId("3").setRoleName("个人用户").setLevel_1(SystemRoleLevel.BUSINESS).setLevel_2(SystemRoleLevel.NORMAL))
												   .add(new SystemRoleVO().setRoleId("4").setRoleName("企业用户").setLevel_1(SystemRoleLevel.BUSINESS).setLevel_2(SystemRoleLevel.COMPANY_USER))
												   .add(new SystemRoleVO().setRoleId("5").setRoleName("企业管理员").setLevel_1(SystemRoleLevel.BUSINESS).setLevel_2(SystemRoleLevel.COMPANY_ADMIN))
												   .getList();
	}
	
	/**
	 * 分组查询角色（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 上午11:53:30
	 * @param Collection<String> roleIds 例外角色id列表
	 * @return List<SystemRoleGroupVO> 分组后的角色列表
	 */
	public List<SystemRoleGroupVO> listWithGroupByExceptIds(Collection<String> roleIds) throws Exception{
		List<SystemRolePO> roles = null;
		if(roleIds==null || roleIds.size()<=0){
			roles = systemRoleDao.findAll();
		}else{
			Set<Long> transRoleIds = new HashSet<Long>();
			for(String roleId:roleIds){
				transRoleIds.add(Long.valueOf(roleId));
			}
			roles = systemRoleDao.findByIdNotIn(transRoleIds);
		}
		return packageSystemRolesWithGroup(roles);
	}
	
	/**
	 * 根据给定的系统角色id分组查询系统橘色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 下午1:16:22
	 * @param Collection<String> roleIds 系统角色id列表
	 * @return List<SystemRoleGroupVO> 分组后的系统角色列表
	 */
	public List<SystemRoleGroupVO> listWithGroupByIds(Collection<String> roleIds) throws Exception{
		List<SystemRolePO> roles = null;
		if(roleIds==null || roleIds.size()<=0){
			return null;
		}else{
			Set<Long> transRoleIds = new HashSet<Long>();
			for(String roleId:roleIds){
				transRoleIds.add(Long.valueOf(roleId));
			}
			roles = systemRoleDao.findAll(transRoleIds);
		}
		return packageSystemRolesWithGroup(roles);
	}
	
	/**
	 * 分组打包系统橘色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 下午1:14:58
	 * @param List<SystemRolePO> roles 系统角色列表
	 * @return List<SystemRoleGroupVO 分组后的角色列表
	 */
	private List<SystemRoleGroupVO> packageSystemRolesWithGroup(List<SystemRolePO> roles) throws Exception{
		if(roles!=null && roles.size()>0){
			Set<Long> groupIds = new HashSet<Long>();
			for(SystemRolePO role:roles){
				groupIds.add(role.getSystemRoleGroupId());
			}
			List<SystemRoleGroupPO> groups = systemRoleGroupDao.findAll(groupIds);
			List<SystemRoleGroupVO> view_groups = new ArrayList<SystemRoleGroupVO>();
			for(SystemRoleGroupPO group:groups){
				SystemRoleGroupVO view_group = new SystemRoleGroupVO().set(group).setRoles(new ArrayList<SystemRoleVO>());
				view_groups.add(view_group);
				for(SystemRolePO role:roles){
					if(role.getSystemRoleGroupId().equals(group.getId())){
						view_group.getRoles().add(new SystemRoleVO().set(role));
					}
				}
			}
			return view_groups;
		}
		return null;
	}
	
	/**
	 * 根据id（批量）查询系统角色列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 下午1:31:31
	 * @param Collection<String> roleIds 系统角色id列表
	 * @return List<SystemRoleVO> 系统角色列表
	 */
	public List<SystemRoleVO> listByIds(Collection<String> roleIds) throws Exception{
		List<SystemRolePO> roles = null;
		if(roleIds==null || roleIds.size()<=0){
			return null;
		}else{
			Set<Long> transRoleIds = new HashSet<Long>();
			for(String roleId:roleIds){
				transRoleIds.add(Long.valueOf(roleId));
			}
			roles = systemRoleDao.findAll(transRoleIds);
			
			if(roles!=null && roles.size()>0){
				List<SystemRoleVO> view_roles = new ArrayList<SystemRoleVO>();
				for(SystemRolePO role:roles){
					view_roles.add(new SystemRoleVO().set(role));
				}
				return view_roles;
			}else{
				return null;
			}
		}
	}
	
}
