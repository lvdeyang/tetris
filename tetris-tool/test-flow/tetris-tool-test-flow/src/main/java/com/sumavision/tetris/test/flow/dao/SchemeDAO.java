package com.sumavision.tetris.test.flow.dao;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;
import com.sumavision.tetris.test.flow.po.SchemePO;

import antlr.collections.List;

@RepositoryDefinition(domainClass = SchemePO.class, idClass = Long.class)
public interface SchemeDAO extends BaseDAO<SchemePO>{

	/**
	 * @Title: 每个uri在各自的服务下是唯一的<br/> 
	 * @param serviceUuid 服务唯一标识
	 * @param uri 调用地址
	 * @return SchemePO 接口方案
	 */
	public SchemePO findByServiceUuidAndUri(String serviceUuid, String uri);
	
}
