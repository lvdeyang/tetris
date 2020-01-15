package com.sumavision.bvc.device.group.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.CombineVideoSrcPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CombineVideoSrcPO.class, idClass = long.class)
public interface CombineVideoSrcDAO extends MetBaseDAO<CombineVideoSrcPO>{

	/**
	 * @Title: 查询设备组中的合屏源
	 * @param groupId 设备组id
	 * @return List<CombineVideoSrcPO> 合屏源
	 * @throws
	 */
	@Query("select src from CombineVideoSrcPO src where src.position.combineVideo.group.id=?1")
	public List<CombineVideoSrcPO> findByGroupId(Long groupId);
	
	/**
	 * @Title: 查询合屏下的合屏源
	 * @param videoIds 合屏ids
	 * @return List<CombineVideoSrcPO> 合屏源
	 * @throws
	 */
	@Query("select src from CombineVideoSrcPO src where src.position.combineVideo.id in ?1")
	public List<CombineVideoSrcPO> findByGroupId(List<Long> videoIds);
	
	/**
	 * @Title: 查询设备组中的合屏的ids
	 * @param groupId 设备组id
	 * @return List<Long> 合屏ids
	 * @throws
	 */
	@Query(value = "select cvideo.id from CombineVideoSrcPO src right join src.position position right join position.combineVideo cvideo where cvideo.group.id=?1 group by cvideo.id having count(position.id)>1", nativeQuery = false)
	public Page<Long> findByGroupId(Long groupId, Pageable page);
	
	/**
	 * @Title: 根据合屏uuid查合屏源
	 * @param combineUuid 合屏uuid
	 * @return List<CombineVideoSrcPO> 合屏源
	 * @throws
	 */
	@Query("select src from CombineVideoSrcPO src where src.position.combineVideo.uuid=?1")
	public List<CombineVideoSrcPO> findByCombineUuid(String uuid);
}
