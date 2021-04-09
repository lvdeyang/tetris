package com.sumavision.bvc.device.group.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Component;

import com.sumavision.bvc.device.group.po.CombineAudioPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@Component("com.sumavision.bvc.device.group.dao.CombineAudioDAO")
@RepositoryDefinition(domainClass = CombineAudioPO.class, idClass = long.class)
public interface CombineAudioDAO extends MetBaseDAO<CombineAudioPO> {

	/**
	 * @Title: 查询设备组中的混音
	 * @param groupId 设备组id
	 * @return List<CombineAudioPO> 混音
	 * @throws
	 */
	@Query("select combineAudio from com.sumavision.bvc.device.group.po.CombineAudioPO combineAudio left join combineAudio.srcs src where combineAudio.group.id=?1 group by combineAudio.id having count(src.id)>1")
	public Page<CombineAudioPO> findByGroupId(Long groupId, Pageable page);
	
	/**
	 * @Title: 根据uuid查询混音
	 * @param combineUuid 混音uuid
	 * @return CombineAudioPO 混音
	 * @throws
	 */
	@Query("select combineAudio from com.sumavision.bvc.device.group.po.CombineAudioPO combineAudio where combineAudio.uuid=?1")
	public CombineAudioPO findByCombineUuid(String combineUuid);

}
