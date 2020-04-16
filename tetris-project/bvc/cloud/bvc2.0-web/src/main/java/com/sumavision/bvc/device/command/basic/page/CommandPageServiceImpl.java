package com.sumavision.bvc.device.command.basic.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.command.group.basic.CommandGroupAvtplGearsPO;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.enumeration.ExecuteStatus;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

/**
 * 
* @ClassName: CommandPageServiceImpl 
* @Description: 分页观看
* @author zsy
* @date 2020年4月10日 下午3:25:33 
*
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CommandPageServiceImpl {
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	public List<CommandGroupUserPlayerPO> rollAllVodMembers(Long userId, Long groupId) throws Exception{
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
			}
			if(!group.getUserId().equals(userId)){
				throw new BaseException(StatusCode.FORBIDDEN, "只有主席才能点播成员");
			}
			
			List<CommandGroupMemberPO> members = group.getMembers();
			List<CommandGroupForwardPO> forwards = group.getForwards();
			CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			
			//查询未能观看的，如果没有，则抛错
			List<CommandGroupForwardPO> noPlayerForwards = commandCommonUtil.queryForwardsByDstmemberIds(forwards, new ArrayListWrapper<Long>().add(chairmanMember.getId()).getList(), null, ExecuteStatus.NO_AVAILABLE_PLAYER);
			if(noPlayerForwards.size() == 0){
				throw new BaseException(StatusCode.FORBIDDEN, "您已观看所有成员");
			}
			
			//查询正在观看的，如果没有，则抛错
			List<CommandGroupForwardPO> playingForwards = commandCommonUtil.queryForwardsByDstmemberIds(forwards, new ArrayListWrapper<Long>().add(chairmanMember.getId()).getList(), null, ExecuteStatus.DONE);
			if(playingForwards.size() == 0){
				throw new BaseException(StatusCode.FORBIDDEN, "您没有观看任何成员");
			}
			
			List<CommandGroupUserPlayerPO> playerPOs = new ArrayList<CommandGroupUserPlayerPO>();
			
			//换源的个数
			int count = playingForwards.size();
			if(playingForwards.size() > noPlayerForwards.size()){
				count = noPlayerForwards.size();
			}			
			
			//选择被替换的forward
			List<CommandGroupForwardPO> oldForwards = playingForwards.subList(0, count);
			
			//选择新的forward
			List<CommandGroupForwardPO> newForwards = chooseForwardsAfterId(oldForwards, noPlayerForwards, count);
			
			for(int i=0; i<count; i++){
				CommandGroupForwardPO oldForward = oldForwards.get(i);
				CommandGroupForwardPO newForward = newForwards.get(i);
				CommandGroupMemberPO newSrcMember = commandCommonUtil.queryMemberById(members, newForward.getSrcMemberId());
				
				CommandGroupUserPlayerPO player = commandCommonUtil.queryPlayerByForwardAndDstMember(oldForward, chairmanMember);
				player.setBusinessName(group.getName() + "：" + newSrcMember.getUserName());//添加成员名称
				player.setBusinessId(group.getId().toString() + "-" + newSrcMember.getUserId());
				player.setPlayerBusinessType(PlayerBusinessType.CHAIRMAN_BASIC_COMMAND);
				playerPOs.add(player);
				
				oldForward.clearDst();
				oldForward.setExecuteStatus(ExecuteStatus.NO_AVAILABLE_PLAYER);
				newForward.setDstPlayer(player);
				newForward.setExecuteStatus(ExecuteStatus.UNDONE);//写成UNDONE，以便进行下一步过滤
			}
			
			//过滤出可以执行的
			Set<CommandGroupForwardPO> allNeedForwards = commandCommonUtil.queryForwardsReadyAndCanBeDone(members, newForwards);
			for(CommandGroupForwardPO allNeedForward : allNeedForwards){
				allNeedForward.setExecuteStatus(ExecuteStatus.DONE);
			}			
			commandGroupDao.save(group);
			
			//生成connectBundle，携带转发信息
			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
			LogicBO logic = commandBasicServiceImpl.openBundle(null, null, null, allNeedForwards, null, codec, group.getUserId());
			LogicBO logicCastDevice = commandCastServiceImpl.openBundleCastDevice(null, null, allNeedForwards, null, null, null, codec, group.getUserId());
			logic.merge(logicCastDevice);
			
			executeBusiness.execute(logic, group.getName() + " 主席刷新观看成员");
			
			return playerPOs;
			
		}
	}
	
	private List<CommandGroupForwardPO> chooseForwardsAfterId(List<CommandGroupForwardPO> playingForwards, List<CommandGroupForwardPO> forwards, int count){
		
		//排序
		Collections.sort(forwards, new CommandGroupForwardPO.ForwardComparatorFromId());
		Collections.sort(playingForwards, new CommandGroupForwardPO.ForwardComparatorFromId());
		
		//选最大的id作为基准
		Long id = playingForwards.get(playingForwards.size() - 1).getId();
		List<CommandGroupForwardPO> result = new ArrayList<CommandGroupForwardPO>();
		if(count <= 0) return result;
		int size = forwards.size();
		
		for(int i=0; i<size; i++){			
			if(forwards.get(i).getId() > id){
				if(size - i >= count){
					return forwards.subList(i, i+count);
				}else{
					result.addAll(forwards.subList(i, size));
					result.addAll(forwards.subList(0, count-size+i));
					return result;
				}
			}
		}
		//没有找到比id更大的
		return forwards.subList(0, count);
	}
}
