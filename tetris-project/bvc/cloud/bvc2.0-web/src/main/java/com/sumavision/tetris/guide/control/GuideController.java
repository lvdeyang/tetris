/**
 * 
 */
package com.sumavision.tetris.guide.control;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月3日 下午1:49:14
 */

@Controller
@RequestMapping(value = "/tetris/guide/control/guide/po")
public class GuideController {

	@Autowired
	private GuideQuery broadcastingTaskQuery;
	
	@Autowired
	private GuideService broadcastingTaskService;
	
	/**
	 * 
	 * 查询所有导播任务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午3:42:30
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query")
	public Object queryBroadcastingTask(HttpServletRequest request) throws Exception{
		
		return broadcastingTaskQuery.queryTask();
	}
	
	/**
	 * 
	 * 添加导播任务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午3:43:00
	 * @param taskName 导播任务名称
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String taskName,
			HttpServletRequest request) throws Exception{
		return broadcastingTaskService.add(taskName);
	}
	
	/**
	 * 
	 * 修改导播任务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午3:43:39
	 * @param id 导播任务id
	 * @param taskName 导播任务名称
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String taskName,
			HttpServletRequest request) throws Exception{
		return broadcastingTaskService.edit(id, taskName);
	}
	
	/**
	 * 
	 * 删除导播任务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午3:44:24
	 * @param id 导播任务id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id,
			HttpServletRequest request) throws Exception{
		
		broadcastingTaskService.delete(id);
		return null;
	}
	
	/**
	 * 
	 * 开始直播<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午3:45:02
	 * @param id 导播任务id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start")
	public Object start(
			Long id, 
			HttpServletRequest request) throws Exception{
		return broadcastingTaskService.start(id);
	}
	
	/**
	 * 
	 * 停止直播<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午3:45:33
	 * @param id 导播任务id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop")
	public Object stop(
			Long id,
			HttpServletRequest request) throws Exception{
		return broadcastingTaskService.stop(id);
	}
}
