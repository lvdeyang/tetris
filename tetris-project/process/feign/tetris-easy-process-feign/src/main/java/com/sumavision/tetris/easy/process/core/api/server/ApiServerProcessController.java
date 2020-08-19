package com.sumavision.tetris.easy.process.core.api.server;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.tetris.easy.process.core.ProcessService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * 流程相关api<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月7日 下午4:14:32
 */
@Controller
@RequestMapping(value = "/api/server/process")
public class ApiServerProcessController {

	@Autowired
	private ProcessService processService;
	
	/**
	 * 根据流程的主键启动流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月9日 下午2:41:31
	 * @param String primaryKey 流程主键
	 * @param JSONString variables 流程必要变量初始值
	 * @param String category 流程主题
	 * @param String business 流程承载业务内容 
	 * @return String processInstanceId 流程实例id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start/by/key")
	public Object startByKey(
			String primaryKey,
			String variables,
			String category,
			String business,
			HttpServletRequest request) throws Exception{
		
		String processInstanceId = processService.startByKey(primaryKey, variables, category, business);
		
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
