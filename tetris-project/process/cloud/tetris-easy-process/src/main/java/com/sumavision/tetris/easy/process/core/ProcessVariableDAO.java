package com.sumavision.tetris.easy.process.core;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ProcessVariablePO.class, idClass = Long.class)
public interface ProcessVariableDAO extends BaseDAO<ProcessVariablePO>{

	/**
	 * 分页查询流程下的变量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月3日 上午10:23:31
	 * @param Long processId 流程id
	 * @param Pageable page 分页信息
	 * @return Page<ProcessVariablePO> 流程变量列表
	 */
	public Page<ProcessVariablePO> findByProcessId(Long processId, Pageable page);
	
	/**
	 * 统计流程下的变量数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月3日 上午10:32:08
	 * @param Long processId 流程id
	 * @return int 数量
	 */
	public int countByProcessId(Long processId);
	
	/**
	 * 查询流程下的所有变量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月3日 下午1:43:10
	 * @param Long processId 流程id
	 * @return List<ProcessVariablePO> 变量列表
	 */
	public List<ProcessVariablePO> findByProcessId(Long processId);
	
	/**
	 * 查询流程下的所有变量（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月19日 上午11:39:23
	 * @param Long processId 流程id
	 * @param Collection<Long> except 例外变量id列表
	 * @return List<ProcessVariablePO> 变量列表
	 */
	public List<ProcessVariablePO> findByProcessIdAndIdNotIn(Long processId, Collection<Long> except);
	
	/**
	 * 查询流程下的引用变量述<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月25日 下午2:07:46
	 * @param Long processId 流程id
	 * @return List<ProcessVariablePO> 变量列表
	 */
	public List<ProcessVariablePO> findByProcessIdAndExpressionValueNotNull(Long processId);
	
}
