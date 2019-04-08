package com.sumavision.tetris.easy.process.core;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
	private UserQuery userQuery;
	
	@Autowired
	private ProcessDeploymentPermissionDAO processDeploymentPermissionDao;
	
	@Autowired
	private ProcessInstanceDeploymentPermissionDAO processInstanceDeploymentPermissionDao;
	
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
	 * @return String processInstanceId 流程实例id
	 */
	public String startByKey(
			String primaryKey,
			String variables) throws Exception{
		
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
		
		//内置变量
		//这个接口里内置变量没办法加入流程实例id
		//contextVariableMap.put(InternalVariableKey.START_USER_ID.getVariableKey(), user.getUuid());
		
		JSONObject finalVariableContext = aliFastJsonObject.convertFromHashMap(contextVariableMap);
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(process.getUuid(), new HashMapWrapper<String, Object>().put("variable-context", finalVariableContext).getMap());
		
		ProcessInstanceDeploymentPermissionPO permission = new ProcessInstanceDeploymentPermissionPO();
		permission.setProcessInstanceId(processInstance.getId());
		permission.setDeploymentId(processInstance.getDeploymentId());
		permission.setUpdateTime(new Date());
		processInstanceDeploymentPermissionDao.save(permission);
		
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
		
		JSONObject variableContext = (JSONObject)processVariables.get("variable-context");
		
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
					ProcessPO process = processDao.findByUuid(__processId__);
					List<ProcessParamReferencePO> paramReferences = processParamReferenceDao.findByProcessId(process.getId());
					if(paramReferences!=null && paramReferences.size()>0){
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
										reverceVariableMap.put(keyPath, effectValue);
									}
								}
							}
						}
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
					runtimeService.setVariable(__processId__, "variable-context", variableContext);
				}
			}
		}
		
		//流程往下走
		Execution execution = runtimeService.createExecutionQuery().processInstanceId(__processId__).onlyChildExecutions().singleResult();
		
		runtimeService.trigger(execution.getId());
		
	}
	
}
