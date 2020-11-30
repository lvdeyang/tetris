package com.suma.venus.resource.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.LianwangPassbyPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = LianwangPassbyPO.class, idClass = Long.class)
public interface LianwangPassbyDAO extends BaseDAO<LianwangPassbyPO>{

	public LianwangPassbyPO findByUuidAndType(String uuid, String type);
	
	@Query(value = "select * from LIANWANG_PASSBY where uuid = ?1", nativeQuery = true)
	public List<LianwangPassbyPO> findUuid(String uuid);
	
	public List<LianwangPassbyPO> findByLayerId(String layerId);
	
}
