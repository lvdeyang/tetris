package com.sumavision.tetris.user.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.auth.token.TokenService;
import com.sumavision.tetris.websocket.core.event.WebsocketSessionOpenEvent;

@Service
@Transactional(rollbackFor = Exception.class)
public class WebsocketSessionOpenListener implements ApplicationListener<WebsocketSessionOpenEvent>{

	@Autowired
	private TokenService tokenService;
	
	@Override
	public void onApplicationEvent(WebsocketSessionOpenEvent event) {
		String token = event.getToken();
		try {
			tokenService.online(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
