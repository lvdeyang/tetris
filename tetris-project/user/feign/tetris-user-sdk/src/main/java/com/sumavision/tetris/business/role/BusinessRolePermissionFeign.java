package com.sumavision.tetris.business.role;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-user", configuration = FeignConfiguration.class)
public interface BusinessRolePermissionFeign {
	
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
	@RequestMapping(value = "/user/business/role/permission/feign/list/by/userId")
	public JSONObject listByUserId(
			@RequestParam("userId") Long userId,
			@RequestParam("currentPage") int currentPage,
			@RequestParam("pageSize") int pageSize) throws Exception;
}
