package com.sumavision.tetris.p2p.room;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.p2p.config.ServerProps;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoomService {
	
	@Autowired
	private RoomDAO roomDao;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private ServerProps serverProps;

	/**
	 * 发起点对点通话<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月11日 下午1:14:27
	 * @param String roomId 房间标识
	 * @param Long initiatorId 发起人id
	 * @param String initiatorName 发起人名
	 * @param Long calleeId 被呼叫人id
	 * @param String calleename 被呼叫人名
	 * @return RoomPO 房间信息
	 */
	public synchronized RoomPO start(
					String roomId,
					Long initiatorId,
					String initiatorName,
					Long calleeId,
					String calleeName) throws Exception{
		
		//房间号校验
		RoomPO room1 = roomDao.findByRoomUuid(roomId);
		if(room1 != null){
			throw new RoomAlreadyExistException(roomId);
		}
		
		RoomPO room = new RoomPO();
		room.setUpdateTime(new Date());
		room.setRoomUuid(roomId);
		room.setInitiatorId(initiatorId);
		room.setInitiatorName(initiatorName);
		room.setCalleeId(calleeId);
		room.setCalleeName(calleeName);
		room.setStatus(RoomStatus.CONNECTING);
		
		JSONObject callRecieve = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("roomId", roomId);
		data.put("otherId", initiatorId);
		data.put("otherName", initiatorName);
		callRecieve.put("action", "callReceive");
		callRecieve.put("data", data);
		
		//websocket服务发消息
		WebsocketMessageVO ws = websocketMessageService.send(calleeId, callRecieve.toJSONString(), WebsocketMessageType.COMMAND);
		room.setMessageId(ws.getId());
		roomDao.save(room);
		
		return room;
	}
	
	/**
	 * 通话接听<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月11日 下午5:21:04
	 * @param String roomId 房间标识
	 * @param Long otherId 对方id
	 * @param Long userId 用户id
	 */
	public void answer(
			String roomId,
			Long calleeId,
			Long initiatorId) throws Exception{
		
		RoomPO room = roomDao.findByRoomUuid(roomId);
		if(room == null){
			throw new BaseException(StatusCode.FORBIDDEN, "房间号不存在！");
		}else{
			if(room.getInitiatorId().equals(calleeId) && room.getCalleeId().equals(initiatorId)){
				
				room.setStatus(RoomStatus.CONNECT);
				roomDao.save(room);
				
				JSONObject callAnswer = new JSONObject();
				JSONObject data = new JSONObject();
				data.put("roomId", roomId);
				data.put("otherId", calleeId);
				data.put("otherName", room.getCalleeName());
				callAnswer.put("action", "callAnswer");
				callAnswer.put("data", data);
				
				//websocket服务发消息
				WebsocketMessageVO ws = websocketMessageService.send(initiatorId, callAnswer.toJSONString(), WebsocketMessageType.COMMAND);
				websocketMessageService.consume(ws.getId());
				
			}else{
				throw new RoomUserIsCorrectException(roomId, room.getInitiatorId(), room.getCalleeId(), initiatorId, calleeId);
			}
		}
		
	}
	
	/**
	 * 通话挂断<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月11日 下午5:21:04
	 * @param String roomId 房间标识
	 * @param Long otherId 对方用户id
	 * @param Long userId 用户id
	 */
	public void hangUp(
			String roomId,
			Long otherId,
			Long userId) throws Exception{
		
		RoomPO room = roomDao.findByRoomUuid(roomId);
		if(room == null){
			throw new BaseException(StatusCode.FORBIDDEN, "房间号不存在！");
		}else{
			if((room.getInitiatorId().equals(otherId) || room.getCalleeId().equals(otherId))
				&& (room.getInitiatorId().equals(userId) || room.getCalleeId().equals(userId))){
				
				if(room.getInitiatorId().equals(userId)){
					
					JSONObject callHangUp = new JSONObject();
					JSONObject data = new JSONObject();
					data.put("roomId", roomId);
					data.put("otherId", userId);
					data.put("otherName", room.getInitiatorName());
					callHangUp.put("action", "callHangUp");
					callHangUp.put("data", data);
					
					//websocket服务发消息
					WebsocketMessageVO ws = websocketMessageService.send(otherId, callHangUp.toJSONString(), WebsocketMessageType.COMMAND);
					websocketMessageService.consume(ws.getId());
				}
				
				if(room.getCalleeId().equals(userId)){
					
					JSONObject callHangUp = new JSONObject();
					JSONObject data = new JSONObject();
					data.put("roomId", roomId);
					data.put("otherId", userId);
					data.put("otherName", room.getCalleeName());
					callHangUp.put("action", "callHangUp");
					callHangUp.put("data", data);
					
					//websocket服务发消息
					WebsocketMessageVO ws = websocketMessageService.send(otherId, callHangUp.toJSONString(), WebsocketMessageType.COMMAND);
					websocketMessageService.consume(ws.getId());
				}
				
				roomDao.delete(room);
				
			}else{
				throw new RoomUserIsCorrectException(roomId, room.getInitiatorId(), room.getCalleeId(), userId, otherId);
			}
		}
	}
	
	/**
	 * 根据角色清理房间<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月23日 上午10:48:33
	 * @param Long userId 角色id
	 */
	public void deleteRoomByUserId(Long userId) throws Exception{
		
		List<RoomPO> rooms = roomDao.findByInitiatorIdOrCalleeId(userId, userId);
		
		System.out.println("来了老弟！");
		
		roomDao.deleteInBatch(rooms);
	}
	
	/**
	 * 获取websocket连接url<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月12日 上午11:15:04
	 * @return String websocket连接url
	 */
	@Deprecated
	public String getWebSocketInfo() throws Exception{
		
		String ip = serverProps.getIp();
		String port = serverProps.getPort();
		
		String ws = new StringBufferWrapper().append("ws://")
											 .append(ip)
											 .append(":")
											 .append(port)
											 .append("/websocket/")
											 .toString();
		
		return ws;
	}
	
}
