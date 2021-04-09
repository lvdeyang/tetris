package com.sumavision.tetris.bvc.business.group.cooperate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sumavision.bvc.control.device.group.controller.GroupMemberRoleService;
import com.sumavision.tetris.bvc.business.dao.*;
import com.sumavision.tetris.bvc.business.group.*;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.cascade.util.CommandCascadeUtil;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.record.CommandRecordServiceImpl;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.log.OperationLogService;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;

import lombok.extern.slf4j.Slf4j;

/**
 *
* @ClassName: GroupCooperateService
* @Description: 协同会议业务
* @author zsy
* @date 2019年10月24日 上午10:56:48
*
 */
@Slf4j
@Service
public class CommandGroupCooperateService {

	/** synchronized锁的前缀 */
	private static final String lockProcessPrefix = "tetris-group-";

	@Autowired
	private BusinessReturnService businessReturnService;

	@Autowired
	private PageInfoDAO pageInfoDao;

	@Autowired
	private PageTaskDAO pageTaskDao;

	@Autowired
	private CommonForwardDAO commonForwardDao;

	@Autowired
	private GroupDAO groupDao;

	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDAO;

	@Autowired
	private GroupMemberRolePermissionDAO groupMemberRolePermissionDao;

	@Autowired
	private CommandGroupDAO commandGroupDao;

	@Autowired
	private CommandGroupUserPlayerDAO commandGroupUserPlayerDao;

	//@Autowired
	//private AgendaExecuteService agendaExecuteService;

	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;

	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;

	@Autowired
	private CommandRecordServiceImpl commandRecordServiceImpl;

	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;

	@Autowired
	private WebsocketMessageService websocketMessageService;

	@Autowired
	private BusinessCommonService businessCommonService;

	@Autowired
	private CommandCommonUtil commandCommonUtil;

	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;

	@Autowired
	private ExecuteBusinessProxy executeBusiness;

	@Autowired
	private OperationLogService operationLogService;

	@Autowired
	private UserQuery userQuery;

	@Autowired
	private CommandCascadeUtil commandCascadeUtil;

	@Autowired
	private CommandCascadeService commandCascadeService;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
    private GroupMemberRoleService groupMemberRoleService;


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
	 * 重构，发起协同会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 上午9:08:02
	 * @param groupId
	 * @param memberIds 协同成员id列表
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public Object start(Long groupId, List<Long> memberIds) throws Exception{

		UserVO user = userQuery.current();
		JSONArray chairSplits = new JSONArray();

		if(groupId==null || groupId.equals("")){
			log.info("开始协同指挥，会议id有误");
			return chairSplits;
		}

		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {

			GroupPO group = groupDao.findOne(groupId);
			BusinessType groupType = group.getBusinessType();
			String commandString = commandCommonUtil.generateCommandString(groupType);
			if(!groupType.equals(BusinessType.COMMAND)){
				throw new BaseException(StatusCode.FORBIDDEN, "只能在指挥中进行协同");
			}

			if(group.getStatus().equals(GroupStatus.STOP)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
				}else{
					return chairSplits;
				}
			}

			//groupMember中查找
			List<BusinessGroupMemberPO> members = businessGroupMemberDAO.findByIdIn(memberIds);
			BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
			if(chairmanMember != null){
				if(memberIds.contains(chairmanMember.getId())){
					throw new BaseException(StatusCode.FORBIDDEN, "不能选择主席进行协同");
				}
			}

			RolePO cooperateRole = roleDAO.findByInternalRoleType(InternalRoleType.COMMAND_COOPERATION);
			List<BusinessGroupMemberPO> cooperateMembers = new ArrayList<BusinessGroupMemberPO>();//统计本次新增的协同成员
			List<GroupMemberRolePermissionPO> ps = groupMemberRolePermissionDao.findByRoleIdAndGroupMemberIdIn(cooperateRole.getId(), memberIds);
			for(BusinessGroupMemberPO member : members){
				if(memberIds.contains(member.getId())){
					//通过GroupMemberRolePermissionPO查询该成员是否已经是协同成员
					GroupMemberRolePermissionPO p = tetrisBvcQueryUtil.queryGroupMemberRolePermissionByGroupMemberId(ps, member.getId());
					if(p == null){
                        cooperateMembers.add(member);
					}else{
						throw new BaseException(StatusCode.FORBIDDEN, member.getName() + " 已经被授权协同");
					}

				}
			}

            //发送websocket通知
            JSONObject message = new JSONObject();
            message.put("businessType", "cooperationAgree");
            message.put("businessId", group.getId().toString());
            List<String> names = businessCommonService.obtainMemberNames(cooperateMembers);
            message.put("businessInfo", group.getName() + " 主席授权 " + StringUtils.join(names.toArray(), ",") + " 协同" + commandString);
            message.put("splits", new JSONArray());
            List<Long> notifyMemberIds = businessCommonService.obtainCooperateMemberIds(groupId, cooperateRole.getId(),true);
            businessCommonService.notifyMemberInfo(notifyMemberIds, message.toJSONString(), WebsocketMessageType.COMMAND);

			List<Long> cooperateMemberIds=cooperateMembers.stream().map(BusinessGroupMemberPO::getId).collect(Collectors.toList());
			groupMemberRoleService.exchangeMemberRole(groupId,cooperateMemberIds,cooperateRole.getId());


			//级联

			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(BusinessType.COMMAND.equals(group.getBusinessType())){
					GroupBO groupBO = commandCascadeUtil.startAgendaCooperation(group, cooperateMembers);
					commandCascadeService.startCooperation(groupBO);
				}
			}

