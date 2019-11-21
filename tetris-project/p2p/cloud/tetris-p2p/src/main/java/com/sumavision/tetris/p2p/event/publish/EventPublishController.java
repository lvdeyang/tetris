package com.sumavision.tetris.p2p.event.publish;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.websocket.core.event.WebsocketSessionClosedEvent;

@Controller
@RequestMapping(value = "/event/publish")
public class EventPublishController {
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	/**
	 * 用户websocket断开事件发布代理<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月23日 上午11:15:01
	 * @param Long userId 用户id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/websocket/closed")
	public Object websocketClosed(
			Long userId,
			HttpServletRequest request) throws Exception{
		
		WebsocketSessionClosedEvent event = new WebsocketSessionClosedEvent(applicationEventPublisher, userId);
		applicationEventPublisher.publishEvent(event);
		
		return null;
		
	}
	
}
