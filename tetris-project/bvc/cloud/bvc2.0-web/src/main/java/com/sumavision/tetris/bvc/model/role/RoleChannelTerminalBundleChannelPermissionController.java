package com.sumavision.tetris.bvc.model.role;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/role/channel/terminal/bundle/channel/permission")
public class RoleChannelTerminalBundleChannelPermissionController {

	@Autowired
	private RoleChannelTerminalBundleChannelPermissionQuery roleChannelTerminalBundleChannelPermissionQuery;
	
	@Autowired
	private RoleChannelTerminalBundleChannelPermissionService roleChannelTerminalBundleChannelPermissionService;
	
	/**
	 * 根据角色通道查询终端通道关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月29日 下午2:36:20
	 * @param Long roleChannelId 角色通道id
	 * @return List<RoleChannelTerminalBundleChannelPermissionVO> 通道关联列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long roleChannelId,
			HttpServletRequest request) throws Exception{
		
		return roleChannelTerminalBundleChannelPermissionQuery.load(roleChannelId);
	}
	
	/**
	 * 添加角色通道和终端通道关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月29日 下午4:56:50
	 * @param Long roleId 角色id
	 * @param Long roleChannelId 角色通道id
	 * @param Long terminalId 终端id
	 * @param JSONString terminalChannelIds 终端通道id列表
	 * @return List<RoleChannelTerminalBundleChannelPermissionVO> 关联列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long roleId,
			Long roleChannelId,
			Long terminalId,
			String terminalChannelIds,
			HttpServletRequest request) throws Exception{
		
		return roleChannelTerminalBundleChannelPermissionService.add(roleId, roleChannelId, terminalId, terminalChannelIds);
	}
	
	/**
	 * 删除关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月29日 下午5:06:44
	 * @param Long id 关联id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id,
			HttpServletRequest request) throws Exception{
		
		roleChannelTerminalBundleChannelPermissionService.delete(id);
		return null;
	}
	
}
