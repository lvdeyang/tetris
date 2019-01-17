package com.sumavision.tetris.easy.process.api;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.tetris.easy.process.core.ProcessDAO;
import com.sumavision.tetris.easy.process.core.ProcessPO;
import com.sumavision.tetris.easy.process.core.ProcessVariableQuery;
import com.sumavision.tetris.easy.process.core.ProcessVariableVO;
import com.sumavision.tetris.easy.process.core.exception.ProcessNotExistException;
import com.sumavision.tetris.mims.app.user.UserQuery;
import com.sumavision.tetris.mims.app.user.UserVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/process/query")
public class ApiProcessQueryController {

	@Autowired
	private UserQuery userTool;
	
	@Autowired
	private ProcessDAO processDao;
	
	@Autowired
	private ProcessVariableQuery processVariableTool;
	
	/**
	 * 查询用户自定义变量列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月8日 下午5:30:44
	 * @param String primaryKey 流程主键 
	 * @return List<ProcessVariableVO> 变量列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/custom/variables")
	public Object customVariables(
			String primaryKey,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		//TODO 查询权限下的流程
		ProcessPO process = processDao.findByProcessId(primaryKey);
		
		if(process == null){
			throw new ProcessNotExistException(primaryKey);
		}
		
		List<ProcessVariableVO> variables = processVariableTool.findCustomVariable(process.getId());
		
		return variables;
	}
	
	/**
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月8日 下午5:32:01
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/require/variables")
	public Object requireVariables(
			String primaryKey,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		//TODO 查询权限下的流程
		ProcessPO process = processDao.findByProcessId(primaryKey);
		
		if(process == null){
			throw new ProcessNotExistException(primaryKey);
		}
		
		List<ProcessVariableVO> variables = new ArrayList<ProcessVariableVO>();
		
		//用户自定义变量
		List<ProcessVariableVO> customVariables = processVariableTool.findCustomVariable(process.getId());
		if(customVariables!=null && customVariables.size()>0){
			variables.addAll(customVariables);
		}
		
		//内置变量？？
		
		//接入点参数
		List<ProcessVariableVO> accessPointVariables = processVariableTool.findAccessPointVariable(process.getId());
		if(accessPointVariables!=null && accessPointVariables.size()>0){
			variables.addAll(accessPointVariables);
		}
		
		return variables;
	}
	
}
