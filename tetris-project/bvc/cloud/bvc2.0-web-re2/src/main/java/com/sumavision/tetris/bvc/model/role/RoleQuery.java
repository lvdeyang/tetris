package com.sumavision.tetris.bvc.model.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.bvc.model.agenda.RoleAgendaPermissionDAO;
import com.sumavision.tetris.bvc.model.agenda.RoleAgendaPermissionPO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

@Component
public class RoleQuery {
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
	@Autowired
	private RoleAgendaPermissionDAO roleAgendaPermissionDao;

	/**
	 * 查询类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午1:30:15
	 * @return roleTypes Map<String, String> 内置角色类型
	 * @return mappingTypes Map<String, String> 授权类型
	 */
	public Map<String, Object> queryTypes() throws Exception{
		Map<String, String> roleTypes = new HashMap<String, String>();
		InternalRoleType[] roleTypeEnumss = InternalRoleType.values();
		for(InternalRoleType e:roleTypeEnumss){
			roleTypes.put(e.toString(), e.getName());
		}
		
		RoleUserMappingType[] mappingTypeEnums = RoleUserMappingType.values();
		Map<String, String> mappingTypes = new HashMap<String, String>();
		for(RoleUserMappingType e:mappingTypeEnums){
			mappingTypes.put(e.toString(), e.getName());
		}
		return new HashMapWrapper<String, Object>().put("roleTypes", roleTypes)
												   .put("mappingTypes", mappingTypes)
												   .getMap();
	}
	
	/**
	 * 分页查询内置角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午1:57:43
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return total long 总数据量
	 * @return rows List<RoleVO> 角色列表
	 */
	public Map<String, Object> loadInternal(
			int currentPage,
			int pageSize) throws Exception{
		Long total = roleDao.countByBusinessIdIsNullAndUserIdIsNull();
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<RolePO> pagedEntities = roleDao.findByBusinessIdIsNullAndUserIdIsNull(page);
		List<RoleVO> roles = RoleVO.getConverter(RoleVO.class).convert(pagedEntities.getContent(), RoleVO.class);
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", roles)
												   .getMap();
	}
	
	/**
	 * 查询全部内置角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午1:57:43
	 * @return List<RoleVO> 角色列表
	 */
	public List<RoleVO> loadAllInternal() throws Exception{
		List<RolePO> entities = roleDao.findByBusinessIdIsNullAndUserIdIsNull();
		return RoleVO.getConverter(RoleVO.class).convert(entities, RoleVO.class);
	}
	
	/**
	 * 查询议程角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午4:33:32
	 * @param Long agendaId 议程id
 	 * @return List<RoleVO> 角色列表
	 */
	public List<RoleVO> loadByAgendaId(Long agendaId) throws Exception{
		List<RoleAgendaPermissionPO> permissions = roleAgendaPermissionDao.findByAgendaId(agendaId);
		List<Long> roleIds = new ArrayList<Long>();
		if(permissions!=null && permissions.size()>0){
			for(RoleAgendaPermissionPO permission:permissions){
				roleIds.add(permission.getRoleId());
			}
		}
		if(roleIds.size() > 0){
			List<RolePO> roleEntities = roleDao.findAll(roleIds);
			return RoleVO.getConverter(RoleVO.class).convert(roleEntities, RoleVO.class);
		}
		return null;
	}
	
	/**
	 * 查询议程特定类型的角色（带通道信息）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午4:33:32
	 * @param Long agendaId 议程id
 	 * @param String type 通道类型
 	 * @return List<RoleVO> 角色列表
	 */
	public List<RoleVO> loadByAgendaIdAndTypeWithChannel(
			Long agendaId, 
			String type) throws Exception{
		List<RoleAgendaPermissionPO> permissions = roleAgendaPermissionDao.findByAgendaId(agendaId);
		List<Long> roleIds = new ArrayList<Long>();
		if(permissions!=null && permissions.size()>0){
			for(RoleAgendaPermissionPO permission:permissions){
				roleIds.add(permission.getRoleId());
			}
		}
		if(roleIds.size() > 0){
			List<RolePO> roleEntities = roleDao.findAll(roleIds);
			List<RoleChannelPO> channelEntities = roleChannelDao.findByRoleIdInAndType(roleIds, RoleChannelType.valueOf(type));
			List<RoleVO> roles = RoleVO.getConverter(RoleVO.class).convert(roleEntities, RoleVO.class);
			if(channelEntities!=null && channelEntities.size()>0){
				for(RoleVO role:roles){
					if(role.getChannels() == null) role.setChannels(new ArrayList<RoleChannelVO>());
					for(RoleChannelPO channelEntity:channelEntities){
						if(role.getId().equals(channelEntity.getRoleId())){
							role.getChannels().add(new RoleChannelVO().set(channelEntity));
						}
					}
				}
			}
			return roles;
		}
		return null;
	}
	
