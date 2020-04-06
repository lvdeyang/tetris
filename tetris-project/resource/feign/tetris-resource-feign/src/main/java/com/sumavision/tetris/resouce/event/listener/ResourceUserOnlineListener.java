package com.sumavision.tetris.resouce.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.resouce.event.ResourceUserRegisteredFeign;
import com.sumavision.tetris.websocket.core.event.WebsocketSessionOpenEvent;

@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceUserOnlineListener implements ApplicationListener<WebsocketSessionOpenEvent>{

	@Autowired
	private ResourceUserRegisteredFeign resourceUserRegisteredFeign;
	
	@Override
	public void onApplicationEvent(WebsocketSessionOpenEvent event) {
		try {
			resourceUserRegisteredFeign.userOffline(event.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
