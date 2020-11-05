package com.sumavision.tetris.easy.process.core;

import java.util.List;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ProcessParamReferencePO.class, idClass = Long.class)
public interface ProcessParamReferenceDAO extends BaseDAO<ProcessParamReferencePO>{

	/**
	 * 查询流程下的参数映射<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月25日 上午10:35:31
	 * @param Long processId 流程id
	 * @return List<ProcessParamReferencePO> 映射列表
	 */
	public List<ProcessParamReferencePO> findByProcessId(Long processId);
	
}
