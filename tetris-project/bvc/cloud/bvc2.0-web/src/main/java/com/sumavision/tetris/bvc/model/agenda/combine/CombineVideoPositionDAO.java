package com.sumavision.tetris.bvc.model.agenda.combine;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = CombineVideoPositionPO.class, idClass = Long.class)
public interface CombineVideoPositionDAO extends BaseDAO<CombineVideoPositionPO>{

	/**
	 * 查询合屏中的分屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月7日 上午11:21:25
	 * @param Long combineVideoId 合屏id
	 * @return List<CombineVideoPositionPO> 分屏列表
	 */
	public List<CombineVideoPositionPO> findByCombineVideoId(Long combineVideoId);
	
}
