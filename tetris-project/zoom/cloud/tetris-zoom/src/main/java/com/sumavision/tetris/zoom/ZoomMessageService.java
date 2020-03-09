package com.sumavision.tetris.zoom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.zoom.exception.ZoomMemberNotFoundException;
import com.sumavision.tetris.zoom.exception.ZoomNotFoundException;

@Service
@Transactional(rollbackFor = Exception.class)
public class ZoomMessageService {

	@Autowired
	private ZoomDAO zoomDao;
	
	@Autowired
	private ZoomMemberDAO zoomMemberDao;
	
	@Autowired
	private ZoomMessageDAO zoomMessageDao;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	/**
	 * 发会议私信<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午1:57:31
	 * @param Long myZoomMemberId 消息发送者成员id
	 * @param Long targetZoomMemberId 消息发送目标成员id
	 * @param String message 消息内容
	 * @return ZoomMessageVO 消息数据
	 */
	public ZoomMessageVO privateLetter(
			Long myZoomMemberId,
			Long targetZoomMemberId,
			String message) throws Exception{
		
		ZoomMemberPO myMember = zoomMemberDao.findOne(myZoomMemberId);
		if(myMember == null){
			throw new ZoomMemberNotFoundException(myZoomMemberId);
		}
		
		ZoomMemberPO targetMember = zoomMemberDao.findOne(targetZoomMemberId);
		if(targetMember == null){
			throw new ZoomMemberNotFoundException(targetZoomMemberId);
		}
		
		ZoomPO zoom = zoomDao.findOne(myMember.getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException();
		}
		
		ZoomMessagePO messageEntity = new ZoomMessagePO();
		messageEntity.setFromMemberId(myMember.getId());
		messageEntity.setFromMemberRename(myMember.getRename());
		messageEntity.setMessage(message);
		messageEntity.setToMemberId(targetMember.getId());
		messageEntity.setToMemberRename(targetMember.getRename());
		messageEntity.setBroadcast(false);
		messageEntity.setZoomId(myMember.getZoomId());
		messageEntity.setUpdateTime(new Date());
		zoomMessageDao.save(messageEntity);
		
		JSONObject content = new JSONObject();
		content.put("code", zoom.getCode());
		content.put("name", zoom.getName());
		content.put("fromMemberId", myMember.getId());
		content.put("targetMemberId", targetMember.getId());
		content.put("message", message);
		content.put("time", DateUtil.format(messageEntity.getUpdateTime(), DateUtil.dateTimePattern));
		websocketMessageService.push(targetMember.getUserId(), "zoomPrivateLetter", content, myMember.getUserId(), myMember.getRename());
		
		return new ZoomMessageVO().set(messageEntity);
	}
	
	/**
	 * 群发会议私信<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午2:03:05
	 * @param Long myZoomMemberId 消息发送者成员id
	 * @param Collection<Long> targetZoomMemberIds 消息发送目标成员id列表
	 * @param message 消息内容
	 * @return List<ZoomMessageVO> 消息数据列表
	 */
	public List<ZoomMessageVO> privateLetters(
			Long myZoomMemberId,
			Collection<Long> targetZoomMemberIds,
			String message) throws Exception{
		
		List<ZoomMemberPO> targetMembers = zoomMemberDao.findAll(targetZoomMemberIds);
		if(targetMembers==null || targetMembers.size()<=0) return null;
		
		ZoomMemberPO myMember = zoomMemberDao.findOne(myZoomMemberId);
		if(myMember == null){
			throw new ZoomMemberNotFoundException(myZoomMemberId);
		}
		
		ZoomPO zoom = zoomDao.findOne(myMember.getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException();
		}
		
		List<ZoomMessagePO> messageEntities = new ArrayList<ZoomMessagePO>();
		Date time = new Date();
		for(ZoomMemberPO targetMember:targetMembers){
			ZoomMessagePO messageEntity = new ZoomMessagePO();
			messageEntity.setFromMemberId(myMember.getId());
			messageEntity.setFromMemberRename(myMember.getRename());
			messageEntity.setMessage(message);
			messageEntity.setToMemberId(targetMember.getId());
			messageEntity.setToMemberRename(targetMember.getRename());
			messageEntity.setBroadcast(false);
			messageEntity.setZoomId(zoom.getId());
			messageEntity.setUpdateTime(time);
			messageEntities.add(messageEntity);
		}
		zoomMessageDao.save(messageEntities);
		
		for(ZoomMessagePO messageEntity:messageEntities){
			ZoomMemberPO targetMember = null;
			for(ZoomMemberPO m:targetMembers){
				if(m.getId().equals(messageEntity.getToMemberId())){
					targetMember = m;
					break;
				}
			}
			JSONObject content = new JSONObject();
			content.put("code", zoom.getCode());
			content.put("name", zoom.getName());
			content.put("fromMemberId", messageEntity.getFromMemberId());
			content.put("targetMemberId", messageEntity.getToMemberId());
			content.put("message", messageEntity.getMessage());
			content.put("time", DateUtil.format(messageEntity.getUpdateTime(), DateUtil.dateTimePattern));
			websocketMessageService.push(targetMember.getUserId(), "zoomPrivateLetter", content, myMember.getUserId(), myMember.getRename());
		}
		
		return ZoomMessageVO.getConverter(ZoomMessageVO.class).convert(messageEntities, ZoomMessageVO.class);
	}
	
	/**
	 * 会议广播消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午2:35:14
	 * @param Long myZoomMemberId 消息发送者成员id
	 * @param String message 消息内容
	 * @return ZoomMessageVO 消息数据
	 */
	public ZoomMessageVO broadcast(
			Long myZoomMemberId,
			String message) throws Exception{
		
		ZoomMemberPO myMember = zoomMemberDao.findOne(myZoomMemberId);
		if(myMember == null){
			throw new ZoomMemberNotFoundException(myZoomMemberId);
		}
		
		ZoomPO zoom = zoomDao.findOne(myMember.getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException();
		}
		
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNot(zoom.getId(), myMember.getId());
		
		ZoomMessagePO messageEntity = new ZoomMessagePO();
		messageEntity.setFromMemberId(myMember.getId());
		messageEntity.setFromMemberRename(myMember.getRename());
		messageEntity.setMessage(message);
		messageEntity.setZoomId(zoom.getId());
		messageEntity.setBroadcast(true);
		messageEntity.setUpdateTime(new Date());
		zoomMessageDao.save(messageEntity);
		
		if(others!=null && others.size()>0){
			for(ZoomMemberPO other:others){
				JSONObject content = new JSONObject();
				content.put("code", zoom.getCode());
				content.put("name", zoom.getName());
				content.put("fromMemberId", myMember.getId());
				content.put("message", messageEntity.getMessage());
				content.put("time", DateUtil.format(messageEntity.getUpdateTime(), DateUtil.dateTimePattern));
				websocketMessageService.push(other.getUserId(), "zoomBroadcast", content, myMember.getUserId(), myMember.getRename());
			}
		}
		
		return new ZoomMessageVO().set(messageEntity);
	} 
	
}
