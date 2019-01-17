package com.sumavision.tetris.easy.process.demo.activiti.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

/**
 * 申请会议室流程示例
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年10月30日 下午5:15:19
 */
@Controller
@RequestMapping(value = "")
public class IndexController {

	@Autowired
	private ProcessEngine processEngine;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private HistoryService historyService;
	
	/**
	 * 模拟登陆页面<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月30日 下午5:16:13
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login")
	public ModelAndView login() throws Exception{
		ModelAndView mv = new ModelAndView("web/demo/activiti/login");
		return mv;
	}
	
	/**
	 * 用户登录后首页<br/>
	 * <p>
	 * 	根据不同用户加载不同数据内容，包括三个用户<br/>
	 * 		1.employee：员工<br/>
	 * 		2.manager：经理<br/>
	 * 		3.ceo：ceo<br/>	
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月30日 下午5:16:31
	 * @param username 登录用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/home")
	public ModelAndView home(String username) throws Exception{
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		TaskQuery taskQuery = taskService.createTaskQuery();
		
		//查询用户发起的任务
		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().variableValueEquals("startUserId", username)
																							.list();
		List<Map<String, String>> myApply = new ArrayList<Map<String, String>>();
		if(processInstances!=null && processInstances.size()>0){
			for(ProcessInstance processInstance:processInstances){
				
				
				Execution processInstanceExecution = runtimeService.createExecutionQuery()
																   .processInstanceId(processInstance.getId())
																   .onlyProcessInstanceExecutions()
																   .singleResult();
				Object isReject = runtimeService.getVariableLocal(processInstanceExecution.getId(), "isReject");
				if(isReject!=null && Boolean.parseBoolean(isReject.toString())){
					//被驳回的流程在待办事项中操作
					continue;
				}
				
				Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).onlyChildExecutions().singleResult();
				Map<String, String> myApplyInstance = new HashMap<String, String>();
				myApplyInstance.put("processId", processInstance.getId());
				myApplyInstance.put("processName", processInstance.getProcessDefinitionName());
				myApplyInstance.put("activityId", execution.getActivityId());
				HistoricActivityInstance node = historyService.createHistoricActivityInstanceQuery()
															  .activityId(execution.getActivityId())
															  .processInstanceId(processInstance.getId())
															  .singleResult();
				Task task = taskService.createTaskQuery()
									   .taskDefinitionKey(execution.getActivityId())
									   .executionId(execution.getId())
									   .singleResult();
				System.out.println(node.getActivityType());
				myApplyInstance.put("taskName", node.getActivityName());
				myApplyInstance.put("createTime", DateUtil.format(processInstance.getStartTime(), DateUtil.dateTimePattern));
				myApplyInstance.put("taskTime", task==null?"":DateUtil.format(task.getCreateTime(), DateUtil.dateTimePattern));
				myApply.add(myApplyInstance);
			}
		}
		
		model.put("myApply", myApply);
		
		//查询代办事项
		List<Task> tasks = taskQuery.taskAssignee(username).list();
		List<Map<String, String>> myTasks = new ArrayList<Map<String,String>>();
		if(tasks.size() > 0){
			for(Task task:tasks){
				Map<String, String> myTaskInstance = new HashMap<String, String>();
				myTaskInstance.put("taskId", task.getId());
				myTaskInstance.put("taskName", task.getName());
				myTaskInstance.put("taskTime", DateUtil.format(task.getCreateTime(), DateUtil.dateTimePattern));
				//获取流程定义
				ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
				myTaskInstance.put("processName", processDefinition.getName());
				//获取流程实例
				ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
				myTaskInstance.put("processId", processInstance.getId());
				Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId())
																		   .onlyProcessInstanceExecutions()
																		   .singleResult();
				Map<String, Object> variablesMap = runtimeService.getVariables(execution.getId());
				myTaskInstance.put("createTime", DateUtil.format(processInstance.getStartTime(), DateUtil.dateTimePattern));
				myTaskInstance.put("startUserId", variablesMap.get("startUserId").toString());
				myTaskInstance.put("time", variablesMap.get("time").toString());
				myTasks.add(myTaskInstance);
			}
		}
		
		model.put("myTasks", myTasks);
		
		//查询历史审批
		List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery().taskAssignee(username)
																 .processVariableValueNotEquals("startUserId", username)
																 .finished()
																 .orderByHistoricTaskInstanceStartTime()
																 .asc()
																 .list();
		List<Map<String, String>> historicApproval = new ArrayList<Map<String,String>>();
		if(historicTasks!=null && historicTasks.size()>0){
			for(HistoricTaskInstance historicTask:historicTasks){
				Map<String, String> historicTaskInfo = new HashMap<String, String>();
				HistoricProcessInstance historicInstance = historyService.createHistoricProcessInstanceQuery()
																		 .processInstanceId(historicTask.getProcessInstanceId())
																		 .singleResult();
				historicTaskInfo.put("processName", historicInstance.getProcessDefinitionName());
				historicTaskInfo.put("createTime", DateUtil.format(historicInstance.getStartTime(), DateUtil.dateTimePattern));
				historicTaskInfo.put("endTime", historicInstance.getEndTime()==null?"":DateUtil.format(historicInstance.getEndTime(), DateUtil.dateTimePattern));
				historicTaskInfo.put("taskName", historicTask.getName());
				historicTaskInfo.put("taskTime", DateUtil.format(historicTask.getStartTime(), DateUtil.dateTimePattern));
				List<Comment> comments = taskService.getTaskComments(historicTask.getId());
				String status = null;
				if(comments!=null && comments.size()>0){
					status = comments.get(0).getFullMessage();
				}else if(historicTask.getDeleteReason() != null){
					status = historicTask.getDeleteReason();
				}else {
					status = username + "同意";
				}
				historicTaskInfo.put("status", status);
				historicApproval.add(historicTaskInfo);
			}
		}
		
		model.put("historicApproval", historicApproval);
		
		//查询历史申请
		List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery()
																			   .variableValueEquals("startUserId", username)
																			   .finished()
																			   .orderByProcessInstanceStartTime()
																			   .asc()
																			   .list();
		List<Map<String, String>> historicApply = new ArrayList<Map<String,String>>();
		if(historicProcessInstances!=null && historicProcessInstances.size()>0){
			for(HistoricProcessInstance historicProcessInstance:historicProcessInstances){
				Map<String, String> historicProcessInfo = new HashMap<String, String>();
				historicProcessInfo.put("processName", historicProcessInstance.getProcessDefinitionName());
				historicProcessInfo.put("createTime", DateUtil.format(historicProcessInstance.getStartTime(), DateUtil.dateTimePattern));
				String status = null;
				if(historicProcessInstance.getEndActivityId() != null){
					status = "审批通过";
				}else{
					status = historicProcessInstance.getDeleteReason();
				}
				historicProcessInfo.put("status", status);
				historicProcessInfo.put("endTime", DateUtil.format(historicProcessInstance.getEndTime(), DateUtil.dateTimePattern));
				historicApply.add(historicProcessInfo);
			}
		}
		
		model.put("historicApply", historicApply);
		
		ModelAndView mv = new ModelAndView("web/demo/activiti/home");
		mv.addAllObjects(model);
		return mv;
	}
	
	/**
	 * 员工提交申请<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月30日 下午5:17:37
	 * @param username 登录用户
	 * @param time 会议室占用时间
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/apply")
	public ModelAndView apply(String username, float time) throws Exception{
		
		Map<String, Object> variables = new HashMapWrapper<String, Object>().put("startUserId", username)
																			.put("time", time)
																			.getMap();
		//开始流程
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("_80937dfc0bbc4bb2a1c95e6de6d7377f", variables);
		
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId())
												 .taskAssignee(username)
												 .singleResult();
		
		taskService.complete(task.getId());
		
		ModelAndView mv = new ModelAndView("redirect:/home");
		mv.addObject("username", username);
		return mv;
	}
	
	/**
	 * 通过审批<br/>
	 * <p>
	 * 	包含三种情况：
	 * 		1.员工再次提交驳回的流程<br/>
	 * 		2.经理通过审批执行经理通过后续服务处理<br/>
	 * 		3.ceo通过审批执行ceo通过后续服务处理<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月30日 下午5:20:36
	 * @param username 登录用户
	 * @param taskId 待操作的任务id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/agree")
	public ModelAndView agree(String username, String taskId) throws Exception{
		
		Map<String, Object> variables = new HashMapWrapper<String, Object>().put("isReject", false)
																			.getMap();
		taskService.complete(taskId, variables);
		
		ModelAndView mv = new ModelAndView("redirect:/home");
		mv.addObject("username", username);
		return mv;
	}
	
	/**
	 * 员工关闭比申请<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月30日 下午5:22:33
	 * @param username 登录用户
	 * @param processId 要关闭的流程实例id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cancel")
	public ModelAndView cancel(String username, String processId) throws Exception{
		
		runtimeService.deleteProcessInstance(processId, "发起者取消！");
		
		ModelAndView mv = new ModelAndView("redirect:/home");
		mv.addObject("username", username);
		return mv;
	}
	
	/**
	 * 驳回申请<br/>
	 * <p>
	 * 	包含两种操作：<br/>
	 * 		1.经理驳回<br/>
	 * 		2.ceo驳回<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月30日 下午5:23:20
	 * @param username 登录用户
	 * @param taskId 待执行的任务id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/reject")
	public ModelAndView reject(String username, String taskId) throws Exception{
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		taskService.addComment(taskId, task.getProcessInstanceId(), username + "驳回!");
		
		Map<String, Object> variables = new HashMapWrapper<String, Object>().put("isReject", true)
																			.getMap();
		taskService.complete(taskId, variables);
		
		ModelAndView mv = new ModelAndView("redirect:/home");
		mv.addObject("username", username);
		return mv;
	}
	
	/**
	 * 审批未通过<br/>
	 * <p>
	 * 	包含两种操作：<br/>
	 * 		1.经理不通过<br/>
	 * 		2.ceo不通过<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月30日 下午5:24:35
	 * @param username 登录用户
	 * @param taskId 待操作的任务id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/refuse")
	public ModelAndView refuse(String username, String taskId) throws Exception{
		
		Task task = taskService.createTaskQuery()
							   .taskId(taskId)
							   .singleResult();
		
		runtimeService.deleteProcessInstance(task.getProcessInstanceId(), username + "审批未通过！");
		
		ModelAndView mv = new ModelAndView("redirect:/home");
		mv.addObject("username", username);
		return mv;
	}
	
}
