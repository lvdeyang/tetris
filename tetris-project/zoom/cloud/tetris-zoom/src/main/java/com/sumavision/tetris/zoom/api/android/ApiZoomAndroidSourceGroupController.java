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
	@RequestMapping(value = "/create/favorites/group")
	public Object createFavorites(
			String name,
			HttpServletRequest request) throws Exception{
		
		return sourceGroupService.createFavoritesGroup(name);
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
	@RequestMapping(value = "/remove/favorites/group")
	public Object removeFavorites(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		sourceGroupService.removeFavoritesGroup(id);
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
	
	/**
	 * 创建联系人分组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午10:49:39
	 * @param String name 分组名称
	 * @return SourceGroupVO 分组
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/create/contacts/group")
	public Object createContacts(
			String name,
			HttpServletRequest request) throws Exception{
		
		return sourceGroupService.createContactsGroup(name);
	}
	
	/**
	 * 删除联系人组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午11:01:41
	 * @param Long id 联系人组id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/contacts/group")
	public Object removeContacts(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		sourceGroupService.removeContactsGroup(id);
		return null;
	}
	
	/**
	 * 向联系人分组下添加联系人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午11:20:40
	 * @param Long id 联系人分组id
	 * @param JSONArray contactIds 联系人id列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/append/contacts")
	public Object appendContacts(
			Long id, 
			String contactIds,
			HttpServletRequest request) throws Exception{
		
		sourceGroupService.appendContacts(id, JSON.parseArray(contactIds, Long.class));
		return null;
	}
	
	/**
	 * 从联系人分组中移除联系人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午11:41:06
	 * @param JSONArray contactIds 联系人id列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/contacts")
	public Object removeContacts(
			String contactIds,
			HttpServletRequest request) throws Exception{
		
		sourceGroupService.removeContacts(JSON.parseArray(contactIds, Long.class));
		return null;
	}
	
	/**
	 * 联系人更换分组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午11:59:21
	 * @param Long id 目标联系人分组id
	 * @param JSONArray contactIds 联系人id列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/change/contacts")
	public Object changeContacts(
			Long id, 
			String contactIds,
			HttpServletRequest request) throws Exception{
		
		sourceGroupService.changeContacts(id, JSON.parseArray(contactIds, Long.class));
		return null;
	}
	
}
