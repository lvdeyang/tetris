package com.sumavision.bvc.control.device.group.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.bvc.business.bo.BusinessDeliverBO;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.deliver.DeliverExecuteService;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.RunningAgendaPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.vo.BusinessGroupMemberVO;
import com.sumavision.tetris.bvc.model.agenda.AgendaExecuteService;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RoleExecuteService;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.role.RoleUserMappingType;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.util.BaseUtils;

@Service
public class GroupMemberRoleService {
	
	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDao;
	
	@Autowired
	private RoleExecuteService roleExecuteService;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private DeliverExecuteService deliverExecuteService;
	
	@Autowired
	private RunningAgendaDAO runningAgendaDao;
	
	@Autowired
	private  AgendaExecuteService agendaExecuteService;

	/**
	 * 通过组id以及角色id查找组内成员列表<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月8日 下午1:46:55
	 * @param groupId 业务组id
	 * @param roleId 角色id
	 * @return
	 */
	public List<BusinessGroupMemberVO> queryMemberByRoleId(
			Long groupId, 
			Long roleId) {
		
		List<BusinessGroupMemberVO> businessGroupMemberVoList = businessGroupMemberDao.findByGroupIdAndRoleId(groupId, roleId).stream()
																					  										  .map(member-> new BusinessGroupMemberVO().set(member))
																					  										  .collect(Collectors.toList());
		return businessGroupMemberVoList;
	}

	/**
	 * 修改成员角色<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月8日 下午1:55:08
	 * @param groupId 业务组id
	 * @param memberId 成员id集合
	 * @param roleId 目标角色id
	 */
	@Transactional(rollbackFor = Exception.class)
	public Map<String, List<BusinessGroupMemberVO>> exchangeMemberRole(
			Long groupId, 
			List<Long> memberIdList, 
			Long roleId) throws Exception {
		
		GroupPO group = groupDao.findOne(groupId);
		
		RolePO role = roleDao.findOne(roleId);
		
		if(group == null || role == null || !BaseUtils.collectionIsNotBlank(memberIdList)){
			throw new BaseException(StatusCode.FORBIDDEN, "业务组或者角色或者成员信息错误");
		}
		
		String roleName = role.getName();
		
		List<BusinessGroupMemberPO> groupMembers = businessGroupMemberDao.findByIdIn(memberIdList);
		
		Map<Long, List<BusinessGroupMemberPO>> roleIdAddMembersMap = new HashMap<Long, List<BusinessGroupMemberPO>>();
		roleIdAddMembersMap.put(roleId, groupMembers);
		
		//roleId对应的角色是1：1，原来如果有成员，将此成员变为观众。
		Optional<RoleUserMappingType> roleType = Optional.ofNullable(roleDao.findOne(roleId)).map(RolePO::getRoleUserMappingType).filter(type -> RoleUserMappingType.ONE_TO_ONE.equals(type));
		BusinessGroupMemberPO oldMember = null;
		if(roleType.isPresent()){
			Optional<BusinessGroupMemberPO> oldMemberOptional = group.getMembers().stream().filter(member -> member.getRoleId().equals(roleId)).findFirst();
			if(oldMemberOptional.isPresent()){
				RolePO audienceRole = roleDao.findByInternalRoleType(InternalRoleType.MEETING_AUDIENCE);
				if(audienceRole == null){
					throw new BaseException(StatusCode.FORBIDDEN, "该会议没有配置会议观众角色");
				}
				oldMember = oldMemberOptional.get();
				roleIdAddMembersMap.put(audienceRole.getId(), new ArrayListWrapper<BusinessGroupMemberPO>().add(oldMemberOptional.get()).getList());
			}
		}
		
		BusinessDeliverBO businessDeliver = new BusinessDeliverBO().setGroup(group);
		roleExecuteService.modifyRoleMembers(group, roleIdAddMembersMap, businessDeliver, true);
		RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(groupId);
		if(runningAgenda != null){
			agendaExecuteService.runAgenda(groupId, runningAgenda.getAgendaId(), businessDeliver, false);
		}
		deliverExecuteService.execute(businessDeliver, "修改角色", true);
		
		Map<String, List<BusinessGroupMemberVO>> businessGroupMemberVoMap = new HashMap<String, List<BusinessGroupMemberVO>>();
		List<BusinessGroupMemberVO> businessGroupMemberVoList = groupMembers.stream().map(member-> new BusinessGroupMemberVO().set(member)).collect(Collectors.toList());
		businessGroupMemberVoMap.put("self", businessGroupMemberVoList);
		
		if(oldMember != null){
			BusinessGroupMemberVO oldGroupMemberVo = new BusinessGroupMemberVO().set(oldMember);
			businessGroupMemberVoMap.put("other", new ArrayListWrapper<BusinessGroupMemberVO>().add(oldGroupMemberVo).getList());
		}
		
		return businessGroupMemberVoMap;
	}

	/**
	 * 修改成员角色<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月8日 下午1:55:08
	 * @param groupId 业务组id
	 * @param memberId 成员id集合
	 * @param roleId 目标角色id
	 */
	@Transactional(rollbackFor = Exception.class)
	public Map<String, List<BusinessGroupMemberVO>> exchangeMemberRoleToAudience(
			Long groupId, 
			List<Long> memberIdList) throws Exception {
		
		RolePO role = roleDao.findByInternalRoleType(InternalRoleType.MEETING_AUDIENCE);
		
		if(role == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到观众角色");
		}
		
		return exchangeMemberRole(groupId, memberIdList, role.getId());
		
	}
}
