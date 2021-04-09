package com.sumavision.tetris.bvc.business.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = BusinessCombineVideoPO.class, idClass = long.class)
public interface BusinessCombineVideoDAO extends MetBaseDAO<BusinessCombineVideoPO>{
	
	public List<BusinessCombineVideoPO> findByCombineVideoUid(String combineVideoUid);

	/**
	 * 根据groupId模糊查询
	 * @param condition groupId_%
	 * @return
	 */
	public List<BusinessCombineVideoPO> findByCombineVideoUidLike(String condition);
}
