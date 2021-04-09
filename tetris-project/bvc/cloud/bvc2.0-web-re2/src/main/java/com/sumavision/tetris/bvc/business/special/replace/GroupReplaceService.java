package com.sumavision.tetris.bvc.business.special.replace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sumavision.bvc.control.device.group.controller.GroupMemberRoleService;
import com.sumavision.tetris.bvc.business.dao.*;
import com.sumavision.tetris.bvc.business.deliver.DeliverExecuteService;
import com.sumavision.tetris.bvc.business.group.*;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandInfoDAO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandInfoPO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandReplacePO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.special.GroupSpecialCommonService;
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
import com.sumavision.bvc.device.command.cascade.util.GroupCascadeUtil;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.record.CommandRecordServiceImpl;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.log.OperationLogService;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.bo.BusinessDeliverBO;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.model.agenda.AgendaExecuteService;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RoleExecuteService;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;

import lombok.extern.slf4j.Slf4j;

/**
 * 接替指挥业务<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年3月1日 下午3:18:15
 */
@Slf4j
@Service
public class GroupReplaceService {

	/** synchronized锁的前缀 */
	private static final String lockProcessPrefix = "tetris-group-";

	@Autowired
	private BusinessReturnService businessReturnService;

	@Autowired
	private AgendaExecuteService agendaExecuteService;

	@Autowired
	private DeliverExecuteService deliverExecuteService;

	@Autowired
	private GroupSpecialCommonService groupSpecialCommonService;

	@Autowired
	private PageInfoDAO pageInfoDao;

	@Autowired
	private PageTaskDAO pageTaskDao;

	@Autowired
	private CommonForwardDAO commonForwardDao;

	@Autowired
	private GroupDAO groupDao;

	@Autowired
	private GroupCommandInfoDAO groupCommandInfoDao;

	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDAO;

	@Autowired
	private GroupMemberRolePermissionDAO groupMemberRolePermissionDao;

	@Autowired
	private CommandGroupDAO commandGroupDao;

	@Autowired
	private RoleExecuteService roleExecuteService;

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
	private GroupCascadeUtil groupCascadeUtil;

	@Autowired
	private CommandCascadeService commandCascadeService;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
    private GroupMemberRoleService groupMemberRoleService;


	@Transactional(rollbackFor = Exception.class)
	public void startU(Long groupId, Long opUserId, Long targetUserId) throws Exception{
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			List<Long> opMemberIds = businessCommonService.fromUserIdsToMemberIds(groupId, new ArrayListWrapper<Long>().add(opUserId).getList());
			List<Long> targetMemberIds = businessCommonService.fromUserIdsToMemberIds(groupId, new ArrayListWrapper<Long>().add(targetUserId).getList());
			start(groupId, opMemberIds.get(0), targetMemberIds.get(0));
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void startM(Long groupId, Long opMemberId, Long targetMemberId) throws Exception{
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			start(groupId, opMemberId, targetMemberId);
		}
	}
	
