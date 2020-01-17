package com.sumavision.tetris.system.role;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

/**
 * 系统角色feign接口<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月17日 下午1:05:08
 */
@FeignClient(name = "tetris-user", configuration = FeignConfiguration.class)
public interface SystemRoleFeign {

	/**
	 * 分组查询系统角色feign接口（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 下午1:05:03
	 * @param JSONString roleIds 例外系统角色id列表
	 * @return JSONObject 分组后的系统角色列表
	 */
	@RequestMapping(value = "/system/role/feign/list/with/group/by/except/ids")
	public JSONObject listWithGroupByExceptIds(@RequestParam("roleIds") String roleIds) throws Exception;

	/**
	 * 根据给定的系统角色id分组查询系统角色feign接口<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 下午1:05:03
	 * @param JSONString roleIds 给定的系统角色id列表
	 * @return JSONObject 分组后的系统角色列表
	 */
	@RequestMapping(value = "/system/role/feign/list/with/group/by/ids")
	public JSONObject listWithGroupByIds(@RequestParam("roleIds") String roleIds) throws Exception;
	
	/**
	 * 根据id（批量）查询系统角色列表feign接口<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 下午1:38:19
	 * @param JSONString roleIds 给定的系统角色id列表
	 * @return JSONObject 系统角色列表
	 */
	@RequestMapping(value = "/system/role/feign/list/by/ids")
	public JSONObject listByIds(@RequestParam("roleIds") String roleIds) throws Exception;
	
	/**
	 * 查询用户绑定的系统角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 上午11:58:53
	 * @return JSONObject 系统角色列表
	 */
	@RequestMapping(value = "/system/role/feign/query/user/roles")
	public JSONObject queryUserRoles() throws Exception;
	
	/**
	 * 查询系统内置角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月4日 下午1:44:52
	 * @return SystemRoleVO 系统内置角色
	 */
	@RequestMapping(value = "/system/role/feign/query/internal/role")
	public JSONObject queryInternalRole() throws Exception;
	
	/**
	 * 查询所有角色<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 上午11:17:02
	 * @return List<SystemRoleVO>
	 */
	@RequestMapping(value = "/system/role/feign/query/all/roles")
	public JSONObject queryAllRoles() throws Exception;
	
	/**
	 * 查询用户角色<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月3日 下午4:52:46
	 * @param Long userId 用户id
	 */
	@RequestMapping(value = "/system/role/feign/query/roles/by/user")
	public JSONObject queryRolesByUserId(@RequestParam("userId") Long userId) throws Exception;
}
