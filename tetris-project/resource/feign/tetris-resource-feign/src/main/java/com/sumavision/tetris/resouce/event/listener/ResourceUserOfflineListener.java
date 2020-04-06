package com.sumavision.tetris.resouce.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.resouce.event.ResourceUserRegisteredFeign;
import com.sumavision.tetris.websocket.core.event.WebsocketSessionClosedEvent;

@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceUserOfflineListener implements ApplicationListener<WebsocketSessionClosedEvent>{

	@Autowired
	private ResourceUserRegisteredFeign resourceUserRegisteredFeign;
	
	@Override
	public void onApplicationEvent(WebsocketSessionClosedEvent event) {
		try {
			resourceUserRegisteredFeign.userOnline(event.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
