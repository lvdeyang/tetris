package com.sumavision.tetris.mims.app.media.settings;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/media/settings")
public class MediaSettingsController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MediaSettingsDAO mediaSettingsDao;
	
	@Autowired
	private MediaSettingsService mediaSettingsService;
	
	/**
	 * 查询媒资仓库设置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月17日 下午2:56:41
	 * @return List<MediaSettingsVO> 设置列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		List<MediaSettingsPO> entities = mediaSettingsDao.findByCompanyId(Long.valueOf(user.getGroupId()));
		List<MediaSettingsVO> settings = MediaSettingsVO.getConverter(MediaSettingsVO.class).convert(entities, MediaSettingsVO.class);
		return settings;
	}
	
	/**
	 * 保存媒资仓库设置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月17日 下午3:43:25
	 * @param String type 设置类型
	 * @param String settings 设置内容
	 * @return MediaSettingsVO 仓库设置
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/save")
	public Object save(
			String type,
			String settings,
			HttpServletRequest request) throws Exception{
		MediaSettingsPO entity = mediaSettingsService.save(type, settings);
		return new MediaSettingsVO().set(entity);
	}
	
	/**
	 * 保存媒资仓库设置（带id名称）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月17日 下午3:43:25
	 * @param String type 设置类型
	 * @param Long id 目标id
	 * @param String name 目标名称
	 * @return MediaSettingsVO 仓库设置
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/save/with/id/and/name")
	public Object saveWithIdAndName(
			String type,
			Long id,
			String name,
			HttpServletRequest request) throws Exception{
		MediaSettingsPO entity = mediaSettingsService.save(type, new StringBufferWrapper().append(id).append("@@").append(name).toString());
		return new MediaSettingsVO().set(entity);
	}
	
	/**
	 * 清除仓库设置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月17日 下午3:52:40
	 * @param String type 设置类型
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			String type, 
			HttpServletRequest request) throws Exception{
		mediaSettingsService.delete(type);
		return null;
	}
	
}
