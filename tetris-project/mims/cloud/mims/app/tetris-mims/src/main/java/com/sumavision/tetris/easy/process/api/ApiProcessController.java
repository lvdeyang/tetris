package com.sumavision.tetris.easy.process.api;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.constraint.api.ConstraintValidator;
import com.sumavision.tetris.easy.process.api.exception.VariableValueCheckFailedException;
import com.sumavision.tetris.easy.process.api.exception.VariableValueMissingException;
import com.sumavision.tetris.easy.process.core.ProcessDAO;
import com.sumavision.tetris.easy.process.core.ProcessPO;
import com.sumavision.tetris.easy.process.core.ProcessVariableQuery;
import com.sumavision.tetris.easy.process.core.ProcessVariableVO;
import com.sumavision.tetris.easy.process.core.exception.ProcessNotExistException;
import com.sumavision.tetris.easy.process.core.exception.UserHasNoPermissionForProcessActionException;
import com.sumavision.tetris.mims.app.user.UserQuery;
import com.sumavision.tetris.mims.app.user.UserVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;

/**
 * 流程相关api<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月7日 下午4:14:32
 */
@Controller
@RequestMapping(value = "/api/process")
public class ApiProcessController {

	@Autowired
	private UserQuery userTool;
	
	@Autowired
	private Path path;
	
	@Autowired
	private ProcessDAO processDao;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private ProcessVariableQuery processVariableTool;
	
	@Autowired
	private ConstraintValidator constraintValidator;
	
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
		
		UserVO user = userTool.current();
		
		ProcessPO process = processDao.findOne(id);
		
		if(process == null){
			throw new ProcessNotExistException(id);
		}
		
		//判断权限
		boolean flag = false;
		if(flag){
			throw new UserHasNoPermissionForProcessActionException(user.getUuid(), id, "发布流程。");
		}
		
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
			repositoryService.createDeployment().addInputStream(process.getPath(), inputStream).deploy();
			
		}finally{
			if(inputStream != null) inputStream.close();
			
			//删除临时文件
			tmpFile.delete();
		}
		
		return null;
	}
	
	/**
	 * 根据流程的主键启动流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月9日 下午2:41:31
	 * @param String primaryKey 流程主键
	 * @param JSONString variables 流程必要变量初始值
	 * @return String processInstanceId 流程实例id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start/by/key")
	public Object startByKey(
			String primaryKey,
			String variables,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		//判断权限
		
		ProcessPO process = processDao.findByProcessId(primaryKey);
		
		if(process == null){
			throw new ProcessNotExistException(primaryKey);
		}
		
		JSONObject requestVariables = JSON.parseObject(variables);
		
		Map<String, Object> requireVariables = new HashMap<String, Object>();
		
		//变量定义
		List<ProcessVariableVO> variableDefinitions = processVariableTool.findProcessTotalVariables(process.getId());
		
		//变量赋值
		for(ProcessVariableVO variableDefinition:variableDefinitions){
			if(!ProcessVariableOrigin.INTERNAL.equals(ProcessVariableOrigin.fromName(variableDefinition.getOrigin()))){
				String findedValue = null;
				if(requestVariables!=null && requestVariables.size()>0){
					findedValue = requestVariables.getString(variableDefinition.getPrimaryKey());
				}
				if(findedValue == null){
					if(variableDefinition.getDefaultValue() == null){
						throw new VariableValueMissingException(primaryKey, variableDefinition.getPrimaryKey(), variableDefinition.getName());
					}else{
						variableDefinition.setValue(variableDefinition.getDefaultValue());
					}
				}else{
					variableDefinition.setValue(findedValue);
				}
				//变量值有效性校验
				if(variableDefinition.getConstraintExpression() != null){
					boolean result = constraintValidator.validate(variableDefinition.getPrimaryKey(), variableDefinition.getValue(), variableDefinition.getConstraintExpression());
					if(!result){
						//校验未通过
						throw new VariableValueCheckFailedException(variableDefinition.getPrimaryKey(), variableDefinition.getName(), variableDefinition.getValue(), variableDefinition.getConstraintExpression());	
					}
				}
			}
		}
		
		//转换变量格式
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(process.getUuid());
		
		//设置变量
		for(ProcessVariableVO variableDefinition:variableDefinitions){
			if(variableDefinition.getPrimaryKey().equals(InternalVariableKey.PROCESSID.getVariableKey())){
				variableDefinition.setValue(processInstance.getId());
			}else if(variableDefinition.getPrimaryKey().equals(InternalVariableKey.STARTUSERID.getVariableKey())){
				variableDefinition.setValue(user.getUuid());
			}
			requireVariables.put(variableDefinition.getPrimaryKey(), variableDefinition.getValue());
		}
		
		requireVariables.put("variableDefinitions", variableDefinitions);
		
		runtimeService.setVariables(processInstance.getId(), requireVariables);
		
		return processInstance.getId();
	}
	
}