	/**
	 * 开始接替指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月1日 下午4:05:54
	 * @param groupId
	 * @param opMemberId
	 * @param targetMemberId
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void start(Long groupId, Long opMemberId, Long targetMemberId) throws Exception{

		UserVO user = userQuery.current();

		if(groupId==null || groupId.equals("")){
			log.info("开始接替指挥，会议id有误");
			return;
		}

		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			
			if(opMemberId.equals(targetMemberId)){
				throw new BaseException(StatusCode.FORBIDDEN, "请选择2个不同的成员");
			}

			GroupPO group = groupDao.findOne(groupId);
			BusinessType groupType = group.getBusinessType();
			String commandString = commandCommonUtil.generateCommandString(groupType);
			if(!groupType.equals(BusinessType.COMMAND)){
				throw new BaseException(StatusCode.FORBIDDEN, "只能在指挥中进行接替");
			}

			if(group.getStatus().equals(GroupStatus.STOP)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
				}else{
					return;
				}
			}
			
			
			GroupCommandInfoPO info = groupCommandInfoDao.findByGroupId(groupId);
			if(info == null){
				info = new GroupCommandInfoPO(groupId);
			}
			
			//校验是否已有特殊指挥
			groupSpecialCommonService.checkHasSpecialCommand(info, true);
			
			List<BusinessGroupMemberPO> members = group.getMembers();
			BusinessGroupMemberPO opMember = tetrisBvcQueryUtil.queryMemberById(members, opMemberId);
			BusinessGroupMemberPO targetMember = tetrisBvcQueryUtil.queryMemberById(members, targetMemberId);
			
			//校验成员：上下级，进会
			if(opMember.getLevel() >= targetMember.getLevel()){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, "请选择高级成员接替低级成员");
				}else{
					return;
				}
			}
			if(!opMember.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
				throw new BaseException(StatusCode.FORBIDDEN, opMember.getName() + " 未进入指挥");
			}
			if(!targetMember.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
				throw new BaseException(StatusCode.FORBIDDEN, targetMember.getName() + " 未进入指挥");
			}
			
			//生成PO
			GroupCommandReplacePO replace = new GroupCommandReplacePO(groupId);
			replace.setOpMemberCode(opMember.getCode());
			replace.setOpMemberId(opMemberId);
			replace.setOpMemberLevel(opMember.getLevel());
			replace.setOpMemberOriginRoleId(opMember.getRoleId());
			replace.setTargetMemberCode(targetMember.getCode());
			replace.setTargetMemberId(targetMemberId);
			replace.setTargetMemberLevel(targetMember.getLevel());
			replace.setTargetMemberOriginRoleId(targetMember.getRoleId());
			replace.setCommandInfo(info);
			info.setHasReplace(true);
			if(info.getReplaces() == null) info.setReplaces(new ArrayList<GroupCommandReplacePO>());
			info.getReplaces().add(replace);
			groupCommandInfoDao.save(info);
						
			//交换角色
			groupSpecialCommonService.exchangeRole(targetMember, opMember, false);
			
			//重新执行议程
			BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setUserId(group.getUserId().toString()).setGroup(group);
			agendaExecuteService.reRunAgenda(groupId, businessDeliverBO, false);

            //websocket通知
            JSONObject message = new JSONObject();
            message.put("businessType", "cooperationAgree");
            message.put("businessId", group.getId().toString());
            message.put("businessInfo", group.getName() + " 主席授权 " + opMember.getName() + " 对 " + targetMember.getName() + " 接替指挥");
            message.put("splits", new JSONArray());
            List<Long> notifyMemberIds = businessCommonService.obtainMemberIds(groupId, true, true);
            businessCommonService.notifyMemberInfo(notifyMemberIds, message.toJSONString(), WebsocketMessageType.COMMAND);

			//级联
			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(BusinessType.COMMAND.equals(group.getBusinessType())){
					GroupBO groupBO = groupCascadeUtil.replaceGroupBO(group, opMember.getCode(), targetMember.getCode());
					commandCascadeService.startReplace(groupBO);
				}
			}
			
//			operationLogService.send(user.getNickname(), "开启协同指挥", user.getNickname() + "开启协同指挥，groupId:" + groupId + "，memberIds:" + memberIds.toString());
			
			//执行
			deliverExecuteService.execute(businessDeliverBO, group.getName() + opMember.getName() + " 对 " + targetMember.getName() + " 接替指挥", true);
		}
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void stop(Long groupId) throws Exception{
		UserVO user = userQuery.current();

		if(groupId==null || groupId.equals("")){
			log.info("停止接替指挥，会议id有误");
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
				throw new BaseException(StatusCode.FORBIDDEN, "只能在指挥中进行接替");
			}
			
			GroupCommandInfoPO info = groupCommandInfoDao.findByGroupId(groupId);
			if(info == null || !info.isHasReplace()){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 中没有接替指挥");
				}else{
					return;
				}
			}
			
			//处理接替指挥PO，正常有且仅有1个
			List<BusinessGroupMemberPO> members = group.getMembers();
			List<GroupCommandReplacePO> replaces = info.getReplaces();
			for(GroupCommandReplacePO replace : replaces){
				
				Long opMemberId = replace.getOpMemberId();
				Long targetMemberId = replace.getTargetMemberId();				

				BusinessGroupMemberPO opMember = tetrisBvcQueryUtil.queryMemberById(members, opMemberId);
				BusinessGroupMemberPO targetMember = tetrisBvcQueryUtil.queryMemberById(members, targetMemberId);
								
				//交换角色
//				groupSpecialCommonService.exchangeRole(opMember, targetMember, true);
				
				//恢复角色opMemberPer
				GroupMemberRolePermissionPO opMemberPer = groupMemberRolePermissionDao.findByGroupMemberId(opMember.getId());
				opMemberPer.setRoleId(replace.getOpMemberOriginRoleId());
				opMember.setRoleId(replace.getOpMemberOriginRoleId());
				groupMemberRolePermissionDao.save(opMemberPer);
				businessGroupMemberDAO.save(opMember);
				
				//恢复角色targetMember（暂时不用）
				/*GroupMemberRolePermissionPO targetMemberPer = groupMemberRolePermissionDao.findByGroupMemberId(targetMember.getId());
				targetMemberPer.setRoleId(replace.getTargetMemberOriginRoleId());
				targetMember.setRoleId(replace.getTargetMemberOriginRoleId());
				groupMemberRolePermissionDao.save(targetMemberPer);
				businessGroupMemberDAO.save(targetMember);*/
				
				//websocket通知
				JSONObject message = new JSONObject();
	            message.put("businessType", "cooperationRevoke");
	            message.put("businessId", group.getId().toString());
	            message.put("businessInfo", group.getName() + opMember.getName() + " 对 " + targetMember.getName() + " 的接替指挥已经取消");
	            message.put("splits", new JSONArray());
	            List<Long> notifyMemberIds = businessCommonService.obtainMemberIds(groupId, true, true);
	            businessCommonService.notifyMemberInfo(notifyMemberIds, message.toJSONString(), WebsocketMessageType.COMMAND);
	            
	            //级联
				if(!OriginType.OUTER.equals(group.getOriginType())){
					if(BusinessType.COMMAND.equals(group.getBusinessType())){
						GroupBO groupBO = groupCascadeUtil.replaceGroupBO(group, opMember.getCode(), targetMember.getCode());
						commandCascadeService.stopReplace(groupBO);
					}
				}
			}
			
			info.setHasReplace(false);
			info.getReplaces().clear();
			groupCommandInfoDao.save(info);
						
			//重新执行议程
			BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setUserId(group.getUserId().toString()).setGroup(group);
			agendaExecuteService.reRunAgenda(groupId, businessDeliverBO, false);
			
//			operationLogService.send(user.getNickname(), "撤销协同指挥", user.getNickname() + "撤销协同指挥groupId:" + groupId + ", memberIds:" + memberIds.toString());
			
			//执行
			deliverExecuteService.execute(businessDeliverBO, group.getName() + " 的接替指挥取消", true);
			
		}
	}


}
