package com.sumavision.tetris.bvc.business.group.remind;

import java.util.ArrayList;
import java.util.List;
import com.sumavision.tetris.bvc.business.dao.*;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.bvc.business.group.GroupMemberStatus;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupStatus;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 指挥提醒业务<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年2月22日 下午3:28:20
 */
@Slf4j
@Service
public class GroupRemindService {

	/** synchronized锁的前缀 */
	private static final String lockProcessPrefix = "tetris-group-";

	@Autowired
	private PageTaskDAO pageTaskDao;

	@Autowired
	private GroupDAO groupDao;

	@Autowired
	private WebsocketMessageService websocketMessageService;

	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;

	public JSONObject remind(Long groupId) throws Exception{
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
					
			GroupPO group = groupDao.findOne(groupId);
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
			}
			
			JSONObject chairSplit = new JSONObject();
			group.setRemind(true);
			groupDao.save(group);
			List<BusinessGroupMemberPO> members = group.getMembers();
			BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
			
			//会议中所有的task
			List<PageTaskPO> tasks = pageTaskDao.findByBusinessIdStartingWithOrBusinessId(groupId.toString()+"-", groupId.toString());
			
			//给主席找播放器，使用播放器中的第1个
			PageTaskPO chairmanTask = null;
			for(PageTaskPO task : tasks){
				if(chairmanMember.getId().equals(task.getDstMemberId())
						&& task.isShowing()){
					chairmanTask =  task;
					break;
				}
			}
			if(chairmanTask != null){
				chairSplit.put("serial", chairmanTask.getLocationIndex());
				chairSplit.put("businessType", "commandRemind");
				chairSplit.put("businessId", group.getId().toString());
				chairSplit.put("businessInfo", "提示：" + group.getName() + " 已开始");
				chairSplit.put("status", (GroupStatus.REMIND.getCode()));
			}
			
			//发消息
			List<Long> consumeIds = new ArrayList<Long>();
			for(BusinessGroupMemberPO member : members){
				if(member.getIsAdministrator()){
//					continue;//主席也发个消息，目前看http返回之后主席没有播放或停止音乐
				}
				//已接听的成员才通知
				if(!member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
					continue;
				}
				//只通知用户
				if(!GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
					continue;
				}
				
				//没找到播放器的无法提醒；播放器show==false的不提醒
				PageTaskPO memberTask = null;
				for(PageTaskPO task : tasks){
					if(member.getId().equals(task.getDstMemberId())
							&& task.isShowing()){
						memberTask =  task;
						break;
					}
				}
				if(memberTask != null){
					JSONObject message = new JSONObject();
					message.put("serial", memberTask.getLocationIndex());
					message.put("businessType", "commandRemind");
					message.put("businessId", groupId.toString());
					message.put("businessInfo", "提示：" + group.getName() + " 已开始");
					message.put("status", GroupStatus.REMIND.getCode());
					WebsocketMessageVO ws = websocketMessageService.send(Long.parseLong(member.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND);
					consumeIds.add(ws.getId());
				}				
			}
			websocketMessageService.consumeAll(consumeIds);
			return chairSplit;
		}
	}
	
	public JSONObject remindStop(Long groupId) throws Exception{
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
					
			GroupPO group = groupDao.findOne(groupId);
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
			}
			
			JSONObject chairSplit = new JSONObject();
			group.setRemind(false);
			groupDao.save(group);
			List<BusinessGroupMemberPO> members = group.getMembers();
			BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
			
			//会议中所有的task
			List<PageTaskPO> tasks = pageTaskDao.findByBusinessIdStartingWithOrBusinessId(groupId.toString()+"-", groupId.toString());
			
			//给主席找播放器，使用播放器中的第1个
			PageTaskPO chairmanTask = null;
			for(PageTaskPO task : tasks){
				if(chairmanMember.getId().equals(task.getDstMemberId())
						&& task.isShowing()){
					chairmanTask =  task;
					break;
				}
			}
			if(chairmanTask != null){
				chairSplit.put("serial", chairmanTask.getLocationIndex());
				chairSplit.put("businessType", "commandRemindStop");
				chairSplit.put("businessId", group.getId().toString());
				chairSplit.put("businessInfo", group.getName() + " 关闭提醒");
				chairSplit.put("status", group.getStatus().getCode());
			}
			
			//发消息
			List<Long> consumeIds = new ArrayList<Long>();
			for(BusinessGroupMemberPO member : members){
				if(member.getIsAdministrator()){
//					continue;//主席也发个消息，目前看http返回之后主席没有播放或停止音乐
				}
				//已接听的成员才通知
				if(!member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
					continue;
				}
				//只通知用户
				if(!GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
					continue;
				}
				
				//没找到播放器的无法提醒；播放器show==false的不提醒
				PageTaskPO memberTask = null;
				for(PageTaskPO task : tasks){
					if(member.getId().equals(task.getDstMemberId())
							&& task.isShowing()){
						memberTask =  task;
						break;
					}
				}
				if(memberTask != null){
					JSONObject message = new JSONObject();
					message.put("serial", memberTask.getLocationIndex());
					message.put("businessType", "commandRemindStop");
					message.put("businessId", groupId.toString());
					message.put("businessInfo", group.getName() + " 关闭提醒");
					message.put("status", group.getStatus().getCode());
					WebsocketMessageVO ws = websocketMessageService.send(Long.parseLong(member.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND);
					consumeIds.add(ws.getId());
				}				
			}
			websocketMessageService.consumeAll(consumeIds);
			return chairSplit;
		}
	}

}
