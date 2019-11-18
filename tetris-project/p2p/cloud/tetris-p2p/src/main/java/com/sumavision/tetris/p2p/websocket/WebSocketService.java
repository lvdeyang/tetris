package com.sumavision.tetris.p2p.websocket;

import javax.websocket.Session;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.p2p.room.WebsocketIsClosedException;

@Service
@Transactional(rollbackFor = Exception.class)
public class WebSocketService {

	/**
	 * websocket发送消息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月11日 下午2:09:55
	 * @param MessageBO message 消息体
	 */
	public void sendMessage(MessageBO message) throws Exception{
		
		String messageKey = message.getKey();
		Session session = WebSocketManager.getInstance().getSession(messageKey);
		if(session == null){
			throw new WebsocketIsClosedException(messageKey);
		}
		
		session.getBasicRemote().sendText(message.getMessage());
		
	}
	
	
}
