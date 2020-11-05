package com.sumavision.tetris.bvc.model.role;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/role")
public class RoleController {

	@Autowired
	private RoleQuery roleQuery;
	
	@Autowired
	private RoleService roleService;
	
	/**
	 * 查询类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午1:30:15
	 * @return roleTypes Map<String, String> 内置角色类型
	 * @return mappingTypes Map<String, String> 授权类型
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/types")
	public Object queryTypes(HttpServletRequest request) throws Exception{
		
		return roleQuery.queryTypes();
	}
	
	/**
	 * 分页查询内置角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午1:57:43
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return total long 总数据量
	 * @return rows List<RoleVO> 角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/internal")
	public Object loadInternal(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		return roleQuery.loadInternal(currentPage, pageSize);
	}
	
	/**
	 * 查询全部内置角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午1:57:43
	 * @return List<RoleVO> 角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/all/internal")
	public Object loadAllInternal(HttpServletRequest request) throws Exception{
		
		return roleQuery.loadAllInternal();
	}
	
	/**
	 * 添加内置角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午1:38:17
	 * @param String name 角色名称
	 * @param String internalRoleType 内置角色类型
	 * @param String roleUserMappingType 授权类型
	 * @return RoleVO 角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/internal")
	public Object addInternal(
			String name,
			String internalRoleType,
			String roleUserMappingType,
			HttpServletRequest request) throws Exception{
		
		return roleService.addInternal(name, internalRoleType, roleUserMappingType);
	}
	
	/**
	 * 修改内置角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午1:38:17
	 * @param Long id 角色id
	 * @param String name 角色名称
	 * @param String internalRoleType 内置角色类型
	 * @param String roleUserMappingType 授权类型
	 * @return RoleVO 角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/internal")
	public Object editInternal(
			Long id,
			String name,
			String internalRoleType,
			String roleUserMappingType,
			HttpServletRequest request) throws Exception{
		
		return roleService.editInternal(id, name, internalRoleType, roleUserMappingType);
	}
	
	/**
	 * 删除角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午1:44:13
	 * @param Long id 角色id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(Long id) throws Exception{
		
		roleService.delete(id);
		return null;
	}
}
