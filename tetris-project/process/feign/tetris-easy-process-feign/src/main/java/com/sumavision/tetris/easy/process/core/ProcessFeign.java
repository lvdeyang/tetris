package com.sumavision.tetris.easy.process.core;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-easy-process", configuration = FeignConfiguration.class)
public interface ProcessFeign {

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
	@RequestMapping(value = "/process/feign/start/by/key")
	public JSONObject startByKey(
			@RequestParam("primaryKey") String primaryKey,
			@RequestParam("variables") String variables);
	
	/**
	 * 异步服务节点回调<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 上午10:58:46
	 * @param String __processId__ 流程实例id
	 * @param String __accessPointId__ 回调接入点id
	 * @param JSONString variables 回传流程变量
	 */
	public JSONObject receiveTaskTrigger(
			@RequestParam("__processId__") String __processId__,
			@RequestParam("__accessPointId__") Long __accessPointId__,
			@RequestParam("variables") String variables);
}
