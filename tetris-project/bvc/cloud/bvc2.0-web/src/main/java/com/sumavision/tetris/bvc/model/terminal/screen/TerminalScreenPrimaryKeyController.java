package com.sumavision.tetris.bvc.model.terminal.screen;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/terminal/screen/primary/key")
public class TerminalScreenPrimaryKeyController {

	@Autowired
	private TerminalScreenPrimaryKeyQuery terminalScreenPrimaryKeyQuery;
	
	@Autowired
	private TerminalScreenPrimaryKeyService terminalScreenPrimaryKeyService;
	
	/**
	 * 查询屏幕主键<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 下午3:06:40
	 * @return List<TerminalScreenPrimaryKeyVO> 屏幕主键
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(HttpServletRequest request) throws Exception{
		
		return terminalScreenPrimaryKeyQuery.load();
	}
	
	/**
	 * 自动添加屏幕主键screen_1到screen_16<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 下午3:15:20
	 * @return List<TerminalScreenPrimaryKeyVO> 主键
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/auto/add")
	public Object autoAdd(HttpServletRequest request) throws Exception{
		
		return terminalScreenPrimaryKeyService.autoAdd();
	}
	
	/**
	 * 添加屏幕主键<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 下午3:15:20
	 * @param String name 名称
	 * @param String primaryKey 主键
	 * @return TerminalScreenPrimaryKeyVO 主键
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			String primaryKey, 
			HttpServletRequest request) throws Exception{
		
		return terminalScreenPrimaryKeyService.add(name, primaryKey);
	}
	/**
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 下午3:15:20
	 * @param Long id 屏幕主键id
	 * @param String name 名称
	 * @param String primaryKey 主键
	 * @return TerminalScreenPrimaryKeyVO 主键
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String name,
			String primaryKey,
			HttpServletRequest request) throws Exception{
		
		return terminalScreenPrimaryKeyService.edit(id, name, primaryKey);
	}
	
	/**
	 * 删除屏幕主键<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 下午3:15:20
	 * @param Long id 屏幕主键id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id,
			HttpServletRequest request) throws Exception{
		
		terminalScreenPrimaryKeyService.delete(id);
		return null;
	}
	
}