			/*if(businessReturnService.getSegmentedExecute()){
				businessReturnService.execute();
			}*/

			operationLogService.send(user.getNickname(), "开启协同指挥", user.getNickname() + "开启协同指挥，groupId:" + groupId + "，memberIds:" + memberIds.toString());
			return chairSplits;
		}
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
	 * 重构，批量撤销授权协同会议<br/>
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
			log.info("停止协同指挥，会议id有误");
			return;
		}


		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {

			GroupPO group = groupDao.findOne(groupId);

			if(GroupStatus.STOP.equals(group.getStatus())){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已经停止。id: " + group.getId());
				}else{
					return;
				}
			}
			BusinessType groupType = group.getBusinessType();
			if(!groupType.equals(BusinessType.COMMAND)){
				throw new BaseException(StatusCode.FORBIDDEN, "只能在指挥中进行协同");
			}

			String commandString = commandCommonUtil.generateCommandString(groupType);
			List<BusinessGroupMemberPO> members = businessGroupMemberDAO.findByGroupId(groupId);

			RolePO cooperateRole = roleDAO.findByInternalRoleType(InternalRoleType.COMMAND_COOPERATION);
			List<BusinessGroupMemberPO> revokeMembers = new ArrayList<BusinessGroupMemberPO>();//统计本次解除的协同成员
			List<GroupMemberRolePermissionPO> ps = groupMemberRolePermissionDao.findByRoleIdAndGroupMemberIdIn(cooperateRole.getId(), memberIds);
			for(BusinessGroupMemberPO member : members){
				if(memberIds.contains(member.getId())){
					//通过GroupMemberRolePermissionPO查询该成员是否已经是协同成员
					GroupMemberRolePermissionPO p = tetrisBvcQueryUtil.queryGroupMemberRolePermissionByGroupMemberId(ps, member.getId());
					if(p != null){
						revokeMembers.add(member);
					}else{
//						throw new BaseException(StatusCode.FORBIDDEN, member.getName() + " 没有被授权协同");
					}

				}
			}

            //发送websocket通知
            JSONObject message = new JSONObject();
            message.put("businessType", "cooperationRevoke");
            message.put("businessId", group.getId().toString());
            List<String> names = businessCommonService.obtainMemberNames(revokeMembers);
            message.put("businessInfo", group.getName() + " 主席撤销 " + StringUtils.join(names.toArray(), ",") + " 协同" + commandString);
            message.put("splits", new JSONArray());
            List<Long> notifyMemberIds = businessCommonService.obtainCooperateMemberIds(groupId,cooperateRole.getId(), true);
            businessCommonService.notifyMemberInfo(notifyMemberIds, message.toJSONString(), WebsocketMessageType.COMMAND);

			Long commandAudienceId=roleDAO.findByInternalRoleType(InternalRoleType.COMMAND_AUDIENCE).getId();
            List<Long> revokeMemberIds=revokeMembers.stream().map(BusinessGroupMemberPO::getId).collect(Collectors.toList());
            groupMemberRoleService.exchangeMemberRole(groupId,revokeMemberIds,commandAudienceId);

			/*
			//录制更新
			LogicBO logicRecord = commandRecordServiceImpl.update(group.getUserId(), group, 1, false);
			logic.merge(logicRecord);
			ExecuteBusinessReturnBO returnBO = executeBusiness.execute(logic, revokeMembersNames.toString());
			commandRecordServiceImpl.saveStoreInfo(returnBO, group.getId());
            */
			//级联
			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(BusinessType.COMMAND.equals(group.getBusinessType())){
					GroupBO groupBO = commandCascadeUtil.stopAgendaCooperation(group, revokeMembers);
					commandCascadeService.stopCooperation(groupBO);
				}
			}

			operationLogService.send(user.getNickname(), "撤销协同指挥", user.getNickname() + "撤销协同指挥groupId:" + groupId + ", memberIds:" + memberIds.toString());
		}
	}


}
