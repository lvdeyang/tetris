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
public class GuidePOController {

	@Autowired
	private BroadcastingTaskQuery broadcastingTaskQuery;
	
	@Autowired
	private BroadcastingTaskService broadcastingTaskService;
	
	/**
	 * 
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月3日 下午7:45:39
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
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月3日 下午8:23:43
	 * @param taskName
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
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月3日 下午8:23:53
	 * @param taskNumber
	 * @param taskName
	 * @param liveBroadcastDuration
	 * @param creationTime
	 * @param parameterNumber
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
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月3日 下午8:24:03
	 * @param taskNumber
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
}
