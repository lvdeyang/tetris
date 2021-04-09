package com.sumavision.tetris.bvc.model.layout;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/layout")
public class LayoutController {

	@Autowired
	private LayoutQuery layoutQuery;
	
	@Autowired
	private LayoutService layoutService;
	
	/**
	 * 分页查询虚拟源br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月26日 下午3:55:28
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return total long 总数据量
	 * @return rows List<LayoutVO> 虚拟源列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		return layoutQuery.load(currentPage, pageSize);
	}
	
	/**
	 * 查询虚拟源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午4:41:22
	 * @return List<LayoutVO> 虚拟源列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/all")
	public Object loadAll(HttpServletRequest request) throws Exception{
		
		return layoutQuery.loadAll();
	}
	
	/**
	 * 查询全部的虚拟源（带布局信息）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月29日 下午12:05:11
	 * @return List<LayoutVO> 布局列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/add/with/position")
	public Object loadAllWithPosition(HttpServletRequest request) throws Exception{
		
		return layoutQuery.loadAllWithPosition();
	}
	
	/**
	 * 添加虚拟源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月26日 下午4:12:29
	 * @param String name 虚拟源名称
	 * @return LayoutVO 虚拟源
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			HttpServletRequest request) throws Exception{
		
		return layoutService.add(name);
	}
	
	/**
	 * 修改虚拟源名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 上午9:02:49
	 * @param Long id 虚拟源id
	 * @param String name 虚拟源名称
	 * @return LayoutVO 虚拟源
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id, 
			String name, 
			HttpServletRequest request) throws Exception{
		
		return layoutService.edit(id, name);
	}
	
	/**
	 * 删除虚拟源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 上午10:13:17
	 * @param Long id 虚拟源id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		layoutService.remove(id);
		return null;
	}
	
}
