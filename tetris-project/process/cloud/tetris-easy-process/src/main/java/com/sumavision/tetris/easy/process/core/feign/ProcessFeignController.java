package com.sumavision.tetris.easy.process.core.feign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.easy.process.core.ProcessQuery;
import com.sumavision.tetris.easy.process.core.ProcessService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/process/feign")
public class ProcessFeignController {

	@Autowired
	private ProcessService processService;
	
	@Autowired
	private ProcessQuery processQuery;
	
	/**
	 * 根据id查询流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 上午9:36:26
	 * @param Long id 流程id
	 * @return ProcessVO 流程
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/id")
	public Object findById(
			Long id, 
			HttpServletRequest request) throws Exception{
		return processQuery.findById(id);
	}
	
	/**
	 * 根据id查询流程bpmn配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 上午9:40:31
	 * @param Long id 流程id
	 * @return String 流程bpmn配置
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/bpmn/by/id")
	public Object findBpmnById(
			Long id, 
			HttpServletRequest request) throws Exception{
		return processQuery.findBpmnById(id);
	}
	
	/**
	 * 分页查询流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午5:31:49
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 总数据量
	 * @return List<ProcessVO> rows 流程列表
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start/by/key")
	public Object startByKey(
			String primaryKey,
			String variables,
			String category,
			String business,
			HttpServletRequest request) throws Exception{
		
		return processService.startByKey(primaryKey, variables, category, business);
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
