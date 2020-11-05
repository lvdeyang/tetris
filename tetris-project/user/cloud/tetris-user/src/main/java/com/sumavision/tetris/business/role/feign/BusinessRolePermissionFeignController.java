package com.sumavision.tetris.business.role.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.system.role.SystemRoleType;
import com.sumavision.tetris.system.role.UserSystemRolePermissionQuery;

@Controller
@RequestMapping(value = "/user/business/role/permission/feign")
public class BusinessRolePermissionFeignController {
	@Autowired
	private UserSystemRolePermissionQuery userSystemRolePermissionQuery;
	
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
}
