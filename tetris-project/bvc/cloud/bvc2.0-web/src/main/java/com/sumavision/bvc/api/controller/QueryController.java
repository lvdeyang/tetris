package com.sumavision.bvc.api.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupBusinessRoleVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupMemberVO;
import com.sumavision.bvc.device.group.dao.DeviceGroupAuthorizationDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupBusinessRoleDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupMemberDAO;
import com.sumavision.bvc.device.group.dto.DeviceGroupMemberDTO;
import com.sumavision.bvc.device.group.enumeration.MemberStatus;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: API的查询接口。目前只有机顶盒使用
 * @author zsy
 * @date 2018年12月8日 下午2:27:00
 */
@Slf4j
@Controller
@RequestMapping(value = "/api/tvos/api/query")
public class QueryController {	
	
	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private DeviceGroupMemberDAO deviceGroupMemberDao;
	
	@Autowired
	DeviceGroupAuthorizationDAO deviceGroupAuthorizationDao;
	
	@Autowired
	private DeviceGroupBusinessRoleDAO deviceGroupBusinessRoleDao;
	
	/**
	 * 查询会议管理员信息
	 * @Title: queryGroupAdministrator 
	 * @param @param jsonParam
	 * @return Object    返回类型 
	 * @throws
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/group/administrator", method = RequestMethod.POST)
	public Object queryGroupAdministrator(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		String groupUuid = jsonParam.getString("groupUuid");
		
		DeviceGroupPO group = deviceGroupDao.findByUuid(groupUuid);
		
		JSONObject data = new JSONObject();
		data.put("administratorId", group.getUserId());
		data.put("administratorName", group.getUserName());
		
		return data;
	}

	/**
	 * 查询会议已接听成员列表
	 * @Title: queryConnectMembers 
	 * @param jsonParam
	 * @return Object
	 * @throws
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/connectMembers", method = RequestMethod.POST)
	public Object queryConnectMembers(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		String groupUuid = jsonParam.getString("groupUuid");
		
		DeviceGroupPO group = deviceGroupDao.findByUuid(groupUuid);
		Set<DeviceGroupMemberPO> members = group.getMembers();
		
		JSONArray datas = new JSONArray();
		
		for(DeviceGroupMemberPO member : members){
			if(member.getMemberStatus().equals(MemberStatus.CONNECT)){
				JSONObject data = new JSONObject();
				data.put("id", member.getBundleId());
				data.put("name", member.getBundleName());
				data.put("type", member.getBundleType());
				datas.add(data);
			}
		}
		return datas;
	}
	
	/**
	 * 获取成员所在会议列表<br/>
	 * @Title: queryMeetings 
	 * @param jsonParam
	 * @return Object
	 * @throws
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/queryMeetings", method = RequestMethod.POST)
	public Object queryMeetings(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		String bundleId = jsonParam.getString("bundleId");
		
		List<DeviceGroupMemberPO> members = deviceGroupMemberDao.findByBundleId(bundleId);
		
		JSONArray datas = new JSONArray();
		
		for(DeviceGroupMemberPO member: members){
			DeviceGroupPO group = member.getGroup();
			JSONObject data = new JSONObject();
			data.put("initiatorName", group.getUserName());
			data.put("groupType", group.getType().getName());
			data.put("groupUuid", group.getUuid());
			data.put("groupName", group.getName());
			datas.add(data);
		}
		
		return datas;
	}
	
	/**
	 * @Title: 查询有权限的播放列表
	 * @param request
	 * @throws
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/authorizedPlaylist", method = RequestMethod.POST)
	public Object authorizedPlaylist(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		String bundleId = jsonParam.getString("bundleId");
		
		List<String> cids = deviceGroupAuthorizationDao.findAuthorizationCidsByBundleId(bundleId);
		List<String> pids = deviceGroupAuthorizationDao.findAuthorizationPidsByBundleId(bundleId);
		
		JSONObject datas = new JSONObject();
		datas.put("liveChannel", cids);
		datas.put("asset", pids);
		log.info("authorizedPlaylist查询，参数 = " + jsonParam + ", 返回 = " + datas);
		return datas;
	}
	
	/**
	 * @Title: 根据设备组id查询角色 
	 * @param @param groupId 设备组id
	 * @return Object 返回类型 
	 * @throws
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/role", method = RequestMethod.POST)
	public Object queryRole(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		Long groupId = Long.parseLong(jsonParam.getString("groupId"));
		
		List<DeviceGroupBusinessRolePO> groupRolePOs = deviceGroupBusinessRoleDao.findByGroupId(groupId);
		List<DeviceGroupMemberDTO> groupMemberDTOs = deviceGroupMemberDao.findGroupMembersByGroupId(groupId);
		
		List<DeviceGroupBusinessRoleVO> groupRoleVOs = DeviceGroupBusinessRoleVO.getConverter(DeviceGroupBusinessRoleVO.class).convert(groupRolePOs, DeviceGroupBusinessRoleVO.class);
		List<DeviceGroupMemberVO> groupMemberVOs = DeviceGroupMemberVO.getConverter(DeviceGroupMemberVO.class).convert(groupMemberDTOs, DeviceGroupMemberVO.class);
		
		for(DeviceGroupBusinessRoleVO groupRoleVO: groupRoleVOs){
			for(DeviceGroupMemberVO groupMemberVO: groupMemberVOs){
				if(groupMemberVO.getRoleId() != null ){
					if(groupMemberVO.getRoleId().equals(groupRoleVO.getId())){
						groupRoleVO.getMembers().add(groupMemberVO);
					}
				}				
			}
		}
		
		return new HashMapWrapper<String, Object>().put("members", groupMemberVOs)
												   .put("roles", groupRoleVOs)
												   .getMap();
	}
}
