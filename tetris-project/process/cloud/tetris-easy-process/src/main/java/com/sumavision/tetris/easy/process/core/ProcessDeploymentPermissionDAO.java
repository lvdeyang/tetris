package com.sumavision.tetris.easy.process.core;

import java.util.List;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ProcessDeploymentPermissionPO.class, idClass = Long.class)
public interface ProcessDeploymentPermissionDAO extends BaseDAO<ProcessDeploymentPermissionPO>{

	/**
	 * 查询流程的所有发布信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月4日 上午10:58:07
	 * @param Long processId 流程id
	 * @return List<ProcessDeploymentPermissionPO> 发布列表
	 */
	public List<ProcessDeploymentPermissionPO> findByProcessId(Long processId);
	
}
