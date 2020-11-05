package com.sumavision.bvc.device.command.time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.enumeration.MemberStatus;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.resource.dao.ResourceLayerDAO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

/**
 * 
* @ClassName: CommandFightTimeServiceImpl 
* @Description: 作战时间
* @author zsy
* @date 2020年2月11日 上午10:57:58 
*
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CommandFightTimeServiceImpl {
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	ResourceLayerDAO conn_layer;
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	/**
	 * 对某个会议设置作战时间<br/>
	 * <p>会议开始的情况下，给所有进入的成员推送</p>
	 * <p>会议未开始则直接保存数据库</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月12日 下午3:22:45
	 * @param groupId
	 * @param fightTime
	 * @throws Exception
	 */
	public void setFightTime(Long groupId, Date fightTime) throws Exception{
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			List<Long> consumeIds = new ArrayList<Long>();
						
			//会议进行中，包括暂停的时候
			if(group.getStatus().equals(GroupStatus.STOP)){
				group.setFightTime(fightTime);
			}else{
				//计算以起始时间为基点的作战时间进行保存
				Date time = new Date();
				Date startFightTime = new Date(fightTime.getTime() - (time.getTime() - group.getStartTime().getTime()));
				group.setFightTime(startFightTime);
				
				JSONObject message = new JSONObject();
				message.put("businessType", "fightTimeSet");
				message.put("businessInfo", "作战时间已更新");
				message.put("businessId", groupId.toString());
				message.put("fightTime", DateUtil.format(fightTime, DateUtil.dateTimePattern));
				
				//给所有进入的成员推送，除了主席
				List<CommandGroupMemberPO> members = group.getMembers();
				CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
				if(group.getType().equals(GroupType.SECRET)){
					for(CommandGroupMemberPO member : members){
						if(!member.isAdministrator() && member.getMemberStatus().equals(MemberStatus.CONNECT)){
							messageCaches.add(new MessageSendCacheBO(member.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairmanMember.getUserId(), chairmanMember.getUserName()));
						}
					}
				}
			}
			
			commandGroupDao.save(group);
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
		}
	}
	
	/**
	 * 计算一个会议的作战时间<br/>
	 * <p>会议没开始则返回null</p>
	 * <p>会议未设置作战时间则返回null</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月12日 下午2:16:23
	 * @param group
	 * @return 作战时间，可能为null
	 */
	public Date calculateCurrentFightTime(CommandGroupPO group){
		
		if(group.getStatus().equals(GroupStatus.STOP)) return null;
		
		if(group.getFightTime()==null || group.getStartTime()==null) return null;
		
		Date time = new Date();
		Date currentFightTime = new Date(time.getTime() - group.getStartTime().getTime() + group.getFightTime().getTime());
		
		return currentFightTime;
	}
}
