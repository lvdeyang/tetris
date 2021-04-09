package com.sumavision.bvc.control.device.group.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/group/member/role")
public class GroupMemberRoleController {

	@Autowired
	private GroupMemberRoleService groupMemberRoleService;
	
	@Autowired
	private BusinessReturnService businessReturnService;
	
	/**
	 * 通过组id以及角色id查找组内成员列表<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月8日 下午1:46:55
	 * @param groupId 业务组id
	 * @param roleId 角色id
	 * @return
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/member/by/roleId")
	public Object queryMemberByRoleId(
			Long groupId,
			Long roleId){
		
		return groupMemberRoleService.queryMemberByRoleId(groupId, roleId);
	}
	
	/**
	 * 修改成员角色<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月8日 下午1:55:08
	 * @param groupId 业务组id
	 * @param memberId 成员id集合
	 * @param roleId 目标角色id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/exchange/member/role")
	public Object exchangeMemberRole(
			Long groupId,
			String memberIds,
			Long roleId) throws Exception{
		
		List<Long> memberIdList= JSONArray.parseArray(memberIds, Long.class);
		
		businessReturnService.init(true);
		
		return groupMemberRoleService.exchangeMemberRole(groupId, memberIdList, roleId);
	}
	
	/**
	 * 修改成员角色为观众<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月8日 下午1:46:55
	 * @param groupId 业务组id
	 * @param roleId 角色id
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/exchange/member/role/to/audience")
	public Object exchangeMemberRoleToAudience(
			Long groupId,
			String memberIds) throws Exception{
		
		List<Long> memberIdList= JSONArray.parseArray(memberIds, Long.class);
		
		businessReturnService.init(true);
		
		return groupMemberRoleService.exchangeMemberRoleToAudience(groupId, memberIdList);
	}
	
}
