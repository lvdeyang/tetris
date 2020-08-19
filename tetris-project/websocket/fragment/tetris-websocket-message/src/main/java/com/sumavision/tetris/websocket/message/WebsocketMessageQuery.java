package com.sumavision.tetris.websocket.message;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class WebsocketMessageQuery {
	
	@Autowired
	private WebsocketMessageDAO websocketMessageDao;

	/**
	 * 分页用户收到的特定类型消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 下午10:27:16
	 * @param Long userId 用户id
	 * @param Long exceptFromUserId 过滤发送用户
	 * @param WebsocketMessageType messageType 消息类型
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<WebsocketMessageVO> 消息列表
	 */
	public List<WebsocketMessageVO> findByUserIdAndMessageTypeOrderByUpdateTimeDesc(
			long userId, 
			WebsocketMessageType messageType, 
			int currentPage, 
			int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<WebsocketMessagePO> pagedEntities =  websocketMessageDao.findByUserIdAndMessageTypeOrderByUpdateTimeDesc(userId, messageType, page);
		List<WebsocketMessagePO> entities = pagedEntities.getContent();
		if(entities==null || entities.size()<=0){
			return null;
		}else{
			List<WebsocketMessageVO> messages = new ArrayList<WebsocketMessageVO>();
			for(WebsocketMessagePO entity:entities){
				messages.add(new WebsocketMessageVO().set(entity));
			}
			return messages;
		}
	}
	
}
