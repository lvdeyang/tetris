package com.sumavision.tetris.bvc.model.terminal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/terminal/layout/permission")
public class TerminalLayoutPermissionController {

	@Autowired
	private TerminalLayoutPermissionQuery terminalLayoutPermissionQuery;
	
	@Autowired
	private TerminalLayoutPermissionService terminalLayoutPermissionService;
	
	/**
	 * 查询终端可添加的布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 上午10:27:47
	 * @param Long terminalId 终端id
	 * @return List<LayoutVO> 布局列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/layouts")
	public Object queryLayouts(
			Long terminalId,
			HttpServletRequest request) throws Exception{
		
		return terminalLayoutPermissionQuery.queryLayouts(terminalId);
	}
	
	/**
	 * 查询终端布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 上午10:24:48
	 * @param Long terminalId 终端id
	 * @return List<TerminalLayoutPermissionVO> 布局列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long terminalId, 
			HttpServletRequest request) throws Exception{
		
		return terminalLayoutPermissionQuery.load(terminalId);
	}
	
	/**
	 * 添加布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 上午10:59:02
	 * @param Long terminalId 终端id
	 * @param JSONArray layoutIds 布局id列表
	 * @return List<TerminalLayoutPermissionVO> 布局列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long terminalId,
			String layoutIds,
			HttpServletRequest request) throws Exception{
		
		return terminalLayoutPermissionService.add(terminalId, layoutIds);
	}
	
	/**
	 * 移除终端布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 上午11:11:24
	 * @param Long id 布局id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(Long id) throws Exception{
		terminalLayoutPermissionService.delete(id);
		return null;
	}
	
}
