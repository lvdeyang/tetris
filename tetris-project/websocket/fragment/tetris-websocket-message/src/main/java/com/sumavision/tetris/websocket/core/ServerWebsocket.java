package com.sumavision.tetris.websocket.core;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.core.config.ApplicationConfig;
import com.sumavision.tetris.websocket.core.exception.IllegalTokenException;
import com.sumavision.tetris.websocket.core.load.balance.SessionMetadataService;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;

@Component
@ServerEndpoint("/server/websocket/{token}")
public class ServerWebsocket {

	private ApplicationConfig applicationConfig;
	
	private SessionMetadataService sessionMetadataService;
	
	private WebsocketMessageService messageService;
	
	private UserQuery userQuery;
	
	private EventPublisher eventPublisher;
	
	@OnMessage
    public void onMessage(String message, Session session) throws Exception{
		initBean();
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws Exception{
    	initBean();
    	UserVO user = userQuery.findByToken(token);
    	if(user == null){
    		session.close();
    		throw new IllegalTokenException(token);
    	}
    	//这个地方很奇怪，反射能调到，直接调不到
    	//Class userClass = user.getClass();
    	//Method method = userClass.getMethod("setUuid", String.class);
    	//method.invoke(user, user.getId().toString());
    	//user.setUuid(user.getId().toString());
		sessionMetadataService.add(user, session);
		//messageService.offlineMessage(user.getId());
		System.out.println(new StringBufferWrapper().append("用户：").append(user.getNickname()).append("连上来啦...").toString());
		eventPublisher.publishWebsocketSessionOpenEvent(user.getId());
    }

    @OnClose
    public void onClose(Session session) throws Exception{
    	initBean();
		String userId = sessionMetadataService.remove(session);
		eventPublisher.publishWebsocketSessionClosedEvent(Long.valueOf(userId));
    }
	
    @OnError
    public void onError(Session session, Throwable error) throws Exception{
    	session.close();
    	error.printStackTrace();
    }
    
    /**
     * 初始化bean<br/>
     * <b>作者:</b>lvdeyang<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年9月10日 上午11:21:24
     */
    private void initBean() throws Exception{
    	if(userQuery == null) userQuery = SpringContext.getBean(UserQuery.class);
		if(applicationConfig == null) applicationConfig = SpringContext.getBean(ApplicationConfig.class);
		if(sessionMetadataService == null) sessionMetadataService = SpringContext.getBean(SessionMetadataService.class);
		if(messageService == null) messageService = SpringContext.getBean(WebsocketMessageService.class);
		if(eventPublisher == null) eventPublisher = SpringContext.getBean(EventPublisher.class);
	}
    
}