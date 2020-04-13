package com.sumavision.bvc.device.command.basic.remind;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.command.group.enumeration.MemberStatus;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: CommandRemindServiceImpl 
* @Description: 指挥提醒
* @author zsy
* @date 2019年11月19日 上午10:56:48 
*
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class CommandRemindServiceImpl {
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	/**
	 * 开启指挥提醒<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月20日 上午11:02:47
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public JSONObject remind(Long groupId) throws Exception{
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
					
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
			}
			if(group.getStatus().equals(GroupStatus.PAUSE)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已暂停，无法操作，id: " + group.getId());
			}
			
			JSONObject chairSplit = new JSONObject();
			group.setStatus(GroupStatus.REMIND);
			commandGroupDao.save(group);
			List<CommandGroupMemberPO> members = group.getMembers();
			List<CommandGroupForwardPO> forwards = group.getForwards();
			CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			
			//给主席找播放器，使用播放器中的第1个
			List<CommandGroupUserPlayerPO> cPlayers = chairmanMember.getPlayers();
			if(cPlayers.size() > 0){
//				Collections.sort(cPlayers, new CommandGroupUserPlayerPO.PlayerComparatorFromIndex());
				CommandGroupUserPlayerPO cPlayer = cPlayers.get(0);
				chairSplit.put("serial", cPlayer.getLocationIndex());
				chairSplit.put("businessId", group.getId().toString());
				chairSplit.put("businessInfo", group.getName() + " 开启提醒");
				chairSplit.put("status", (GroupStatus.REMIND.getCode()));
			}
			
			//发消息
			List<Long> consumeIds = new ArrayList<Long>();
			for(CommandGroupMemberPO member : members){
				if(member.isAdministrator()){
					continue;
				}
				//已接听的成员才通知
				if(!member.getMemberStatus().equals(MemberStatus.CONNECT)){
					continue;
				}
				
				CommandGroupForwardPO forward = commandCommonUtil.queryForwardBySrcAndDstMemberId(forwards, chairmanMember.getId(), member.getId());
				CommandGroupUserPlayerPO player = commandCommonUtil.queryPlayerByForwardAndDstMember(forward, member);
				//没找到播放器的也无法提醒
				if(player != null){
					JSONObject message = new JSONObject();
					message.put("serial", player.getLocationIndex());
					message.put("businessType", "commandRemind");
					message.put("businessId", groupId.toString());
					message.put("businessInfo", group.getName() + " 开启提醒");
					message.put("status", GroupStatus.REMIND.getCode());
					WebsocketMessageVO ws = websocketMessageService.send(member.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairmanMember.getUserId(), chairmanMember.getUserName());
					consumeIds.add(ws.getId());
				}				
			}
			websocketMessageService.consumeAll(consumeIds);
			return chairSplit;
		}
	}	
	
	/**
	 * 关闭指挥提醒<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月20日 上午11:02:28
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public JSONObject remindStop(Long groupId) throws Exception{
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
					
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
			}
			if(group.getStatus().equals(GroupStatus.PAUSE)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已暂停，无法操作，id: " + group.getId());
			}
			
			JSONObject chairSplit = new JSONObject();
			group.setStatus(GroupStatus.START);
			commandGroupDao.save(group);
			List<CommandGroupMemberPO> members = group.getMembers();
			List<CommandGroupForwardPO> forwards = group.getForwards();
			CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			
			//给主席找播放器，使用播放器中的第1个
			List<CommandGroupUserPlayerPO> cPlayers = chairmanMember.getPlayers();
			if(cPlayers.size() > 0){
//				Collections.sort(cPlayers, new CommandGroupUserPlayerPO.PlayerComparatorFromIndex());
				CommandGroupUserPlayerPO cPlayer = cPlayers.get(0);
				chairSplit.put("serial", cPlayer.getLocationIndex());
				chairSplit.put("businessId", group.getId().toString());
				chairSplit.put("businessInfo", group.getName() + " 关闭提醒");
				chairSplit.put("status", (group.getStatus().getCode()));
			}
			
			//发消息
			List<Long> consumeIds = new ArrayList<Long>();
			for(CommandGroupMemberPO member : members){
				if(member.isAdministrator()){
					continue;
				}
				//已接听的成员才通知
				if(!member.getMemberStatus().equals(MemberStatus.CONNECT)){
					continue;
				}
				
				CommandGroupForwardPO forward = commandCommonUtil.queryForwardBySrcAndDstMemberId(forwards, chairmanMember.getId(), member.getId());
				CommandGroupUserPlayerPO player = commandCommonUtil.queryPlayerByForwardAndDstMember(forward, member);
				//没找到播放器的也无法提醒
				if(player != null){
					JSONObject message = new JSONObject();
					message.put("serial", player.getLocationIndex());
					message.put("businessType", "commandRemindStop");
					message.put("businessId", groupId.toString());
					message.put("businessInfo", group.getName() + " 关闭提醒");
					message.put("status", GroupStatus.START.getCode());
					WebsocketMessageVO ws = websocketMessageService.send(member.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairmanMember.getUserId(), chairmanMember.getUserName());
					consumeIds.add(ws.getId());
				}				
			}
			websocketMessageService.consumeAll(consumeIds);
			return chairSplit;
		}
	}
	
}
