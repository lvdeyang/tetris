package com.sumavision.tetris.cms.region;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = RegionPO.class, idClass = Long.class)
public interface RegionDAO extends BaseDAO<RegionPO>{

	/**
	 * 查询地区下的所有子地区<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日 下午3:56:01
	 * @param String reg1 '%/id'
	 * @param String reg2 '%/id/%'
	 * @return List<RegionPO> 地区列表
	 */
	@Query(value = "SELECT * FROM TETRIS_CMS_REGION WHERE PARENT_PATH LIKE ?1 OR PARENT_PATH LIKE ?2", nativeQuery = true)
	public List<RegionPO> findAllSubRegions(String reg1, String reg2);
	
	/**
	 * 根据区域编码表查询区域列表<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月13日 下午4:49:14
	 * @param codes 区域编码表
	 * @return List<RegionPO> 区域列表
	 */
	public List<RegionPO> findByCodeIn(Collection<String> codes);
	
	/**
	 * 根据用户id查询地区列表<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月26日 下午5:20:51
	 * @param userId 用户id
	 * @return List<RegionPO> 地区列表
	 */
	@Query(value = "SELECT region.* FROM TETRIS_CMS_REGION region LEFT JOIN TETRIS_CMS_REGION_USER_PERMISSION permission ON region.id = permission.region_id WHERE permission.user_id = ?1", nativeQuery = true)
	public List<RegionPO> findByUserId(String userId);
	
	/**
	 * 根据组织id查询地区列表<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月26日 下午5:20:54
	 * @param groupId 组织id
	 * @return List<RegionPO> 地区列表
	 */
	@Query(value = "SELECT region.* FROM TETRIS_CMS_REGION region LEFT JOIN TETRIS_CMS_REGION_USER_PERMISSION permission ON region.id = permission.region_id WHERE permission.group_id = ?1", nativeQuery = true)
	public List<RegionPO> findByGroupId(String groupId);
	
	@Query(value = "SELECT region.* FROM TETRIS_CMS_REGION region "
			+ "LEFT JOIN TETRIS_CMS_REGION_USER_PERMISSION permission "
			+ "ON region.id = permission.region_id "
			+ "WHERE permission.group_id = ?1 "
			+ "AND region.name LIKE CONCAT('%', ?2 '%')", nativeQuery = true)
	public List<RegionPO> findByGroupIdAndName(String groupId, String name);
}
