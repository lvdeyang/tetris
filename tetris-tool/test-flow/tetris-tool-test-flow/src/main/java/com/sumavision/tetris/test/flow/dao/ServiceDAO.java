package com.sumavision.tetris.test.flow.dao;

import java.util.List;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;
import com.sumavision.tetris.test.flow.po.ServicePO;

@RepositoryDefinition(domainClass = ServicePO.class, idClass = Long.class)
public interface ServiceDAO extends BaseDAO<ServicePO>{

	/**
	 * @Title: 根据uuid查询服务<br/> 
	 * @param uuid
	 * @return List<ServicePO> 
	 */
	public List<ServicePO> findByUuid(String uuid);
	
}
