package com.sumavision.tetris.business.role.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.business.role.BusinessRoleQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/business/role/feign")
public class BusinessRoleFeignController {

	@Autowired
	private BusinessRoleQuery businessRoleQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 根据id查询角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月8日 下午1:44:02
	 * @param JSONString ids 角色id列表 
	 * @return List<SystemRoleVO> 角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/id/in")
	public Object findByIdIn(
			String ids,
			HttpServletRequest request) throws Exception{
		if(ids == null) return null;
		return businessRoleQuery.findByIdIn(JSON.parseArray(ids, Long.class));
	}
	
	/**
	 * 分页查询企业业务角色（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月2日 下午5:07:34
	 * @param JSONString except 例外角色id列表
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return long total 总数据量
	 * @return List<SystemRoleVO> rows 业务角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/with/except/ids")
	public Object listWithExceptIds(
			String except,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		if(except == null){
			return businessRoleQuery.listWithExceptIds(null, currentPage, pageSize);
		}else{
			return businessRoleQuery.listWithExceptIds(JSON.parseArray(except, Long.class), currentPage, pageSize);
		}
	}
	
	/**
	 * 分页查询企业业务角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月2日 下午5:07:34
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return long total 总数据量
	 * @return List<SystemRoleVO> rows 业务角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		return businessRoleQuery.list(currentPage, pageSize);
	}
	
	/**
	 * 查询企业管理员业务角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月7日 下午4:54:09
	 * @return SystemRoleVO 企业管理员业务角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/company/admin/role")
	public Object findCompanyAdminRole(HttpServletRequest request) throws Exception{
		return businessRoleQuery.findCompanyAdminRole();
	}
	
	/**
	 * 查询用户私有角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 上午10:58:27
	 * @return SystemRoleVO 私有角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/private/role")
	public Object findPrivateRole(HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		return businessRoleQuery.findPrivateRole(user.getId());
	}
	
	/**
	 * 根据用户id列表查询私有角色列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 下午1:50:22
	 * @param JSONArray userIds 用户id列表
	 * @return List<SystemRoleVO> 角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/private/role/by/user/ids")
	public Object findPrivateRoleByUserIds(
			String userIds, 
			HttpServletRequest request) throws Exception{
		return businessRoleQuery.findPrivateRoleByUserIds(JSON.parseArray(userIds, Long.class));
	}
	
}
