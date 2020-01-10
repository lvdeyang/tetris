package com.sumavision.tetris.system.role.feign;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.system.role.SystemRoleGroupVO;
import com.sumavision.tetris.system.role.SystemRoleQuery;
import com.sumavision.tetris.system.role.SystemRoleVO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 系统角色rest接口<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月17日 下午12:54:34
 */
@Controller
@RequestMapping(value = "/system/role/feign")
public class SystemRoleFeignController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private SystemRoleQuery systemRoleQuery;
	
	/**
	 * 分组查询系统角色（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 下午12:54:29
	 * @param JSONString roleIds 例外角色id列表 
	 * @return List<SystemRoleGroupVO> 分组后的角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/with/group/by/except/ids")
	public Object listWithGroupByExceptIds(
			String roleIds, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		List<SystemRoleGroupVO> groups = null;
		
		if(roleIds==null || "".equals(roleIds)){
			groups = systemRoleQuery.listWithGroupByExceptIds(null);
		}else{
			List<String> parsedRoleIds = JSON.parseArray(roleIds, String.class);
			groups = systemRoleQuery.listWithGroupByExceptIds(parsedRoleIds);
		}
		
		return groups;
	}
	
	/**
	 * 根据给定的系统角色id分组查询系统角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 下午12:54:29
	 * @param JSONString roleIds 给定的角色id列表 
	 * @return List<SystemRoleGroupVO> 分组后的角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/width/group/by/ids")
	public Object listWithGroupAndIds(
			String roleIds,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		List<SystemRoleGroupVO> groups = null;
		
		if(roleIds == null){
			groups = systemRoleQuery.listWithGroupByIds(null);
		}else{
			List<String> parsedRoleIds = JSON.parseArray(roleIds, String.class);
			groups = systemRoleQuery.listWithGroupByIds(parsedRoleIds);
		}
		
		return groups;
	}
	
	/**
	 * 根据id（批量）查询系统角色列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 下午1:34:55
	 * @param JSONString roleIds 给定的角色id列表 
	 * @return List<SystemRoleVO> 系统角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/by/ids")
	public Object listByIds(
			String roleIds,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		List<SystemRoleVO> roles = null;
		
		if(roleIds != null){
			List<String> parsedRoleIds = JSON.parseArray(roleIds, String.class);
			roles = systemRoleQuery.listByIds(parsedRoleIds);
		}
		
		return roles;
	}
	
	/**
	 * 获取用户的系统角色列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 上午11:55:39
	 * @return List<SystemRoleVO> 系统角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/user/roles")
	public Object queryUserRoles(HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		return systemRoleQuery.queryUserRoles(user.getUuid());
	}
	
	/**
	 * 查询系统内置角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月4日 下午1:47:47
	 * @return SystemRoleVO 系统角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/internal/role")
	public Object queryInternalRole() throws Exception{
		
		return systemRoleQuery.queryInternalRole();
	}
	
	/**
	 * 查询用户所属公司的所有角色<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 上午11:13:35
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/all/roles")
	public Object queryAllRoles(HttpServletRequest request) throws Exception{
		
		UserVO userVO = userQuery.current();
		
		return systemRoleQuery.queryAllRoles(userVO.getId());
	}
	
	/**
	 * 根据用户查询角色<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月3日 下午4:51:17
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/roles/by/user")
	public Object queryRolesByUserId(Long userId, HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		return systemRoleQuery.queryUserRoles(userId.toString());
	}
	
}
