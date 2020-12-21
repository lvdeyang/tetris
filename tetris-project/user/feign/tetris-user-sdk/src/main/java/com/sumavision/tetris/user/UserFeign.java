package com.sumavision.tetris.user;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.sumavision.tetris.config.feign.FeignConfiguration;

/**
 * 系统用户feign接口<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月7日 上午10:25:16
 */
@FeignClient(name = "tetris-user", configuration = FeignConfiguration.class)
public interface UserFeign {

	/**
	 * 用户登录校验<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月7日 下午2:39:14
	 * @param String token 登录token
	 * @return boolean 判断结果
	 */
	/*@RequestMapping(value = "/user/feign/check/token")
	public JSONObject checkToken(@RequestParam("token") String token);*/
	
	/**
	 * 获取当前登录用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月22日 上午10:14:26
	 * @return JSONObject 当前用户
	 */
	@RequestMapping(value = "/user/feign/current")
	public JSONObject current();
	
	/**
	 * 根据token查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月7日 下午6:23:15
	 * @param String token 用户登录token
	 * @return JSONObject 用户数据
	 */
	@RequestMapping(value = "/user/feign/find/by/token")
	public JSONObject findByToken(@RequestParam("token") String token);
	
	/**
	 * 根据id查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月11日 下午3:30:27
	 * @param JSONArray ids 用户id列表
	 * @return JSONObject 用户列表
	 */
	@RequestMapping(value = "/user/feign/find/by/id/in")
	public JSONObject findByIdIn(@RequestParam("ids") String ids);
	
	/**
	 * 根据id和类型查询用户<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月19日 下午3:30:27
	 * @param JSONArray ids 用户id列表
	 * @param String terminalType 查询类型
	 * @return JSONObject 用户列表
	 */
	@RequestMapping(value = "/user/feign/find/by/id/in/and/type")
	public JSONObject findByIdInAndType(
			@RequestParam("ids") String ids,
			@RequestParam("terminalType") String terminalType);
	
	/**
	 * 分页查询公司下的用户列表（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 上午11:17:57
	 * @param Long companyId 公司id
	 * @param JSONString except 例外用户id列表
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 用户总量
	 * @return List<UserVO> rows 用户列表
	 */
	@RequestMapping(value = "/user/feign/list/by/{companyId}/with/except")
	public JSONObject listByCompanyIdWithExcept(
			@PathVariable("companyId") Long companyId,
			@RequestParam("except") String except,
			@RequestParam("currentPage") int currentPage,
			@RequestParam("pageSize") int pageSize);
	
	/**
	 * 修改一个用户<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 下午4:54:11
	 * @param Long id 用户id
	 * @param String tags 修改的标签(全量)
	 * @return UserVO 修改后的数据
	 */
	@RequestMapping(value = "/user/feign/edit")
	public JSONObject edit(
			@RequestParam("id") Long id,
			@RequestParam("tags") String tags);
	
	/**
	 * 根据用户公司id和类型查询用户列表（带例外-类型）<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 上午10:17:43
	 * @param Long companyId 公司id
	 * @param String except 例外
	 * @param String classify 类型
	 * @return List<UserVO> 用户列表
	 */
	@RequestMapping(value = "/user/feign/list/by/{companyId}/with/except/and/type/and/classify")
	public JSONObject listByCompanyIdWithExceptAndClassify(
			@PathVariable("companyId") Long companyId,
			@RequestParam("terminalType") String terminalType,
			@RequestParam("except") String except,
			@RequestParam("classify") String classify);
	
	/**
	 * 根据用户公司id和类型查询用户列表（带例外）<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 上午10:17:43
	 * @param Long companyId 公司id
	 * @param String except 例外
	 * @param String classify 类型
	 * @return List<UserVO> 用户列表
	 */
	@RequestMapping(value = "/user/feign/list/by/{companyId}/with/except/and/classify")
	public JSONObject listByCompanyIdWithExceptAndClassify(
			@PathVariable("companyId") Long companyId,
			@RequestParam("except") String except,
			@RequestParam("classify") String classify);
	
	/**
	 * 分页查询所有用户基本信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月24日 上午9:19:30
	 * @return List<UserVO> 用户列表
	 */
	@RequestMapping(value = "/user/feign/query/all/user/baseInfo/by/page")
	public JSONObject queryAllUserBaseInfo(
			@RequestParam("terminalType") String terminalType,
			@RequestParam("currentPage") int currentPage,
			@RequestParam("pageSize") int pageSize);
	
	/**
	 * 查询所有用户基本信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月31日 下午1:59:55
	 * @return List<UserVO> 用户列表
	 */
	@RequestMapping(value = "/user/feign/query/all/user/baseInfo")
	public JSONObject queryAllUserBaseInfo(@RequestParam("terminalType") String terminalType);
	
	/**
	 * 根据用户名模糊查询分页<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月24日 下午1:23:37
	 * @param String userName 用户名
	 * @return List<UserVO> 用户列表
	 */
	@RequestMapping(value = "/user/feign/query/users/by/name/like/by/page")
	public JSONObject queryUsersByNameLike(
			@RequestParam("userName") String userName,
			@RequestParam("except") String except,
			@RequestParam("currentPage") int currentPage,
			@RequestParam("pageSize") int pageSize);
	
	/**
	 * 根据用户名模糊查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月24日 下午1:23:37
	 * @param String userName 用户名
	 * @return List<UserVO> 用户列表
	 */
	@RequestMapping(value = "/user/feign/query/users/by/name/like")
	public JSONObject queryUsersByNameLike(@RequestParam("userName") String userName);
	
