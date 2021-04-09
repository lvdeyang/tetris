package com.sumavision.tetris.bvc.model.layout;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/layout/position")
public class LayoutPositionController {

	@Autowired
	private LayoutPositionQuery layoutPositionQuery;
	
	@Autowired
	private LayoutPositionService layoutPositionService;
	
	/**
	 * 查询布局类型列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月30日 下午5:08:49
	 * @return List<Map<String, String>> 类型列表<br/>
	 * @return label String 名称
	 * @return value String key 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/types")
	public Object loadTypes(HttpServletRequest request) throws Exception{
	
		return layoutPositionQuery.loadTypes();
	}
	
	
	/**
	 * 查询虚拟源下的布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 上午9:42:10
	 * @param Long layoutId 虚拟源id
	 * @return List<LayoutPositionVO> 布局列表
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
	 * 添加布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 上午10:08:34
	 * @param Long layoutId 虚拟源id
	 * @param Integer number 布局个数
	 * @param Integer beginIndex 起始布局序号
	 * @return List<LayoutPositionVO> 布局列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long layoutId,
			Integer number,
			Integer beginIndex,
			HttpServletRequest request) throws Exception{
		
		return layoutPositionService.add(layoutId, number, beginIndex);
	}
	
	/**
	 * 修改虚拟源布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 下午2:06:56
	 * @param JSONString positions 修改后的布局列表
	 * @return List<LayoutPositionVO> 布局列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			String positions,
			HttpServletRequest request) throws Exception{
		
		return layoutPositionService.edit(positions);
	}
	
	/**
	 * 删除虚拟源布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 上午11:06:16
	 * @param Long id 虚拟源布局id
	 * @param List<LayoutPositionVO> 剩余布局列表（重新排序）
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			Long id,
			HttpServletRequest request) throws Exception{
		
		return layoutPositionService.remove(id);
	}
	
}
