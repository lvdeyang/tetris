package com.sumavision.tetris.bvc.business.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.forward.CommonForwardPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommonForwardPO.class, idClass = long.class)
public interface CommonForwardDAO extends MetBaseDAO<CommonForwardPO>{

	public List<CommonForwardPO> findByBusinessId(String businessId);
}
