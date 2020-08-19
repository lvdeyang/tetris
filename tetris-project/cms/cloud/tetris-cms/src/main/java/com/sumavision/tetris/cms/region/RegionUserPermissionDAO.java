package com.sumavision.tetris.cms.region;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = RegionUserPermissionPO.class, idClass = Long.class)
public interface RegionUserPermissionDAO extends BaseDAO<RegionUserPermissionPO>{

	/**
	 * 根据组织id查询地区id列表<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月26日 下午2:45:32
	 * @param groupId 组织id
	 * @return List<Long> 地区id列表
	 */
	public List<String> findRegionIdByGroupId(String groupId);
	
	/**
	 * 根据用户id查询地区is列表<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月26日 下午2:47:35
	 * @param userId 用户id
	 * @return List<Long> 地区id列表
	 */
	public List<String> findRegionIdByUserId(String userId);
	
	/**
	 * 根据地区id查询地区用户关联表<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月26日 下午5:25:38
	 * @param regionId 地区id
	 * @return List<RegionUserPermissionPO> 地区用户关联表
	 */
	public List<RegionUserPermissionPO> findByRegionId(Long regionId);
	
	/**
	 * 根据地区id列表查询地区用户关联表<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月26日 下午5:25:38
	 * @param regionId 地区id
	 * @return List<RegionUserPermissionPO> 地区用户关联表
	 */
	public List<RegionUserPermissionPO> findByRegionIdIn(Collection<Long> regionIds);
	
	/**
	 * 根据地区id和组织id查询关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 下午2:06:31
	 * @param regionId 地区id
	 * @param groupId 组织id
	 * @return RegionUserPermissionPO 关联
	 */
	public RegionUserPermissionPO findByRegionIdAndGroupId(Long regionId, String groupId);
	
	/**
	 * 根据地区id和用户id查询关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 下午2:06:31
	 * @param regionId 地区id
	 * @param userId 用户id
	 * @return RegionUserPermissionPO 关联
	 */
	public RegionUserPermissionPO findByRegionIdAndUserId(Long regionId, String userId);
}
