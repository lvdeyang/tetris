package com.sumavision.tetris.easy.process.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.persistence.entity.VariableInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

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
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 根据流程id查询流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 上午9:33:53
	 * @param Long id 流程id
	 * @return ProcessVO 流程
	 */
	public ProcessVO findById(Long id) throws Exception{
		ProcessPO process = processDao.findOne(id);
		if(process == null) return null;
		return new ProcessVO().set(process);
	}
	
	/**
	 * 根据id查询流程bpmn文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 上午9:39:00
	 * @param Long id 流程id
	 * @return String bpmn文件
	 */
	public String findBpmnById(Long id) throws Exception{
		ProcessPO process = processDao.findOne(id);
		if(process == null) return null;
		return process.getBpmn();
	}
	
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
	 * 分页查询流程模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月13日 下午1:19:48
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据
	 * @return rows List<ProcessVO> 流程列表
	 * @return total int 总数据量
	 */
	public Map<String, Object> findProcessTemplates(int currentPage, int pageSize) throws Exception{
		long total = processDao.countByType(ProcessType.TEMPLATE);
		
		List<ProcessPO> entities = findByType(ProcessType.TEMPLATE, currentPage, pageSize);
		
		List<ProcessVO> rows = ProcessVO.getConverter(ProcessVO.class).convert(entities, ProcessVO.class);
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("total", total)
																		 .put("rows", rows)
																		 .getMap();
		return result;
	}
	
	/**
	 * 分页查询公司下的流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月17日 上午10:40:43
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return rows List<ProcessVO> 流程列表
	 * @return total int 总数据量
	 */
	public Map<String, Object> findByComponentId(int currentPage, int pageSize) throws Exception{
		
		UserVO user = userQuery.current();
		
		int total = processDao.countByCompanyId(user.getGroupId());
		
		List<ProcessPO> entities = findByCompanyId(user.getGroupId(), currentPage, pageSize);
		
		List<ProcessVO> rows = ProcessVO.getConverter(ProcessVO.class).convert(entities, ProcessVO.class);
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("total", total)
																		 .put("rows", rows)
																		 .getMap();
		return result;
	}
	
	/**
	 * 根据类型分页查询流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月13日 下午1:17:25
	 * @param ProcessType type 类型
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return List<ProcessPO> 流程列表
	 */
	public List<ProcessPO> findByType(ProcessType type, int currentPage, int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<ProcessPO> pagedEntities = processDao.findByType(type, page);
		if(pagedEntities != null) return pagedEntities.getContent();
		return null;
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
	 * 分页查询公司下的流程（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月17日 下午12:59:25
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @param Collection<Long> except 例外流程id列表
	 * @return rows List<ProcessVO> 流程列表
	 * @return total int 总数据量
	 */
	public Map<String, Object> findByCompanyIdWithExcept(int currentPage, int pageSize, Collection<Long> except) throws Exception{
		if(except ==null || except.size()<=0) return findByComponentId(currentPage, pageSize);
		UserVO user = userQuery.current();
		
		int total = processDao.countByCompanyIdWithExcept(user.getGroupId(), except);
		
		List<ProcessPO> entities = findByCompanyIdWithExcept(user.getGroupId(), currentPage, pageSize, except);
		
		List<ProcessVO> rows = ProcessVO.getConverter(ProcessVO.class).convert(entities, ProcessVO.class);
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("total", total)
																		 .put("rows", rows)
																		 .getMap();
		return result;
	}
	
	/**
	 * 分页查询公司下的流程（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月11日 下午1:16:58
	 * @param String companyId 公司id
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return List<ProcessPO> 流程列表
	 */
	public List<ProcessPO> findByCompanyIdWithExcept(String companyId, int currentPage, int pageSize, Collection<Long> except) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<ProcessPO> pagedEntities = processDao.findByCompanyIdWithExcept(companyId,except, page);
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
	
	/**
	 * 查询我的待办事项<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月23日 下午4:16:23
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return long total 总数据量
	 * @return List<ProcessTaskMyReviewVO> rows 任务列表
	 */
	public Map<String, Object> queryMyTaskReview(int currentPage, int pageSize) throws Exception{
		UserVO user = userQuery.current();
		//这里认为是从1开始
		int firstResult = (pageSize*(currentPage-1));
		int maxResults = pageSize*currentPage-1;
		
		List<Task> tasks = null;
		Long total = 0l;
		
		if(user.getBusinessRoles() == null){
			total = taskService.createTaskQuery().taskCandidateUser(user.getId().toString()).count();
			tasks = taskService.createTaskQuery().taskCandidateUser(user.getId().toString())
												 .orderByTaskCreateTime()
												 .asc()
										 		 .listPage(firstResult, maxResults);
		}else{
			total = taskService.createTaskQuery().or()
												 .taskCandidateUser(user.getId().toString())
												 .taskCandidateGroupIn(Arrays.asList(user.getBusinessRoles().split(","))).count();
			tasks = taskService.createTaskQuery().or()
												 .taskCandidateUser(user.getId().toString())
												 .taskCandidateGroupIn(Arrays.asList(user.getBusinessRoles().split(",")))
												 .orderByTaskCreateTime()
												 .asc()
												 .listPage(firstResult, maxResults);
		}
		
		List<ProcessTaskMyReviewVO> myReviews = null;
		
		if(tasks==null || tasks.size()<=0) return new HashMapWrapper<String, Object>().put("total", total).put("rows", myReviews).getMap();
		
		myReviews = new ArrayList<ProcessTaskMyReviewVO>();
		
		Set<String> processInstanceIds = new HashSet<String>();
		Set<String> processDefinitionIds = new HashSet<String>();
		for(Task task:tasks){
			processInstanceIds.add(task.getProcessInstanceId());
			processDefinitionIds.add(task.getProcessDefinitionId().split(":")[0]);
		}
		List<ProcessPO> processes = processDao.findByUuidIn(processDefinitionIds);
		List<VariableInstance> vars = runtimeService.getVariableInstancesByExecutionIds(processInstanceIds);
		
		for(Task task:tasks){
			ProcessTaskMyReviewVO myReview = new ProcessTaskMyReviewVO().setProcessDefinitionId(task.getProcessDefinitionId().split(":")[0])
																		.setProcessInstanceId(task.getProcessInstanceId())
																		.setTaskDefinitionKey(task.getTaskDefinitionKey())
																		.setTaskId(task.getId())
																		.setUpdateTime(task.getCreateTime());
			if(vars!=null && vars.size()>0){
				int count = 0;
				for(VariableInstance var:vars){
					if(var.getProcessInstanceId().equals(myReview.getProcessInstanceId()) && var.getName().equals(InternalVariableKey.START_USER_NICKNAME.getVariableKey())){
						myReview.setStartUser(var.getValue().toString());
						count++;
					}
					if(var.getProcessInstanceId().equals(myReview.getProcessInstanceId()) && var.getName().equals(InternalVariableKey.START_TIME.getVariableKey())){
						myReview.setStartTime(var.getValue().toString());
						count++;
					}
					if(count >= 2){
						break;
					}
				}
			}
			
			if(processes!=null && processes.size()>0){
				for(ProcessPO process:processes){
					if(process.getUuid().equals(myReview.getProcessDefinitionId())){
						myReview.setProcessName(process.getName());
						break;
					}
				}
			}
			myReviews.add(myReview);
		}
		
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", myReviews)
												   .getMap();
	}
	
	/**
	 * 根据任务定义id和流程实例查询变量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月25日 上午11:26:51
	 * @param String processInstanceId 流程实例id
	 * @param String taskDefinitionKey 任务定义id
	 * @return history
	 * @return show List<ProcessTaskMyReviewVariableVO> 展示变量
	 * @return set List<ProcessTaskMyReviewVariableVO> 设置变量
	 */
	public Map<String, Object> queryVariablesByTaskDefinitionKey(
			String processInstanceId, 
			String taskDefinitionKey) throws Exception{
		
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		
		ProcessPO process = processDao.findByUuid(processInstance.getProcessDefinitionKey());
		
		List<ProcessTaskMyReviewVariableVO> show = null;
		List<ProcessTaskMyReviewVariableVO> set = null;
		
		if(process.getUserTaskBindVariables() != null){
			JSONObject variables = JSONObject.parseObject(process.getUserTaskBindVariables());
			if(variables.containsKey("show")){
				show = new ArrayList<ProcessTaskMyReviewVariableVO>();
				JSONArray array =  variables.getJSONArray("show");
				for(int i=0; i<array.size(); i++){
					JSONObject var =  array.getJSONObject(i);
					if(var.getString("taskId").equals(taskDefinitionKey)){
						show.add(new ProcessTaskMyReviewVariableVO().set(var));
					}
				}
			}
			if(variables.containsKey("set")){
				set = new ArrayList<ProcessTaskMyReviewVariableVO>();
				JSONArray array = variables.getJSONArray("set");
				for(int i=0; i<array.size(); i++){
					JSONObject var = array.getJSONObject(i);
					if(var.getString("taskId").equals(taskDefinitionKey)){
						set.add(new ProcessTaskMyReviewVariableVO().set(var));
					}
				}
			}
		}
		
		JSONObject variableContext = (JSONObject)runtimeService.getVariable(processInstanceId, InternalVariableKey.VARIABLE_CONTEXT.getVariableKey());
		
		if(show!=null && show.size()>0){
			for(ProcessTaskMyReviewVariableVO var:show){
				if(variableContext.containsKey(var.getKey())){
					var.setValue(variableContext.getString(var.getKey()));
				}
			}
		}

		if(set!=null && set.size()>0){
			for(ProcessTaskMyReviewVariableVO var:set){
				if(variableContext.containsKey(var.getKey())){
					var.setValue(variableContext.getString(var.getKey()));
				}
			}
		}
		
		return new HashMapWrapper<String, Object>().put("history", "")
												   .put("show", show)
												   .put("set", set)
												   .getMap();
	}
	
	/**
	 * 查询任务历史变量设置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月31日 上午9:27:25
	 * @param String processInstanceId 流程实例id
	 * @param String taskDefinitionKey 任务定义id
	 * @return String userId 提交用户id
	 * @return String userNickName 提交用户昵称
	 * @return String taskId 任务定义id
	 * @return String type 任务类型
	 * @return List<ProcessTaskMyReviewVariableVO> variableSet 提交的变量内容
	 */
	public JSONObject queryTaskHistory(
			String processInstanceId, 
			String taskDefinitionKey) throws Exception{
		List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
		List<JSONObject> nodeHistorys = null;
		for(HistoricVariableInstance variable:variables){
			if(variable.getVariableName().equals(InternalVariableKey.NODE_HISTORY.getVariableKey())){
				nodeHistorys = (List<JSONObject>)variable.getValue();
				break;
			}
		}
		JSONObject targetNodeHistory = null;
		if(nodeHistorys!=null && nodeHistorys.size()>0){
			for(JSONObject nodeHistory:nodeHistorys){
				if(nodeHistory.getString("taskId").equals(taskDefinitionKey)){
					targetNodeHistory = nodeHistory;
					break;
				}
			}
		}
		return targetNodeHistory;
	}
	
	/**
	 * 查询当前用户发起的流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月31日 下午3:34:07
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return total Long 总数据量
	 * @return rows List<ProcessMyStartVO> 流程列表
	 */
	public Map<String, Object> queryMyStartProcess(int currentPage, int pageSize) throws Exception{
		UserVO user = userQuery.current();
		Long total = 0l;
		List<ProcessMyStartVO> rows = null;
		//这里认为是从1开始
		int firstResult = (pageSize*(currentPage-1));
		int maxResults = pageSize*currentPage-1;
		List<HistoricProcessInstance> processInstances = historyService.createHistoricProcessInstanceQuery()
																	   .variableValueEquals(InternalVariableKey.START_USER_ID.getVariableKey(), user.getId().toString())
																	   .orderByProcessInstanceEndTime()
																	   .asc()
																	   .listPage(firstResult, maxResults);
		if(processInstances!=null && processInstances.size()>0){
			rows = new ArrayList<ProcessMyStartVO>();
			total = historyService.createHistoricProcessInstanceQuery()
							      .variableValueEquals(InternalVariableKey.START_USER_ID.getVariableKey(), user.getId().toString())
							      .count();
			
			Set<String> processDefinitionKeys = new HashSet<String>();
			Set<String> processInstanceIds = new HashSet<String>();
			for(HistoricProcessInstance processInstance:processInstances){
				processDefinitionKeys.add(processInstance.getProcessDefinitionKey());
				processInstanceIds.add(processInstance.getId());
			}
			
			List<ProcessPO> processes = processDao.findByUuidIn(processDefinitionKeys);
			
			List<HistoricVariableInstance> categories = historyService.createHistoricVariableInstanceQuery()
																	  .executionIds(processInstanceIds)
																	  .variableName(InternalVariableKey.CATEGORY.getVariableKey())
																	  .list();
			
			List<HistoricVariableInstance> businesses = historyService.createHistoricVariableInstanceQuery()
																	  .executionIds(processInstanceIds)
																	  .variableName(InternalVariableKey.BUSINESS.getVariableKey())
																	  .list();
			
			for(HistoricProcessInstance processInstance:processInstances){
				ProcessPO targetProcess = null;
				HistoricVariableInstance targetCategory = null;
				HistoricVariableInstance targetBusiness = null;
				for(ProcessPO process:processes){
					if(process.getUuid().equals(processInstance.getProcessDefinitionKey())){
						targetProcess = process;
						break;
					}
				}
				for(HistoricVariableInstance category:categories){
					if(category.getProcessInstanceId().equals(processInstance.getId())){
						targetCategory = category;
						break;
					}
				}
				for(HistoricVariableInstance business:businesses){
					if(business.getProcessInstanceId().equals(processInstance.getId())){
						targetBusiness = business;
						break;
					}
				}
				ProcessMyStartVO processMyStart = new ProcessMyStartVO().setProcessDefinitionKey(processInstance.getProcessDefinitionKey())
																		.setProcessInstanceId(processInstance.getId())
																		.setStartTime(DateUtil.format(processInstance.getStartTime(), DateUtil.dateTimePattern))
																		.setName(targetProcess.getName());
				if(processInstance.getEndTime() != null){
					processMyStart.setEndTime(DateUtil.format(processInstance.getEndTime(), DateUtil.dateTimePattern));
					processMyStart.setStatus("已结束");
				}else{
					processMyStart.setStatus("进行中");
				}
				if(targetCategory != null) processMyStart.setCategory(targetCategory.getValue().toString());
				if(targetBusiness != null) processMyStart.setBusiness(targetBusiness.getValue().toString());
																		
				rows.add(processMyStart);
			}
		}
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
}
