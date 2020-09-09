package com.sumavision.tetris.spring.eureka.application;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ApplicationPO.class, idClass = Long.class)
public interface ApplicationDAO extends BaseDAO<ApplicationPO>{

	/**
	 * 根据实例查询服务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月23日 上午11:44:03
	 * @param String instanceId 实例id
	 * @return ApplicationPO 服务
	 */
	public ApplicationPO findByInstanceId(String instanceId);
	
}
