package com.sumavision.bvc.device.command.basic.silence;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.command.group.basic.CommandGroupAvtplGearsPO;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupMemberDAO;
import com.sumavision.bvc.command.group.enumeration.ExecuteStatus;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl.MemberLevelComparator;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.record.CommandRecordServiceImpl;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: CommandSilenceServiceImpl 
* @Description: 静默业务
* @author zsy
* @date 2019年11月06日 上午10:56:48 
*
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class CommandSilenceServiceImpl {
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private CommandGroupMemberDAO commandGroupMemberDao;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;
	
	@Autowired
	private CommandRecordServiceImpl commandRecordServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;	
	
	/** 指挥中的一个成员开启静默<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午11:01:33
	 * @param groupId
	 * @param userId 操作成员的userId
	 * @param silenceToHigher 开启对上静默
	 * @param silenceToLower 开启对下静默
	 * @throws Exception
	 */
	public void startSilence(Long groupId, Long userId, boolean silenceToHigher, boolean silenceToLower) throws Exception{
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
					
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
			}
			
			Set<CommandGroupMemberPO> members = group.getMembers();
			CommandGroupMemberPO operateMember = commandCommonUtil.queryMemberByUserId(members, userId);
			if(silenceToHigher) operateMember.setSilenceToHigher(true);
			if(silenceToLower) operateMember.setSilenceToLower(true);
			
			//操作静默的user作为源查找会中所有的DONE转发relativeForwards
			Set<CommandGroupForwardPO> needDelForwards = new HashSet<CommandGroupForwardPO>();
			Set<CommandGroupForwardPO> forwards = group.getForwards();
			List<Long> srcMemberIds = new ArrayListWrapper<Long>().add(operateMember.getId()).getList();
			Set<CommandGroupForwardPO> relativeForwards = commandCommonUtil.queryForwardsBySrcmemberIds(forwards, srcMemberIds, null, ExecuteStatus.DONE);
			
			//从源和目的判断是否需要静默，是的话删除转发
			for(CommandGroupForwardPO forward : relativeForwards){
				
				//自己给自己的转发不处理
				if(operateMember.getId().equals(forward.getDstMemberId())){
					continue;
				}
				
				CommandGroupMemberPO dstMember = commandCommonUtil.queryMemberById(members, forward.getDstMemberId());
				int levelCompare = commandCommonServiceImpl.compareLevelByMember(operateMember, dstMember);
				if(silenceToHigher && levelCompare<0){
					//对上静默
					forward.setExecuteStatus(ExecuteStatus.UNDONE);
					needDelForwards.add(forward);
				}else if(silenceToLower && levelCompare>=0){
					//对下静默
					forward.setExecuteStatus(ExecuteStatus.UNDONE);
					needDelForwards.add(forward);
				}
			}
			
			commandGroupDao.save(group);
			
			//生成forwardDel的logic
			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
			LogicBO logic = commandBasicServiceImpl.openBundle(null, null, null, null, needDelForwards, codec, group.getUserId());
			LogicBO logicCastDevice = commandCastServiceImpl.openBundleCastDevice(null, null, needDelForwards, null, null, codec, group.getUserId());
			logic.merge(logicCastDevice);
			
			//录制更新
			LogicBO logicRecord = commandRecordServiceImpl.update(group.getUserId(), group, 1, false);
			logic.merge(logicRecord);
			
			StringBufferWrapper description = new StringBufferWrapper()
					.append(group.getName()).append(" 指挥，")
					.append(operateMember.getUserName()).append(" 成员 ");
			if(silenceToHigher) description.append("对上静默 ");
			if(silenceToLower) description.append("对下静默 ");
			ExecuteBusinessReturnBO returnBO = executeBusiness.execute(logic, description.toString());
			commandRecordServiceImpl.saveStoreInfo(returnBO, group.getId());

		}
	}	
	
	/**
	 * 指挥中的一个成员停止静默<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午11:02:00
	 * @param groupId
	 * @param userId 操作成员的userId
	 * @param stopSilenceToHigher 停止对上静默
	 * @param stopSilenceToLower 停止对下静默
	 * @throws Exception
	 */
	public void stopSilence(Long groupId, Long userId, boolean stopSilenceToHigher, boolean stopSilenceToLower) throws Exception{
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
					
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
			}
			
			Set<CommandGroupMemberPO> members = group.getMembers();
			CommandGroupMemberPO operateMember = commandCommonUtil.queryMemberByUserId(members, userId);
			if(stopSilenceToHigher) operateMember.setSilenceToHigher(false);
			if(stopSilenceToLower) operateMember.setSilenceToLower(false);
			commandGroupDao.save(group);//TODO:需要吗？
			
			//恢复会中的转发
			commandBasicServiceImpl.startGroupForwards(group, true, true);
			
			StringBufferWrapper description = new StringBufferWrapper()
					.append(group.getName()).append(" 指挥，")
					.append(operateMember.getUserName()).append(" 成员 ");
			if(stopSilenceToHigher) description.append("取消对上静默 ");
			if(stopSilenceToLower) description.append("取消对下静默 ");
			
			log.info(description.toString());

		}
	}
	
	/**
	 * 判断一条转发是否因为静默操作而暂停<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午3:45:06
	 * @param forward
	 * @return
	 */
	public boolean whetherStopForSilence(CommandGroupForwardPO forward){
		
		CommandGroupMemberPO srcMember = commandGroupMemberDao.findOne(forward.getSrcMemberId());		
		if(!srcMember.isSilenceToHigher() && !srcMember.isSilenceToLower()){
			return false;
		}
		
		//自己转给自己的，false
		if(srcMember.getId().equals(forward.getDstMemberId())){
			return false;
		}
		
		//源和目的的上下级关系
		CommandGroupMemberPO dstMember = commandGroupMemberDao.findOne(forward.getDstMemberId());
		int levelCompare = commandCommonServiceImpl.compareLevelByMember(srcMember, dstMember);
		
		if(srcMember.isSilenceToHigher() && levelCompare<0){
			return true;
		}
		if(srcMember.isSilenceToLower() && levelCompare>=0){
			return true;
		}		
		return false;
	}
}
