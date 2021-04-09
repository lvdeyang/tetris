package com.sumavision.bvc.control.device.group.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.group.dao.DeviceGroupAuthorizationDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.service.AuthorizationServiceImpl;
import com.sumavision.bvc.device.group.service.log.LogService;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * @ClassName: 看会权限接口<br> 
 * @author zsy 
 * @date 2018年12月24日 下午2:08:31 
 */
@Controller
@RequestMapping(value = "/authorization")
public class AuthorizationController {

	@Autowired
	private AuthorizationServiceImpl authorizationServiceImpl;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	DeviceGroupAuthorizationDAO deviceGroupAuthorizationDao;
	
	/**
	 * @Title: 设置看会权限<br/> 
	 * @param id 会议ID
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/watch/meeting/save/{id}")
	public Object watchMeetingAuthorization(
			@PathVariable Long id,
			String bundleIds,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		List<String> bundleIdArray = JSONArray.parseArray(bundleIds, String.class);
		
		authorizationServiceImpl.save(id, bundleIdArray);	
		
		logService.logsHandle(user.getName(), "设置看会权限", "会议id："+id);
		
		return null;
	}

	/**
	 * @Title: 查询有权看会的设备列表
	 * @param request
	 * @throws
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/group/bundles/{id}")
	public Object authorizedGroupBundles(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		List<TreeNodeVO> bundleTrees = new ArrayList<TreeNodeVO>();
		DeviceGroupPO group = deviceGroupDao.findOne(id);
		DeviceGroupAuthorizationPO authorization = deviceGroupAuthorizationDao.findByGroupUuid(group.getUuid());
		if(null == authorization){
		}else{
			Set<DeviceGroupAuthorizationMemberPO> members = authorization.getAuthorizationMembers();
			for(DeviceGroupAuthorizationMemberPO member: members){
				TreeNodeVO node = new TreeNodeVO().set(member); 
				bundleTrees.add(node);
			}
		}
		return new HashMapWrapper<String, Object>().put("bundles", bundleTrees)
				   .getMap();
	}
	
}
