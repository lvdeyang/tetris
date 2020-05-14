package com.sumavision.tetris.api.g01.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.system.role.SystemRoleType;
import com.sumavision.tetris.system.role.UserSystemRolePermissionQuery;
import com.sumavision.tetris.system.role.UserSystemRolePermissionService;
import com.sumavision.tetris.system.role.UserSystemRolePermissionVO;

@Controller
@RequestMapping(value = "/api/g01/web/user/role/permission")
public class ApiG01WebUserRolePermissionController {
	@Autowired
	private UserSystemRolePermissionQuery userSystemRolePermissionQuery;
	
	@Autowired
	private UserSystemRolePermissionService userSystemRolePermissionService;
	
	/**
	 * 分页查询用户绑定的业务角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月21日 下午12:28:39
	 * @param Long userId 用户id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 用户绑定的角色数量
	 * @return List<UserSystemRolePermissionVO> rows 系统角色权限列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/by/userId")
	public Object listByUserId(
			Long userId,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		return userSystemRolePermissionQuery.listByUserIdAndRoleType(userId, SystemRoleType.BUSINESS, currentPage, pageSize);
	}
	
	/**
	 * 用户绑定业务角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月21日 下午2:35:47
	 * @param Long userId 用户id
	 * @param JSONString roleIds 系统角色id列表
	 * @return List<UserSystemRolePermissionVO> 绑定的权限列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/bind")
	public Object bindSystemRole(
			Long userId,
			String roleIds,
			HttpServletRequest request) throws Exception{
		
		if(roleIds==null || "".equals(roleIds)) return null;
		
		List<UserSystemRolePermissionVO> permissions = userSystemRolePermissionService.bindSystemRole(userId, JSON.parseArray(roleIds, Long.class));
		
		return permissions;
	}
}