	/**
	 * 查询会议中的角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月26日 上午9:46:44
	 * @param Long businessId 业务id
	 * @return List<RoleVO> 角色列表
	 */
	public List<RoleVO> loadByGroupId(Long businessId) throws Exception{
		List<RolePO> roleEntities = roleDao.findByBusinessId(businessId);
		if(roleEntities == null) roleEntities = new ArrayList<RolePO>();
		RolePO chairman = roleDao.findByInternalRoleType(InternalRoleType.MEETING_CHAIRMAN);
		RolePO audience = roleDao.findByInternalRoleType(InternalRoleType.MEETING_AUDIENCE);
		roleEntities.add(chairman);
		roleEntities.add(audience);
		return RoleVO.getConverter(RoleVO.class).convert(roleEntities, RoleVO.class);
	}
	
	/**
	 * 根据会议和类型查询角色带通道信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月26日 上午10:43:44
	 * @param Long businessId 会议id
	 * @param String type 通道类型， ALL表示全类型
	 * @return List<RoleVO> 角色列表
	 */
	public List<RoleVO> loadByGroupIdAndTypeWithChannel(
			Long businessId,
			String type) throws Exception{
		
		List<RolePO> roleEntities = roleDao.findByBusinessId(businessId);
		RolePO chairman = roleDao.findByInternalRoleType(InternalRoleType.MEETING_CHAIRMAN);
		RolePO audience = roleDao.findByInternalRoleType(InternalRoleType.MEETING_AUDIENCE);
		
		List<RolePO> sortedRoleEntities = new ArrayList<RolePO>();
		List<Long> roleIds = new ArrayList<Long>();
		sortedRoleEntities.add(chairman);
		roleIds.add(chairman.getId());
		if(roleEntities!=null && roleEntities.size()>0){
			for(RolePO roleEntity:roleEntities){
				if(roleEntity.getRoleUserMappingType().equals(RoleUserMappingType.ONE_TO_ONE)){
					sortedRoleEntities.add(roleEntity);
					roleIds.add(roleEntity.getId());
				}
			}
		}
		sortedRoleEntities.add(audience);
		roleIds.add(audience.getId());
		if(roleEntities!=null && roleEntities.size()>0){
			for(RolePO roleEntity:roleEntities){
				if(roleEntity.getRoleUserMappingType().equals(RoleUserMappingType.ONE_TO_MANY)){
					sortedRoleEntities.add(roleEntity);
					roleIds.add(roleEntity.getId());
				}
			}
		}
		
		List<RoleChannelPO> channelEntities = null;
		if("ALL".equals(type)){
			channelEntities = roleChannelDao.findByRoleIdInOrderByTypeDesc(roleIds);
		}else{
			channelEntities = roleChannelDao.findByRoleIdInAndType(roleIds, RoleChannelType.valueOf(type));
		}
		
		List<RoleVO> roles = RoleVO.getConverter(RoleVO.class).convert(sortedRoleEntities, RoleVO.class);
		if(channelEntities!=null && channelEntities.size()>0){
			for(RoleVO role:roles){
				if(role.getChannels() == null) role.setChannels(new ArrayList<RoleChannelVO>());
				for(RoleChannelPO channelEntity:channelEntities){
					if(role.getId().equals(channelEntity.getRoleId())){
						role.getChannels().add(new RoleChannelVO().set(channelEntity));
					}
				}
			}
		}
		return roles;
	}
}
