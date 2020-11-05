package com.sumavision.tetris.bvc.model.agenda.combine;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.orm.dao.BaseDAO;

@Component("com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoSrcDAO")
@RepositoryDefinition(domainClass = CombineVideoSrcPO.class, idClass = Long.class)
public interface CombineVideoSrcDAO extends BaseDAO<CombineVideoSrcPO>{

	/**
	 * 根据分屏查询源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月7日 上午11:30:30
	 * @param Collection<Long> combineVideoPositionIds 分屏id列表
	 * @return List<CombineVideoSrcPO> 源列表
	 */
	public List<CombineVideoSrcPO> findByCombineVideoPositionIdIn(Collection<Long> combineVideoPositionIds);
	
}
