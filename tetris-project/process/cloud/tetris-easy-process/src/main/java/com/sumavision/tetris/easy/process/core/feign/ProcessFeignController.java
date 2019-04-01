package com.sumavision.tetris.easy.process.core.feign;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.tetris.easy.process.core.ProcessService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/process/feign")
public class ProcessFeignController {

	@Autowired
	private ProcessService processService;
	
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start/by/key")
	public Object startByKey(
			String primaryKey,
			String variables,
			HttpServletRequest request) throws Exception{
		
		return processService.startByKey(primaryKey, variables);
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
	public Object receiveTaskTrigger(
			String __processId__,
			Long __accessPointId__,
			String variables,
			HttpServletRequest request) throws Exception{
		
		processService.receiveTaskTrigger(__processId__, __accessPointId__, variables);
		
		return null;
	}
	
}
