package com.sumavision.tetris.bvc.model.terminal.screen;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/terminal/screen")
public class TerminalScreenController {

	@Autowired
	private TerminalScreenQuery terminalScreenQuery;
	
	@Autowired
	private TerminalScreenService terminalScreenService;
	
	/**
	 * 查询终端下的屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月22日 下午4:46:06
	 * @param Long terminalId 终端id
	 * @return List<TerminalScreenVO> 屏幕列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long terminalId, 
			HttpServletRequest request) throws Exception{
		
		return terminalScreenQuery.load(terminalId);
	}
	
	/**
	 * 添加终端屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月23日 下午3:00:53
	 * @param Long terminalId 终端id
	 * @param JSONArray screenParams 屏幕参数[{name:"",screenPrimaryKey:"",terminalChannelId:""}]
	 * @return List<TerminalScreenVO> 屏幕列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long terminalId, 
			String screenParams, 
			HttpServletRequest request) throws Exception{
		
		return terminalScreenService.add(terminalId, screenParams);
	}
	
	/**
	 * 修改名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月23日 下午4:07:49
	 * @param Long id 屏幕id
	 * @param String name 名称
	 * @return TerminalScreenVO 屏幕
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/name")
	public Object editName(
			Long id,
			String name,
			HttpServletRequest request) throws Exception{
		
		return terminalScreenService.editName(id, name);
	}
	
	/**
	 * 修改屏幕和通道绑定关系<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月23日 下午4:11:26
	 * @param Long id 屏幕id
	 * @param Long terminalChannelId 通道id
	 * @return TerminalScreenVO 屏幕
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/channel")
	public Object editChannel(
			Long id,
			Long terminalChannelId,
			HttpServletRequest request) throws Exception{
		
		return terminalScreenService.editChannel(id, terminalChannelId);
	}
	
	/**
	 * 删除屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月22日 下午5:13:16
	 * @param Long id 屏幕id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		terminalScreenService.delete(id);
		return null;
	}
	

}
