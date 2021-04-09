package com.sumavision.tetris.bvc.model.layout.forward;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = CombineTemplatePositionPO.class, idClass = Long.class)
public interface CombineTemplatePositionDAO extends BaseDAO<CombineTemplatePositionPO>{

	/**
	 * 根据合屏模板查询合屏模板布局列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月29日 下午2:45:48
	 * @param Collection<Long> combineTemplateIds 合屏模板id列表
	 * @return List<CombineTemplatePositionPO> 合屏模板布局列表
	 */
	public List<CombineTemplatePositionPO> findByCombineTemplateIdIn(Collection<Long> combineTemplateIds);
	
}
