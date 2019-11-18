package com.sumavision.tetris.p2p.websocket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@ServerEndpoint("/websocket/{token}")
@Component
public class WebSocketServer {
	
	private static Logger log = LoggerFactory.getLogger(WebSocketServer.class); 
	
	private WebSocketService webSocketService;
	
	private UserQuery userQuery;
	
	@OnOpen
	public void onOpen(
			Session session,
			@PathParam("token") String token
			) throws Exception{
		
		if(userQuery == null){
			SpringContext.getBean(UserQuery.class);
		}
		
		UserVO user = userQuery.findByToken(token);
		String userId = user.getId().toString();
		
		log.info("客户端连接成功，连接id：" + session.getId() + "，用户：" + userId);
		
		String key = new StringBufferWrapper().append(userId).toString();
		
		WebSocketManager.getInstance().add(key, session);
	}
	
	@OnClose
	public void onClose(Session session){
		log.info("websocket连接关闭，连接id：" + session.getId());
		WebSocketManager.getInstance().remove(session);
	}
	
	@OnError
    public void onError(Session session, Throwable error) throws Exception{
    	session.close();
    	error.printStackTrace();
    }
	
	@OnMessage
	public void onMessage(String message, Session session) throws Exception{
		try {
			if(message == null){
				return;
			}
			if(webSocketService == null){
				webSocketService = SpringContext.getBean(WebSocketService.class);
			}
			
			//区分消息
			
			System.out.println("收到消息！");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("websocket消息处理报错。。。");
		}
	}
	
}
