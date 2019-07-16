package com.sumavision.tetris.easy.process.core;

import java.util.List;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * 流程查询<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月24日 下午5:19:55
 */
@Component
public class ProcessQuery {

	@Autowired
	private ProcessDAO processDao;
	
	@Autowired
	private RuntimeService runtimeService;
	
	/**
	 * 分页查询流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午5:17:17
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<ProcessPO> 流程列表
	 */
	public List<ProcessPO> findAll(int currentPage, int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage - 1, pageSize);
		Page<ProcessPO> pagedProcesses = processDao.findAll(page);
		return pagedProcesses.getContent();
	}
	
	/**
	 * 分页查询公司下的流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月11日 下午1:16:58
	 * @param String companyId 公司id
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return List<ProcessPO> 流程列表
	 */
	public List<ProcessPO> findByCompanyId(String companyId, int currentPage, int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<ProcessPO> pagedEntities = processDao.findByCompanyId(companyId, page);
		if(pagedEntities != null) return pagedEntities.getContent();
		return null;
	}
	
	/**
	 * 查询流程实例进度<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月2日 下午1:32:05
	 * @throws Exception
	 */
	public void queryProgressByProcessInstanceId(String processInstanceId) throws Exception{
		
		Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).onlyChildExecutions().singleResult();
		
		//execution.getActivityId()
		
	}
	
}
