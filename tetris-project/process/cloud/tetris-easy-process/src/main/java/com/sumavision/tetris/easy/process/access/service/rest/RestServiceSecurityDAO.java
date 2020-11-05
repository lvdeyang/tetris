package com.sumavision.tetris.easy.process.access.service.rest;

import java.util.List;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = RestServiceSecurityPO.class, idClass = Long.class)
public interface RestServiceSecurityDAO extends BaseDAO<RestServiceSecurityPO>{

	/**
	 * 查询rest服务接口访问安全配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月4日 下午1:25:45
	 * @param Long restServiceId rest服务id
	 * @return List<RestServiceSecurityPO> 配置列表
	 */
	public List<RestServiceSecurityPO> findByRestServiceId(Long restServiceId);
	
}
