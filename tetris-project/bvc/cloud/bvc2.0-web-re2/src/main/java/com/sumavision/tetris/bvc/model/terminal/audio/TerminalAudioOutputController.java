package com.sumavision.tetris.bvc.model.terminal.audio;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/terminal/audio/output")
public class TerminalAudioOutputController {

	@Autowired
	private TerminalAudioOutputQuery terminalAudioOutputQuery;
	
	@Autowired
	private TerminalAudioOutputService terminalAudioOutputService;
	
	/**
	 * 根据终端查询音频输出<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月15日 上午10:52:50
	 * @param Long terminalId 终端id
	 * @return List<TerminalAudioOutputVO> 音频输出列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long terminalId,
			HttpServletRequest request) throws Exception{
	
		return terminalAudioOutputQuery.load(terminalId);
	}
	
	/**
	 * 添加音频输出<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月15日 下午12:32:52
	 * @param Long terminalId 终端id
	 * @param String name 名称
	 * @param Long terminalPhysicalScreenId 关联终端物理屏幕id
	 * @return TerminalGraphNodeVO 终端拓补图
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long terminalId,
			String name,
			Long terminalPhysicalScreenId,
			HttpServletRequest request) throws Exception{
		
		return terminalAudioOutputService.add(terminalId, name, terminalPhysicalScreenId);
	}
	
	/**
	 * 修改音频输出名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 下午6:58:54
	 * @param Long id 音频输出id
	 * @param String name 名称
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String name) throws Exception{
		
		terminalAudioOutputService.edit(id, name);
		return null;
	}
	
	/**
	 * 修改音频输出关联的物理屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 下午8:01:58
	 * @param Long id 音频输出id
	 * @param Long physicalScreenId 物理屏幕id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/move")
	public Object move(
			Long id,
			Long physicalScreenId,
			HttpServletRequest request) throws Exception{
		
		terminalAudioOutputService.move(id, physicalScreenId);
		return null;
	}
	
	/**
	 * 删除音频输出<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 下午7:03:25
	 * @param Long id 音频输出id
	 * @param Boolean removeChildren 是否删除子节点
	 *  1.音频输出
	 *  2.音频输出屏幕关联
	 * 	2.音频输出与音频解码通道关联
	 * 子节点包括：
	 * 	3.音频解码通道
	 * 	4.音频解码通道与参数关联
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			Long id,
			Boolean removeChildren,
			HttpServletRequest request) throws Exception{
		
		terminalAudioOutputService.remove(id, removeChildren);
		return null;
	}
	
}
