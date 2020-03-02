package com.sumavision.signal.bvc.director.controller.feign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.signal.bvc.director.bo.AddOutputBO;
import com.sumavision.signal.bvc.director.bo.AddTaskBO;
import com.sumavision.signal.bvc.director.bo.DeleteOutputBO;
import com.sumavision.signal.bvc.director.bo.DeleteTaskBO;
import com.sumavision.signal.bvc.director.service.DirectorTaskService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/director/feign")
public class DirectorFeignController {
	
	@Autowired
	private DirectorTaskService directorTaskService;

	/**
	 * 添加导播任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 下午1:48:02
	 * @param String tasks 添加任务协议
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/task")
	public Object addTask(
			String tasks,
			HttpServletRequest request) throws Exception{
		
		List<AddTaskBO> addTasks = JSONArray.parseArray(tasks, AddTaskBO.class);
		directorTaskService.addTasks(addTasks);
		
		return null;
	}
	
	/**
	 * 删除导播任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 下午1:53:49
	 * @param String tasks 删除导播任务协议
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/task")
	public Object deleteTask(
			String tasks,
			HttpServletRequest request) throws Exception{
		
		List<DeleteTaskBO> deleteTasks = JSONArray.parseArray(tasks, DeleteTaskBO.class);
		directorTaskService.deleteTasks(deleteTasks);
		
		return null;
	}
	
	/**
	 * 导播任务添加输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 下午2:06:41
	 * @param String outputs 导播任务添加输出协议
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/output")
	public Object addOutput(
			String outputs,
			HttpServletRequest request) throws Exception{
		
		List<AddOutputBO> addOutputs = JSONArray.parseArray(outputs, AddOutputBO.class);
		directorTaskService.addOutputs(addOutputs);
		
		return null;
	}
	
	/**
	 * 导播任务删除输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 下午2:09:49
	 * @param String outputs 导播任务删除输出协议
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/output")
	public Object deleteOutput(
			String outputs,
			HttpServletRequest request) throws Exception{
		
		List<DeleteOutputBO> deleteOutputs = JSONArray.parseArray(outputs, DeleteOutputBO.class);
		directorTaskService.deleteOutputs(deleteOutputs);
		
		return null;
	}
	
}
