package com.sumavision.tetris.bvc.model.terminal.layout;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/terminal/layout/position")
public class LayoutPositionController {

	@Autowired
	private LayoutPositionQuery layoutPositionQuery;
	
	@Autowired
	private LayoutPositionService layoutPositionService;
	
	/**
	 * 查询布局排版<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 下午1:30:44
	 * @param Long layoutId 布局id
	 * @return List<LayoutPositionVO> 排版列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long layoutId,
			HttpServletRequest request) throws Exception{
		
		return layoutPositionQuery.load(layoutId);
	}
	
	/**
	 * 添加排版<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 下午3:01:49
	 * @param Long layoutId 布局id
	 * @param String screenPrimaryKey 屏幕主键
	 * @param String x 横坐标
	 * @param String y 纵坐标
	 * @param String width 宽
	 * @param String height 高
	 * @param Integer zIndex 涂层顺序
	 * @return LayoutPositionVO 排版
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long layoutId,
			String screenPrimaryKey,
			String x,
			String y,
			String width,
			String height,
			Integer zIndex,
			HttpServletRequest request) throws Exception{
		
		return layoutPositionService.add(layoutId, screenPrimaryKey, x, y, width, height, zIndex);
	}
	
	/**
	 * 批量添加模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 下午3:30:48
	 * @param Long layoutId 布局id
	 * @param String screenPrimaryKeys 屏幕主键列表
	 * @return List<LayoutPositionVO> 排版列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/batch")
	public Object addBatch(
			Long layoutId,
			String screenPrimaryKeys,
			HttpServletRequest request) throws Exception{
		
		return layoutPositionService.addBatch(layoutId, screenPrimaryKeys);
	}
	
	/**
	 * 修改排版<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 下午3:01:49
	 * @param Long id 排版id
	 * @param String x 横坐标
	 * @param String y 纵坐标
	 * @param String width 宽
	 * @param String height 高
	 * @param Integer zIndex 涂层顺序
	 * @return LayoutPositionVO 排版
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String x,
			String y,
			String width,
			String height,
			Integer zIndex) throws Exception{
		
		return layoutPositionService.edit(id, x, y, width, height, zIndex);
	}
	
	/**
	 * 删除排版<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 下午3:08:18
	 * @param Long id 排版id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id,
			HttpServletRequest request) throws Exception{
		
		layoutPositionService.delete(id);
		return null;
	}
	
}
