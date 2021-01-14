package com.suma.venus.resource.controller.feign;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.bo.AccessCapacityBO;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.InitVerification;
import com.suma.venus.resource.service.ResourceService;
import com.suma.venus.resource.service.WorkNodeService;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/feign/resource")
public class ResourceFeignController {
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private WorkNodeService workNodeService;
	
	@Autowired
	private BundleService bundleService;
	
	@Autowired
	private InitVerification initVerification;

	/**
	 * 查询用户同一权限下所有用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月9日 上午11:15:23
	 * @param Long userId 用户id
	 * @param String terminalType 终端类型
	 * @return List<UserBO> 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/users/by/userId")
	public Object queryUsersByUserId(
			Long userId,
			String terminalType,
			HttpServletRequest request) throws Exception{
		
		String _terminalType = (terminalType == null || "".equals(terminalType))?null: terminalType;
		
		List<UserBO> users = resourceService.queryUserresByUserId(userId, TerminalType.fromName(_terminalType));
		
		return users;
		
	}
	
	/**
	 * 查询webrtc接入<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月17日 下午4:15:06
	 * @return List<WorkNodeVO> 节点信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/webrtc")
	public Object queryLayerNode(HttpServletRequest request) throws Exception{
		
		return workNodeService.queryLayerNode(NodeType.ACCESS_WEBRTC);
	}
	
	/**
	 * 查询用户下对应类型的资源信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月17日 下午5:05:15
	 * @param String userIds 用户id列表
	 * @param String type 资源类型
	 * @return List<ResourceBO> 资源信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/resource")
	public Object queryResource(
			String userIds,
			String type,
			HttpServletRequest request) throws Exception{
		
		List<Long> userIdList = JSONArray.parseArray(userIds, Long.class);
		
		return bundleService.queryResource(userIdList, type);
	}
	
	/**
	 * 获取接入设备的限制数量<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月7日 下午3:42:45
	 * @return AccessCapacityBO 设备限制数量
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/access/capacity")
	public Object queryAccessCapacity()throws Exception{
		return initVerification.accessCapacity;
	}
}
