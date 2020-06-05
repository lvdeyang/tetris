package com.sumavision.bvc.device.group.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.CombineVideoPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CombineVideoPO.class, idClass = long.class)
public interface CombineVideoDAO extends MetBaseDAO<CombineVideoPO> {

	/**
	 * @Title: 查询设备组中的合屏的ids
	 * @param groupId 设备组id
	 * @return List<Long> 合屏ids
	 * @throws
	 */
	@Query(value = "select cvideo.id from com.sumavision.bvc.device.group.po.CombineVideoPO cvideo left join cvideo.positions position left join position.srcs src where cvideo.group.id=?1 group by cvideo.id having count(position.id)>1", nativeQuery = false)
	public Page<Long> findByGroupId(Long groupId, Pageable page);
	
	/**
	 * @Title: 根据uuid查合屏
	 * @param uuid 合屏uuid
	 * @return CombineVideoPO 
	 * @throws
	 */
	@Query("select cvideo from com.sumavision.bvc.device.group.po.CombineVideoPO cvideo where cvideo.uuid=?1")	
	public CombineVideoPO findByUuid(String uuid);
}
