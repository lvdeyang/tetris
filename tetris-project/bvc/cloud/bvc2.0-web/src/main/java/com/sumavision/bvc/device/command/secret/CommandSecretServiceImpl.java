package com.sumavision.bvc.device.command.secret;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.enumeration.MemberStatus;
import com.sumavision.bvc.command.group.enumeration.OriginType;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: CommandSecretServiceImpl 
* @Description: 专项会议业务
* @author zsy
* @date 2019年10月24日 上午10:56:48 
*
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class CommandSecretServiceImpl {
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;

	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	/**
	 * 开始专向会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 下午2:46:07
	 * @param creatorUserId
	 * @param creatorUsername
	 * @param name
	 * @param memberUserId
	 * @param locationIndex 指定播放器序号，序号从0起始；-1为自动选择
	 * @throws Exception
	 */
	public Object start(
			Long creatorUserId,
			String creatorUsername,
			String name,
			Long memberUserId,
			int locationIndex) throws Exception{
		
		String commandString = commandCommonUtil.generateCommandString(GroupType.SECRET);
		
		if(creatorUserId.equals(memberUserId)){
			throw new BaseException(StatusCode.FORBIDDEN, "请选择其他成员进行专向" + commandString);
		}
		
		List<CommandGroupPO> secretGroups = commandGroupDao.findByType(GroupType.SECRET);
		for(CommandGroupPO secretGroup : secretGroups){
			if(!secretGroup.getStatus().equals(GroupStatus.STOP)){
				List<CommandGroupMemberPO> members = secretGroup.getMembers();
				for(CommandGroupMemberPO member : members){
					if(member.getUserId().equals(creatorUserId)){
						throw new BaseException(StatusCode.FORBIDDEN, "您已经在参加其他专向" + commandString);
					}else if(member.getUserId().equals(memberUserId)){
						throw new BaseException(StatusCode.FORBIDDEN, "对方已经在参加其他专向" + commandString);
					}
				}
			}
		}
		
		log.info(creatorUsername + "发起专向会议，成员用户userId：" + memberUserId);
		
		List<Long> userIdArray = new ArrayListWrapper<Long>().add(memberUserId).getList();		
		CommandGroupPO group = commandBasicServiceImpl.save(creatorUserId, creatorUserId, creatorUsername, name, name, GroupType.SECRET, OriginType.INNER, userIdArray);
		Object chairSplits = commandBasicServiceImpl.start(group.getId(), locationIndex);
		return chairSplits;
	}
	
	/**
	 * 停止专向会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 下午2:54:38
	 * @param userId 操作人
	 * @param groupId
	 * @throws Exception
	 */
	public JSONObject stop(Long userId, Long groupId) throws Exception{
		
		CommandGroupPO group = commandGroupDao.findOne(groupId);
		log.info("专向会议停止：" + group.getName());
		
		JSONArray splits = commandBasicServiceImpl.stop(userId, groupId, 0);
		
		//恢复其它业务中的转发
		List<Long> groupIds = new ArrayListWrapper<Long>().add(groupId).getList();
		commandBasicServiceImpl.startAllGroupForwards(groupIds, true, true);
				
		commandBasicServiceImpl.remove(group.getUserId(), groupIds);
		
		if(splits.size() > 0){
			return splits.getJSONObject(0);
		}
		return null;
	}
	
	/**
	 * 成员同意加入专向会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 下午2:58:42
	 * @param userId 成员
	 * @param groupId
	 * @throws Exception
	 */
	public Object accept(Long userId, Long groupId) throws Exception{
				
		CommandGroupPO group = commandGroupDao.findOne(groupId);
		if(group == null){
			String commandString = commandCommonUtil.generateCommandString(GroupType.SECRET);
			throw new BaseException(StatusCode.FORBIDDEN, "专向" + commandString + "已停止");
		}
		log.info("成员同意加入专向会议：" + group.getName());
		
		//停止其它业务观看专向会议的2个成员
		List<Long> groupIds = new ArrayListWrapper<Long>().add(groupId).getList();
		List<Long> srcUserIds = new ArrayList<Long>();
		for(CommandGroupMemberPO member : group.getMembers()){
			srcUserIds.add(member.getUserId());
		}
		commandBasicServiceImpl.stopAllGroupForwardsBySrcMemberIds(groupIds, srcUserIds, true, true);
			
		JSONArray groupInfos = commandBasicServiceImpl.enter(userId, groupIds);
		if(groupInfos.size() > 0){
			JSONArray splits = groupInfos.getJSONObject(0).getJSONArray("splits");
			if(splits.size() > 0){
				return splits.get(0);
			}
		}
		return null;
	}
	
	/**
	 * 成员拒绝加入专向会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 下午3:06:14
	 * @param userId 成员（无用，因为直接按停会处理）
	 * @param groupId
	 * @throws Exception
	 */
	public void refuse(Long userId, Long groupId) throws Exception{
		
		CommandGroupPO group = commandGroupDao.findOne(groupId);
		if(group == null){
//			String commandString = commandCommonUtil.generateCommandString(GroupType.SECRET);
			throw new BaseException(StatusCode.FORBIDDEN, "专向会议已停止");
		}
		log.info("成员拒绝加入专向会议，会议：" + group.getName());
		
		commandBasicServiceImpl.stop(userId, groupId, 1);
		
		List<Long> groupIds = new ArrayListWrapper<Long>().add(groupId).getList();
		commandBasicServiceImpl.remove(group.getUserId(), groupIds);
	}
	
	/**
	 * 判断一条转发是否因为专项会议业务而暂停<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午3:13:07
	 * @param forward
	 * @return
	 */
	public boolean whetherStopForSecret(CommandGroupForwardPO forward){
		
		//以forward的源，作为源和目的去查找专向转发
		CommandGroupPO group = forward.getGroup();
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO member = commandCommonUtil.queryMemberById(members, forward.getSrcMemberId());
		Long forwardSrcUserId = member.getUserId();
		
		//查找专向会议group，非STOP的，且成员都CONNECT的，判断成员是否是forward的源（后续如果业务有变动，需要改成根据forward来查询判断）
//		Set<CommandGroupForwardPO> secretForwards = new HashSet<CommandGroupForwardPO>();
		List<CommandGroupPO> secretGroups = commandGroupDao.findByType(GroupType.SECRET);
		for(CommandGroupPO secretGroup : secretGroups){
			//forward自己的会议不需要判断
			if(secretGroup.getId().equals(group.getId())){
				continue;
			}
			if(!secretGroup.getStatus().equals(GroupStatus.STOP)){
				List<CommandGroupMemberPO> secretMembers = secretGroup.getMembers();
				//先判断成员是否都CONNECT
				for(CommandGroupMemberPO secretMember : secretMembers){
					if(!secretMember.getMemberStatus().equals(MemberStatus.CONNECT)){
						break;
					}
				}
				//再判断成员是否是forward的源
				for(CommandGroupMemberPO secretMember : secretMembers){
					if(secretMember.getUserId().equals(forwardSrcUserId)){
						return true;
					}
				}
//				for(CommandGroupForwardPO secretForward : secretGroup.getForwards()){
//					if(secretForward.getExecuteStatus().equals(ExecuteStatus.DONE)){
//						secretForwards.add(secretForward);
//					}
//				}
			}
		}
		
		return false;
		
//		List<Long> memberIds = new ArrayList<Long>();
//		memberIds.add(forward.getSrcMemberId());
//		Set<CommandGroupForwardPO> forwards = commandCommonUtil.queryForwardsByMemberIds(
//				secretForwards, memberIds, ForwardBusinessType.BASIC_COMMAND, ExecuteStatus.DONE);
//		
//		if(forwards.size() > 0){
//			return true;
//		}else{
//			return false;
//		}
	}
}
