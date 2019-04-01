package com.sumavision.tetris.easy.process.api;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.tetris.easy.process.core.ProcessDAO;
import com.sumavision.tetris.easy.process.core.ProcessPO;
import com.sumavision.tetris.easy.process.core.ProcessService;
import com.sumavision.tetris.easy.process.core.exception.ProcessNotExistException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 流程相关api<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月7日 下午4:14:32
 */
@Controller
@RequestMapping(value = "/api/service/process")
public class ApiServiceProcessController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private ProcessDAO processDao;
	
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
		
		UserVO user = userQuery.current();
		
		ProcessPO process = processDao.findByProcessId(primaryKey);
		if(process == null){
			throw new ProcessNotExistException(primaryKey);
		}
		
		String processInstanceId = processService.startByKey(process, variables, user);
		
		return processInstanceId;
	}
	
	/**
	 * 异步接入点回调<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 上午11:47:47
	 * @param String __processId__ 流程实例id
	 * @param Long __accessPointId__ 回调接入点id
	 * @param JSONString variables 回写流程变量
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/receive/task/trigger")
	public Object receiveTaskTrigger(
			String __processId__,
			Long __accessPointId__,
			String variables,
			HttpServletRequest request) throws Exception{
		
		processService.receiveTaskTrigger(__processId__, __accessPointId__, variables);
		
		return null;
	}
	
}
