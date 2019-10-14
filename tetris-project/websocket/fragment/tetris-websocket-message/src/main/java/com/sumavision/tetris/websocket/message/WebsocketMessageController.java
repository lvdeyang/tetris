package com.sumavision.tetris.websocket.message;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/message")
public class WebsocketMessageController {

	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 发送消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月11日 下午1:35:30
	 * @param Long userId 用户id
	 * @param String message 推送消息
	 * @param String type 消息类型
	 * @param Long fromUserId 消息来源用户id
	 * @param String fromUsername 消息来源用户名称
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/send")
	public Object send(
			Long userId,
			String message,
			String type,
			Long fromUserId,
			String fromUsername,
			HttpServletRequest request) throws Exception{
		if(fromUserId == null){
			UserVO user = userQuery.current();
			fromUserId = user.getId();
		}
		websocketMessageService.send(userId, message, WebsocketMessageType.valueOf(type), fromUserId, fromUsername);
		return null;
	}
	
}
