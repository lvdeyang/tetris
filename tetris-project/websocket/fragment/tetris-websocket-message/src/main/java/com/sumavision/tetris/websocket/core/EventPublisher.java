package com.sumavision.tetris.websocket.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.websocket.core.event.WebsocketSessionClosedEvent;

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
	
}
