package com.sumavision.tetris.bvc.model.layout.forward;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/layout/forward")
public class LayoutForwardController {

	@Autowired
	private LayoutForwardQuery layoutForwardQuery;
	
	@Autowired
	private LayoutForwardService layoutForwardService;
	
	/**
	 * 根据虚拟源和物理屏幕查询转发配置信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月29日 下午3:27:36
	 * @param Long layoutId 虚拟源id
	 * @param Long terminalPhysicalScreenId 物理屏幕id
	 * @return List<LayoutForwardTreeNodeVO> 转发配置树
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/layout/id/and/terminal/physical/screen/id")
	public Object findByLayoutIdAndTerminalPhysicalScreenId(
			Long layoutId,
			Long terminalPhysicalScreenId,
			HttpServletRequest request) throws Exception{
		
		return layoutForwardQuery.findByLayoutIdAndTerminalPhysicalScreenId(layoutId, terminalPhysicalScreenId);
	}
	
	/**
	 * 查询解码<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月30日 下午1:20:39
	 * @param Long layoutId 虚拟源id
	 * @param Long terminalPhysicalScreenId 终端物理屏id
	 * @return mode String 模式：无模式，自选通道模式以及随机通道模式
	 * @return confirmChannels List<TerminalChannelVO> 可配置通道列表
	 * @return adaptableChannels List<TerminalChannelVO> adaptableChannels 随机通道列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/decodes")
	public Object loadDecodes(
			Long layoutId,
			Long terminalPhysicalScreenId,
			HttpServletRequest request) throws Exception{
		
		return layoutForwardQuery.loadDecodes(layoutId, terminalPhysicalScreenId);
	}
	
	/**
	 * 虚拟源终端显示设置中--添加解码操作<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月2日 下午1:41:26
	 * @param Long layoutId 虚拟源
	 * @param Long terminalId 终端id
	 * @param Long terminalPhysicalScreenId 终端物理屏id
	 * @param JSONArray decodeIds 解码通道id列表
	 * @return List<LayoutForwardTreeNodeVO> 转发配置树
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long layoutId,
			Long terminalId,
			Long terminalPhysicalScreenId,
			String decodeIds,
			HttpServletRequest request) throws Exception{
		
		return layoutForwardService.add(layoutId, terminalId, terminalPhysicalScreenId, decodeIds);
	}
	
	/**
	 * 修改源类型（转发，合屏）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午5:15:53
	 * @param Long layoutForwardId 转发配置id
	 * @param String treeNodeType 树节点类型
	 * @return LayoutForwardTreeNodeVO 源类型节点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/change/source/type")
	public Object changeSourceType(
			Long layoutForwardId,
			String treeNodeType,
			HttpServletRequest request) throws Exception{
		
		return layoutForwardService.changeSourceType(layoutForwardId, treeNodeType);
	}
	
	/**
	 * 修改是否启动解码布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午4:26:19
	 * @param Long layoutForwardId 转发配置id
	 * @param Boolean enablePosition 是否启动解码布局
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/change/enable/position")
	public Object changeEnablePosition(
			Long layoutForwardId,
			Boolean enablePosition,
			HttpServletRequest request) throws Exception{
		
		layoutForwardService.changeEnablePosition(layoutForwardId, enablePosition);
		return null;
	}
	
	/**
	 * 保存布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 上午10:17:10
	 * @param Long layoutForwardId 虚拟源布局设置id
	 * @param String x 左偏移
	 * @param String y 上偏移
	 * @param String width 宽度
	 * @param String height 高度
	 * @param String zIndex 涂层
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/save/position")
	public Object savePosition(
			Long layoutForwardId,
			String x,
			String y,
			String width,
			String height,
			String zIndex,
			HttpServletRequest request) throws Exception{
		
		layoutForwardService.savePosition(layoutForwardId, x, y, width, height, zIndex);
		return null;
	}
	
	/**
	 * 查询布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 上午10:30:04
	 * @param Long layoutForwardId 虚拟源布局设置id
	 * @return String x 左偏移
	 * @return String y 上偏移
	 * @return String width 宽度
	 * @return String height 高度
	 * @return String zIndex 涂层
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/position")
	public Object queryPosition(
			Long layoutForwardId,
			HttpServletRequest request) throws Exception{
		
		return layoutForwardQuery.queryPosition(layoutForwardId);
	}
	
	/**
	 * 保存合屏模板布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 下午5:04:19
	 * @param Long combineTemplatePositionId 合屏模板布局id
	 * @param String x 左偏移
	 * @param String y 上偏移
	 * @param String width 宽度
	 * @param String height 高度
	 * @param String zIndex 涂层
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/save/combine/template/position")
	public Object saveCombineTemplatePosition(
			Long combineTemplatePositionId,
			String x,
			String y,
			String width,
			String height,
			String zIndex,
			HttpServletRequest request) throws Exception{
		
		layoutForwardService.saveCombineTemplatePosition(combineTemplatePositionId, x, y, width, height, zIndex);
		return null;
	}
	
	/**
	 * 查询合屏模板布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 下午5:14:25
	 * @param Long combineTemplatePositionId 合屏模板布局id
	 * @return String x 左偏移
	 * @return String y 上偏移
	 * @return String width 宽度
	 * @return String height 高度
	 * @return String zIndex 涂层
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/combine/template/position")
	public Object queryCombineTemplatePosition(
			Long combineTemplatePositionId,
			HttpServletRequest request) throws Exception{
		
		return layoutForwardQuery.queryCombineTemplatePosition(combineTemplatePositionId);
	}
	
	/**
	 * 添加转发虚拟源布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 下午1:27:35
	 * @param Long layoutForwardId 虚拟源布局设置id
	 * @param Long layoutPositionSerialNum 虚拟源布局序号
	 * @return LayoutForwardTreeNodeVO 转发源节点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/forward/position")
	public Object addForwardPosition(
			Long layoutForwardId,
			Long layoutPositionSerialNum,
			HttpServletRequest request) throws Exception{
		
		return layoutForwardService.addForwardPosition(layoutForwardId, layoutPositionSerialNum);
	}
	
	/**
	 * 为和平模板添加布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 下午3:47:42
	 * @param  Long layoutForwardId 虚拟源布局设置id
	 * @param String layoutPositions 虚拟源布局列表（vo列表）
	 * @return List<LayoutForwardTreeNodeVO> 合屏模板布局节点列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/combine/position")
	public Object addCombinePosition(
			Long layoutForwardId,
			String layoutPositions,
			HttpServletRequest request) throws Exception{
		
		return layoutForwardService.addCombinePositions(layoutForwardId, layoutPositions);
	}
	
	/**
	 * 删除转发节点布局信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 下午4:25:10
	 * @param Long layoutForwardId 虚拟源布局设置id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/forward/position")
	public Object removeForwardPosition(
			Long layoutForwardId,
			HttpServletRequest request) throws Exception{
		
		layoutForwardService.removeForwardPosition(layoutForwardId);
		return null;
	}
	
	/**
	 * 删除合屏节点布局信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 下午4:30:06
	 * @param Long combineTemplatePositionId 合屏模板布局id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/combine/template/position")
	public Object removeCombineTemplatePosition(
			Long combineTemplatePositionId, 
			HttpServletRequest request) throws Exception{
		
		layoutForwardService.removeCombineTemplatePosition(combineTemplatePositionId);
		return null;
	}
	
	/**
	 * 删除虚拟源转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月8日 下午2:16:55
	 * @param Long layoutForwardId 虚拟源转发id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			Long layoutForwardId,
			HttpServletRequest request) throws Exception{
		
		layoutForwardService.remove(layoutForwardId);
		return null;
	}
	
}
