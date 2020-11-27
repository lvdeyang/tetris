package com.sumavision.tetris.bvc.business.location;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value="/location/template/layout")
public class LocationTemplateLayoutController {
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private LocationTemplateLayoutService locationTemplateLayoutService;
	
	/**
	 * 添加屏幕墙<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 上午9:06:35
	 * @param templateName 模板名字
	 * @param screenNumberOfX x方向屏幕墙数量
	 * @param screenNumberOfY y方向屏幕墙数量
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/add")
	public Object add(
			String templateName,
			Integer screenNumberOfX,
			Integer screenNumberOfY,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
				
		return locationTemplateLayoutService.add(templateName, screenNumberOfX, screenNumberOfY, userId);
	}

	/**
	 * 删除屏幕墙模板<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 上午10:02:37
	 * @param id 屏幕墙模板id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/delete")
	public Object delete(
			Long id) throws Exception{
		
		locationTemplateLayoutService.delete(id);
		
		return null;
	}
	
	/**
	 * 修改屏幕墙模板<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 上午10:11:30
	 * @param id
	 * @param templateName
	 * @param screenNumberOfX
	 * @param screenNumberOfY
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/edit")
	public Object edit(
			Long id,
			String templateName,
			Integer screenNumberOfX,
			Integer screenNumberOfY) throws Exception{
		
		return locationTemplateLayoutService.edit(id, templateName, screenNumberOfX, screenNumberOfY);
		
	}
	
	/**
	 * 查询屏幕墙模板<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 上午10:40:26
	 * @return List<LocationTemplateLayoutVO>
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/query/all")
	public Object queryAll() throws Exception{
		
		return locationTemplateLayoutService.queryAll();
		
	}
	
	/**
	 * 查询屏幕墙模板<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 上午10:40:26
	 * @param templateName 屏幕墙模板名称
	 * @return List<LocationTemplateLayoutVO>
	 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value="/query")
//	public Object query(
//			String templateName,
//			HttpServletRequest request) throws Exception{
//		
//		Long userId = userUtils.getUserIdFromSession(request);
//		
//		return locationTemplateLayoutService.query(templateName, userId);
//		
//	}
}
