package com.sumavision.tetris.p2p.event.listener;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.p2p.room.RoomService;
import com.sumavision.tetris.websocket.core.event.WebsocketSessionClosedEvent;

/**
 * websocket用户离线事件<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月23日 上午10:50:41
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WebsocketSessionClosedListener implements ApplicationListener<WebsocketSessionClosedEvent>{

	@Autowired
	private RoomService roomService;
	
	@Override
	public void onApplicationEvent(WebsocketSessionClosedEvent event) {
		
		try {
			roomService.deleteRoomByUserId(event.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
