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
	 * 查询议程角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午4:33:32
	 * @param Long agendaId 议程id
 	 * @return List<RoleVO> 角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/by/agenda/id")
	public Object loadByAgendaId(
			Long agendaId,
			HttpServletRequest request) throws Exception{
	
		return roleQuery.loadByAgendaId(agendaId);
	}
	
	/**
	 * 查询议程特定类型的角色（带通道信息）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午4:33:32
	 * @param Long agendaId 议程id
 	 * @param String type 通道类型
 	 * @return List<RoleVO> 角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/by/agenda/id/and/type/with/channel")
	public Object loadByAgendaIdAndTypeWithChannel(
			Long agendaId,
			String type,
			HttpServletRequest request) throws Exception{
		
		return roleQuery.loadByAgendaIdAndTypeWithChannel(agendaId, type);
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
	 * 添加自定义角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月27日 下午3:49:53
	 * @param String name 角色名称
	 * @param Long businessId 业务id
	 * @param String roleUserMappingType 授权类型
	 * @return RoleVO 角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/custom")
	public Object addCustom(
			String name,
			Long businessId,
			String roleUserMappingType) throws Exception{
		
		return roleService.addCustom(name, businessId, roleUserMappingType);
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
	
	/**
	 * 查询会议中的角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月26日 上午9:46:44
	 * @param Long businessId 业务id
	 * @return List<RoleVO> 角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/by/group/id")
	public Object loadByGroupId(
			Long businessId,
			HttpServletRequest request) throws Exception{
		
		return roleQuery.loadByGroupId(businessId);
	}
	
	/**
	 * 根据会议和类型查询角色带通道信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月26日 上午10:43:44
	 * @param Long businessId 会议id
	 * @param String type 通道类型， ALL表示全类型
	 * @return List<RoleVO> 角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/by/group/id/and/type/with/channel")
	public Object loadByGroupIdAndTypeWithChannel(
			Long businessId,
			String type,
			HttpServletRequest request) throws Exception{
		
		return roleQuery.loadByGroupIdAndTypeWithChannel(businessId, type);
	}
	
}
