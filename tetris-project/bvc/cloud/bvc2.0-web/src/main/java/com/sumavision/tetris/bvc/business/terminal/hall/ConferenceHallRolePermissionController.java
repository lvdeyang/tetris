package com.sumavision.tetris.bvc.business.terminal.hall;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/conference/hall/role/permission")
public class ConferenceHallRolePermissionController {

	@Autowired
	private ConferenceHallRolePermissionQuery conferenceHallRolePermissionQuery;
	
	@Autowired
	private ConferenceHallRolePermissionService conferenceHallRolePermissionService;
	
	/**
	 * 分页查询会场授权<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月5日 下午3:06:54
	 * @param String name 名称
	 * @param Long roleId 角色id
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return rows List<ConferenceHallVO> 权限列表
	 * @return total long 总数据量
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			String name,
			Long roleId,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		return conferenceHallRolePermissionQuery.load(name, roleId, currentPage, pageSize);
	}
	
	/**
	 * 添加权限<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月5日 下午4:36:12
	 * @param Long roleId 角色id
	 * @param Long conferenceHallId 会场id
	 * @param String privilege 权限类型
	 * @return key String 权限类型key
	 * @return value String 权限类型value
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long roleId,
			Long conferenceHallId,
			String privilege,
			HttpServletRequest request) throws Exception{
		
		return conferenceHallRolePermissionService.add(roleId, conferenceHallId, privilege);
	}
	
	/**
	 * 删除权限<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月5日 下午4:36:12
	 * @param Long roleId 角色id
	 * @param Long conferenceHallId 会场id
	 * @param String privilege 权限类型
	 * @return key String 权限类型key
	 * @return value String 权限类型value
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			Long roleId,
			Long conferenceHallId,
			String privilege,
			HttpServletRequest request) throws Exception{
		
		return conferenceHallRolePermissionService.remove(roleId, conferenceHallId, privilege);
	}
	
}
