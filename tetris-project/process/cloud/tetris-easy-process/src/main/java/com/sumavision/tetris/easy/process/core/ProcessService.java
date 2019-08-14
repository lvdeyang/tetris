package com.sumavision.tetris.easy.process.core;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.json.AliFastJsonObject;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.easy.process.access.point.AccessPointPO;
import com.sumavision.tetris.easy.process.access.point.AccessPointParamDAO;
import com.sumavision.tetris.easy.process.access.point.AccessPointParamPO;
import com.sumavision.tetris.easy.process.access.point.AccessPointProcessPermissionDAO;
import com.sumavision.tetris.easy.process.access.point.AccessPointProcessPermissionPO;
import com.sumavision.tetris.easy.process.access.point.ParamDirection;
import com.sumavision.tetris.easy.process.core.exception.ProcessIdAlreadyExistException;
import com.sumavision.tetris.easy.process.core.exception.ProcessInUseException;
import com.sumavision.tetris.easy.process.core.exception.ProcessNotExistException;
import com.sumavision.tetris.easy.process.core.exception.VariableValueCheckFailedException;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.sdk.constraint.api.ConstraintValidator;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProcessService.class);

	@Autowired
	private AccessPointProcessPermissionDAO accessPointProcessPermissionDao;
	
	@Autowired
	private ProcessDAO processDao;
	
	@Autowired
	private ProcessCompanyPermissionDAO processCompanyPermissionDao;
	
	@Autowired
	private ProcessVariableDAO processVariableDao;
	
	@Autowired
	private ProcessParamReferenceDAO processParamReferenceDao;
	
	@Autowired
	private AccessPointParamDAO accessPointParamDao;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private Path path;
	
	@Autowired
	private AliFastJsonObject aliFastJsonObject;
	
	@Autowired
	private ConstraintValidator constraintValidator;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ProcessDeploymentPermissionDAO processDeploymentPermissionDao;
	
	@Autowired
	private ProcessInstanceDeploymentPermissionDAO processInstanceDeploymentPermissionDao;
	
	/**
	 * 添加流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月11日 上午10:29:42
	 * @param Long templateId 模板id
	 * @param String type 流程类型
	 * @param String processId 流程id
	 * @param String name 流程名称
	 * @param String remarks 备注
	 * @return ProcessPO 流程定义
	 */
	public ProcessPO saveProcess(
			Long templateId,
			String type,
			String processId,
			String name,
			String remarks) throws Exception{
		
		UserVO user = userQuery.current();
		
		ProcessPO template = null;
		if(templateId != null){
			template = processDao.findOne(templateId);
			if(template == null){
				throw new ProcessNotExistException(templateId);
			}
		}
		
		ProcessType processType = ProcessType.fromName(type);
		
		ProcessPO process = null;
		
		if(processType.equals(ProcessType.PUBLISH)){
			//去重校验
			process = processDao.findByProcessId(processId);
			if(process != null){
				throw new ProcessIdAlreadyExistException(processId);
			}
		}
		
		Date updateTime = new Date();
		
		process = new ProcessPO();
		process.setType(processType);
		process.setProcessId(processType.equals(ProcessType.TEMPLATE)?null:processId);
		process.setName(name);
		process.setRemarks(remarks);
		process.setPath(new StringBufferWrapper().append("tmp")
												 .append(File.separator)
												 .append("processes")
												 .append(File.separator)
												 .append(user.getUuid())
												 .append("-")
												 .append(processId)
												 .append("-")
												 .append(updateTime.getTime())
												 .append(".bpmn")
												 .toString());
		process.setUpdateTime(updateTime);
		
		if(template != null){
			String templateBpmn = template.getBpmn();
			if(templateBpmn != null){
				templateBpmn = templateBpmn.replaceAll(template.getUuid(), process.getUuid());
				process.setBpmn(templateBpmn);
			}
			String templateUserTaskBindVariables = template.getUserTaskBindVariables();
			if(templateUserTaskBindVariables != null){
				process.setUserTaskBindVariables(templateUserTaskBindVariables);
			}
		}
		
		processDao.save(process);
		
		ProcessCompanyPermissionPO permission = new ProcessCompanyPermissionPO();
		permission.setProcessId(process.getId());
		permission.setCompanyId(user.getGroupId());
		permission.setUpdateTime(updateTime);
		processCompanyPermissionDao.save(permission);
		
		if(template != null){
			//复制流程变量
			List<ProcessVariablePO> existVariables = processVariableDao.findByProcessId(templateId);
			if(existVariables!=null && existVariables.size()>0){
				List<ProcessVariablePO> copyVariables = new ArrayList<ProcessVariablePO>();
				for(ProcessVariablePO variable:existVariables){
					copyVariables.add(variable.copy(process.getId()));
				}
				processVariableDao.save(copyVariables);
			}
			
			//复制流程变量映射
			List<ProcessParamReferencePO> existParamReferences = processParamReferenceDao.findByProcessId(templateId);
			if(existParamReferences!=null && existParamReferences.size()>0){
				List<ProcessParamReferencePO> copyParamReferences = new ArrayList<ProcessParamReferencePO>();
				for(ProcessParamReferencePO paramReference:existParamReferences){
					copyParamReferences.add(paramReference.copy(process.getId()));
				}
				processParamReferenceDao.save(copyParamReferences);
			}
		}
		
		return process;
	}
	
	/**
	 * 保存流程的bpmn配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月28日 下午4:07:01
	 * @param ProcessPO process 流程
	 * @param String bpmn bpmn配置
	 * @param Collection<AccessPointPO> accessPoints 选中的接入点
	 * @return ProcessPO 流程数据
	 */
	public ProcessPO saveBpmn(
			ProcessPO process, 
			String bpmn,
			String userTaskBindVariables,
			Collection<AccessPointPO> accessPoints) throws Exception{
		
		List<AccessPointProcessPermissionPO> permissions = accessPointProcessPermissionDao.findByProcessId(process.getId());
		if(permissions!=null && permissions.size()>0){
			accessPointProcessPermissionDao.deleteInBatch(permissions);
		}
		
		permissions = new ArrayList<AccessPointProcessPermissionPO>();
		if(accessPoints!=null && accessPoints.size()>0){
			for(AccessPointPO accessPoint:accessPoints){
				AccessPointProcessPermissionPO permission = new AccessPointProcessPermissionPO();
				permission.setProcessId(process.getId());
				permission.setAccessPointId(accessPoint.getId());
				permission.setUpdateTime(new Date());
				permissions.add(permission);
			}
			accessPointProcessPermissionDao.save(permissions);
		}
		
		process.setBpmn(bpmn);
		process.setUserTaskBindVariables(userTaskBindVariables);
		process.setUpdateTime(new Date());
		processDao.save(process);
		
		return process;
	}
	
	/**
	 * 删除流程<br/>
	 * <p>
	 * 	删除流程与接入点关联信息<br/>
	 * 	删除流程下变量列表<br/>
	 *  删除流程<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月9日 下午3:10:58
	 * @param ProcessPO process 待删流程
	 */
	public void delete(ProcessPO process) throws Exception{
		
		//删除发布信息
		List<ProcessDeploymentPermissionPO> deployments = processDeploymentPermissionDao.findByProcessId(process.getId());
		if(deployments!=null && deployments.size()>0){
			Set<String> deploymentIds = new HashSet<String>();
			for(ProcessDeploymentPermissionPO deployment:deployments){
				deploymentIds.add(deployment.getDeploymentId());
			}
			List<ProcessInstanceDeploymentPermissionPO> permissions = processInstanceDeploymentPermissionDao.findByDeploymentIdIn(deploymentIds);
			if(permissions!=null && permissions.size()>0){
				throw new ProcessInUseException(process.getId(), "流程不能删除！");
			}
		}
		
		List<AccessPointProcessPermissionPO> permissions = accessPointProcessPermissionDao.findByProcessId(process.getId());
		if(permissions!=null && permissions.size()>0){
			accessPointProcessPermissionDao.deleteInBatch(permissions);
		}
		
		//删除流程变量
		List<ProcessVariablePO> variables = processVariableDao.findByProcessId(process.getId());
		if(variables!=null && variables.size()>0){
			processVariableDao.deleteInBatch(variables);
		}
		
		//删除流程变量映射
		List<ProcessParamReferencePO> processParamReferences = processParamReferenceDao.findByProcessId(process.getId());
		if(processParamReferences!=null && processParamReferences.size()>0){
			processParamReferenceDao.deleteInBatch(processParamReferences);
		}
		
		processDao.delete(process);
		
		//删除流程发布信息
		if(deployments!=null && deployments.size()>0){
			for(ProcessDeploymentPermissionPO deployment:deployments){
				repositoryService.deleteDeployment(deployment.getDeploymentId());
			}
			processDeploymentPermissionDao.deleteInBatch(deployments);
		}
	}
	
	/**
	 * 发布一个流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月25日 下午4:00:47
	 * @param ProcessPO process 流程
	 */
	public void publish(ProcessPO process) throws Exception{
		
		File tmpFolders = new File(new StringBufferWrapper().append(path.classPath())
														    .append(File.separator)
														    .append("tmp")
														    .append(File.separator)
														    .append("processes")
														    .toString());
		if(!tmpFolders.exists()) tmpFolders.mkdirs();
		
		File tmpFile = new File(new StringBufferWrapper().append(path.classPath())
													  .append(File.separator)
													  .append(process.getPath())
													  .toString());
		if(!tmpFile.exists()) tmpFile.createNewFile();
		
		InputStream inputStream = null;
		
		try{
			
			//写临时文件
			FileUtils.writeStringToFile(tmpFile, process.getBpmn(), "utf-8");
			
			inputStream = ReflectUtil.getResourceAsStream(process.getPath());
			
			//发布流程
			Deployment deployment = repositoryService.createDeployment().addInputStream(process.getPath(), inputStream).deploy();
			
			ProcessDeploymentPermissionPO permission = new ProcessDeploymentPermissionPO();
			permission.setUpdateTime(new Date());
			permission.setProcessId(process.getId());
			permission.setName(process.getName());
			permission.setRemarks(process.getRemarks());
			permission.setBpmn(process.getBpmn());
			permission.setDeploymentId(deployment.getId());
			processDeploymentPermissionDao.save(permission);
		}finally{
			if(inputStream != null) inputStream.close();
			
			//删除临时文件
			tmpFile.delete();
		}
	}
	
	/**
	 * 根据流程的主键启动流程<br/>
	 * <p>
	 * 	传来的变量分类：流程变量+接入点参数<br/>
	 * 	接口中对变量的处理：<br/>
	 * 		1.接入点参数关联关系处理<br/>
	 * 		2.接入点参数赋值约束校验<br/>
	 * 		3.处理流程变量中的引用值<br/>
	 * 		4.加入内置变量<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月9日 下午2:41:31
	 * @param String primaryKey 流程主键
	 * @param JSONString variables 流程必要变量初始值
	 * @param String category 流程主题
	 * @param String business 流程承载业务内容
	 * @return String processInstanceId 流程实例id
	 */
	public String startByKey(
			String primaryKey,
			String variables,
			String category,
			String business) throws Exception{
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		
		Map<String, String> headers = new HashMap<String, String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()){
			String headerName = headerNames.nextElement();
			headers.put(headerName, request.getHeader(headerName));
		}
		
		UserVO user = userQuery.current();
		
		ProcessPO process = processDao.findByProcessId(primaryKey);
		if(process == null){
			throw new ProcessNotExistException(primaryKey);
		}
		
		//构建上下文流程变量
		JSONObject contextVariables = variables!=null?JSON.parseObject(variables):new JSONObject();
		
		Map<String, Object> contextVariableMap = aliFastJsonObject.convertToHashMap(contextVariables);
		
		if(contextVariables.size() > 0){
			
			//处理接入点参数映射
			List<ProcessParamReferencePO> reference = processParamReferenceDao.findByProcessId(process.getId());
			if(reference!=null && reference.size()>0){
				Set<String> variableKeys = contextVariables.keySet();
				for(String key:variableKeys){
					for(ProcessParamReferencePO scope:reference){
						String scopeReference = scope.getReference();
						if(scopeReference==null || "".equals(scopeReference)) continue;
						if(scopeReference.indexOf(key) < 0) continue;
						String[] primaryKeyPaths = scopeReference.split(ProcessParamReferencePO.KEY_SEPARATOR);
						Object effectValue = null;
						//校验值的有效性
						for(String keyPath:primaryKeyPaths){
							Object setValue = contextVariableMap.get(keyPath);
							if(setValue != null){
								if(effectValue == null){
									effectValue = setValue;
								}else{
									if(!effectValue.equals(setValue)){
										throw new Exception("两个key存在映射但赋值不一样！");
									}
								}
							}
						}
						//设置值
						for(String keyPath:primaryKeyPaths){
							contextVariableMap.put(keyPath, effectValue);
						}
					}
				}
			}
			
			JSONObject validateContext = aliFastJsonObject.convertFromHashMap(contextVariableMap);
			
			//接入点参数有效性校验
			List<AccessPointParamPO> accessPointParams = accessPointParamDao.findByProcessId(process.getId());
			if(accessPointParams!=null && accessPointParams.size()>0){
				Set<String> mapKeys = contextVariableMap.keySet();
				for(String key:mapKeys){
					for(AccessPointParamPO param:accessPointParams){
						if(key.equals(param.getPrimaryKeyPath())){
							boolean result = constraintValidator.validate(validateContext, param.getConstraintExpression());
							if(!result){
								//校验未通过
								throw new VariableValueCheckFailedException(param.getPrimaryKeyPath(), param.getName(), contextVariableMap.get(key).toString(), param.getConstraintExpression());	
							}
							break;
						}
					}
				}
			}
			
		}
		
		JSONObject tmpParamContext = aliFastJsonObject.convertFromHashMap(contextVariableMap);
		
		//自定义变量
		List<ProcessVariablePO> entities = processVariableDao.findByProcessId(process.getId());
		if(entities!=null && entities.size()>0){
			for(ProcessVariablePO entity:entities){
				if(entity.getExpressionValue()!=null && !"".equals(entity.getExpressionValue())){
					//引用赋值优先--抛出异常就空着变量
					try{
						String referenceValue = constraintValidator.getStringValue(tmpParamContext, entity.getExpressionValue());
						contextVariableMap.put(entity.getPrimaryKey(), referenceValue);
					}catch(Exception e){
						LOG.info(new StringBufferWrapper().append("取消流程变量引用赋值，变量：")
												    	  .append(entity.getPrimaryKey())
												    	  .append("，表达式：")
												    	  .append(entity.getExpressionValue())
												    	  .toString());
					}
				}else{
					//设置默认值
					if(contextVariableMap.get(entity.getPrimaryKey()) == null) contextVariableMap.put(entity.getPrimaryKey(), entity.getDefaultValue());
				}
			}
		}
		
		JSONObject finalVariableContext = aliFastJsonObject.convertFromHashMap(contextVariableMap);
		
		//启动流程--并加入变量
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(process.getUuid(), new HashMapWrapper<String, Object>().put(InternalVariableKey.START_USER_ID.getVariableKey(), user.getUuid())
																																	      .put(InternalVariableKey.START_USER_NICKNAME.getVariableKey(), user.getNickname())
																																	      .put(InternalVariableKey.START_TIME.getVariableKey(), DateUtil.format(new Date(), DateUtil.dateTimePattern))
																																	      .put(InternalVariableKey.VARIABLE_CONTEXT.getVariableKey(), finalVariableContext)
																																	      .put(InternalVariableKey.REQUEST_HEADERS.getVariableKey(), headers)
																																	      .put(InternalVariableKey.CATEGORY.getVariableKey(), category)
																																	      .put(InternalVariableKey.BUSINESS.getVariableKey(), business)
																																	      .getMap());
		
		ProcessInstanceDeploymentPermissionPO permission = new ProcessInstanceDeploymentPermissionPO();
		permission.setProcessInstanceId(processInstance.getId());
		permission.setDeploymentId(processInstance.getDeploymentId());
		permission.setUpdateTime(new Date());
		processInstanceDeploymentPermissionDao.save(permission);
		
		//变量上下文中加入内置变量
		JSONObject existVariableContext = (JSONObject)runtimeService.getVariable(processInstance.getId(), InternalVariableKey.VARIABLE_CONTEXT.getVariableKey());
		if(!existVariableContext.containsKey(InternalVariableKey.PROCESS_INSTANCE_ID.getVariableKey())){
			existVariableContext.put(InternalVariableKey.PROCESS_INSTANCE_ID.getVariableKey(), processInstance.getId());
			runtimeService.setVariable(processInstance.getId(), InternalVariableKey.VARIABLE_CONTEXT.getVariableKey(), existVariableContext);
		}
		
		return processInstance.getId();
	}
	
	/**
	 * 异步服务节点回调<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 上午10:58:46
	 * @param String __processId__ 流程实例id
	 * @param String __accessPointId__ 回调接入点id
	 * @param JSONString variables 回传流程变量
	 */
	public void receiveTaskTrigger(
			String __processId__,
			Long __accessPointId__,
			String variables) throws Exception{
		
		Map<String, Object> processVariables = runtimeService.getVariables(__processId__);
		
		String processUuid = runtimeService.createProcessInstanceQuery().processInstanceId(__processId__).singleResult().getProcessDefinitionKey();
		
		JSONObject variableContext = (JSONObject)processVariables.get(InternalVariableKey.VARIABLE_CONTEXT.getVariableKey());
		
		Map<String, Object> variableMapContext = aliFastJsonObject.convertToHashMap(variableContext);
		
		if(variables != null){
			List<AccessPointParamPO> reverceParamDefinitions = accessPointParamDao.findByAccessPointIdInAndDirection(new ArrayListWrapper<Long>().add(__accessPointId__).getList(), ParamDirection.REVERSE);
			if(reverceParamDefinitions!=null && reverceParamDefinitions.size()>0){
				JSONObject variableJson = JSON.parseObject(variables);
				if(variableJson.size() > 0){
					Map<String, Object> variableMap = aliFastJsonObject.convertToHashMap(variableJson);
					Map<String, Object> reverceVariableMap = new HashMap<String, Object>();
					Set<String> variableMapKeys = variableMap.keySet();
					//key值转换
					for(String variableMapKey:variableMapKeys){
						for(AccessPointParamPO reverceParamdefinition:reverceParamDefinitions){
							if(variableMapKey.equals(reverceParamdefinition.getReferenceKeyPath())){
								reverceVariableMap.put(reverceParamdefinition.getPrimaryKeyPath(), variableMap.get(variableMapKey));
								break;
							}
						}
					}
					//处理映射
					Set<String> reverceVariableMapKeys = reverceVariableMap.keySet();
					ProcessPO process = processDao.findByUuid(processUuid);
					List<ProcessParamReferencePO> paramReferences = processParamReferenceDao.findByProcessId(process.getId());
					if(paramReferences!=null && paramReferences.size()>0){
						Map<String, Object> reverceReferenceVariableMap = new HashMap<String, Object>();
						for(String reverceVariableMapKey:reverceVariableMapKeys){
							for(ProcessParamReferencePO paramReference:paramReferences){
								String scopeReference = paramReference.getReference();
								if(scopeReference != null){
									if(scopeReference==null || "".equals(scopeReference)) continue;
									if(scopeReference.indexOf(reverceVariableMapKey) < 0) continue;
									String[] primaryKeyPaths = scopeReference.split(ProcessParamReferencePO.KEY_SEPARATOR);
									Object effectValue = null;
									//校验值的有效性
									for(String keyPath:primaryKeyPaths){
										Object setValue = reverceVariableMap.get(keyPath);
										if(setValue != null){
											if(effectValue == null){
												effectValue = setValue;
											}else{
												if(!effectValue.equals(setValue)){
													throw new Exception("两个key存在映射但赋值不一样！");
												}
											}
										}
									}
									//设置值
									for(String keyPath:primaryKeyPaths){
										reverceReferenceVariableMap.put(keyPath, effectValue);
									}
								}
							}
						}
						reverceVariableMap.putAll(reverceReferenceVariableMap);
					}
					//有效性校验
					JSONObject reverceVariableValidateContext = aliFastJsonObject.convertFromHashMap(reverceVariableMap);
					for(String reverceVariableMapKey:reverceVariableMapKeys){
						for(AccessPointParamPO reverceParamdefinition:reverceParamDefinitions){
							if(reverceVariableMapKey.equals(reverceParamdefinition.getPrimaryKeyPath())){
								boolean result = constraintValidator.validate(reverceVariableValidateContext, reverceParamdefinition.getConstraintExpression());
								if(!result){
									//校验未通过
									throw new VariableValueCheckFailedException(reverceParamdefinition.getPrimaryKeyPath(), reverceParamdefinition.getName(), reverceVariableMap.get(reverceVariableMapKey).toString(), reverceParamdefinition.getConstraintExpression());
								}
								break;
							}
						}
					}
					
					//回写流程变量
					for(String reverceVariableMapKey:reverceVariableMapKeys){
						variableMapContext.put(reverceVariableMapKey, reverceVariableMap.get(reverceVariableMapKey));
					}
					variableContext = aliFastJsonObject.convertFromHashMap(variableMapContext);
					runtimeService.setVariable(__processId__, InternalVariableKey.VARIABLE_CONTEXT.getVariableKey(), variableContext);
				}
			}
		}
		
		//流程往下走
		Execution execution = runtimeService.createExecutionQuery().processInstanceId(__processId__).onlyChildExecutions().singleResult();
		
		runtimeService.trigger(execution.getId());
		
	}
	
	/**
	 * 用户任务提交<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月25日 下午4:36:18
	 * @param String taskId 任务id
	 * @param JSONArrayString variables 设置变量
	 */
	@SuppressWarnings("unchecked")
	public void doReview(String taskId, String variables) throws Exception{
		
		UserVO user = userQuery.current();
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		Object variable = runtimeService.getVariable(task.getProcessInstanceId(), InternalVariableKey.NODE_HISTORY.getVariableKey());
		List<JSONObject> nodeHistorys = variable==null?new ArrayList<JSONObject>():(List<JSONObject>)variable;
		JSONObject nodeHistory = null;
		for(JSONObject existNodeHistory:nodeHistorys){
			if(existNodeHistory.getString("userId").equals(user.getId().toString()) && 
					existNodeHistory.getString("taskId").equals(task.getTaskDefinitionKey())){
				nodeHistory = existNodeHistory;
				break;
			}
		}
		if(nodeHistory == null){
			nodeHistory = new JSONObject();
		}
		nodeHistory.put("userId", user.getId());
		nodeHistory.put("userNickName", user.getNickname());
		nodeHistory.put("taskId", task.getTaskDefinitionKey());
		nodeHistory.put("type", InternalVariableKey.NODE_YTPE_USER.getVariableKey());
		
		if(variables != null){
			List<JSONObject> setVariables = JSON.parseArray(variables, JSONObject.class);
			nodeHistory.put("variableSet", setVariables);
			
			JSONObject variableContext = (JSONObject)runtimeService.getVariable(task.getProcessInstanceId(), InternalVariableKey.VARIABLE_CONTEXT.getVariableKey());
			for(JSONObject setVariable:setVariables){
				variableContext.put(setVariable.getString("key"), setVariable.getString("value"));
			}
			runtimeService.setVariable(task.getProcessInstanceId(), InternalVariableKey.VARIABLE_CONTEXT.getVariableKey(), variableContext);
		}
		
		nodeHistorys.add(nodeHistory);
		runtimeService.setVariable(task.getProcessInstanceId(), InternalVariableKey.NODE_HISTORY.getVariableKey(), nodeHistorys);
		
		taskService.complete(task.getId());
	}
	
}
