package com.sumavision.tetris.bvc.model.terminal.physical.screen;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/terminal/physical/screen")
public class TerminalPhysicalScreenController {
	
	@Autowired
	private TerminalPhysicalScreenQuery terminalPhysicalScreenQuery;
	
	@Autowired
	private TerminalPhysicalScreenService terminalPhysicalScreenService;
	
	/**
	 * 根据终端查询物理屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月29日 下午1:23:33
	 * @param Long terminalId 终端id
	 * @return List<TerminalPhysicalScreenVO> 物理屏列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/terminal/id")
	public Object findByTerminalId(
			Long terminalId,
			HttpServletRequest request) throws Exception{
		
		return terminalPhysicalScreenQuery.findByTerminalId(terminalId);
	}
	
	/**
	 * 添加物理屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月29日 下午3:43:01
	 * @param Long terminalId 终端id
	 * @param String name 名称
	 * @return TerminalGraphNodeVO 拓补图物理屏幕节点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object addPhysicalScreen (
			Long terminalId,
			String name,
			HttpServletRequest request)throws Exception{
		
		return terminalPhysicalScreenService.add(terminalId, name);
	}
	
	/**
	 * 修改物理屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月29日 下午3:43:01
	 * @param Long id 物理屏幕id
	 * @param String name 名称
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object editPhysicalScreen(
			Long id,
			String name,
			HttpServletRequest request) throws Exception{
		
		terminalPhysicalScreenService.edit(id, name);
		return null;
	}
	
	/**
	 * 删除物理屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 下午4:22:15
	 * @param Long id 物理屏幕id
 	 * @param Boolean removeChildren 是否删除子节点<br/>
 	 * 子节点包括：
	 * 	1.物理屏幕视频解码通道关联
	 * 	2.视频解码通道
	 * 	3.视频解码与参数关联
	 * 	4.物理屏幕音频输出关联
	 * 	5.音频输出
	 * 	6.音频输出与音频解码通道关联
	 * 	7.音频解码通道
	 * 	8.音频解码通道与参数关联
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			Long id,
			Boolean removeChildren,
			HttpServletRequest request) throws Exception{
		
		terminalPhysicalScreenService.remove(id, removeChildren);
		return null;
	}
	
	/**
	 * 设置物理屏幕布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 上午9:59:11
	 * @param Long terminalPhysicalScreenId 物理屏幕id
	 * @param Integer column 列号
	 * @param Integer row 行号
	 * @param String x 左偏移
	 * @param String y 上偏移
	 * @param String width 宽
	 * @param String height 高
	 * @return List<TerminalPhysicalScreenVO> 布局改变的物理屏幕列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set/physical/screen/layout")
	public Object setPhysicalScreenLayout(
			Long terminalPhysicalScreenId,
			Integer column,
			Integer row,
			String x,
			String y,
			String width,
			String height,
			HttpServletRequest request) throws Exception{
		
		return terminalPhysicalScreenService.setPhysicalScreenLayout(terminalPhysicalScreenId, column, row, x, y, width, height);
	}
	
}
