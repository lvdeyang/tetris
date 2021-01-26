package com.sumavision.tetris.easy.process.core;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-easy-process", configuration = FeignConfiguration.class)
public interface ProcessFeign {

	/**
	 * 根据id查询流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 上午9:36:26
	 * @param Long id 流程id
	 * @return ProcessVO 流程
	 */
	@RequestMapping(value = "/process/feign/find/by/id")
	public JSONObject findById(@RequestParam("id") Long id);
	
	/**
	 * 根据id查询流程bpmn配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 上午9:40:31
	 * @param Long id 流程id
	 * @return String 流程bpmn配置
	 */
	@RequestMapping(value = "/process/feign/find/bpmn/by/id")
	public JSONObject findBpmnById(@RequestParam("id") Long id);
	
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
	@RequestMapping(value = "/process/feign/list")
	public JSONObject list(
			@RequestParam("currentPage") int currentPage,
			@RequestParam("pageSize") int pageSize);
	
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
	@RequestMapping(value = "/process/feign/list/with/except")
	public JSONObject listWithExcept(
			@RequestParam("currentPage") int currentPage,
			@RequestParam("pageSize") int pageSize,
			@RequestParam("except") String except);
	
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
	@RequestMapping(value = "/process/feign/start/by/key")
	public JSONObject startByKey(
			@RequestParam("primaryKey") String primaryKey,
			@RequestParam("variables") String variables,
			@RequestParam("category") String category,
			@RequestParam("business") String business);
	
	/**
	 * 异步服务节点回调<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 上午10:58:46
	 * @param String __processId__ 流程实例id
	 * @param String __accessPointId__ 回调接入点id
	 * @param JSONString variables 回传流程变量
	 */
	@RequestMapping(value = "/process/feign/receive/task/trigger")
	public JSONObject receiveTaskTrigger(
			@RequestParam("__processId__") String __processId__,
			@RequestParam("__accessPointId__") Long __accessPointId__,
			@RequestParam("variables") String variables);
}
