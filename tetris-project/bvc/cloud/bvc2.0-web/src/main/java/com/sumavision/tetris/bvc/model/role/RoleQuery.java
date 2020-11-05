package com.sumavision.tetris.bvc.model.role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

@Component
public class RoleQuery {
	
	@Autowired
	private RoleDAO roleDao;

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
	
}
