/**
 * 
 */
package com.sumavision.tetris.omms.software.service.installation;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月12日 下午2:00:41
 */
@RepositoryDefinition(domainClass = ProcessPO.class, idClass = Long.class)
public interface ProcessDAO extends BaseDAO<ProcessPO>{
	
	public List<ProcessPO> findByInstallationPackageId(Long installationPackageId);
}
