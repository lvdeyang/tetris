package com.sumavision.tetris.bvc.business.special.authorize;

import java.util.ArrayList;
import java.util.List;
import com.sumavision.bvc.control.device.group.controller.GroupMemberRoleService;
import com.sumavision.tetris.bvc.business.dao.*;
import com.sumavision.tetris.bvc.business.deliver.DeliverExecuteService;
import com.sumavision.tetris.bvc.business.group.*;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandAuthorizePO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandInfoDAO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandInfoPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.special.GroupSpecialCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
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
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.bvc.model.agenda.AgendaExecuteService;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RoleExecuteService;
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
 * 授权指挥业务<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年3月4日 下午3:18:15
 */
@Slf4j
@Service
public class GroupAuthorizeService {

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
	public void startU(Long groupId, Long userId, Long acceptUserId, Long cmdUserId) throws Exception{
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			List<Long> acceptMemberIds = businessCommonService.fromUserIdsToMemberIds(groupId, new ArrayListWrapper<Long>().add(acceptUserId).getList());
			List<Long> cmdMemberIds = businessCommonService.fromUserIdsToMemberIds(groupId, new ArrayListWrapper<Long>().add(cmdUserId).getList());
			start(groupId, userId, acceptMemberIds.get(0), cmdMemberIds.get(0));
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void startM(Long groupId, Long userId, Long acceptMemberId, Long cmdMemberId) throws Exception{
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			start(groupId, userId, acceptMemberId, cmdMemberId);
		}
	}
	
