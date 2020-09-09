package com.sumavision.tetris.bvc.model.agenda.combine;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.orm.dao.BaseDAO;

import com.sumavision.tetris.bvc.model.agenda.combine.CombineAudioSrcPO;

@Component("com.sumavision.tetris.bvc.model.agenda.combine.CombineAudioSrcDAO")
@RepositoryDefinition(domainClass = CombineAudioSrcPO.class, idClass = Long.class)
public interface CombineAudioSrcDAO extends BaseDAO<CombineAudioSrcPO>{

	/**
	 * 查询混音源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月8日 下午1:18:55
	 * @param Long combineAudioId 混音id
	 * @return List<CombineAudioSrcPO> 混音源列表
	 */
	public List<CombineAudioSrcPO> findByCombineAudioId(Long combineAudioId);
	
}
