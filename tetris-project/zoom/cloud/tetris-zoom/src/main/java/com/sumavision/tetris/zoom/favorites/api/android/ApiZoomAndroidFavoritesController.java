package com.sumavision.tetris.zoom.favorites.api.android;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.zoom.favorites.FavoritesService;

@Controller
@RequestMapping(value = "/api/zoom/android/favorites")
public class ApiZoomAndroidFavoritesController {

	@Autowired
	private FavoritesService favoritesService;
	
	/**
	 * 创建会议收藏夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 下午2:12:25
	 * @param String code 会议号码
	 * @param Long sourceGroupId 收藏夹分组
	 * @return FavoritesVO 收藏夹数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/create/zoom")
	public Object creatZoom(
			String code,
			Long sourceGroupId,
			HttpServletRequest request) throws Exception{
		
		return favoritesService.creatZoom(code, sourceGroupId);
	}
	
	/**
	 * 修改收藏夹名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 下午3:00:01
	 * @param Long id 收藏夹id
	 * @param String name 收藏夹名称
	 * @return FavoritesVO 收藏夹数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/name")
	public Object editName(
			Long id,
			String name,
			HttpServletRequest request) throws Exception{
		
		return favoritesService.editName(id, name);
	}
	
	/**
	 * 删除收藏夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 下午3:08:57
	 * @param Long id 收藏夹id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		favoritesService.remove(id);
		return null;
	}
	
}
