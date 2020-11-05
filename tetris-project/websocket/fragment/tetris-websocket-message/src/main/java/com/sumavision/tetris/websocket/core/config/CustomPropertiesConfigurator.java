package com.sumavision.tetris.websocket.core.config;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

import com.sumavision.tetris.websocket.core.CustomProperties;

public class CustomPropertiesConfigurator extends Configurator{

	@Override
	public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
		HttpSession httpSession = (HttpSession)request.getHttpSession();
		String clientIp = "";
		if(httpSession != null){
			clientIp = (String)httpSession.getAttribute(CustomProperties.CLIENT_IP.toString());
		}
		//把HttpSession中保存的ClientIP放到ServerEndpointConfig中，关键字可以跟之前不同
		config.getUserProperties().put(CustomProperties.CLIENT_IP.toString(), clientIp);
	}
	
}