	/**
	 * 根据用户名查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月24日 下午1:23:37
	 * @param String userName 用户名
	 * @return List<UserVO> 用户列表
	 */
	@RequestMapping(value = "/user/feign/query/user/by/name")
	public JSONObject queryUserByName(@RequestParam("userName") String userName);
	
	/**
	 * 根据用户id查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月24日 下午1:23:37
	 * @param Long id 用户名
	 * @return UserVO 用户
	 */
	@RequestMapping(value = "/user/feign/query/user/by/id/and/type")
	public JSONObject queryUserById(
			@RequestParam("id") Long id,
			@RequestParam("terminalType") String terminalType);
	
	/**
	 * 根据用户号码查询用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午7:20:34
	 * @param String userno 用户号码
	 * @return UserVO 用户
	 */
	@RequestMapping(value = "/user/feign/query/user/by/no")
	public JSONObject queryUserByNo(@RequestParam("userno") String userno);
	
	/**
	 * 根据角色查询用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月3日 上午11:12:26
	 * @param Long roleId 角色id
	 * @return List<UserVO> 用户
	 */
	@RequestMapping(value = "/user/feign/query/user/by/role")
	public JSONObject queryUserByRole(@RequestParam("roleId") Long roleId);
	
	@RequestMapping(value = "/user/feign/find/by/nickname/in")
	public JSONObject queryUsersByNicknameIn(@RequestParam("nicknames") String nicknames);
	
	/**
	 * 添加游客<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午4:51:54
	 * @param String nickname 游客昵称
	 * @return UserVO 游客
	 */
	@RequestMapping(value = "/user/feign/add/tourist")
	public JSONObject addTourist(@RequestParam("nickname") String nickname);
	
	/**
	 * 删除游客<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午4:53:29
	 * @param String userId 游客id
	 */
	@RequestMapping(value = "/user/feign/remove/tourist")
	public JSONObject removeTourist(@RequestParam("userId") Long userId);
	
	/**
	 * 批量删除游客<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午1:57:38
	 * @param JSONString userIds 游客id列表
	 */
	@RequestMapping(value = "/user/feign/remove/tourist/batch")
	public JSONObject removeTouristBatch(@RequestParam("userIds") String userIds);
	
	/**
	 * 根据游客id查询游客<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午5:16:36
	 * @param String userId 游客id
	 * @return UserVO 游客
	 */
	@RequestMapping(value = "/user/feign/find/tourist")
	public JSONObject findTourist(@RequestParam("userId") String userId);
	
	/**
	 * 根据用户号码查询用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 上午10:46:51
	 * @param String userno 用户号码
	 * @return UserVO
	 */
	@RequestMapping(value = "/user/feign/find/by/userno")
	public JSONObject findByUserno(@RequestParam("userno") String userno);
	
	/**
	 * 根据用户号码批量查询用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 上午10:46:51
	 * @param String usernos 用户号码
	 * @return List<UserVO>
	 */
	@RequestMapping(value = "/user/feign/find/by/userno/in")
	public JSONObject findByUsernoIn(@RequestBody String usernos);
	
	/**
	 * 删除ldap用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午2:19:18
	 * @param String userIds 用户ids
	 */
	@RequestMapping(value = "/user/feign/delete/ldap/user/by/ids")
	public JSONObject deleteLdapUser(@RequestParam("userIds") String userIds);
	
	/**
	 * 删除ldap用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午2:19:18
	 */
	@RequestMapping(value = "/user/feign/delete/ldap/user")
	public JSONObject deleteLdapUsers();
	
	
	/**
	 * 添加ldap用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午3:58:37
	 * @param String users ldap用户信息
	 * @return List<UserVO> 持久化过的用户信息
	 */
	@RequestMapping(value = "/user/feign/add/ldap/user")
	public JSONObject addLdapUser(@RequestParam("users") String users);
	
	/**
	 * 根据公司和条件查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月13日 上午11:05:41
	 * @param String nickname 用户昵称
	 * @param String userno 用户号码
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return int total 用户总量
	 * @return List<UserVO> rows 用户列表
	 */
	@RequestMapping(value = "/user/feign/find/by/company/id/and/condition")
	public JSONObject findByCompanyIdAndCondition(
			@RequestParam("nickname") String nickname, 
			@RequestParam("userno") String userno, 
			@RequestParam("currentPage") int currentPage, 
			@RequestParam("pageSize") int pageSize);
	
	
	@RequestMapping(value = "/user/feign/query/user/online")
	public JSONObject queryUserOnline();
	
	@RequestMapping(value = "/user/feign/add/tag/hotcount")
	public JSONObject addTagHotcount(
			@RequestParam("userId") Long userId, 
			@RequestParam("tagNames") String tagNames);
	
	@RequestMapping(value = "/user/feign/query/user/tags")
	public JSONObject queryUserTags(
			@RequestParam("userId") Long userId);
	
	@RequestMapping(value = "/user/feign/find/all")
	public JSONObject findAll();
	
	@RequestMapping(value = "/user/feign/find/by/roleId/in")
	public JSONObject findByRoleIdIn(
			@RequestParam("roleIds") List<Long> roleIds);
	
	@RequestMapping(value = "/user/feign/set/userCapacity")
	public JSONObject setUserCapacity(
			@RequestParam("userCapacity") Long setUserCapacity);

}
