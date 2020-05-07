package com.sumavision.tetris.api.g01.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.business.role.BusinessRoleQuery;
import com.sumavision.tetris.business.role.BusinessRoleService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/g01/web/role")
public class ApiG01WebRoleController {
	@Autowired
	private BusinessRoleQuery businessRoleQuery;
	
	@Autowired
	private BusinessRoleService businessRoleService;
	
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
	 * 修改企业业务角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月2日 下午5:31:49
	 * @param Long id 角色id
	 * @param String name 角色名称
	 * @return SystemRoleVO 角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String name,
			HttpServletRequest request) throws Exception{
		return businessRoleService.edit(id, name);
	}
}
