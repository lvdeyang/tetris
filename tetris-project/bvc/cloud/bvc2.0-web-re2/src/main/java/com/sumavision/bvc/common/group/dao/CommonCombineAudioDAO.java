package com.sumavision.bvc.common.group.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.common.group.po.CommonCombineAudioPO;
import com.sumavision.bvc.device.group.po.CombineAudioPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommonCombineAudioPO.class, idClass = long.class)
public interface CommonCombineAudioDAO extends MetBaseDAO<CommonCombineAudioPO> {

	/**
	 * @Title: 查询设备组中的混音
	 * @param groupId 设备组id
	 * @return List<CombineAudioPO> 混音
	 * @throws
	 */
//	@Query("select combineAudio from CombineAudioPO combineAudio left join combineAudio.srcs src where combineAudio.group.id=?1 group by combineAudio.id having count(src.id)>1")
//	public Page<CombineAudioPO> findByGroupId(Long groupId, Pageable page);
	
	/**
	 * @Title: 根据uuid查询混音
	 * @param combineUuid 混音uuid
	 * @return CombineAudioPO 混音
	 * @throws
	 */
//	@Query("select combineAudio from CombineAudioPO combineAudio where combineAudio.uuid=?1")
//	public CombineAudioPO findByCombineUuid(String combineUuid);
}
