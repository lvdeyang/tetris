package com.sumavision.tetris.easy.process.core;

import java.util.Collection;
import java.util.List;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ProcessInstanceDeploymentPermissionPO.class, idClass = Long.class)
public interface ProcessInstanceDeploymentPermissionDAO extends BaseDAO<ProcessInstanceDeploymentPermissionPO>{

	/**
	 * 查询流程的使用情况<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月4日 上午11:19:18
	 * @param Collection<String> deploymentIds 流程发布信息
	 * @return List<ProcessInstanceDeploymentPermissionPO> 流程使用情况
	 */
	public List<ProcessInstanceDeploymentPermissionPO> findByDeploymentIdIn(Collection<String> deploymentIds);
	
}
