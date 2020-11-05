package com.sumavision.tetris.websocket.core.listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sumavision.tetris.websocket.core.CustomProperties;

@WebListener
public class HttpServletRequestListener implements ServletRequestListener {

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		
	}

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
		HttpSession session = request.getSession();
		session.setAttribute(CustomProperties.CLIENT_IP.toString(), sre.getServletRequest().getRemoteAddr());
	}

}
