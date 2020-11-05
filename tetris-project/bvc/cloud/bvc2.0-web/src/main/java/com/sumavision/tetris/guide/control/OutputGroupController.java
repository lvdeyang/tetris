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
 * <b>日期：</b>2020年9月27日 下午2:32:52
 */
@Controller
@RequestMapping(value = "/tetris/guide/control/output/group/po")
public class OutputGroupController {
	
	@Autowired
	private OutputGroupQuery outputGroupQuery;
	
	@Autowired
	private OutputGroupService outputGroupService;
	
	/**
	 * 
	 * 查询输出组<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午3:53:21
	 * @param guideId 导播任务id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query")
	public Object queryOutputGroup(Long guideId, HttpServletRequest request) throws Exception{
		
		return outputGroupQuery.query(guideId);
	
	}
	
	/**
	 * 
	 * 添加输出组<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午3:54:44
	 * @param name 输出组名称
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(String name, HttpServletRequest request) throws Exception{
		
		return outputGroupService.add(name);
	}
	
	/**
	 * 
	 * 修改输出组<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午3:55:45
	 * @param groups
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			String groups,
			HttpServletRequest request) throws Exception{
		return outputGroupService.edit(groups);
	}
	
	/**
	 * 
	 * 删除输出组<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午3:56:27
	 * @param id 输出组id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(Long id, HttpServletRequest request) throws Exception{
		outputGroupService.delete(id);
		return null;
	}
}
