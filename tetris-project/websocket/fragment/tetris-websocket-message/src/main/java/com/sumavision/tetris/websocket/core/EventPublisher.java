package com.sumavision.tetris.websocket.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.websocket.core.event.WebsocketSessionClosedEvent;
import com.sumavision.tetris.websocket.core.event.WebsocketSessionOpenEvent;

@Service
public class EventPublisher {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	/**
	 * 发射websocket session关闭事件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月23日 下午4:11:52
	 * @param Long userId 下线的用户id
	 */
	public void publishWebsocketSessionClosedEvent(Long userId){
		WebsocketSessionClosedEvent event = new WebsocketSessionClosedEvent(applicationEventPublisher, userId);
    	applicationEventPublisher.publishEvent(event);
	}
	
	/**
	 * 发射websocket session开启事件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 下午1:51:55
	 * @param Long userId 上线的用户id
	 */
	public void publishWebsocketSessionOpenEvent(Long userId, String token){
		WebsocketSessionOpenEvent event = new WebsocketSessionOpenEvent(applicationEventPublisher, userId, token);
		applicationEventPublisher.publishEvent(event);
	}
	
}
