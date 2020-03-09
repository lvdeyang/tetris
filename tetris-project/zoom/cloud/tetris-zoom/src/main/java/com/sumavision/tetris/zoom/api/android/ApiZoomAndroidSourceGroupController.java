package com.sumavision.tetris.zoom.api.android;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.zoom.SourceGroupService;

@Controller
@RequestMapping(value = "/api/zoom/android/source/group")
public class ApiZoomAndroidSourceGroupController {

	@Autowired
	private SourceGroupService sourceGroupService;
	
	/**
	 * 修改资源分组名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午11:47:38
	 * @param Long id 收藏夹分组id
	 * @param String name 收藏夹分组名称
	 * @return SourceGroupVO 收藏夹分组数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/name")
	public Object editName(
			Long id, 
			String name,
			HttpServletRequest request) throws Exception{
		
		return sourceGroupService.editName(id, name);
	}
	
	/**
	 * 创建收藏夹分组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午10:49:39
	 * @param String name 分组名称
	 * @return SourceGroupVO 分组
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/create/favorites")
	public Object createFavorites(
			String name,
			HttpServletRequest request) throws Exception{
		
		return sourceGroupService.createFavorites(name);
	}
	
	
	
	/**
	 * 删除收藏夹组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午11:01:41
	 * @param Long id 收藏夹组id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/favorites")
	public Object removeFavorites(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		sourceGroupService.removeFavorites(id);
		return null;
	}
	
	/**
	 * 向收藏夹分组下添加收藏夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午11:20:40
	 * @param Long id 收藏夹分组id
	 * @param JSONString favoritesIds 收藏夹id列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/append/favorites")
	public Object appendFavorites(
			Long id, 
			String favoritesIds,
			HttpServletRequest request) throws Exception{
		
		sourceGroupService.appendFavorites(id, JSON.parseArray(favoritesIds, Long.class));
		return null;
	}
	
	/**
	 * 从收藏夹分组中移除收藏夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午11:41:06
	 * @param JSONString favoritesIds 收藏夹id列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/favorites")
	public Object removeFavorites(
			String favoritesIds,
			HttpServletRequest request) throws Exception{
		
		sourceGroupService.removeFavorites(JSON.parseArray(favoritesIds, Long.class));
		return null;
	}
	
	/**
	 * 收藏夹更换分组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午11:59:21
	 * @param Long id 目标收藏夹分组id
	 * @param Collection<Long> favoritesIds 收藏夹id列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/change/favorites")
	public Object changeFavorites(
			Long id, 
			String favoritesIds,
			HttpServletRequest request) throws Exception{
		
		sourceGroupService.changeFavorites(id, JSON.parseArray(favoritesIds, Long.class));
		return null;
	}
	
}
