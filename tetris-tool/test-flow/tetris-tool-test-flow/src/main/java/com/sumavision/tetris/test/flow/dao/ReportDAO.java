package com.sumavision.tetris.test.flow.dao;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;
import com.sumavision.tetris.test.flow.po.ReportPO;

@RepositoryDefinition(domainClass = ReportPO.class, idClass = Long.class)
public interface ReportDAO extends BaseDAO<ReportPO>{
	
}
