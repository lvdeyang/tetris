package com.sumavision.tetris.subordinate.role;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/subordinate/role")
public class SubordinateRoleController {
	@Autowired
	private UserQuery userQuery;
	@Autowired
	private SubordinateRoleQuery subordinateRoleQuery;
	@Autowired
	private SubordinateRoleService subordinateRoleService;
	@Autowired
	private UserSubordinateRolePermissionQuery userSubordinateRolePermissionQuery;
	@Autowired
	private UserSubordinateRolePermissionService userSubordinateRolePermissionService;

	/**
	 * 获取隶属角色<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月29日 上午11:44:44
	 * 
	 * @return List<SubordinateRoleVO> 角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(Long companyId, HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验

		return subordinateRoleQuery.getListFromCompany(companyId);
	}

	/**
	 * 添加隶属角色<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月29日 上午11:44:44
	 * 
	 * @return SubordinateRoleVO 新建角色信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(Long companyId, String roleName, HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验

		return subordinateRoleService.addRole(companyId, roleName);
	}

	/**
	 * 编辑隶属角色<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月29日 上午11:44:44
	 * 
	 * @return SubordinateRoleVO 修改后角色信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(Long roleId, String roleName, HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验

		return subordinateRoleService.editRole(roleId, roleName);
	}

	/**
	 * 删除隶属角色<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月29日 上午11:44:44
	 * 
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(Long roleId, HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验

		return subordinateRoleService.removeRole(roleId);
	}

	/**
	 * 获取隶属角色的成员列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月29日 上午11:44:44
	 * 
	 * @return int total 总数据量
	 * @return List<UserVO> rows 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/member/list")
	public Object memberList(Long roleId, int currentPage, int pageSize, HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验

		return userSubordinateRolePermissionQuery.getUserFromRole(roleId, currentPage, pageSize);
	}
	
	/**
	 * 获取不隶属于角色的成员列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月29日 上午11:44:44
	 * @param Long roleId 角色id
	 * @param Long currentPage 当前页码
	 * @param Long pageSize 每页数据量
	 * @return int total 总数据量
	 * @return List<UserVO> rows 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/member/with/except/list/{companyId}")
	public Object memberListWithExcept(@PathVariable Long companyId, int currentPage, int pageSize,String except, HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验
		Long userId;
		if(user.getId() != null){
			userId = user.getId();
		}else if (user.getUuid() != null) {
			userId = Long.parseLong(user.getUuid());
		}else {
			return null;
		}

		List<Long> exceptIds = except != null ? JSON.parseArray(except, Long.class) : new ArrayList<Long>();
		return userSubordinateRolePermissionQuery.getUserByCompanyWithExcept(userId, exceptIds, companyId, currentPage, pageSize);
	}

	/**
	 * 绑定隶属角色的成员列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月29日 上午11:44:44
	 * 
	 * @return List<UserVO> 角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/member/bind")
	public Object memberBind(String userIds, Long roleId, HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验
		
		if (userIds == null || userIds.isEmpty()) {
			return null;
		}

		return userSubordinateRolePermissionService.bindUserFromRole(JSON.parseArray(userIds, Long.class), roleId);
	}
	
	/**
	 * 移除隶属角色的成员<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月5日 上午11:44:44
	 * 
	 * @return List<SubordinateRoleVO> 角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/member/remove")
	public Object memberRemove(Long userId, HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验

		return userSubordinateRolePermissionService.removeUserFromRole(userId);
	}
}
