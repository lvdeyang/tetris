package com.sumavision.tetris.easy.process.access.service.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class RestServiceQuery {

	@Autowired
	private RestServiceDAO restServiceDao;
	
	/**
	 * 分页查询rest服务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月17日 上午11:48:13
	 * @param int pageSize 每页数据量
	 * @param int currentPage 当前页码
	 * @return List<RestServicePO> rest服务列表
	 */
	public List<RestServicePO> findAll(int currentPage, int pageSize) throws Exception{
		Pageable page = PageRequest.of(currentPage-1, pageSize);
		Page<RestServicePO> pagedEntities = restServiceDao.findAll(page);
		return pagedEntities.getContent();
	}
	
}
