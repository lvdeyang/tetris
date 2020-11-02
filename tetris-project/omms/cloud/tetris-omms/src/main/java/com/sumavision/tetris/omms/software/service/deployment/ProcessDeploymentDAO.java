/**
 * 
 */
package com.sumavision.tetris.omms.software.service.deployment;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月12日 下午5:07:57
 */
@RepositoryDefinition(domainClass = ProcessDeploymentPO.class, idClass = Long.class)
public interface ProcessDeploymentDAO extends BaseDAO<ProcessDeploymentPO>{
	
	public List<ProcessDeploymentPO> findByServiceDeploymentId(Long serviceDeploymentId);
	
	public List<ProcessDeploymentPO> findByServiceDeploymentIdIn(List<Long> deploymentIds);
	
	public ProcessDeploymentPO findByProcessId(String processId);
	
	public List<ProcessDeploymentPO> findByServerId(Long serverId);
}
