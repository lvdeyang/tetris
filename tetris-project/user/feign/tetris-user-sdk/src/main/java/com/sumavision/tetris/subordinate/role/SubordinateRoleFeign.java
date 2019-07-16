package com.sumavision.tetris.subordinate.role;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

/**
 * 公司角色feign接口<br/>
 * <b>作者:</b>ql<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月17日 下午7:05:08
 */
@FeignClient(name = "tetris-user", configuration = FeignConfiguration.class)
public interface SubordinateRoleFeign {

	/**
	 * 通过公司id查询公司管理员角色feign接口<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午11:05:03
	 * @param JSONString companyId 公司id
	 * @return JSONObject 公司管理员角色
	 */
	@RequestMapping(value = "/subordinate/role/feign/role/by/company")
	public JSONObject roleByCompany(@RequestParam("companyId") String company)throws Exception;
	
	/**
	 * 通过公司id查找所有公司角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午10:21:59
	 * @param company 用户id
	 * @return List<SubordinaryRoleVO> 角色
	 */
	@RequestMapping(value = "/subordinate/role/feign/roles/by/company")
	public JSONObject rolesByCompany(@RequestParam("companyId") String company)throws Exception;
	
	/**
	 * 通过角色id列表和公司id查找角色组<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:21:59
	 * @return List<SubordinaryRoleVO> 角色
	 */
	@RequestMapping(value = "/subordinate/role/feign/roles/by/company/and/ids")
	public JSONObject rolesByCompanyAndIds(@RequestParam("company") String company,@RequestParam("ids") String ids)throws Exception;

	/**
	 * 添加角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:21:59
	 * @return SubordinaryRoleVO 角色
	 */
	@RequestMapping(value = "/subordinate/role/feign/add")
	public JSONObject add(@RequestParam("userId")String userId, @RequestParam("roleName")String roleName,@RequestParam("upDate")String upDate,@RequestParam("Removeable")String Removeable,@RequestParam("Serial")String Serial,@RequestParam("companyId") String companyId);
	
	/**
	 * 修改角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:21:59
	 * @return SubordinaryRoleVO 角色
	 */
	@RequestMapping(value = "/subordinate/role/feign/edit")
	public JSONObject edit(@RequestParam("roleId")String roleId, @RequestParam("roleName") String roleName)throws Exception;

	/**
	 * 删除角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午11:05:03 
	 * @return JSONObject 公司角色
	 */
	@RequestMapping(value = "/subordinate/role/feign/delet")
	public JSONObject delet(@RequestParam("roleId")String roleId)throws Exception;
	
	/**
	 * 通过用户id查找角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:21:59
	 * @return List<SubordinaryRoleVO> 角色
	 */
	@RequestMapping(value = "/subordinate/role/feign/query/by/user")
	public JSONObject queryByUser(@RequestParam("userId")String userId)throws Exception;
	
	/**
	 * 通过角色id列表查找角色列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午10:21:59
	 * @param  JSONString 用户ids
	 * @return JSONObject 角色列表
	 */
	@RequestMapping(value = "/subordinate/role/feign/find/by/id/in")
	public JSONObject findByIdIn(@RequestParam("ids")String ids)throws Exception;
	
	/**
	 * 通过角色id列表查找角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:21:59
	 * @param  Long 用户id
	 * @return SubordinaryRoleVO 角色
	 */
	@RequestMapping(value = "/subordinate/role/feign/find/by/id")
	public JSONObject findById(@RequestParam("id")Long id)throws Exception;
	
	/**
	 * 查询公司下的业务角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月16日 上午9:09:00
	 * @param Long companyId 公司id
	 * @return JSONObject 角色列表
	 */
	@RequestMapping(value = "/subordinate/role/feign/find/by/company/id")
	public JSONObject findByCompanyId(@RequestParam("companyId") Long companyId) throws Exception;
	
	/**
	 * 查询公司下的业务角色（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月16日 上午9:13:29
	 * @param Long companyId 公司id
	 * @param JSONArray except 例外角色id列表
	 * @return JSONObject 角色列表
	 */
	@RequestMapping(value = "/subordinate/role/feign/find/by/company/id/with/except")
	public JSONObject findByCompanyIdWithExcept(@RequestParam("companyId") Long companyId, @RequestParam("except") String except) throws Exception;
	
}
