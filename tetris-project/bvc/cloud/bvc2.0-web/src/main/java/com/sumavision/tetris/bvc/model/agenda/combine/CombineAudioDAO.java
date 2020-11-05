package com.sumavision.tetris.bvc.model.agenda.combine;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.orm.dao.BaseDAO;

@Component("com.sumavision.tetris.bvc.model.agenda.combine.CombineAudioDAO")
@RepositoryDefinition(domainClass = CombineAudioPO.class, idClass = Long.class)
public interface CombineAudioDAO extends BaseDAO<CombineAudioPO>{

	/**
	 * 根据业务查询混音<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月7日 下午3:49:33
	 * @param Long businessId 业务id
	 * @param CombineBusinessType businessType 业务类型
	 * @return List<CombineAudioPO> 混音列表
	 */
	public List<CombineAudioPO> findByBusinessIdAndBusinessType(Long businessId, CombineBusinessType businessType);
	
}
