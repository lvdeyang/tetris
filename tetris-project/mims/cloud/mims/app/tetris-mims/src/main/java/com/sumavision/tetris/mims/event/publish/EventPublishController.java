package com.sumavision.tetris.mims.event.publish;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.user.event.UserRegisteredEvent;

@Controller
@RequestMapping(value = "/event/publish")
public class EventPublishController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	/**
	 * 用户注册事件发布代理<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 下午4:19:47
	 * @param String userId 用户id
	 * @param String nickname 昵称
	 * @param String companyId 公司id
	 * @param String companyName 公司名称
	 * @param String roleId 管理员角色id
	 * @param String roleName 管理员角色名称
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/user/registered")
	public Object userRegistered(
			String userId,
			String nickname,
			String companyId,
			String companyName,
			String roleId,
			String roleName,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		System.out.println(nickname);
		UserRegisteredEvent event = new UserRegisteredEvent(applicationEventPublisher, userId, nickname, companyId, companyName, roleId, roleName);
		applicationEventPublisher.publishEvent(event);
		
		return null;
	}
	
}
