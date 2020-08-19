package com.sumavision.tetris.easy.process.access.service.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = RestServicePO.class, idClass = Long.class)
public interface RestServiceDAO extends BaseDAO<RestServicePO>{

	/**
	 * 分页查询rest服务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月17日 上午11:29:20
	 * @param page 分页
	 * @return Page<RestServicePO> 查询结果
	 */
	public Page<RestServicePO> findAll(Pageable page);
}
