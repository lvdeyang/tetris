package com.sumavision.tetris.bvc.business.group.forward;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = Jv230CombineAudioSrcPO.class, idClass = Long.class)
public interface Jv230CombineAudioSrcDAO extends BaseDAO<Jv230CombineAudioSrcPO>{

	/**
	 * 根据混音查询源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月20日 下午1:52:59
	 * @param Collection<Long> jv230CombineAudioId 混音id列表
	 * @return List<Jv230CombineAudioSrcPO> 源列表
	 */
	public List<Jv230CombineAudioSrcPO> findByJv230CombineAudioIdIn(Collection<Long> jv230CombineAudioId);
	
}
