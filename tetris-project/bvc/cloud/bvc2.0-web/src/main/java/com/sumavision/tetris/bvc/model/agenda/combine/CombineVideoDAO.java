package com.sumavision.tetris.bvc.model.agenda.combine;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.orm.dao.BaseDAO;

@Component("com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoDAO")
@RepositoryDefinition(domainClass = CombineVideoPO.class, idClass = Long.class)
public interface CombineVideoDAO extends BaseDAO<CombineVideoPO>{

	/**
	 * 根据业务查询合屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月2日 下午5:07:01
	 * @param Long businessId 业务id
	 * @param CombineBusinessType businessType 业务类型
	 * @return List<CombineVideoPO> 合屏列表
	 */
	public List<CombineVideoPO> findByBusinessIdAndBusinessType(Long businessId, CombineBusinessType businessType);
	
}