	/**
	 * 开始授权指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月4日 下午4:05:54
	 * @param groupId
	 * @param userId 操作人
	 * @param opMemberId
	 * @param targetMemberId
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void start(Long groupId, Long userId, Long acceptMemberId, Long cmdMemberId) throws Exception{

		UserVO user = userQuery.current();

		if(groupId==null || groupId.equals("")){
			log.info("开始接替指挥，会议id有误");
			return;
		}

		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			
			if(acceptMemberId.equals(cmdMemberId)){
				throw new BaseException(StatusCode.FORBIDDEN, "请选择其它成员");
			}

			GroupPO group = groupDao.findOne(groupId);
			BusinessType groupType = group.getBusinessType();
			String commandString = commandCommonUtil.generateCommandString(groupType);
			if(!groupType.equals(BusinessType.COMMAND)){
				throw new BaseException(StatusCode.FORBIDDEN, "只能在指挥中进行授权");
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
			BusinessGroupMemberPO acceptMember = tetrisBvcQueryUtil.queryMemberById(members, acceptMemberId);
			BusinessGroupMemberPO cmdMember = tetrisBvcQueryUtil.queryMemberById(members, cmdMemberId);
			
			//校验成员：上下级，进会
			if(acceptMember.getLevel().equals(cmdMember.getLevel())){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, "请选择其它级别的成员");
				}else{
					log.warn(group.getName() + " 授权指挥参数有误，请选择其它级别的成员");
					return;
				}
			}
			if(!acceptMember.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
				throw new BaseException(StatusCode.FORBIDDEN, acceptMember.getName() + " 未进入指挥");
			}
			if(!cmdMember.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
				throw new BaseException(StatusCode.FORBIDDEN, cmdMember.getName() + " 未进入指挥");
			}
			
			//生成PO
			GroupCommandAuthorizePO authorize = new GroupCommandAuthorizePO(groupId);
			authorize.setAcceptMemberCode(acceptMember.getCode());
			authorize.setAcceptMemberId(acceptMember.getId());
			authorize.setAcceptMemberLevel(acceptMember.getLevel());
			authorize.setAcceptMemberOriginRoleId(acceptMember.getRoleId());
			authorize.setCmdMemberCode(cmdMember.getCode());
			authorize.setCmdMemberId(cmdMember.getId());
			authorize.setCmdMemberLevel(cmdMember.getLevel());
			authorize.setCommandInfo(info);
			info.setHasAuthorize(true);
			if(info.getAuthorizes() == null) info.setAuthorizes(new ArrayList<GroupCommandAuthorizePO>());
			info.getAuthorizes().add(authorize);
			groupCommandInfoDao.save(info);
			
			
			//交换角色
			groupSpecialCommonService.exchangeRole(cmdMember, acceptMember, false);
			
			//重新执行议程
			BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setUserId(group.getUserId().toString()).setGroup(group);
			agendaExecuteService.reRunAgenda(groupId, businessDeliverBO, false);

            //websocket通知
            JSONObject message = new JSONObject();
            message.put("businessType", "cooperationAgree");
            message.put("businessId", group.getId().toString());
            message.put("businessInfo", group.getName() + " 主席对 " + acceptMember.getName() + " 授权指挥");
            message.put("splits", new JSONArray());
            List<Long> notifyMemberIds = businessCommonService.obtainMemberIds(groupId, true, false);
            businessCommonService.notifyMemberInfo(notifyMemberIds, message.toJSONString(), WebsocketMessageType.COMMAND);

			//级联
			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(BusinessType.COMMAND.equals(group.getBusinessType())){
					GroupBO groupBO = groupCascadeUtil.authorizeGroupBO(group, acceptMember, cmdMember);
					commandCascadeService.startAuthorize(groupBO);
				}
			}
			
//			operationLogService.send(user.getNickname(), "开启协同指挥", user.getNickname() + "开启协同指挥，groupId:" + groupId + "，memberIds:" + memberIds.toString());
			
			//执行
			deliverExecuteService.execute(businessDeliverBO, group.getName() + " 对 " + acceptMember.getName() + " 授权指挥", true);
		}
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void stop(Long groupId) throws Exception{
		UserVO user = userQuery.current();

		if(groupId==null || groupId.equals("")){
			log.info("停止授权指挥，会议id有误");
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
				throw new BaseException(StatusCode.FORBIDDEN, "只能在指挥中进行授权");
			}
			
			GroupCommandInfoPO info = groupCommandInfoDao.findByGroupId(groupId);
			if(info == null || !info.isHasAuthorize()){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 中没有授权指挥");
				}else{
					return;
				}
			}
			
			//处理授权指挥PO，正常有且仅有1个
			List<BusinessGroupMemberPO> members = group.getMembers();
			List<GroupCommandAuthorizePO> authorizes = info.getAuthorizes();
			for(GroupCommandAuthorizePO authorize : authorizes){
				
				Long acceptMemberId = authorize.getAcceptMemberId();
				Long cmdMemberId = authorize.getCmdMemberId();				

				BusinessGroupMemberPO acceptMember = tetrisBvcQueryUtil.queryMemberById(members, acceptMemberId);
				BusinessGroupMemberPO cmdMember = tetrisBvcQueryUtil.queryMemberById(members, cmdMemberId);
								
				//恢复角色
				GroupMemberRolePermissionPO acceptMemberPer = groupMemberRolePermissionDao.findByGroupMemberId(acceptMember.getId());
				acceptMemberPer.setRoleId(authorize.getAcceptMemberOriginRoleId());
				acceptMember.setRoleId(authorize.getAcceptMemberOriginRoleId());
				groupMemberRolePermissionDao.save(acceptMemberPer);
				businessGroupMemberDAO.save(acceptMember);
				
				//websocket通知
				JSONObject message = new JSONObject();
	            message.put("businessType", "cooperationRevoke");
	            message.put("businessId", group.getId().toString());
	            message.put("businessInfo", group.getName() + " 对 " + acceptMember.getName() + " 的授权指挥已经取消");
	            message.put("splits", new JSONArray());
	            List<Long> notifyMemberIds = businessCommonService.obtainMemberIds(groupId, true, false);
	            businessCommonService.notifyMemberInfo(notifyMemberIds, message.toJSONString(), WebsocketMessageType.COMMAND);
	            
	            //级联
				if(!OriginType.OUTER.equals(group.getOriginType())){
					if(BusinessType.COMMAND.equals(group.getBusinessType())){
						GroupBO groupBO = groupCascadeUtil.authorizeGroupBO(group, acceptMember, cmdMember);
						commandCascadeService.stopAuthorize(groupBO);
					}
				}
			}
			
			info.setHasAuthorize(false);
			info.getAuthorizes().clear();
			groupCommandInfoDao.save(info);
						
			//重新执行议程
			BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setUserId(group.getUserId().toString()).setGroup(group);
			agendaExecuteService.reRunAgenda(groupId, businessDeliverBO, false);
			
//			operationLogService.send(user.getNickname(), "撤销协同指挥", user.getNickname() + "撤销协同指挥groupId:" + groupId + ", memberIds:" + memberIds.toString());
			
			//执行
			deliverExecuteService.execute(businessDeliverBO, group.getName() + " 的授权指挥取消", true);
			
		}
	}


}
