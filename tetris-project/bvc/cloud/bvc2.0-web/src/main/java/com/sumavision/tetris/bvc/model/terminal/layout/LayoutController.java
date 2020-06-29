package com.sumavision.tetris.bvc.model.terminal.layout;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/terminal/layout")
public class LayoutController {

	@Autowired
	private LayoutQuery layoutQuery;
	
	@Autowired
	private LayoutService layoutService;
	
	/**
	 * 分页查询布局模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 上午10:57:26
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return long total 总数据量
	 * @return List<LayoutVO> rows 布局列表
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
	 * 添加布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 上午11:06:32
	 * @param String name 布局名称
	 * @return LayoutVO 布局
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
	 * 修改布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 上午11:06:32
	 * @param Long id 布局id
	 * @param String name 布局名称
	 * @return LayoutVO 布局
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
	 * 删除布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 上午11:08:56
	 * @param Long id 布局id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(Long id) throws Exception{
		
		layoutService.delete(id);
		return null;
	}
	
}
