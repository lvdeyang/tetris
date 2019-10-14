package com.sumavision.tetris.websocket.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class WebsocketMessageService {

	@Autowired
	private WebsocketMessageFeign websocketMessageFeign;
	
	/**
	 * 发送websocket消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月11日 下午1:07:05
	 * @param Long userId 用户id
	 * @param String message 推送消息内容
	 * @param WebsocketMessageType type 消息类型
	 */
	public void send(
			Long userId, 
			String message, 
			WebsocketMessageType type) throws Exception{
		
		websocketMessageFeign.send(userId, message, type.toString());
		
	}
	
}
