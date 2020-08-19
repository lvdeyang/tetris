package com.sumavision.bvc.device.command.basic.silence;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.UserBO;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

/**
 * 
* @ClassName: CommandSilenceLocalServiceImpl 
* @Description: 关闭客户端本地音视频发送
* @author zsy
* @date 2020年2月20日 下午3:25:33 
*
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CommandSilenceLocalServiceImpl {
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	/**
	 * 通过websocket消息，通知客户端开始/关闭本地视频/音频发送<br/>
	 * <p>直接关闭客户端的输出，而不是系统内的转发，关闭会导致所以目的都收不到</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月20日 下午3:31:20
	 * @param user 发起人
	 * @param userIdList
	 * @param mediaMode 1视频，2音频
	 * @param startStopMode 2停止
	 * @return
	 * @throws Exception
	 */
	public void operate(UserBO user, List<Long> userIdList, int mediaMode, int startStopMode) throws Exception{
		
		List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
		List<Long> consumeIds = new ArrayList<Long>();
		
		for(Long id : userIdList){
			JSONObject message = new JSONObject();
			if(startStopMode == 2){
				if(mediaMode == 1){
					message.put("businessType", "stopVideoSend");
				}if(mediaMode == 2){
					message.put("businessType", "stopAudioSend");
				}
			}
			messageCaches.add(new MessageSendCacheBO(id, message.toJSONString(), WebsocketMessageType.COMMAND, user.getId(), user.getName()));
		}
		
		//发消息
		for(MessageSendCacheBO cache : messageCaches){
			WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
			consumeIds.add(ws.getId());
		}
		//全部消费，未收到的成员不处理
		websocketMessageService.consumeAll(consumeIds);
	}
}
