package com.sumavision.tetris.mims.event;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

/**
 * 业务角色删除事件代理<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月25日 下午4:31:38
 */
@FeignClient(name = "tetris-mims", configuration = FeignConfiguration.class)
public interface BusinessRoleRemovedFeign {

	/**
	 * 业务角色删除事件代理<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 下午4:31:31
	 * @param Long roleId 角色id
	 * @param String roleName 角色名称
	 * @param String companyId 公司id
	 */
	@RequestMapping(value = "/event/publish/business/role/removed")
	public JSONObject businessRoleRemoved(
			@RequestParam("roleId") Long roleId,
			@RequestParam("roleName") String roleName,
			@RequestParam("companyId") String companyId) throws Exception;
	
}
