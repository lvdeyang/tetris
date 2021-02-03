package com.sumavision.tetris.omms.hardware.server.data.process;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;
@RepositoryDefinition(domainClass = ServerProcessUsagePO.class, idClass = Long.class)
public interface ServerProcessUsageDAO extends BaseDAO<ServerProcessUsagePO>{

	public List<ServerProcessUsagePO> findByDataId(Long dataId);
	
	public List<ServerProcessUsagePO> findByDataIdIn(Collection<Long> dataIds);
}
