package com.sumavision.tetris.easy.process.core;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ProcessCompanyPermissionPO.class, idClass = Long.class)
public interface ProcessCompanyPermissionDAO extends BaseDAO<ProcessCompanyPermissionPO>{

	/**
	 * 查询流程的授权情况<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月14日 下午1:55:36
	 * @param Long processId 流程id
	 * @return List<ProcessCompanyPermissionPO> 授权列表
	 */
	public List<ProcessCompanyPermissionPO> findByProcessId(Long processId);
	
}
