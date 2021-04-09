package com.sumavision.tetris.bvc.business.special.cross;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.cascade.util.GroupCascadeUtil;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.group.BusinessType;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupStatus;
import com.sumavision.tetris.bvc.business.group.RunningAgendaPO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandCooperatePO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandCrossPO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandInfoDAO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandInfoPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.special.GroupSpecialCommonService;
import com.sumavision.tetris.bvc.business.special.GroupSpecialCommonService.MemberForward;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.bvc.model.agenda.AgendaExecuteService;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardPO;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.mvc.util.BaseUtils;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GroupCrossService {
	
	@Autowired
	private BusinessCommonService businessCommonService;
	
	@Autowired
	private BusinessReturnService businessReturnService;
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private GroupCascadeUtil groupCascadeUtil;
	
	@Autowired
	private CommandCascadeService commandCascadeService;
	
	@Autowired
	private GroupSpecialCommonService groupSpecialCommonService;
	
	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private RunningAgendaDAO runningAgendaDao;
	
	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;
	
	@Autowired
	private AgendaExecuteService agendaExecuteService;
	
	@Autowired
	private GroupCommandInfoDAO groupCommandInfoDao;
	
	@Autowired
	private GroupDAO groupDao;

	/** synchronized锁的前缀 */
	private static final String lockProcessPrefix = "tetris-group-";

	@Transactional(rollbackFor = Exception.class)
	public void startU(Long groupId, List<Long> userIds) throws Exception{
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			List<Long> memberIds = businessCommonService.fromUserIdsToMemberIds(groupId, userIds);
			start(groupId,memberIds);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void startM(Long groupId, List<Long> memberIds) throws Exception{
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			start(groupId,memberIds);
		}
	}
	
	/**
	 * 发起越级指挥(目前只支持一个会议中包含一个，扩展参考协同指挥)<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 上午9:08:02
	 * @param groupId 会议组
	 * @param memberIds 协同成员businessGroupMember的id列表
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public Object start(Long groupId, List<Long> memberIds) throws Exception{
		
		UserVO user = userQuery.current();
		JSONArray chairSplits = new JSONArray();

		if(groupId==null || groupId.equals("")){
			log.info("开始越级指挥，会议id有误");
			return chairSplits;
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			GroupPO group = groupDao.findOne(groupId);
			if(group.getStatus().equals(GroupStatus.STOP)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
				}else{
					return chairSplits;
				}
			}
			
			//校验是否已有特殊指挥
			GroupCommandInfoPO info = groupCommandInfoDao.findByGroupId(groupId);
			groupSpecialCommonService.checkHasSpecialCommand(info, true);
			
			BusinessType groupType = group.getBusinessType();
			if(!groupType.equals(BusinessType.COMMAND)){
				throw new BaseException(StatusCode.FORBIDDEN, "只能在指挥中进行越级指挥");
			}
			
			//groupMember中查找
			List<BusinessGroupMemberPO> members = businessGroupMemberDao.findByIdIn(memberIds);
			if(members.size() != 2){
				throw new BaseException(StatusCode.FORBIDDEN, "只能有两人越级指挥");
			}
			
			//校验级别
			if(Math.abs(members.get(0).getLevel()-members.get(1).getLevel()) < 2){
				throw new BaseException(StatusCode.FORBIDDEN, "越级指挥需要两个成员至少相差两级");
			}
			
			//生成转发源-转发目的对应关系
			List<MemberForward> memberForwards = new ArrayList<MemberForward>();
			for(BusinessGroupMemberPO srcMember : members){
				for (BusinessGroupMemberPO dstMember : members) {
					if(srcMember != dstMember){
						MemberForward memberForward = new GroupSpecialCommonService.MemberForward();
						memberForward.setSrcMember(srcMember);
						memberForward.setDstMember(dstMember);
						memberForwards.add(memberForward);
					}
				}
			}
			
			//生成转发、业务组额外信息存储
			if(info != null){
				if(info.isHasCross()){
					throw new BaseException(StatusCode.FORBIDDEN, "同一个会议目前只支持一个越级指挥");
				}
				info.setHasCross(true);
			}else{
				info = new GroupCommandInfoPO();
				info.setGroupId(groupId);
				info.setHasCross(true);
			}
			List<AgendaForwardPO> agendaForwardList = groupSpecialCommonService.createGroupForward(groupId, memberForwards);
			agendaForwardList.forEach(forward -> forward.setAgendaForwardBusinessType(BusinessInfoType.CROSS_COMMAND));
			agendaForwardDao.save(agendaForwardList);
			List<String> forwardIds = agendaForwardList.stream().map(AgendaForwardPO::getId).map(String::valueOf).collect(Collectors.toList());
			if(BaseUtils.stringIsNotBlank(info.getCrossAgendaForwardIds())){
				String crossforwardIds = info.getCrossAgendaForwardIds()+","+String.join(",", forwardIds);
				info.setCrossAgendaForwardIds(crossforwardIds);
			}else{
				info.setCrossAgendaForwardIds(String.join(",", forwardIds));
			}
			
			List<GroupCommandCrossPO> crossPos = new ArrayList<GroupCommandCrossPO>();
			for (BusinessGroupMemberPO member : members) {
				GroupCommandCrossPO cross = new GroupCommandCrossPO();
				cross.setCommandInfo(info);
				cross.setGroupId(groupId);
				cross.setMemberCode(member.getCode());
				cross.setMemberId(member.getId());
				cross.setMemberLevel(member.getLevel());
				crossPos.add(cross);
			}
			info.overWriteCross(crossPos);
			
			groupCommandInfoDao.save(info);
			
			 //发送websocket通知
            JSONObject message = new JSONObject();
            message.put("businessType", "cooperationAgree");
            message.put("businessId", group.getId().toString());
            List<String> names = businessCommonService.obtainMemberNames(members);
            message.put("businessInfo", group.getName() + " 主席授权 " + StringUtils.join(names.toArray(), ",") + " 越级指挥");
            message.put("splits", new JSONArray());
            for(BusinessGroupMemberPO member : members){
				if(!member.getGroupMemberType().equals(GroupMemberType.MEMBER_USER)){
					continue;
				}else{
					MessageSendCacheBO sendCache = new MessageSendCacheBO(Long.parseLong(member.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND);
					businessReturnService.add(null, sendCache, null);
				}
			}

			//级联
			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(BusinessType.COMMAND.equals(group.getBusinessType())){
					GroupBO groupBO = groupCascadeUtil.agendaCross(group, members);
					commandCascadeService.startCross(groupBO);
				}
			}
			
			RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(groupId);
			if(runningAgenda != null){
				agendaExecuteService.runAgenda(groupId, runningAgenda.getAgendaId(), null, true);
			}else {
				throw new BaseException(StatusCode.FORBIDDEN, "没有正在执行的议程转发");
			}
			
		}
		return null;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void revokeBatchU(Long groupId, List<Long> userIds) throws Exception{
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			List<Long> memberIds = businessCommonService.fromUserIdsToMemberIds(groupId, userIds);
			revokeBatch(groupId,memberIds);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void revokeBatchM(Long groupId, List<Long> memberIds) throws Exception{
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			revokeBatch(groupId,memberIds);
		}
	}
	
	/**
	 * 撤销越级指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午4:24:30
	 * @param groupId 组id
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void revokeBatch(Long groupId, List<Long> memberIds) throws Exception{
		
		UserVO user = userQuery.current();

		if(groupId==null || groupId.equals("")){
			log.info("停止越级指挥，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);

			if(group == null){
				throw new BaseException(StatusCode.FORBIDDEN, "没有查到该指挥");
			}
			
			if(GroupStatus.STOP.equals(group.getStatus())){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已经停止。id: " + group.getId());
				}else{
					return;
				}
			}
			BusinessType groupType = group.getBusinessType();
			if(!groupType.equals(BusinessType.COMMAND)){
				throw new BaseException(StatusCode.FORBIDDEN, "只能在指挥中进行越级指挥停止");
			}
			
			//删除数据
			GroupCommandInfoPO info = groupCommandInfoDao.findByHasCrossAndGroupId(true, groupId);
			if(info == null || !info.isHasCross()){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 中没有越级指挥");
				}else{
					return;
				}
			}
			List<Long> allForwardIds = new ArrayList<Long>();
			String deleteAgendaIds = info.getCrossAgendaForwardIds();
			if(BaseUtils.stringIsNotBlank(deleteAgendaIds)){
				allForwardIds = Stream.of(deleteAgendaIds.split(",")).map(Long::valueOf).collect(Collectors.toList());
			}
			List<AgendaForwardPO> agendaforawrds = agendaForwardDao.findAll(allForwardIds);
			groupSpecialCommonService.clearGroupForward(agendaforawrds);
			List<Long> memberIdList = info.getCrosses().stream().map(GroupCommandCrossPO::getMemberId).collect(Collectors.toList());
			info.clearCross();
			groupCommandInfoDao.save(info);
			
			//获取越级指挥所有成员
			List<BusinessGroupMemberPO> members = businessGroupMemberDao.findAll(memberIdList);
			
			//级联
			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(BusinessType.COMMAND.equals(group.getBusinessType())){
					GroupBO groupBO = groupCascadeUtil.agendaCross(group, members);
					commandCascadeService.stopCross(groupBO);
				}
			}
			
			//发送websocket通知
            JSONObject message = new JSONObject();
            message.put("businessType", "cooperationAgree");
            message.put("businessId", group.getId().toString());
            message.put("businessInfo", group.getName() + " 主席撤销越级指挥");
            message.put("splits", new JSONArray());
            for(BusinessGroupMemberPO member : members){
				if(!member.getGroupMemberType().equals(GroupMemberType.MEMBER_USER)){
					continue;
				}else{
					MessageSendCacheBO sendCache = new MessageSendCacheBO(Long.parseLong(member.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND);
					businessReturnService.add(null, sendCache, null);
				}
			}
			
			RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(groupId);
			if(runningAgenda != null){
				agendaExecuteService.runAgenda(groupId, runningAgenda.getAgendaId(), null, true);
			}else {
				throw new BaseException(StatusCode.FORBIDDEN, "没有正在执行的议程转发");
			}
		}
	}
}
