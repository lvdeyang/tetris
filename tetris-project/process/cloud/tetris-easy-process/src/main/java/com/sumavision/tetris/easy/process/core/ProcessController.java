package com.sumavision.tetris.easy.process.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.easy.process.access.point.AccessPointDAO;
import com.sumavision.tetris.easy.process.access.point.AccessPointPO;
import com.sumavision.tetris.easy.process.access.point.AccessPointScope;
import com.sumavision.tetris.easy.process.access.point.exception.AccessPointNotExistException;
import com.sumavision.tetris.easy.process.access.service.ServiceType;
import com.sumavision.tetris.easy.process.access.service.rest.RestServiceDAO;
import com.sumavision.tetris.easy.process.access.service.rest.RestServicePO;
import com.sumavision.tetris.easy.process.core.exception.ProcessNotExistException;
import com.sumavision.tetris.easy.process.core.exception.UserHasNoPermissionForProcessActionException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/process")
public class ProcessController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ProcessDAO processDao;
	
	@Autowired
	private ProcessQuery processQuery;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private RestServiceDAO restServiceDao;
	
	@Autowired
	private AccessPointDAO accessPointDao;
	
	/**
	 * 分页查询流程模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月13日 上午11:44:51
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return int total 总数据量
	 * @return List<ProcessVO> 模板列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/template")
	public Object listTemplate(
			int currentPage, 
			int pageSize, 
			HttpServletRequest request) throws Exception{
		
		return processQuery.findProcessTemplates(currentPage, pageSize);
	}
	
	/**
	 * 分页查询流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午5:31:49
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 总数据量
	 * @return List<ProcessVO> 流程列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		return processQuery.findByComponentId(currentPage, pageSize);
	}
	
	/**
	 * 分页查询流程（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午5:31:49
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @param JSONArray except 例外流程id列表
	 * @return int total 总数据量
	 * @return List<ProcessVO> 流程列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/with/except")
	public Object listWithExcept(
			int currentPage,
			int pageSize,
			String except,
			HttpServletRequest request) throws Exception{
		List<Long> ids = null;
		if(except != null){
			ids = JSON.parseArray(except, Long.class);
		}
		return processQuery.findByCompanyIdWithExcept(currentPage, pageSize, ids);
	}
	
	/**
	 * 添加一个流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午5:36:40
	 * @param Long templateId 模板id
	 * @param String processId 用户自定义流程id
	 * @param String name 流程名称
	 * @param String remarks 流程说明
	 * @return ProcessVO 流程数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long templateId,
			String type,
			String processId,
			String name,
			String remarks,
			HttpServletRequest request) throws Exception{
		
		ProcessPO process = processService.saveProcess(templateId, type, processId, name, remarks);
		
		return new ProcessVO().set(process);
	}
	
	/**
	 * 修改流程数据<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午5:49:56
	 * @param @PathVariable Long id 流程id
	 * @param String processId 自定义流程id
	 * @param String name 流程名称
	 * @param String remarks 流程备注
	 * @return ProcessVO 流程数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/{id}")
	public Object edit(
			@PathVariable Long id,
			String name,
			String remarks,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		ProcessPO process = processDao.findById(id);
		
		if(process == null){
			throw new ProcessNotExistException(id);
		}
		
		process.setName(name);
		process.setRemarks(remarks);
		processDao.save(process);
		
		return new ProcessVO().set(process);
	}
	
	/**
	 * 删除流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月25日 上午8:44:26
	 * @param @PathVariable Long id 流程id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/{id}")
	public Object delete(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		ProcessPO process = processDao.findById(id);
		
		if(process != null){
			processService.delete(process);
		}
		
		return null;
	}
	
	/**
	 * 查询流程类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月26日 上午9:52:43
	 * @return Set<String> 类型名称列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/types")
	public Object queryTypes(HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		Set<String> processTypes = new HashSet<String>();
		ProcessType[] types = ProcessType.values();
		for(ProcessType type:types){
			if(!"0".equals(user.getGroupId()) && type.equals(ProcessType.TEMPLATE)) continue;
			processTypes.add(type.getName());
		}
		
		return processTypes;
	}
	
	/**
	 * 查询流程bpmn内容和可配置的接入点列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月26日 上午10:38:52
	 * @param Long id 流程id
	 * @return String bpmn 配置文件内容
	 * @return String userTaskBindVariables 用户任务绑定变量 
	 * @return String processId 流程主键
	 * @return String uuid 流程uuid
	 * @return List<GroupEntryVO> groupEntries 可配置接入点列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/bpmn/and/entries")
	public Object queryBpmnAndEntries(
			Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		ProcessPO process = processDao.findById(id);
		
		if(process == null){
			throw new ProcessNotExistException(id);
		}
		
		List<GroupEntryVO> groupEntries = new ArrayList<GroupEntryVO>();
		
		List<AccessPointPO> accessPoints = accessPointDao.findByScope(AccessPointScope.SYSTEMSCOPE);
		if(accessPoints!=null && accessPoints.size()>0){
			Set<Long> restIds = new HashSet<Long>();
			for(AccessPointPO accessPoint:accessPoints){
				if(ServiceType.REST.equals(accessPoint.getServiceType())){
					restIds.add(accessPoint.getServiceId());
				}
			}
			List<RestServicePO> restEntities = restServiceDao.findAllById(restIds);
			for(RestServicePO entry:restEntities){
				GroupEntryVO groupEntry = new GroupEntryVO().set(entry).setEntries(new ArrayList<EntryVO>());
				groupEntries.add(groupEntry);
				for(AccessPointPO accessPoint:accessPoints){
					if(ServiceType.REST.equals(accessPoint.getServiceType()) && 
							accessPoint.getServiceId().equals(entry.getId())){
						groupEntry.getEntries().add(new EntryVO().set(accessPoint, true));
					}
				}
			}
		}
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("bpmn", process.getBpmn())
																		 .put("userTaskBindVariables", process.getUserTaskBindVariables())
																		 .put("processId", process.getProcessId())
																		 .put("uuid", process.getUuid())
																		 .put("groupEntries", groupEntries)
																		 .getMap();
		
		return result;
	}
	
	/**
	 * 根据流程实例id查询流程配置文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月30日 上午10:14:36
	 * @param String processInstanceId 流程实例id
	 * @return String bpmn 流程配置文件
	 * @return List<String> completeTaskIds 已经执行完成的任务节点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/bpmn/by/process/instance/id")
	public Object queryBpmnByProcessInstanceId(
			String processInstanceId,
			HttpServletRequest request) throws Exception{
		HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
		String processDefinitionKey = processInstance.getProcessDefinitionKey();
		ProcessPO process = processDao.findByUuid(processDefinitionKey);
		List<String> completeTaskIds = new ArrayList<String>();
		if(activityInstances!=null && activityInstances.size()>0){
			for(HistoricActivityInstance activityInstance:activityInstances){
				completeTaskIds.add(activityInstance.getActivityId());
			}
		}
		return new HashMapWrapper<String, Object>().put("bpmn", process.getBpmn())
												   .put("definitionKey", process.getProcessId())
												   .put("definitionId", process.getUuid())
												   .put("completeTaskIds", completeTaskIds)
												   .getMap();
	}
	
	/**
	 * 保存流程bpmn配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月28日 上午10:14:55
	 * @param @PathVariable Long id 流程id
	 * @param String bpmn bpmn配置
	 * @param JSONArray accessPointIds 配置的接入点id数组
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/save/bpmn/{id}")
	public Object saveBpmn(
			@PathVariable Long id, 
			String bpmn,
			String userTaskBindVariables,
			String accessPointIds,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		ProcessPO process = processDao.findById(id);
		
		if(process == null){
			throw new ProcessNotExistException(id);
		}
		
		List<Long> accessPointIdList = JSON.parseArray(accessPointIds, Long.class);
		
		List<AccessPointPO> accessPoints = accessPointDao.findAllById(accessPointIdList);
		
		if(accessPointIdList.size() != (accessPoints==null?0:accessPoints.size())){
			if(accessPoints!=null && accessPoints.size()>0){
				for(Long accessPointId:accessPointIdList){
					boolean finded = false;
					for(AccessPointPO accessPoint:accessPoints){
						if(accessPoint.getId().equals(accessPointId)){
							finded = true;
							break;
						}
					}
					if(!finded){
						throw new AccessPointNotExistException(accessPointId);
					}
				}
			}
		}
		
		processService.saveBpmn(process, bpmn, userTaskBindVariables, accessPoints);
		
		return null;
	}
	
	/**
	 * 发布流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月7日 下午4:13:52
	 * @param @PathVariable Long id 流程id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/publish/{id}")
	public Object publish(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		ProcessPO process = processDao.findById(id);
		
		if(process == null){
			throw new ProcessNotExistException(id);
		}
		
		//判断权限
		boolean flag = false;
		if(flag){
			throw new UserHasNoPermissionForProcessActionException(user.getUuid(), id, "发布流程。");
		}
		
		ProcessPO entity = processService.publish(process);
		
		return new ProcessVO().set(entity);
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/my/task/preview")
	public Object queryMyTaskReview(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		return processQuery.queryMyTaskReview(currentPage, pageSize);
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/variables/by/task/definition/key")
	public Object queryVariablesByTaskDefinitionKey(
			String processInstanceId,
			String taskDefinitionKey,
			HttpServletRequest request) throws Exception{
		return processQuery.queryVariablesByTaskDefinitionKey(processInstanceId, taskDefinitionKey);
	}
	
	/**
	 * 用户任务提交<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月25日 下午4:36:18
	 * @param String taskId 任务id
	 * @param JSONArrayString variables 设置变量
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/do/review")
	public Object doReview(
			String taskId, 
			String variables,
			HttpServletRequest request) throws Exception{
		processService.doReview(taskId, variables);
		return null;
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/task/history")
	public Object queryTaskHistory(
			String processInstanceId, 
			String taskDefinitionKey,
			HttpServletRequest request) throws Exception{
		return processQuery.queryTaskHistory(processInstanceId, taskDefinitionKey);
	}
	
	/**
	 * 查询当前用户发起的流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月31日 下午3:34:07
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return total int 总数据量
	 * @return rows List<ProcessMyStartVO> 流程列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/my/start/process")
	public Object queryMyStartProcess(
			int currentPage, 
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		return processQuery.queryMyStartProcess(currentPage, pageSize);
	}
	
}
