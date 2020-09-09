package com.sumavision.tetris.bvc.model.role;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.bvc.model.role.exception.RoleNotFoundException;

@Service
public class RoleService {

	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
	/**
	 * 添加内置角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午1:38:17
	 * @param String name 角色名称
	 * @param String internalRoleType 内置角色类型
	 * @param String roleUserMappingType 授权类型
	 * @return RoleVO 角色
	 */
	@Transactional(rollbackFor = Exception.class)
	public RoleVO addInternal(
			String name,
			String internalRoleType,
			String roleUserMappingType) throws Exception{
		RolePO role = new RolePO();
		role.setName(name);
		role.setInternalRoleType(internalRoleType==null?null:InternalRoleType.valueOf(internalRoleType));
		role.setRoleUserMappingType(roleUserMappingType==null?null:RoleUserMappingType.valueOf(roleUserMappingType));
		role.setUpdateTime(new Date());
		roleDao.save(role);
		return new RoleVO().set(role);
	}
	
	/**
	 * 修改内置角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午1:38:17
	 * @param Long id 角色id
	 * @param String name 角色名称
	 * @param String internalRoleType 内置角色类型
	 * @param String roleUserMappingType 授权类型
	 * @return RoleVO 角色
	 */
	@Transactional(rollbackFor = Exception.class)
	public RoleVO editInternal(
			Long id,
			String name,
			String internalRoleType,
			String roleUserMappingType) throws Exception{
		RolePO role = roleDao.findOne(id);
		if(role == null){
			throw new RoleNotFoundException(id);
		}
		role.setName(name);
		role.setInternalRoleType(internalRoleType==null?null:InternalRoleType.valueOf(internalRoleType));
		role.setRoleUserMappingType(roleUserMappingType==null?null:RoleUserMappingType.valueOf(roleUserMappingType));
		roleDao.save(role);
		return new RoleVO().set(role);
	}
	
	/**
	 * 删除角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午1:44:13
	 * @param Long id 角色id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) throws Exception{
		RolePO role = roleDao.findOne(id);
		if(role != null){
			roleDao.delete(role);
		}
		List<RoleChannelPO> channels = roleChannelDao.findByRoleId(id);
		if(channels!=null && channels.size()>0){
			roleChannelDao.deleteInBatch(channels);
		}
	}
	
}
