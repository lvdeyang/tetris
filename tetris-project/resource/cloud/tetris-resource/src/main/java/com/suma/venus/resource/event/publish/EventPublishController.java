package com.suma.venus.resource.event.publish;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.user.event.TouristCreateEvent;
import com.sumavision.tetris.user.event.TouristDeleteBatchEvent;
import com.sumavision.tetris.user.event.TouristDeleteEvent;
import com.sumavision.tetris.user.event.UserDeletedEvent;
import com.sumavision.tetris.user.event.UserImportEvent;
import com.sumavision.tetris.user.event.UserRegisteredEvent;
import com.sumavision.tetris.websocket.core.event.WebsocketSessionClosedEvent;
import com.sumavision.tetris.websocket.core.event.WebsocketSessionOpenEvent;

@Controller
@RequestMapping(value = "/event/publish")
public class EventPublishController {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	/**
	 * 用户注册事件发布代理<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月6日 下午4:19:47
	 * @param String userId 用户id
	 * @param String nickname 昵称
	 * @param String userno 用户号码
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/user/registered")
	public Object userRegistered(
			String userId,
			String nickname,
			String userno,
			String worknodeUid,
			HttpServletRequest request) throws Exception{
		UserRegisteredEvent event = new UserRegisteredEvent(applicationEventPublisher, userId, nickname, userno,worknodeUid);
		applicationEventPublisher.publishEvent(event);
		return null;
	}
	
	/**
	 * 用户导入事件发布代理<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月18日 下午2:26:05
	 * @param String userId 用户id
	 * @param String nickname 昵称
	 * @param String userno 用户号码
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/user/import")
	public Object userImport(
			String userId,
			String nickname,
			String userno,
			String roleIds,
			HttpServletRequest request) throws Exception{
		
		List<String> _roleIds = JSONArray.parseArray(roleIds, String.class);
		UserImportEvent event = new UserImportEvent(applicationEventPublisher, userId, nickname, userno, null, null, _roleIds);
		applicationEventPublisher.publishEvent(event);
		return null;
	}
	
	/**
	 * 游客创建事件发布代理<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月18日 下午2:27:39
	 * @param String userId 用户id
	 * @param String nickname 昵称
	 * @param String userno 用户号码
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/tourist/create")
	public Object touristCreate(
			String userId,
			String nickname,
			String userno,
			HttpServletRequest request) throws Exception{
		TouristCreateEvent event = new TouristCreateEvent(applicationEventPublisher, userId, nickname, userno);
		applicationEventPublisher.publishEvent(event);
		return null;
	}
	
	/**
	 * 游客删除事件发布代理<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月18日 下午2:30:41
	 * @param String userId 游客id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/tourist/delete")
	public Object touristDelete(
			String userId,
			HttpServletRequest request) throws Exception{
		TouristDeleteEvent event = new TouristDeleteEvent(applicationEventPublisher, userId, null, null);
		applicationEventPublisher.publishEvent(event);
		return null;
	}
	
	/**
	 * 游客批量删除事件发布代理<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月18日 下午2:31:44
	 * @param String userIds 批量用户id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/tourist/delete/batch")
	public Object touristDeleteBatch(
			String userIds,
			HttpServletRequest request) throws Exception{
		List<Long> userIdList = JSONArray.parseArray(userIds, Long.class);
		TouristDeleteBatchEvent event = new TouristDeleteBatchEvent(applicationEventPublisher, userIdList);
		applicationEventPublisher.publishEvent(event);
		return null;
	}
	
	/**
	 * 用户离线事件代理<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月31日 下午3:28:31
	 * @param Long userId 用户id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/user/offline")
	public Object userOffline(
			Long userId,
			HttpServletRequest request) throws Exception{
		
		WebsocketSessionClosedEvent event = new WebsocketSessionClosedEvent(applicationEventPublisher, userId);
		applicationEventPublisher.publishEvent(event);
		
		return null;
	}
	
	/**
	 * 用户上线事件代理<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月31日 下午3:28:31
	 * @param Long userId 用户id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/user/online")
	public Object userOnline(
			Long userId,
			HttpServletRequest request) throws Exception{
		
		WebsocketSessionOpenEvent event = new WebsocketSessionOpenEvent(applicationEventPublisher, userId, null);
		applicationEventPublisher.publishEvent(event);
		
		return null;
	}
	
	/**
	 * 用户删除事件代理<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月10日 上午11:37:07
	 * @param String users 用户信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/user/delete")
	public Object userDelete(
			String users,
			HttpServletRequest request) throws Exception{
		
		List<UserVO> userList = JSONArray.parseArray(users, UserVO.class);
		UserDeletedEvent event = new UserDeletedEvent(applicationEventPublisher, userList);
		applicationEventPublisher.publishEvent(event);
		
		return null;
	}
	
}
