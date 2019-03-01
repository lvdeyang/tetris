package com.sumavision.tetris.cms.region;

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
}
